package net.saderlane.pixeltrance.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.command.ConfigCommand;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.ai.goal.FocusLookGoal;
import net.saderlane.pixeltrance.ai.goal.TranceFollowGoal;
import net.saderlane.pixeltrance.command.FocusCommand;
import net.saderlane.pixeltrance.command.TranceCommand;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.networking.ClientPayloadHandler;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;

@EventBusSubscriber(modid = PixelTrance.MOD_ID)
public class ModEvents {

    // Register custom commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new TranceCommand(event.getDispatcher());
        new FocusCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }



    // Register the payload for server and client syncing
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);

        registrar.playToClient(HypnoDataS2C.TYPE, HypnoDataS2C.STREAM_CODEC,
                ClientPayloadHandler::HypnoDataSync2Cache);
    }


    // For each entity's tick
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity subject)) return; // If entity ticking is not living entity, ignore
        if (subject.level().isClientSide()) return; // If subject is on the client side

        // If entity's tick is not in the decay interval, ignore
        if (subject.tickCount % HypnoData.DECAY_INTERVAL != 0) return;

        HypnoData.tickDecay(subject);
    }

    @SubscribeEvent
    public static void onEntityEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return; // If entity ticking is not a mob, ignore
        if (mob.level().isClientSide()) return; // If subject is on the client side

        mob.goalSelector.addGoal(3, new FocusLookGoal(mob)); //Goal has the same priority as being tempted with food

        if (mob instanceof PathfinderMob pathfinderMob) { // this would crash cause mobs like slimes aren't pathfinders
            pathfinderMob.goalSelector.addGoal(1, new TranceFollowGoal(pathfinderMob, 1.0f));
        }

    }

    // Events to cause sync
    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) HypnoData.sync(player);
    }


}
