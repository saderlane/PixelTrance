package saderlane.pixeltrance.data;

import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

// Registry for mob trance behavior profiles
public class MobTranceProfiles {

    private static final Map<EntityType<?>, MobTranceProfile> PROFILE_MAP = new HashMap<>();

    // Register trance profile for given mob type
    public static void register(EntityType<?> type, MobTranceProfile profile) {
        PROFILE_MAP.put(type, profile);
    }

    // Get trance profile for a given mob type
    public static MobTranceProfile get(EntityType<?> type) {
        return PROFILE_MAP.getOrDefault(type, DEFAULT);
    }

    // Default profile: mob does nothing special when tranced
    private static final MobTranceProfile DEFAULT = new MobTranceProfile(
            false, // attentionGrabbed
            false, // slowsMovement
            type -> false // Not supported by default
    );

}
