package saderlane.pixeltrance.data;


// Defines hypnosis parameters for a specific mob
public class MobHypnosisProfile {

    // === Focus Variables ===
    private final float focusRate; // Amount of focus gained per interval
    private final int focusInterval; // Focus gain interval (tick)

    // === Trance Variables ===
    private final float tranceRate; // Amount of trance gained per interval
    private final int tranceInterval; // Trance gain interval (tick)

    // === Additional Variables ===
    private final boolean requiresLineOfSight;

    public MobHypnosisProfile(
            float focusRate, int focusInterval,
            float tranceRate, int tranceInterval,
            boolean requiresLineOfSight) {

        this.focusRate = focusRate;
        this.focusInterval = focusInterval;

        this.tranceRate = tranceRate;
        this.tranceInterval = tranceInterval;

        this.requiresLineOfSight = requiresLineOfSight;

    }


    // === Focus Methods ===

    // Return mob focus rate
    public float getFocusRate() {
        return focusRate;
    }

    // Return mob focus interval
    public int getFocusInterval() {
        return focusInterval;
    }


    // === Trance Methods ===

    // Return mob trance rate
    public float getTranceRate() {
        return tranceRate;
    }

    // Return mob trance interval
    public int getTranceInterval() {
        return tranceInterval;
    }

    // === Additional Variable Methods ===

    // Return if mob requires line of sight
    public boolean getRequiresLineOfSight() {
        return requiresLineOfSight;
    }

}
