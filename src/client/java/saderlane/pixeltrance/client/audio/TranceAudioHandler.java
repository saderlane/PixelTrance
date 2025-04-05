package saderlane.pixeltrance.client.audio;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import saderlane.pixeltrance.sound.TranceSounds;
import saderlane.pixeltrance.util.PTLog;

public class TranceAudioHandler {

    // Holds the currently playing looping sound instance (if any)
    private static LoopingTranceSoundInstance activeSound = null;

    public static void updateTranceSound(float tranceValue, float focusValue) {
        MinecraftClient client = MinecraftClient.getInstance();
        SoundManager soundManager = client.getSoundManager();

        boolean shouldPlay = tranceValue > 30;

        if (shouldPlay) {
            // Calculate volume from 0.0 (at 30 trance) to 1.0 (at 100 trance)
            float scaledVolume = Math.min((tranceValue - 30f) / 70f, 1.0f);

            if (activeSound == null) {
                // Log for debugging
                PTLog.info("Starting trance sound, volume: " + scaledVolume);

                // Start looping sound
                activeSound = new LoopingTranceSoundInstance(TranceSounds.TRANCE_TRIGGER, scaledVolume);
                soundManager.play(activeSound);
            }
            else
            {
                // Update existing sound volume
                activeSound.setVolume(scaledVolume);
            }
        } else if (activeSound != null) {
            // Log for debugging
            PTLog.info("Stopping trance sound — trance fell below threshold");

            // Stop sound if it's no longer needed
            soundManager.stop(activeSound);
            activeSound = null;
        }
    }

}
