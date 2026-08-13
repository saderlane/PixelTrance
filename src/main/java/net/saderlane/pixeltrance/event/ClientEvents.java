package net.saderlane.pixeltrance.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.client.ClientHypnoCache;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;
import net.saderlane.pixeltrance.util.ModKeyMappings;

@EventBusSubscriber(modid = PixelTrance.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerKeyBind(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.PRESS_K.get());
        event.register(ModKeyMappings.PRESS_L.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;   // clicks can drain on a tick with no world loaded

        // When K key is pressed
        while (ModKeyMappings.PRESS_K.get().consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Trance: " + ClientHypnoCache.getTrance() + " / 100"));
        }

        // When L key is pressed
        while (ModKeyMappings.PRESS_L.get().consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Focus: " + ClientHypnoCache.getFocus() + " / 100"));
        }
    }

    // When logging out, clear the cache
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientHypnoCache.clear();
    }

}
