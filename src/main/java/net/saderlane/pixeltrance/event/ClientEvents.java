package net.saderlane.pixeltrance.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.PacketDistributor;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.client.ClientHypnoCache;
import net.saderlane.pixeltrance.dataattachment.ModData;
import net.saderlane.pixeltrance.networking.packet.HypnoDataS2C;
import net.saderlane.pixeltrance.util.ModKeyMappings;

@EventBusSubscriber(modid = PixelTrance.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    // Locations of trance bar images
    private static final ResourceLocation ICON_BG = ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_bg");
    private static final ResourceLocation ICON_PARTIAL = ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_partial");
    private static final ResourceLocation ICON_FULL = ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_full");
    // Icon final variables
    private static final int ICON_COUNT = 9;
    private static final int ICON_SIZE = 8;
    private static final int ICON_SPACING = 9;
    private static final int PER_ICON = 100 / ICON_COUNT;

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

    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_bar"),
            ClientEvents::renderTranceBar);
    }

    private static void renderTranceBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mcInstance = Minecraft.getInstance();

        if (mcInstance.options.hideGui || mcInstance.player.isCreative()) return;

        int trance = ClientHypnoCache.getTrance();
        int full_icons = trance / PER_ICON;
        boolean partial_icon = trance % PER_ICON > 0;

        int left = guiGraphics.guiWidth() / 2 + 10;
        int top = guiGraphics.guiHeight() - 49;


        for (int i = 0; i < ICON_COUNT; i++) {
            int x = left + i * ICON_SPACING;

            guiGraphics.blitSprite(ICON_BG, x, top, ICON_SIZE, ICON_SIZE);

            if (i < full_icons) {
                guiGraphics.blitSprite(ICON_FULL, x, top, ICON_SIZE, ICON_SIZE);
            } else if (i == full_icons && partial_icon) {
                guiGraphics.blitSprite(ICON_PARTIAL, x, top, ICON_SIZE, ICON_SIZE);
            }
        }

    }

    // When logging out, clear the cache
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientHypnoCache.clear();
    }

}
