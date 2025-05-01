package saderlane.pixeltrance.registry;

import net.minecraft.entity.EntityType;
import saderlane.pixeltrance.data.MobTranceProfile;
import saderlane.pixeltrance.data.MobTranceProfiles;

// Handles registering trance profiles for mobs
public class MobSubjectRegistry {

    public static void register() {

        // === Villagers ===
        MobTranceProfiles.register(EntityType.VILLAGER, new MobTranceProfile(
                true,
                false,
                type -> true
        ));

        // === Common Animals ===



    }

}
