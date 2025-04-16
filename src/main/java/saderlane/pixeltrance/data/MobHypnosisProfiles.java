package saderlane.pixeltrance.data;

import net.minecraft.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

// Registry for hypnosis parameters per mob
public class MobHypnosisProfiles {

    private static final Map<EntityType<?>, MobHypnosisProfile> PROFILE_MAP = new HashMap<>();

    // Register trance/focus profile for mob type
    public static void register(EntityType<?> type, MobHypnosisProfile profile) {
        PROFILE_MAP.put(type, profile);
    }

    // Returns hypnosis profile for mob type
    public static MobHypnosisProfile get(EntityType<?> type) {
        return PROFILE_MAP.getOrDefault(type, DEFAULT);
    }

    // Default values for mobs hypnosis profile
    private static final MobHypnosisProfile DEFAULT = new MobHypnosisProfile(
            0f, 20, 0f, 20, true
    );

}
