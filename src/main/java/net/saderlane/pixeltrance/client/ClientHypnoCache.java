package net.saderlane.pixeltrance.client;

// Read only cache of the HypnoData values
public final class ClientHypnoCache {

    private static float trance = 0;
    private static float focus = 0;

    private ClientHypnoCache() {}

    public static float getTrance() {
        return trance;
    }

    public static float getFocus() {
        return focus;
    }

    public static void set(float newTrance, float newFocus) {
        trance = newTrance;
        focus = newFocus;
    }

    // Clear the trance and focus
    public static void clear() {
        trance = 0.0f;
        focus = 0.0f;
    }
}