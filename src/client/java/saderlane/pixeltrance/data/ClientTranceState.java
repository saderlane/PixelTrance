package saderlane.pixeltrance.data;

// This class will only exist on the client
// Stores the most recent trance value synced from the server
// The HUD will read from here when rendering

public class ClientTranceState {

    // Cached trance and focus values for the local player
    private static float trance = 0.0f;
    private static float focus = 0.0f;

    // Get caches trance value
    public static float getTrance() {
        return trance;
    }

    // Update trance value
    public static void setTrance(float value) {
        trance = value;
    }

    // Exposes focus for client
    public static float getClientFocus() {
        return focus;
    }

    public static void setFocus(float value) {
        focus = value;
    }
}
