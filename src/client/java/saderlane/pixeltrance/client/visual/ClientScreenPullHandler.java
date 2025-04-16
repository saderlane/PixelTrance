package saderlane.pixeltrance.client.visual;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import saderlane.pixeltrance.client.data.ClientTranceState;

// Pulls the player subject's camera to the inducer with increasing intensity as focus rises
public class ClientScreenPullHandler {

    // === Variables ===
    private static final float FOCUS_PULL_START = 30f;

    // Max pull strength at focus = 100 in degrees per tick
    private static final float MAX_YAW_ADJUST = 1.5f;
    private static final float MAX_PITCH_ADJUST = 0.8f;

    // Test target direction (North-West and up)
    private static final float TARGET_YAW = -45f;
    private static final float TARGET_PITCH = -10f;


    // === Methods ===
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.player == null || minecraftClient.isPaused()) return;

            // The subject (player being hypnotized)
            PlayerEntity player = minecraftClient.player;

            // Current synced trance state
            float focus = ClientTranceState.getFocus();

            // Screen pull only applies if locked and focus is high enough
            if (focus < FOCUS_PULL_START) return;

            // Get the inducer entity (the source of focus) from the synced entity ID
            LivingEntity inducer = ClientTranceState.resolveInducer(minecraftClient);
            if (inducer == null) return;

            // === Direction vector to inducer ===
            double dx = inducer.getX() - player.getX();
            double dy = inducer.getEyeY() - player.getEyeY();
            double dz = inducer.getZ() - player.getZ();

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance < 0.001) return; // Too close or identical position

            // === Focus-based pull strength ===
            // Starts applying at 30, ramps to full at 100
            float normalized = (focus - FOCUS_PULL_START) / (100f - FOCUS_PULL_START);
            normalized = MathHelper.clamp(normalized, 0f, 1f);
            float strength = normalized * normalized; // quadratic easing

            // === Compute yaw/pitch toward inducer ===
            double yawToTarget = Math.toDegrees(Math.atan2(-dx, dz));
            double pitchToTarget = Math.toDegrees(-Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));

            double yawDiff = MathHelper.wrapDegrees(yawToTarget - player.getYaw());
            double pitchDiff = pitchToTarget - player.getPitch();

            float newYaw = MathHelper.lerp(0.1f * strength, player.getYaw(), (float)yawToTarget);
            float newPitch = MathHelper.lerp(0.1f * strength, player.getPitch(), (float)pitchToTarget);


            // === Apply gradual rotation based on strength ===
            player.setYaw(newYaw);
            player.setPitch(newPitch);



        });
    }


    // Method to smoothly adjust angle toward target
    private static float smoothApproachAngle(float current, float target, float maxStep) {
        float diff = MathHelper.wrapDegrees(target - current);
        diff = MathHelper.clamp(diff, -maxStep, maxStep);

        return  current + diff;
    }


}