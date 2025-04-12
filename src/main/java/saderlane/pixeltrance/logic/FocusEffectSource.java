package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;

/*
 * Represents a focus-affecting source such as a mob, item, or block entity.
 * Each source contributes a specific amount of focus at a custom tick interval.
 * This allows fine control over how quickly different hypnotic sources influence focus buildup.
 */
public class FocusEffectSource {

    private final LivingEntity source;

    // The amount of focus this source contributes per pulse
    private final float focusPerPulse;

    // Number of ticks between pulses (e.g. 20 = once per second)
    private final int intervalTicks;

    // Internal tick counter to determine when the next pulse should occur
    private int ticksSinceLastPulse = 0;

    public FocusEffectSource(LivingEntity source, float focusPerPulse, int intervalTicks) {
        this.source = source;
        this.focusPerPulse = focusPerPulse;
        this.intervalTicks = intervalTicks;
    }

    // Returns the entity responsible for generating this focus effect
    public LivingEntity getSource() {
        return source;
    }

    // Increments internal timer and returns true if a focus pulse should be applied
    public boolean shouldPulse() {
        ticksSinceLastPulse++;
        if (ticksSinceLastPulse >= intervalTicks) {
            ticksSinceLastPulse = 0;
            return true;
        }
        return false;
    }

    // Returns the amount of focus to apply when this source pulses
    public float getFocusPerPulse() {
        return focusPerPulse;
    }

    // Optionally reset the internal tick counter (not required in most cases)
    public void reset() {
        ticksSinceLastPulse = 0;
    }
}
