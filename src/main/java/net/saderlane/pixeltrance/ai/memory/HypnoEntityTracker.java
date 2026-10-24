package net.saderlane.pixeltrance.ai.memory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.saderlane.pixeltrance.hypno.HypnoTargeting;

public class HypnoEntityTracker extends EntityTracker {

    public HypnoEntityTracker(Entity entity, boolean trackEyeHeight) {
        super(entity, trackEyeHeight);
    }

    @Override
    public boolean isVisibleBy(LivingEntity viewer) {
        Entity target = this.getEntity();
        if (target instanceof LivingEntity livingentity) {
            if (!livingentity.isAlive()) {
                return false;
            } else {
                return !HypnoTargeting.gazeObstructed(viewer, livingentity);
            }
        } else {
            return true;
        }
    }
}
