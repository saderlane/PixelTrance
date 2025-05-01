package saderlane.pixeltrance.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.WorldView;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.util.PTLog;

import java.util.EnumSet;

public class TrancedFollowGoal extends Goal {

    private static final float TRANCE_THRESHOLD = 3f; // This will need to be fucked with once we get more effects
    // I am too tired to fuck with making this not ad hoc rn


    private final MobEntity mob;
    private final WorldView world;
    private Entity inducer;
    private final double speed;
    private final EntityNavigation navigation;
    private final float minDistance;
    private final float maxDistance;
    private int updateCountdownTicks;
    private float oldWaterPenalty;

    public TrancedFollowGoal(MobEntity mob, double speed, float minDistance, float maxDistance) {
        if (!(mob.getNavigation() instanceof MobNavigation || mob.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for TrancedFollowGoal");
        }

        this.mob = mob;
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.navigation = mob.getNavigation();
        this.world = mob.getWorld();
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!(mob instanceof TranceDataAccess tranceHolder)) return false;
        TranceData data = tranceHolder.getTranceData();

        return data.getTrance() >= TRANCE_THRESHOLD && data.getTranceInducer() != null;
    }


    @Override
    public boolean shouldContinue() {
        if (!(mob instanceof TranceDataAccess tranceHolder)) return false;
        TranceData data = tranceHolder.getTranceData();

        Entity inducer = data.getTranceInducer();
        return data.getTrance() >= TRANCE_THRESHOLD && inducer != null && inducer.isAlive();
    }



    @Override
    public void start() {
        updateCountdownTicks = 0;
        oldWaterPenalty = mob.getPathfindingPenalty(PathNodeType.WATER);
        mob.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        navigation.stop();
        mob.setPathfindingPenalty(PathNodeType.WATER, oldWaterPenalty);
        this.inducer = null;
    }


    @Override
    public void tick() {
        if (!(mob instanceof TranceDataAccess tranceHolder)) return;
        TranceData data = tranceHolder.getTranceData();
        Entity inducer = data.getTranceInducer();
        if (inducer == null || mob.isLeashed()) return;

        mob.getLookControl().lookAt(inducer, 10.0F, mob.getMaxLookPitchChange());

        if (--updateCountdownTicks <= 0) {
            updateCountdownTicks = this.getTickCount(10);
            double distSq = mob.squaredDistanceTo(inducer);

            if (distSq >= (minDistance * minDistance)) {
                navigation.startMovingTo(inducer, speed);
            } else {
                navigation.stop();
            }
        }
    }



}
