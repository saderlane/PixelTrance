package saderlane.pixeltrance.api;

import net.minecraft.entity.LivingEntity;

public class MobInducerWrapper implements Inducer{

    private final LivingEntity mob;

    public MobInducerWrapper(LivingEntity mob) {
        this.mob = mob;
    }

    public LivingEntity getMob() {
        return mob;
    }

    @Override
    public float getFocusRate() {
        return 0.3f; // Lower than item-based inducers
    }

    @Override
    public int getFocusInterval() {
        return 20; // Every 1 second
    }

    @Override
    public float getTranceRate() {
        return 0f;
    }

    @Override
    public int getTranceInterval() {
        return 20;
    }
}
