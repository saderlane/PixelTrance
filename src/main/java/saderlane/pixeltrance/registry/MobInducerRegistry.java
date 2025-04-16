package saderlane.pixeltrance.registry;

import net.minecraft.entity.EntityType;
import saderlane.pixeltrance.data.MobHypnosisProfile;
import saderlane.pixeltrance.data.MobHypnosisProfiles;

// Handles registration of hypnosis profiles for mob inducers
public class MobInducerRegistry {

    public static void register() {
        //Pink Sheep
        MobHypnosisProfiles.register(EntityType.SHEEP, new MobHypnosisProfile(
                0.3f, 20,0.3f, 20, true
        ));
    }

}
