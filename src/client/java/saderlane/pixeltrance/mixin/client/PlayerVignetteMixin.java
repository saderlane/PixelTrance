package saderlane.pixeltrance.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.client.data.ClientTranceState;

@Mixin(InGameHud.class)
public class PlayerVignetteMixin {

    // Use vanilla texture vignette
    private static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTranceOverlay(DrawContext context, float tickDelta, CallbackInfo ci) {
        float trance = ClientTranceState.getTrance();
        if (trance <= 30) return;

        float opacity = Math.min((trance - 30f) / 70f, 1.0f) * 0.6f;

        // Add pulsing effect if trance is 70 or higher
        if (trance >= 70f) {
            // Use a sine wave to smoothly pulse the alpha
            float pulse = (float) Math.sin(System.currentTimeMillis() / 200.0); // ~3Hz wave
            float pulseStrength = 0.1f + (pulse * 0.1f); // Range: 0.0 - 0.2
            opacity += pulseStrength;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
        RenderSystem.setShaderColor(0.7f, 0.3f, 1.0f, opacity); // purple

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        context.drawTexture(
                VIGNETTE_TEXTURE,
                0, 0, 0, 0,
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight()
        );

        matrices.pop();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();

        // Reset shader color to full brightness/alpha
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

}
