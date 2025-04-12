package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;
import java.util.function.BiPredicate;

/*
 * Represents the trance and focus effect settings for a specific mob type.
 * Stores values for strength, interval, and the conditions under which this mob affects others.
 * Registered in MobHypnosisRegistry for clean, scalable handling of hypnotic mobs.
 */
public class MobHypnosisProfile {

    // How much focus this mob applies per pulse (0 = none)
    private final float focusStrength;

    // Number of ticks between each focus application
    private final int focusInterval;

    // How much trance this mob applies per pulse (0 = none)
    private final float tranceStrength;

    // Number of ticks between each trance application
    private final int tranceInterval;

    // Predicate that determines whether this mob should affect the given observer
    // Parameters: (observer, mob)
    private final BiPredicate<LivingEntity, LivingEntity> shouldAffect;

    // Constructor: initializes all values for the hypnosis profile
    public MobHypnosisProfile(
            float focusStrength,
            int focusInterval,
            float tranceStrength,
            int tranceInterval,
            BiPredicate<LivingEntity, LivingEntity> shouldAffect
    ) {
        this.focusStrength = focusStrength;
        this.focusInterval = focusInterval;
        this.tranceStrength = tranceStrength;
        this.tranceInterval = tranceInterval;
        this.shouldAffect = shouldAffect;
    }

    // Returns the focus strength value for this mob
    public float getFocusStrength() {
        return focusStrength;
    }

    // Returns how often focus is applied (in ticks)
    public int getFocusInterval() {
        return focusInterval;
    }

    // Returns the trance strength value for this mob
    public float getTranceStrength() {
        return tranceStrength;
    }

    // Returns how often trance is applied (in ticks)
    public int getTranceInterval() {
        return tranceInterval;
    }

    // Evaluates whether this mob should affect the given observer
    // Returns true if focus/trance should be applied to that entity
    public boolean shouldAffect(LivingEntity observer, LivingEntity mob) {
        return shouldAffect.test(observer, mob);
    }
}
