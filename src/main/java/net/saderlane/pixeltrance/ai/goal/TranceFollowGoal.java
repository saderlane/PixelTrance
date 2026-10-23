package net.saderlane.pixeltrance.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.hypno.HypnoData;
import net.saderlane.pixeltrance.hypno.InfluenceSource;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class TranceFollowGoal extends Goal {
    protected final PathfinderMob mob;
    private final double speedModifier;

    private float trance;
    private float focus;
    private InfluenceSource lastInfluenceSource;
    private Entity influenceEntity;
    private Vec3 influenceSourceVec3;


    public TranceFollowGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;


        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        updateData(this.mob);

        return trance >= 100 && (influenceEntity != null || influenceSourceVec3 != null);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {

        if (influenceEntity != null) {
            this.mob.getLookControl().setLookAt(this.influenceEntity);
            if (this.mob.distanceToSqr(this.influenceEntity) < (double)6.25F) {
                this.mob.getNavigation().stop();
            } else {
                this.mob.getNavigation().moveTo(this.influenceEntity, this.speedModifier);
            }
        }

        if (influenceSourceVec3 != null) {
            this.mob.getLookControl().setLookAt(this.influenceSourceVec3);
            if (this.mob.distanceToSqr(this.influenceSourceVec3) < (double) 6.25F) {
                this.mob.getNavigation().stop();
            } else {
                this.mob.getNavigation().moveTo(this.influenceSourceVec3.x, this.influenceSourceVec3.y, this.influenceSourceVec3.z, this.speedModifier);
            }
        }


    }

    private void updateData(Mob m) {
        this.influenceEntity = null;
        this.influenceSourceVec3 = null;


        this.trance = HypnoData.getTrance(m);
        this.focus = HypnoData.getFocus(m);
        this.lastInfluenceSource = HypnoData.getInfluenceSource(m);
        Optional<UUID> influenceSourceUUIDOptional = HypnoData.getInfluencerUUID(m);
        Optional<Vec3> influenceSourceVec3Optional = HypnoData.getInfluencerVec3(m);

        if (influenceSourceUUIDOptional.isPresent() && m.level() instanceof ServerLevel serverLevel) {
            Entity influencer = serverLevel.getEntity(influenceSourceUUIDOptional.get());
            if (influencer != null) {
                this.influenceEntity = influencer;
            }
        }

        // If the last influence source is a Vec3
        influenceSourceVec3Optional.ifPresent(vec3 -> this.influenceSourceVec3 = vec3);
    }

}

