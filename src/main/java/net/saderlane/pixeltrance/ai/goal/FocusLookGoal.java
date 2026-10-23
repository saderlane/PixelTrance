package net.saderlane.pixeltrance.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.hypno.InfluenceSource;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class FocusLookGoal extends Goal {

    protected final Mob mob;
    private float trance;
    private float focus;
    private InfluenceSource lastInfluenceSource;
    private Optional<UUID> influenceSourceUUID;
    private Optional<Vec3> influenceSourceVec3;



    public FocusLookGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

    }


    @Override
    public boolean canUse() {
        updateData(mob);

        return this.focus > 0 && (this.influenceSourceUUID.isPresent() || this.influenceSourceVec3.isPresent());
    }

    @Override
    public void start() {
        mob.getNavigation().stop();
    }

    @Override
    public void stop() {

    }

    @Override
    public void tick() {
        updateData(mob);

        // If the last influence source is a UUID
        if (this.influenceSourceUUID.isPresent() && mob.level() instanceof ServerLevel serverLevel) {
            Entity influencer = serverLevel.getEntity(influenceSourceUUID.get());
            if (influencer != null) {
                mob.getLookControl().setLookAt(influencer);
            }
        }

        // If the last influence source is a Vec3
        if (this.influenceSourceVec3.isPresent()) {
            Vec3 blockPos = influenceSourceVec3.get();
            mob.getLookControl().setLookAt(blockPos);
        }

    }

    private void updateData(Mob m) {
        this.trance = HypnoData.getTrance(m);
        this.focus = HypnoData.getFocus(m);
        this.lastInfluenceSource = HypnoData.getInfluenceSource(m);
        this.influenceSourceUUID = HypnoData.getInfluencerUUID(m);
        this.influenceSourceVec3 = HypnoData.getInfluencerVec3(m);
    }
}
