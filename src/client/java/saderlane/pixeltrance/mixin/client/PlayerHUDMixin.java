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

        // Layout
        int maxIcons = 10;
        int iconSize = 7;
        int spacing = 1;

        // Position: above food bar
        int xStart = client.getWindow().getScaledWidth() / 2 + 10;
        int y = client.getWindow().getScaledHeight() - 48;

        for (int i = 0; i < maxIcons; i++)
        {
            int iconX = xStart + i * (iconSize + spacing);
            float iconMin = i * 10f;
            float iconMax = iconMin + 10f;

            // Draw background slot (dark gray currently)
            context.fill(iconX, y, iconX + iconSize, y + iconSize, 0xFF222222);

            if (trance >= iconMax)
            {
                context.fill(iconX, y, iconX + iconSize, y + iconSize, 0xFFAA00FF);
            }
            else if (trance > iconMin)
            {
                // Partial fill
                float percent = (trance - iconMin) / 10f;
                int fillWidth = (int)(iconSize * percent);
                context.fill(iconX, y, iconX + fillWidth, y + iconSize, 0xFFAA00FF);
            }
        }
    }

}
