package net.saderlane.pixeltrance.client;

// Read only cache of the HypnoData values
public final class ClientHypnoCache {

    private static int trance = 0;
    private static int focus = 0;

    private ClientHypnoCache() {}

    public static int getTrance() {
        return trance;
    }

    public static int getFocus() {
        return focus;
    }

    public static void set(int newTrance, int newFocus) {
        trance = newTrance;
        focus = newFocus;
    }

    // Clear the trance and focus
    public static void clear() {
        trance = 0;
        focus = 0;
    }
}