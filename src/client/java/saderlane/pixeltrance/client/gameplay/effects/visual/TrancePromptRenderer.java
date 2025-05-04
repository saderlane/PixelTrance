package saderlane.pixeltrance.client.gameplay.effects.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import saderlane.pixeltrance.client.data.ClientTranceState;

public class TrancePromptRenderer {

    private static final int MAX_FADE_TICKS = 40;
    private static final int MAX_HOLD_TICKS = 60;        // How long the prompt stays fully visible
    private static final int MAX_FADEOUT_TICKS = 40;

    private static final String[] PROMPTS = {
            "OBEY", "SUBMIT", "RELAX", "GIVE IN", "SLEEP", "DRIFT", "SURRENDER"
    };

    private static String currentPrompt = null;

    private static int fadeTicks = 0;
    private static int holdTicks = 0;
    private static int fadeOutTicks = 0;

    private static boolean fadingIn = true;
    private static boolean holding = false;
    private static boolean fadingOut = false;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float trance = ClientTranceState.getTrance();

        // Trigger a new prompt only if no prompt is currently active
        if (currentPrompt == null && trance >= 100f) {
            currentPrompt = PROMPTS[player.getRandom().nextInt(PROMPTS.length)];
            fadeTicks = holdTicks = fadeOutTicks = 0;
            fadingIn = true;
            holding = false;
            fadingOut = false;
        }

        if (currentPrompt != null) {
            if (fadingIn) {
                fadeTicks++;
                if (fadeTicks >= MAX_FADE_TICKS) {
                    fadingIn = false;
                    holding = true;
                }
            } else if (holding) {
                holdTicks++;
                if (holdTicks >= MAX_HOLD_TICKS) {
                    holding = false;
                    fadingOut = true;
                }
            } else if (fadingOut) {
                fadeOutTicks++;
                if (fadeOutTicks >= MAX_FADEOUT_TICKS) {
                    resetPrompt();
                    return;
                }
            }

            // Calculate alpha
            float alpha = fadingIn
                    ? (fadeTicks / (float) MAX_FADE_TICKS)
                    : (fadingOut ? 1f - (fadeOutTicks / (float) MAX_FADEOUT_TICKS) : 1f);

            alpha = Math.max(0f, Math.min(1f, alpha));

            // Render prompt
            int centerX = context.getScaledWindowWidth() / 2;
            int centerY = context.getScaledWindowHeight() / 2;

            double time = System.currentTimeMillis() / 400.0;
            float wave = (float) Math.sin(time);
            int red = (int) (lerp(1.0f, 0.7f, wave) * 255);
            int green = (int) (lerp(1.0f, 0.3f, wave) * 255);
            int blue = (int) (lerp(1.0f, 1.0f, wave) * 255);
            int alphaInt = (int)(255 * alpha);
            int color = (alphaInt << 24) | (red << 16) | (green << 8) | blue;

            MatrixStack matrices = context.getMatrices();
            matrices.push();

            float baseScale = 2.0f;
            float shimmer = (float)(Math.sin(System.currentTimeMillis() / 300.0) * 0.05f);
            float scale = baseScale + shimmer;

            matrices.translate(centerX, centerY, 0);
            matrices.scale(scale, scale, 1f);
            matrices.translate(-centerX, -centerY, 0);

            int textWidth = client.textRenderer.getWidth(currentPrompt);
            int x = centerX - (textWidth / 2);
            int y = centerY;

            context.drawText(client.textRenderer, Text.literal(currentPrompt), x, y, color, true);
            matrices.pop();
        }
    }

    public static void showSuggestion(String message) {
        currentPrompt = message;
        fadeTicks = holdTicks = fadeOutTicks = 0;
        fadingIn = true;
        holding = false;
        fadingOut = false;
    }

    private static void resetPrompt() {
        currentPrompt = null;
        fadeTicks = holdTicks = fadeOutTicks = 0;
        fadingIn = false;
        holding = false;
        fadingOut = false;
    }

    private static float lerp(float a, float b, float progress) {
        return a + (b - a) * ((progress + 1f) / 2f);
    }
}
