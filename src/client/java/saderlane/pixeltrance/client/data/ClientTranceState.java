package saderlane.pixeltrance.client.data;

// This class will only exist on the client
// Stores the most recent trance value synced from the server
// The HUD will read from here when rendering

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ClientTranceState {

    // Cached variables for the local player
    private static float trance = 0.0f;
    private static float focus = 0.0f;
    private static boolean focusLocked = false;

    private static Integer inducerEntityId = null; // ID of the current inducer from server


    // === Trance Methods ===
    // Get caches trance value
    public static float getTrance() {
        return trance;
    }

    // Update trance value
    public static void setTrance(float value) {
        trance = value;
    }

    // === Focus Methods ===
    // Get focus for client
    public static float getFocus() {
        return focus;
    }

    // Update focus value
    public static void setFocus(float value) {
        focus = value;
    }

    // Get focus session status
    public static boolean getFocusLocked() {
        return focusLocked;
    }

    // Set focus session status
    public static void setFocusLocked(boolean value) {
        focusLocked = value;
    }

    // === Inducer Tracking Methods ===
    // Set Inducer Entity ID
    public static void setInducerEntityId(int id) {
        inducerEntityId = id;
    }

    // Remove inducer entity ID
    public static void clearInducerEntityID() {
        inducerEntityId = null;
    }

    //Get inducer entity ID
    public static Integer getInducerEntityId() {
        return inducerEntityId;
    }

    // Resolve inducer entity from current world
    public static LivingEntity resolveInducer(MinecraftClient client) {
        if (inducerEntityId == null || client.world == null) return null;

        Entity entity = client.world.getEntityById(inducerEntityId);
        if (entity instanceof LivingEntity living)
        {
            return living;
        }
        return null;
    }

}
