package saderlane.pixeltrance.client.gameplay.resistance;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import saderlane.pixeltrance.client.data.ClientTranceState;
import saderlane.pixeltrance.client.network.ClientTranceSender;
import saderlane.pixeltrance.util.PTLog;

public class MouseShakeTracker implements ClientModInitializer {
    private float lastYaw = Float.NaN;
    private float lastPitch = Float.NaN;
    private long lastDecayTime = 0;
    private static final float SHAKE_THRESHOLD = 30.0f;
    private static final long DECAY_COOLDOWN_MS = 300; // Only allow one decay per 300ms
    private static final float DECAY_AMOUNT = 0.5f;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused() || client.player == null || client.cameraEntity == null) return;

            float trance = ClientTranceState.getTrance();
            float focus = ClientTranceState.getFocus();
            if (trance >= 90f) return;

            // Get current yaw and pitch
            float yaw = client.cameraEntity.getYaw();
            float pitch = client.cameraEntity.getPitch();

            // Make sure they aren't the same
            if (Float.isNaN(lastYaw)) {
                lastYaw = yaw;
                lastPitch = pitch;
                return;
            }

            // Find changes in pitch/yaw and get the total delta
            float yawDelta = Math.abs(yaw - lastYaw);
            float pitchDelta = Math.abs(pitch - lastPitch);
            float totalShake = yawDelta + pitchDelta;

            // Always log shake intensity for debugging
            //PTLog.info("[Escape] Total Shake: " + totalShake);

            if (totalShake > SHAKE_THRESHOLD) {
                long now = System.currentTimeMillis();
                if (now - lastDecayTime >= DECAY_COOLDOWN_MS) {
                    lastDecayTime = now;

                    // Scale decay amount based on how much the shake exceeds threshold
                    float intensity = (totalShake - SHAKE_THRESHOLD) / SHAKE_THRESHOLD; // 0.0 = threshold, 1.0 = 2x threshold
                    float scaledDecay = DECAY_AMOUNT * (1.0f + intensity); // Minimum 0.5, scales higher

                    if (trance > 0f) {
                        trance = Math.max(0f, trance - scaledDecay);
                        ClientTranceState.setTrance(trance);
                        PTLog.info("[Escape] Trance reduced to " + trance);
                    }
                    if (focus > 0f) {
                        focus = Math.max(0f, focus - scaledDecay);
                        ClientTranceState.setFocus(focus);
                        PTLog.info("[Escape] Focus reduced to " + focus);
                    }

                    ClientTranceSender.send(trance, focus);
                }
            }

            lastYaw = yaw;
            lastPitch = pitch;
        });
    }
}
