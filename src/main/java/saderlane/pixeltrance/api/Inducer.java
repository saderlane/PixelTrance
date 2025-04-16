package saderlane.pixeltrance.api;


// Basic interface for any inducer — entity, item, or block — that can apply focus/trance.
public interface Inducer {

    // Focus parameters
    float getFocusRate();
    int getFocusInterval();

    // Trance parameters
    float getTranceRate();
    int getTranceInterval();

    // Custom parameters
    boolean requiresLineOfSight();

}
