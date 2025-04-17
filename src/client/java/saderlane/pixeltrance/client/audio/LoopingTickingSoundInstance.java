package saderlane.pixeltrance.client.audio;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

// A positional ticking sound that loops at the player's location
public class LoopingTickingSoundInstance extends MovingSoundInstance {

    private final LivingEntity inducer;

    public LoopingTickingSoundInstance(SoundEvent sound, LivingEntity inducer, float volume) {
        super(sound, SoundCategory.PLAYERS, Random.create());
        this.inducer = inducer;
        this.volume = volume;
        this.pitch = 1.0f;
        this.repeat = true;
        this.repeatDelay = 0;
        this.relative = false; // Positional audio - subjects nearby can hear
        updatePosition(); // Set initial position
    }

    // Called every tick to update this sound's location and see if inducer is still valid
    @Override
    public void tick() {
        if (inducer == null || !inducer.isAlive()) {
            this.setDone(); // Stop sound if inducer is gone
            return;
        }

        updatePosition();
    }

    // Syncs sound position with inducer current location
    private void updatePosition() {
        Vec3d pos = inducer.getPos();
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public void stop() {
        this.setDone();
    }

}
