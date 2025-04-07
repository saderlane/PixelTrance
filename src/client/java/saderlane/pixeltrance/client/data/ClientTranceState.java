package saderlane.pixeltrance.client.data;

// This class will only exist on the client
// Stores the most recent trance value synced from the server
// The HUD will read from here when rendering

import net.minecraft.entity.LivingEntity;

public class ClientTranceState {

    // Cached variables for the local player
    private static float trance = 0.0f;
    private static float focus = 0.0f;
    private static boolean focusSessionActive = false;
    private static LivingEntity hypnoticTarget = null;


    // Get caches trance value
    public static float getTrance() {
        return trance;
    }

    // Update trance value
    public static void setTrance(float value) {
        trance = value;
    }

    // Get focus for client
    public static float getFocus() {
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

    // Get hypnotic target
    public static LivingEntity getHypnoticTarget() {
        return hypnoticTarget;
    }

    // Set hypnotic target
    public static void setHypnoticTarget(LivingEntity target) {
        hypnoticTarget = target;
    }

}
