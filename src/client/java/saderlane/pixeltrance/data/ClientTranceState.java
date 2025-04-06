package saderlane.pixeltrance.data;

// This class will only exist on the client
// Stores the most recent trance value synced from the server
// The HUD will read from here when rendering

public class ClientTranceState {

    // Cached variables for the local player
    private static float trance = 0.0f;
    private static float focus = 0.0f;
    private static boolean focusSessionActive = false;


    // Get caches trance value
    public static float getTrance() {
        return trance;
    }

    // Update trance value
    public static void setTrance(float value) {
        trance = value;
    }

    // Get focus for client
    public static float getClientFocus() {
        return focus;
    }

    // Update focus value
    public static void setFocus(float value) {
        focus = value;
    }

    // Get focus session status
    public static boolean getFocusSessionActive() {
        return focusSessionActive;
    }

    // Set focus session status
    public static void setFocusSessionActive(boolean value) {
        focusSessionActive = value;
    }
}
