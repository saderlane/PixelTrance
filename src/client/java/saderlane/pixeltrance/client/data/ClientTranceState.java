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

    private static Integer tranceInducerEntityId = null; // ID of the current trance inducer from server
    private static Integer focusInducerEntityId = null; // ID of the current focus inducer from server


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
    // Set Trance Inducer Entity ID
    public static void setTranceInducerEntityId(int id) {
        tranceInducerEntityId = id;
    }

    // Remove Trance inducer entity ID
    public static void clearTranceInducerEntityID() {
        tranceInducerEntityId = null;
    }

    //Get Trance inducer entity ID
    public static Integer getTranceInducerEntityId() {
        return tranceInducerEntityId;
    }

    // Resolve Trance inducer entity from current world
    public static LivingEntity resolveTranceInducer(MinecraftClient client) {
        if (tranceInducerEntityId == null || client.world == null) return null;

        Entity entity = client.world.getEntityById(tranceInducerEntityId);
        if (entity instanceof LivingEntity living)
        {
            return living;
        }
        return null;
    }

    // Set Focus Inducer Entity ID
    public static void setFocusInducerEntityId(int id) {
        focusInducerEntityId = id;
    }

    // Remove Focus inducer entity ID
    public static void clearFocusInducerEntityID() {
        focusInducerEntityId = null;
    }

    //Get Focus inducer entity ID
    public static Integer getFocusInducerEntityId() {
        return focusInducerEntityId;
    }

    // Resolve Focus inducer entity from current world
    public static LivingEntity resolveFocusInducer(MinecraftClient client) {
        if (focusInducerEntityId == null || client.world == null) return null;

        Entity entity = client.world.getEntityById(focusInducerEntityId);
        if (entity instanceof LivingEntity living)
        {
            return living;
        }
        return null;
    }

}
