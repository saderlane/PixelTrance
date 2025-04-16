package saderlane.pixeltrance.api;

import net.minecraft.entity.LivingEntity;
import saderlane.pixeltrance.data.MobHypnosisProfile;
import saderlane.pixeltrance.data.MobHypnosisProfiles;

public class MobInducerWrapper implements Inducer{

    // Variable to store what mob is the inducer and its hypnosis profile
    private final LivingEntity mob;
    private final MobHypnosisProfile profile;

    // Constructor setting the mob as an inducer
    public MobInducerWrapper(LivingEntity mob) {
        this.mob = mob;
        this.profile = MobHypnosisProfiles.get(mob.getType());
    }

    // Returns the mob
    public LivingEntity getMob() {
        return mob;
    }


    // === Inducer: Focus Properties ===
    @Override
    public float getFocusRate() {
        return profile.getFocusRate();
    }

    @Override
    public int getFocusInterval() {
        return profile.getFocusInterval();
    }


    // === Inducer: Trance Properties ===
    @Override
    public float getTranceRate() {
        return profile.getTranceRate();
    }

    @Override
    public int getTranceInterval() {
        return profile.getTranceInterval();
    }


    // === Inducer: Custom Properties ===
    @Override
    public boolean requiresLineOfSight() {
        return profile.getRequiresLineOfSight();
    }
}
