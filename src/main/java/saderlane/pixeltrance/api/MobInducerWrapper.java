package saderlane.pixeltrance.api;

import net.minecraft.entity.LivingEntity;

public class MobInducerWrapper implements Inducer{

    // Variable to store what mob is the inducer
    private final LivingEntity mob;

    // Constructor setting the mob as an inducer
    public MobInducerWrapper(LivingEntity mob) {
        this.mob = mob;
    }

    // Returns the mob
    public LivingEntity getMob() {
        return mob;
    }


    // === Inducer: Focus Properties ===
    @Override
    public float getFocusRate() {
        return 0.3f; // Lower than item-based inducers
    }

    @Override
    public int getFocusInterval() {
        return 20; // Every 1 second
    }


    // === Inducer: Trance Properties ===
    @Override
    public float getTranceRate() {
        return 0f;
    }

    @Override
    public int getTranceInterval() {
        return 20;
    }


    // === Inducer: Custom Properties ===
    @Override
    public boolean requiresLineOfSight() {
        return true;
    }
}
