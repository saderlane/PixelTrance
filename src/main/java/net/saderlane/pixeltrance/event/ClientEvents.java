package net.saderlane.pixeltrance.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.client.ClientHypnoCache;
import net.saderlane.pixeltrance.sound.ModSounds;
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

    // Wave variables so I don't kms
    private static final double WAVE_SPEED = 4.0;
    private static final double WAVE_STEP = 0.6;
    private static final double WAVE_AMP = 2.0;
    private static final int WAVE_TRIGGER = 50;

    // Building non-infinite ear fucking for binaural
    private static TranceLoopSoundInstance tranceSoundInstance;
    private static boolean wasTrancing = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;   // clicks can drain on a tick with no world loaded

        // When K key is pressed
        while (ModKeyMappings.PRESS_K.consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Trance: " + ClientHypnoCache.getTrance() + " / 100"));
        }

        // When L key is pressed
        while (ModKeyMappings.PRESS_L.consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Focus: " + ClientHypnoCache.getFocus() + " / 100"));
        }

        int trance = ClientHypnoCache.getTrance();
        updateTranceSound(trance);

    }


    private static void updateTranceSound(int trance) {
        boolean isInTrance = trance >= 50;
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();

        if (isInTrance && !wasTrancing) {
            tranceSoundInstance = new TranceLoopSoundInstance(ModSounds.TRANCE_BINAURAL.get());
            soundManager.play(tranceSoundInstance);
        } else if (!isInTrance && wasTrancing) {
            if (tranceSoundInstance != null) {
                tranceSoundInstance.requestStop();
                soundManager.stop(tranceSoundInstance);
                tranceSoundInstance = null;
            }
        }

        wasTrancing = isInTrance;
    }



    @SubscribeEvent
    public static void registerHUD(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR, ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_bar"),
            ClientEvents::renderTranceBar);
    }

    private static void renderTranceBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mcInstance = Minecraft.getInstance();

        // If gui isn't hidden and player isn't in creative
        if (mcInstance.options.hideGui || mcInstance.player.isCreative()) return;

        int trance = ClientHypnoCache.getTrance(); // Get their current trance
        int full_icons = trance / PER_ICON; // Find how many full trance icons there are
        boolean partial_icon = trance % PER_ICON > 0; // Is there a partially filled icon?

        int left = guiGraphics.guiWidth() / 2 + 10; // Left spacing for trance bar
        int top = guiGraphics.guiHeight() - 49; // Top spacing for trance bar

        double waveStrength = Math.clamp((trance - WAVE_TRIGGER) / 40.0, 0.0, 1.0); // Determine wave strength
                                                                                    // Intensity scales 50-70:0-1
        double time = System.nanoTime() / 1_000_000_000.0; // Seconds


        // Draw the icons
        for (int i = 0; i < ICON_COUNT; i++) {
            int x = left + i * ICON_SPACING;
            int y = top;

            if (waveStrength > 0.0) {
                double phase = time * WAVE_SPEED + i * WAVE_STEP;
                y += (int) Math.round(Math.sin(phase) * WAVE_AMP * waveStrength);
            }

            guiGraphics.blitSprite(ICON_BG, x, y, ICON_SIZE, ICON_SIZE);

            if (i < full_icons) { // If there needs to be more trance icons
                guiGraphics.blitSprite(ICON_FULL, x, y, ICON_SIZE, ICON_SIZE);
            } else if (i == full_icons && partial_icon) { // If there needs to be a partial icon
                guiGraphics.blitSprite(ICON_PARTIAL, x, y, ICON_SIZE, ICON_SIZE);
            }
        }

    }

    // When the player disconnects
    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientHypnoCache.clear(); //clear hypno cache
        updateTranceSound(0); //Kill the sound when logging out
    }

    // When the player dies and respawns
    @SubscribeEvent
    public static void onRespawn(ClientPlayerNetworkEvent.Clone event) {
        ClientHypnoCache.clear(); //clear hypno cache
        updateTranceSound(0); // Kill the sound on respawn
    }




    private static class TranceLoopSoundInstance extends AbstractTickableSoundInstance {
        private boolean stopped = false;

        TranceLoopSoundInstance(SoundEvent soundEvent) {
            super(soundEvent, SoundSource.PLAYERS, RandomSource.create());
            this.looping = true;
            this.relative = true;
            this.volume = setVolume();
            this.pitch = 1f;
        }

        @Override
        public void tick() {
            this.volume = setVolume();
        }

        private float setVolume() {
            float minVol = 0.3f;
            float maxVol = 1.0f;
            float t = Mth.clamp((ClientHypnoCache.getTrance() - 50) / 30f, 0f, 1f);
            return Mth.lerp(t, minVol, maxVol);
        }

        @Override
        public boolean isStopped() {
            return stopped;
        }

        void requestStop() {
            stopped = true;
        }
    }

}
