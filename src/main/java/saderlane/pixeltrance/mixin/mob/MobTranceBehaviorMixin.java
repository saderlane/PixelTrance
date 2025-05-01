package saderlane.pixeltrance.mixin.mob;

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
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.data.MobTranceProfile;
import saderlane.pixeltrance.data.MobTranceProfiles;
import saderlane.pixeltrance.data.TranceData;

import saderlane.pixeltrance.data.PixelTranceUUID;

@Mixin(MobEntity.class)
public class MobTranceBehaviorMixin {

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void applyTranceEffects(CallbackInfo ci) {

        MobEntity mob = (MobEntity)(Object)this;

        // Make sure mob has trance data
        if (!(mob instanceof TranceDataAccess tranceHolder)) return;

        // If the mob is above 50 trance
        TranceData data = tranceHolder.getTranceData();
        if (data.getTrance() < 50f) return; // DEV NOTE: THIS WILL LIKELY NEED TO BE TWEAKED SO ITS MORE BALANCED

        // If the mob can be tranced
        MobTranceProfile profile = MobTranceProfiles.get(mob.getType());
        if (!profile.isSupported(mob.getType())) return;

        // If the mob can have their attention grabbed:
            // Stop moving
            // Look at inducer
        if (profile.attentionGrabbed()) {
            // Stop pathing
            if (mob.getNavigation() instanceof MobNavigation nav){
                nav.stop();
            }

            // Look at inducer
            if (data.getCurrentInducer() != null) {

                Vec3d mobPos = mob.getPos();
                Vec3d targetPos = data.getCurrentInducer().getPos();

                double dx = targetPos.x - mobPos.x;
                double dz = targetPos.z - mobPos.z;
                float yaw = (float)(MathHelper.atan2(dz, dx) * (180F / Math.PI)) - 90.0F;

                mob.setBodyYaw(yaw);
                mob.setHeadYaw(yaw);
                mob.prevYaw = yaw;
                mob.setYaw(yaw);
            }

        }

        // If the mob has slowsMovement == true
        var speedAttr = mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if (profile.shouldSlowMovement()) {
            if (speedAttr != null && speedAttr.getModifier(PixelTranceUUID.TRANCE_SLOW_MODIFIER) == null) {
                EntityAttributeModifier slowMod = new EntityAttributeModifier(
                        PixelTranceUUID.TRANCE_SLOW_MODIFIER,
                        "Tranced Movement Slowdown",
                        -0.3,
                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                );
                speedAttr.addPersistentModifier(slowMod);
            }
        } else {
            if (speedAttr != null) {
                speedAttr.removeModifier(PixelTranceUUID.TRANCE_SLOW_MODIFIER);
            }
        }


    }


}
