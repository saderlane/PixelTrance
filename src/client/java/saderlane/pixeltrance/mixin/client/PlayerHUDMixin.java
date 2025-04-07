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

import saderlane.pixeltrance.client.audio.TranceAudioHandler;
import saderlane.pixeltrance.client.data.ClientTranceState;

// Mixin to inject trance bar above food bar
@Mixin(InGameHud.class)
public abstract class PlayerHUDMixin {

    private static final Identifier TRANCE_ICON_TEXTURE = new Identifier("pixeltrance", "textures/gui/hud/trance_bar.png");


    @Inject(method = "renderStatusBars", at = @At("TAIL"))
    private void renderTranceBar(DrawContext context, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        // Don't render if no player is present
        if (client.player == null) return;

        float trance = ClientTranceState.getTrance();


        // === BAR CONFIGURATION ===
        int iconSize = 7;                 // Final on-screen render size
        int spacing = 1;                  // Space between icons
        int maxIcons = 10;

        // === TEXTURE CONFIGURATION ===
        // The icon texture must be a scale of a 21x7 where each icon is a 7x7 cube
        int frameSize = 21;               // Size of a single icon frame in the texture
        int textureWidth = 63;            // Total texture width (e.g. 3 icons * 21px)
        int textureHeight = 21;           // Total texture height
        float scale = (float) iconSize / frameSize;


        // === POSITIONING ===
        int xStart = client.getWindow().getScaledWidth() / 2 + 10;
        int yBase = client.getWindow().getScaledHeight() - 48;

        TranceAudioHandler.updateTranceSound(trance,ClientTranceState.getFocus());

        // Fill icons based on trance status
        for (int i = 0; i < maxIcons; i++)
        {

            float iconMin = i * 10f;
            float iconMax = iconMin + 10f;
            float iconValue = trance - iconMin;

            int u = frameSize * 2; // Default to empty icon
            if (iconValue >= 7f) // Set to full icon
            {
                u = 0;
            }
            else if (iconValue >= 1f) // Set to half icon
            {
                u = frameSize;
            }

            int iconX = xStart + i * (iconSize + spacing);
            int iconY = yBase;

            // Icons move on a wave if trance is high
            if (trance >= 50)
            {
                float time = (client.world.getTime() + client.getTickDelta()) * 0.25f;
                float wavePhase = i * 0.6f;
                iconY += (int)(Math.sin(time + wavePhase) * 2.5f);

            }

            // === Draw Icon with scale transformation ===
            context.getMatrices().push();
            context.getMatrices().translate(iconX, iconY, 0);       // Move to HUD icon position
            context.getMatrices().scale(scale, scale, 1f);       // Scale 21×21 → 7×7

            // Draw icon at (0, 0) in scaled space
            context.drawTexture(
                    TRANCE_ICON_TEXTURE,
                    0, 0,                   // screen coords (already translated)
                    u, 0,                   // texture crop origin
                    frameSize, frameSize,   // crop size
                    textureWidth, textureHeight                  // full texture size
            );
            context.getMatrices().pop();
        }



    }

}
