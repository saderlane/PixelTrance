package saderlane.pixeltrance.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.client.data.ClientTranceState;

@Mixin(KeyboardInput.class)
public abstract class TranceMovementBlockMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void blockInputIfTranced(boolean slowDown, float f, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (ClientTranceState.getTrance() >= 100f) {
            ClientPlayerEntity player = client.player;

            // Block jump input
            player.input.jumping = false;

            // Force sprint key off to prevent re-trigger
            player.input.sneaking = false; // Optional: if you want to prevent sneaking too
            player.input.movementForward = Math.min(player.input.movementForward, 0.5f); // Limit forward speed
            player.setSprinting(false);
        }
    }
}