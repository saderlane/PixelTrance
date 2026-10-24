package net.saderlane.pixeltrance.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.ai.ModActivities;
import net.saderlane.pixeltrance.ai.memory.HypnoEntityTracker;
import net.saderlane.pixeltrance.dev.PTLog;
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
        if (mob.getBrain().isActive(ModActivities.FOCUS_LOCKED_ACTIVITY.value())) {
            mob.getBrain().useDefaultActivity();
            PTLog.debug(mob.getName().getString() + " is stopping FOCUS_LOCK activity and returned to default");
        }

    }

    @Override
    public void tick() {
        updateData(mob);

        boolean isBrainMob = mob.getBrain().checkMemory(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED);
        Brain<Mob> mobBrain = (Brain<Mob>) mob.getBrain();



        // check if mob is a brain using mob
        if (isBrainMob && !mobBrain.isActive(ModActivities.FOCUS_LOCKED_ACTIVITY.value()) && mob.level() instanceof ServerLevel serverLevel)
        {
            mobBrain.stopAll(serverLevel, mob);
            mobBrain.setActiveActivityIfPossible(ModActivities.FOCUS_LOCKED_ACTIVITY.value()); // Add the focus locked activity

            if (!mobBrain.isActive(ModActivities.FOCUS_LOCKED_ACTIVITY.value())) {
                ModActivities.attachTo(mobBrain);
                mobBrain.setActiveActivityIfPossible(ModActivities.FOCUS_LOCKED_ACTIVITY.value());
            }
        }
        PTLog.debug(mob.getName().getString() + " activity: " + mobBrain.getActiveNonCoreActivity());

        // If the last influence source is a UUID
        if (this.influenceSourceUUID.isPresent() && mob.level() instanceof ServerLevel serverLevel) {
            Entity influencer = serverLevel.getEntity(influenceSourceUUID.get());
            if (influencer != null) {

                // check if mob is a brain using mob
                if (isBrainMob)
                {
                    mobBrain.setMemory(MemoryModuleType.LOOK_TARGET,new HypnoEntityTracker(influencer, true));
                }
                else {
                    mob.getLookControl().setLookAt(influencer);
                }


            }
        }

        // If the last influence source is a Vec3
        if (this.influenceSourceVec3.isPresent()) {
            Vec3 blockPos = influenceSourceVec3.get();

            // check if mob is a brain using mob
            if (isBrainMob)
            {
                mobBrain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(blockPos));
            }
            else {
                mob.getLookControl().setLookAt(blockPos);
            }

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
