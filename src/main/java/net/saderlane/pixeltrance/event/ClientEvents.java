package net.saderlane.pixeltrance.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.networking.packet.KeyData;
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

        // When K key is pressed
        while (ModKeyMappings.PRESS_K.get().consumeClick()) {
            //Do things --> On client
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("[Client] I pressed the K key")); // Send message to client
            PacketDistributor.sendToServer(new KeyData("PixelTrance", 100)); // Send packet to server via KeyData
        }

        // When L key is pressed
        while (ModKeyMappings.PRESS_L.get().consumeClick()) {
            //Do things --> On client
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("[Client] I pressed the L key"));
        }
    }

}
