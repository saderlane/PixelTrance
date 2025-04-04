package saderlane.pixeltrance.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import saderlane.pixeltrance.data.ClientTranceState;

// Mixin to inject trance bar above food bar
@Mixin(InGameHud.class)
public abstract class PlayerHUDMixin {

    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderTranceBar(DrawContext context, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        // Don't render if no player is present
        if (client.player == null) return;

        float trance = ClientTranceState.getTrance();

        // Bar settings
        int fullWidth = 81; // same width as food bar
        int height = 6;
        int filled = (int)(fullWidth * (trance / 100f));

        // Position: above food bar (typically bottom-right)
        int x = client.getWindow().getScaledWidth() / 2 + 10; // food bar starts at center + 91
        int y = client.getWindow().getScaledHeight() - 45;   // just above the food bar (y = -39 is food)

        // Background (dark gray)
        context.fill(x, y, x + fullWidth, y + height, 0xFF333333);

        // Filled portion (purple)
        context.fill(x, y, x + filled, y + height, 0xFFAA00FF);
    }

}
