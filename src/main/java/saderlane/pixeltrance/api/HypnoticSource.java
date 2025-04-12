package saderlane.pixeltrance.api;

import net.minecraft.entity.LivingEntity;

/*
 * This interface defines any entity, item, or block that can generate hypnotic effects.
 * Implementing this allows you to specify custom trance and focus behavior per source.
 */
public interface HypnoticSource {

    // Focus effect strength to apply per pulse (0 for none)
    float getFocusStrength();

    // Ticks between focus pulses
    int getFocusInterval();

    // Trance effect strength to apply per pulse (0 for none)
    float getTranceStrength();

    // Ticks between trance pulses
    int getTranceInterval();

    // Optional: Check whether this source should apply effects to the given observer
    boolean shouldAffect(LivingEntity observer, LivingEntity source);
}
