package saderlane.pixeltrance.logic;


// Handles ticking passive trance/focus decay for any entity with TranceData.

import net.minecraft.entity.LivingEntity;
import saderlane.pixeltrance.api.TranceDataAccess;

public class TranceDecayHandler {

    public static void tickForSubject(LivingEntity subject) {
        if (!(subject instanceof TranceDataAccess access)) return;

        access.getTranceData().tick(); // triggers trance + focus decay
    }

}
