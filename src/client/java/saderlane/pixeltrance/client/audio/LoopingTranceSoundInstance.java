package saderlane.pixeltrance.client.audio;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class LoopingTranceSoundInstance extends AbstractSoundInstance{

    public LoopingTranceSoundInstance(SoundEvent sound, float volume) {
        super(sound, SoundCategory.MASTER, Random.create());
        this.volume = volume;
        this.pitch = 1.0f;
        this.repeat = true;
        this.repeatDelay = 0;
        this.relative = true; // Plays in headphones, not in world
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    // Called by Minecraft to determine the current volume of this sound
    @Override
    public float getVolume() {
        return this.volume;
    }

    // Let us update the volume dynamically
    public void setVolume(float newVolume) {
        this.volume = newVolume;
    }

}
