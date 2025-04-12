package saderlane.pixeltrance.logic;

import net.minecraft.entity.LivingEntity;

/**
 * Represents a trance-affecting source such as an item, mob, or block entity.
 * Each source controls how much trance to apply and how often.
 */
public class TranceEffectSource {

    private final LivingEntity source;

    // The amount of trance this source contributes when triggered
    private final float trancePerPulse;

    // The number of ticks between trance applications (e.g., 10 = once every 0.5s)
    private final int intervalTicks;

    // Tracks time since the last trance pulse
    private int ticksSinceLastPulse = 0;

    public TranceEffectSource(LivingEntity source, float trancePerPulse, int intervalTicks) {
        this.source = source;
        this.trancePerPulse = trancePerPulse;
        this.intervalTicks = intervalTicks;
    }

    // Returns the source entity producing this trance effect.
    public LivingEntity getSource() {
        return source;
    }

    // Updates the internal tick counter and checks if it's time to pulse.
    public boolean shouldPulse() {
        ticksSinceLastPulse++;
        if (ticksSinceLastPulse >= intervalTicks) {
            ticksSinceLastPulse = 0;
            return true;
        }
        return false;
    }

    // Returns the amount of trance this source contributes per pulse.
    public float getTrancePerPulse() {
        return trancePerPulse;
    }

    // Resets the internal tick counter manually (optional utility).
    public void reset() {
        ticksSinceLastPulse = 0;
    }
}
