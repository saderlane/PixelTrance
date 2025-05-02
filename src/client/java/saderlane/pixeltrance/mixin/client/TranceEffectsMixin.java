package saderlane.pixeltrance.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.client.effects.visual.TrancePromptRenderer;

@Mixin(InGameHud.class)
public class TranceEffectsMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderTranceEffects(DrawContext context, float tickDelta, CallbackInfo ci) {
        TrancePromptRenderer.render(context);
    }
}
