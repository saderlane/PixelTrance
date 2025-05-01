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
        // This will likely be changed so all mobs by default are true for these values and will switch to a blacklist system
        // They slow down, look dazed
        MobTranceProfiles.register(EntityType.COW, new MobTranceProfile(
                false,
                true,
                type -> true
        ));
        MobTranceProfiles.register(EntityType.PIG, new MobTranceProfile(
                false,
                true,
                type -> true
        ));
        MobTranceProfiles.register(EntityType.SHEEP, new MobTranceProfile(
                false,
                true,
                type -> true
        ));


    }

}
