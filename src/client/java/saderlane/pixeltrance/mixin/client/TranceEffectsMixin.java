package saderlane.pixeltrance.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.client.data.ClientTranceState;

// Mixin for trance-induced gameplay effects (prompts, controls, etc)
@Mixin(InGameHud.class)
public abstract class TranceEffectsMixin {

    private static final int MAX_FADE_TICKS = 40; // 2 seconds at 20 TPS
    private static final int MAX_FADEOUT_TICKS = 40; // Fade out time


    private int fadeTicks = 0; // Counter for fade-in ticks
    private int fadeOutTicks = 0; // Counter for fade-out ticks
    private boolean fadingIn = true; // If text is fading in
    private boolean isFadingOut = false; // If text is fading out


    // Pool of possible messages @ trance=100
    private static final String[] PROMPTS = {
            "OBEY",
            "SUBMIT",
            "RELAX",
            "GIVE IN",
            "SLEEP",
            "DRIFT",
            "SURRENDER"
    };

    private String currentPrompt = null; // Which prompt is currently showing (null if none)

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTranceEffects(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null) return;

        float trance = ClientTranceState.getTrance();

        if (trance >= 100f) {
            if (currentPrompt == null) {
                // First time reaching 100% trance — pick a random prompt
                currentPrompt = PROMPTS[player.getRandom().nextInt(PROMPTS.length)];
                fadeTicks = 0;
                fadingIn = true;
                isFadingOut = false;
            }

            // Continue fading in if not full yet
            if (fadeTicks < MAX_FADE_TICKS) {
                fadeTicks++;
            } else {
                fadingIn = false;
            }

        } else {
            // Start fading out once trance drops below 100
            if (currentPrompt != null && !isFadingOut) {
                isFadingOut = true;
                fadeOutTicks = 0;
            }

            if (isFadingOut) {
                fadeOutTicks++;
                if (fadeOutTicks >= MAX_FADEOUT_TICKS) {
                    // Fully faded out
                    currentPrompt = null;
                    fadeTicks = 0;
                    fadeOutTicks = 0;
                    isFadingOut = false;
                    return; // Don't draw after prompt cleared
                }
            }
        }

        if (currentPrompt != null) {
            int centerX = context.getScaledWindowWidth() / 2;
            int centerY = context.getScaledWindowHeight() / 2;


            // Calculate opacity
            float alpha;
            if (isFadingOut) {
                alpha = 1f - (fadeOutTicks / (float) MAX_FADEOUT_TICKS);
            } else {
                alpha = fadingIn ? (fadeTicks / (float) MAX_FADE_TICKS) : 1f;
            }

            alpha = Math.max(0f, Math.min(1f, alpha)); // clamp

            // === Rolling gradient calculation ===
            double time = System.currentTimeMillis() / 400.0;
            float wave = (float) Math.sin(time); // Sine wave for smooth back-and-forth

            // Calculate color components between White (1.0, 1.0, 1.0) and Purple-ish (0.7, 0.3, 1.0)
            int red = (int) (lerp(1.0f, 0.7f, wave) * 255);
            int green = (int) (lerp(1.0f, 0.3f, wave) * 255);
            int blue = (int) (lerp(1.0f, 1.0f, wave) * 255);

            // Clamp RGB just to be safe
            red = Math.max(0, Math.min(255, red));
            green = Math.max(0, Math.min(255, green));
            blue = Math.max(0, Math.min(255, blue));

            int alphaInt = (int)(255 * alpha);
            int color = (alphaInt << 24) | (red << 16) | (green << 8) | blue;

            MatrixStack matrices = context.getMatrices();
            matrices.push();

            // === Apply scaling and shimmer effect ===
            float baseScale = 2.0f;
            float shimmer = (float)(Math.sin(System.currentTimeMillis() / 300.0) * 0.05f);
            float scale = baseScale + shimmer;

            matrices.translate(centerX, centerY, 0);
            matrices.scale(scale, scale, 1f);
            matrices.translate(-centerX, -centerY, 0);

            // Calculate final draw position
            int textWidth = client.textRenderer.getWidth(currentPrompt);
            int x = centerX - (textWidth / 2);
            int y = centerY;

            // Draw the text
            context.drawText(
                    client.textRenderer,
                    net.minecraft.text.Text.literal(currentPrompt),
                    x,
                    y,
                    color,
                    true
            );

            matrices.pop();
        }


    }

    // Utility to interpolate between two float values based on a progress (-1 to +1)
    private static float lerp(float a, float b, float progress) {
        return a + (b - a) * ((progress + 1f) / 2f);
    }

}