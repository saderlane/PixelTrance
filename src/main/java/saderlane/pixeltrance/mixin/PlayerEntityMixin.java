package saderlane.pixeltrance.mixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import saderlane.pixeltrance.logic.FocusLockConditions;
import saderlane.pixeltrance.util.PTLog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.api.TranceDataAccess;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements TranceDataAccess {


    // Adds tranceData field to every unique player
    @Unique
    private final TranceData tranceData = new TranceData((PlayerEntity) (Object) this);

    // Method to let other classes access trance data attached to player
    @Override
    public TranceData getTranceData() {
        return tranceData;
    }

    // Inject into save method
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveTrance(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("PixelTrance", tranceData.writeToNbt());
    }

    // Inject into load method
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadTrance(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PixelTrance")) {
            PTLog.info("Loaded player trance from NBT");
            tranceData.readFromNbt(nbt.getCompound("PixelTrance"));
        }
    }

    // After player tick, run trance tick to see if trance will decay
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTrance(CallbackInfo ci) {

        TranceData trance = this.getTranceData();
        trance.tick(); // Handles trance decay

        PlayerEntity player = (PlayerEntity) (Object) this;

        // Run only on server side
        if (player.getWorld().isClient) return;

        // === Begin entity raycast test ===

        World world = player.getWorld();
        Vec3d eyePos = player.getCameraPosVec(1.0F);
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d reachVec = eyePos.add(lookVec.multiply(10.0D));

        // PTLog.info("Raycasting from " + eyePos + " to " + reachVec);

        // Search for any living entity in line of sight
        Box searchBox = player.getBoundingBox().stretch(lookVec.multiply(10.0D)).expand(1.0D);
        // PTLog.info("Search box: " + searchBox);

        EntityHitResult entityHit = ProjectileUtil.getEntityCollision(
                world,
                player,
                eyePos,
                reachVec,
                searchBox,
                entity -> entity instanceof LivingEntity && entity.isAlive() && !entity.isSpectator()
        );

        LivingEntity target = null;
        float focusRate = 0f;

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity potential) {
            if (FocusLockConditions.isHypnoticTarget(potential)) {
                target = potential;
                focusRate = FocusLockConditions.getFocusGainRate(potential);
            }
        }

        if (entityHit != null) {
            // PTLog.info("Player is looking at: " + entityHit.getEntity().getName().getString());
        }

        if (target != null) {
            // PTLog.info("Player sees valid hypnotic target: " + target.getName().getString());
        }

        trance.tickFocus(focusRate > 0f, target, focusRate);

        // Mutual gaze: gain trance if target is looking back at us
        boolean tranceBuild = false;
        if (target != null && FocusLockConditions.isLookingAt(target, player)) {
            tranceBuild = true;
        }
        trance.tickTrance(tranceBuild);



    }
}
