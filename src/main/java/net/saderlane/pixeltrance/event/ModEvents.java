package net.saderlane.pixeltrance.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.networking.ClientPayloadHandler;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;

@EventBusSubscriber(modid = PixelTrance.MOD_ID)
public class ModEvents {


    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);

        registrar.playToClient(HypnoDataS2C.TYPE, HypnoDataS2C.STREAM_CODEC,
                ClientPayloadHandler::HypnoDataSync2Cache);
    }


    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity subject)) return; // If entity ticking is not living entity, ignore
        if (subject.level().isClientSide()) return; // If subject is on the client side

        // If entity's tick is not in the decay interval, ignore
        if (subject.tickCount % HypnoData.DECAY_INTERVAL != 0) return;

        HypnoData.tickDecay(subject);

    }


    // Events to cause sync
    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) HypnoData.sync(player);
    }


}
