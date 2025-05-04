package saderlane.pixeltrance.client.gameplay.effects.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import saderlane.pixeltrance.client.data.ClientTranceState;

public class TrancePromptRenderer {

    private static final int MAX_FADE_TICKS = 40;
    private static final int MAX_FADEOUT_TICKS = 40;
    private static final String[] PROMPTS = {
            "OBEY", "SUBMIT", "RELAX", "GIVE IN", "SLEEP", "DRIFT", "SURRENDER"
    };

    private static String currentPrompt = null;
    private static int fadeTicks = 0;
    private static int fadeOutTicks = 0;
    private static boolean fadingIn = true;
    private static boolean isFadingOut = false;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float trance = ClientTranceState.getTrance();

        // Start new prompt on full trance
        if (trance >= 100f) {
            if (currentPrompt == null) {
                currentPrompt = PROMPTS[player.getRandom().nextInt(PROMPTS.length)];
                fadeTicks = 0;
                fadingIn = true;
                isFadingOut = false;
            }

            if (fadeTicks < MAX_FADE_TICKS) fadeTicks++;
            else fadingIn = false;

        } else if (currentPrompt != null && !isFadingOut) {
            // Start fade out if trance drops
            isFadingOut = true;
            fadeOutTicks = 0;
        }

        // Handle fade-out completion
        if (isFadingOut) {
            fadeOutTicks++;
            if (fadeOutTicks >= MAX_FADEOUT_TICKS) {
                currentPrompt = null;
                fadeTicks = fadeOutTicks = 0;
                isFadingOut = false;
                return;
            }
        }

        if (currentPrompt == null) return;

        // Render prompt
        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        float alpha = isFadingOut
                ? 1f - (fadeOutTicks / (float) MAX_FADEOUT_TICKS)
                : (fadingIn ? (fadeTicks / (float) MAX_FADE_TICKS) : 1f);

        alpha = Math.max(0f, Math.min(1f, alpha));

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

    private static float lerp(float a, float b, float progress) {
        return a + (b - a) * ((progress + 1f) / 2f);
    }
}
