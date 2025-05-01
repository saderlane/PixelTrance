package saderlane.pixeltrance.mixin.mob;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.ai.goal.TrancedFollowGoal;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.data.MobTranceProfile;
import saderlane.pixeltrance.data.MobTranceProfiles;
import saderlane.pixeltrance.data.TranceData;

import saderlane.pixeltrance.data.PixelTranceUUID;
import saderlane.pixeltrance.mixin.accessor.MobEntityAccessor;
import saderlane.pixeltrance.util.PTLog;

@Mixin(MobEntity.class)
public class MobTranceBehaviorMixin {

    private static final float TRANCE_THRESHOLD = 3f; // This will need to be fucked with once we get more effects
        // I am too tired to fuck with making this not ad hoc rn

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void applyTranceEffects(CallbackInfo ci) {
        MobEntity mob = (MobEntity)(Object) this;

        // Only apply trance behavior if mob has trance data
        if (!(mob instanceof TranceDataAccess tranceHolder)) return;

        TranceData data = tranceHolder.getTranceData();
        if (data.getTrance() < TRANCE_THRESHOLD) return; // TEMP THRESHOLD: Adjust this later for balance

        // Get the trance behavior profile for this mob type
        MobTranceProfile profile = MobTranceProfiles.get(mob.getType());
        if (!profile.isSupported(mob.getType())) return;

        // === Apply attentionGrabbed: stop movement and look at inducer ===
        if (profile.attentionGrabbed()) {
            if (mob.getNavigation() instanceof MobNavigation nav) {
                nav.stop(); // Freeze movement
            }
        }

        // === Apply slowsMovement: reduce speed with a modifier ===
        var speedAttr = mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (profile.shouldSlowMovement()) {
            if (speedAttr != null && speedAttr.getModifier(PixelTranceUUID.TRANCE_SLOW_MODIFIER) == null) {
                EntityAttributeModifier slowMod = new EntityAttributeModifier(
                        PixelTranceUUID.TRANCE_SLOW_MODIFIER,
                        "[PixelTrance] Tranced Movement Slowdown",
                        -0.1,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );
                speedAttr.addPersistentModifier(slowMod);
            }

            // Follow inducer if one exists and not already close
            if (data.getTranceInducer() != null && mob.getNavigation() instanceof MobNavigation nav) {
                Vec3d targetPos = data.getTranceInducer().getPos();
                double distance = mob.getPos().distanceTo(targetPos);

                // Repath every 20 ticks to keep following
                if (distance > 2.0 && mob.age % 20 == 0) {
                    nav.startMovingTo(targetPos.x, targetPos.y, targetPos.z, 0.5);
                }
            }

//            if (data.getCurrentInducer() == null) {
//                PTLog.info("[Trance] Mob has no inducer: " + mob.getName().getString());
//            }


            // Inject follow goal if it's not already present
            GoalSelector selector = ((MobEntityAccessor) mob).getGoalSelector();
            if (data.getTranceInducer() != null &&
                    selector.getGoals().stream().noneMatch(g -> g.getGoal() instanceof TrancedFollowGoal)) {
                //PTLog.info("[Trance] Injecting follow goal into: " + mob.getName().getString());
                selector.add(5, new TrancedFollowGoal(mob, 1D, 2.0F, 10.0F));
            }


        } else {
            // Remove slow modifier if mob is no longer under trance
            if (speedAttr != null) {
                speedAttr.removeModifier(PixelTranceUUID.TRANCE_SLOW_MODIFIER);
            }
        }

        // === Look directly at inducer — works across all trance profiles ===
        if (data.getTranceInducer() != null && mob.getLookControl() != null) {
            Vec3d targetPos = data.getTranceInducer().getEyePos();
            mob.getLookControl().lookAt(targetPos.x, targetPos.y, targetPos.z);

            // Optional: hard-set yaw for frozen mobs
            Vec3d mobPos = mob.getPos();
            double dx = targetPos.x - mobPos.x;
            double dz = targetPos.z - mobPos.z;
            float yaw = (float)(MathHelper.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;

            mob.setBodyYaw(yaw);
            mob.setHeadYaw(yaw);
            mob.prevYaw = yaw;
            mob.setYaw(yaw);
        }
    }



}
