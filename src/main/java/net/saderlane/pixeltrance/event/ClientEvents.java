package net.saderlane.pixeltrance.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.block.custom.SpiraliteLampBlock;
import net.saderlane.pixeltrance.client.ClientHypnoCache;
import net.saderlane.pixeltrance.sound.ModSounds;
import net.saderlane.pixeltrance.util.ModKeyMappings;

import static net.saderlane.pixeltrance.hypno.HypnoTargeting.customPick;

@EventBusSubscriber(modid = PixelTrance.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    // Locations of trance bar images
    private static final ResourceLocation[] ICON_STAGES = {
            ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_bg"),
            ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_quarter"),
            ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_half"),
            ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_3quarter"),
            ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_icon_full")
    };


    // Icon final variables
    private static final int ICON_COUNT = 9;
    private static final int ICON_SIZE = 8;
    private static final int ICON_SPACING = 9;
    private static final float PER_ICON = 100f / ICON_COUNT;

    // Wave variables so I don't kms
    private static final double WAVE_SPEED = 4.0;
    private static final double WAVE_STEP = 0.6;
    private static final double WAVE_AMP = 2.0;
    private static final int WAVE_TRIGGER = 50;

    // Vignette variables
    private static final ResourceLocation VIGNETTE = ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_vignette");
    private static final int VIGNETTE_START = 50;
    private static final int VIGNETTE_FULL = 80;
    private static final float VIGNETTE_MAX_ALPHA = 0.6f;
        // Pulse variables
    private static final int PULSE_START = 50;
    private static final double PULSE_SPEED = 2.5;
    private static final float PULSE_AMP = 0.15f;


    // Subliminal variables
    private static boolean LAMP_SUBLIMINAL = false;
    private static float TRANSPARENCY = 0;
    private static final int SUBLIMINAL_START = 50;
    private static final String[] SUBLIMINALS = {
            "SLEEP",
            "DROP",
            "DEEPER",
            "FALL"
    };
    private static String CURRENT_SUBLIMINAL = SUBLIMINALS[0];


    // Building non-infinite ear fucking for binaural
    private static TranceLoopSoundInstance tranceSoundInstance;
    private static boolean wasTrancing = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;   // clicks can drain on a tick with no world loaded

        // Every tick, update player's hypno values
        float trance = ClientHypnoCache.getTrance();
        float focus = ClientHypnoCache.getFocus();

        // When K key is pressed
        while (ModKeyMappings.PRESS_K.consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Trance: " + trance + " / 100"));
        }

        // When L key is pressed
        while (ModKeyMappings.PRESS_L.consumeClick()) {
            //Do things --> On client
            player.sendSystemMessage(Component.literal(
                    "Focus: " + focus + " / 100"));
        }

        // Update the binaural audio state per tick based on trance value
        updateTranceSound(trance);


        Level level = player.level();
        HitResult hit = customPick(player, 8);

        LAMP_SUBLIMINAL = false;

        if ( hit instanceof BlockHitResult blockHitResult && blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos(); // Get the hit block's pos
            BlockState blockState = level.getBlockState(blockPos); // Get the blockstate of that block


            // If the block is a Spiralite Lamp and it is clicked
            if (blockState.getBlock() instanceof SpiraliteLampBlock &&
                    blockState.getValue(SpiraliteLampBlock.CLICKED) &&
                    trance >= SUBLIMINAL_START) {
                LAMP_SUBLIMINAL = true;

            }
        }
        // Only re-roll the word while it's invisible, so it stays locked once it starts fading in
        if (TRANSPARENCY == 0f) {
            CURRENT_SUBLIMINAL = SUBLIMINALS[player.getRandom().nextInt(SUBLIMINALS.length)];
        }

        TRANSPARENCY = Mth.approach(TRANSPARENCY, LAMP_SUBLIMINAL ? 1f : 0f, 0.05f);
    }

    private static void renderSubliminalText(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float trance = ClientHypnoCache.getTrance();

        String subliminal = CURRENT_SUBLIMINAL; // Chosen in the tick, not per frame

        Font font = Minecraft.getInstance().font;

        if (TRANSPARENCY < 0.02f) return;

        int x = guiGraphics.guiWidth() / 2 - font.width(subliminal) / 2; // Start half the text's width left of center
        int y = guiGraphics.guiHeight() / 2;

        double waveStrength = Math.clamp((trance - WAVE_TRIGGER) / 40.0, 0.0, 1.0);

        int color = FastColor.ARGB32.color((int) (TRANSPARENCY * 255), 170, 0, 255);

        double time = System.nanoTime() / 1_000_000_000.0; // Seconds


        for (int i = 0; i < subliminal.length(); i++) {
            String letter = String.valueOf(subliminal.charAt(i));
            int letterY = y; // Fresh copy per letter so offsets don't stack

            if (waveStrength > 0.0) {
                double phase = time * WAVE_SPEED + i * WAVE_STEP;
                letterY += (int) Math.round(Math.sin(phase) * WAVE_AMP * waveStrength);
            }

            guiGraphics.drawString(font, letter, x, letterY, color);

            x += font.width(letter);
        }

    }


    private static void updateTranceSound(float trance) {
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
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "trance_vignette"),
                ClientEvents::renderTranceVignette);
        event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "subliminal"),
                ClientEvents::renderSubliminalText);
    }


    private static void renderTranceBar(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mcInstance = Minecraft.getInstance();

        // If gui isn't hidden and player isn't in creative
        if (mcInstance.options.hideGui || mcInstance.player.isCreative()) return;

        float trance = ClientHypnoCache.getTrance(); // Get their current trance
        int full_icons = (int) (trance / PER_ICON); // Find how many full trance icons there are
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

            float fill = Mth.clamp( (trance - i * PER_ICON) / PER_ICON, 0f, 1f ); // How full is the icon
            int quarters = Mth.ceil(fill * 4); // Which quarter of the icon is going to be drawn
            guiGraphics.blitSprite(ICON_STAGES[quarters], x, y, ICON_SIZE, ICON_SIZE);
        }

    }

    private static void renderTranceVignette(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float trance = ClientHypnoCache.getTrance();

        // If trance isn't high enough for a vignette
        if (trance < VIGNETTE_START || Minecraft.getInstance().options.hideGui) return;

        // Set vignette density
        float t = Mth.clamp( (trance - VIGNETTE_START) / (float) (VIGNETTE_FULL - VIGNETTE_START), 0f, 1f);
        float alpha = t * VIGNETTE_MAX_ALPHA;

        // Set vignette pulse
        float pulseStrength = Mth.clamp((trance - PULSE_START) / 20f, 0f, 1f);
        double time = System.nanoTime() / 1_000_000_000.0;
        alpha += (float) Math.sin(time * PULSE_SPEED) * PULSE_AMP * pulseStrength;


        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();


        RenderSystem.setShaderColor(0.6f, 0.2f, 0.9f, alpha);
        guiGraphics.blitSprite(VIGNETTE, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();



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
            float minVol = 0.05f;
            float maxVol = .9f;
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
