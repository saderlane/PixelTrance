package saderlane.pixeltrance.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import saderlane.pixeltrance.data.TranceData;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.logic.FocusLockConditions;
import saderlane.pixeltrance.util.PTLog;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements TranceDataAccess {

    private final TranceData tranceData = new TranceData((LivingEntity) (Object) this);

    protected MobEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    // Accessor to retrieve this mob’s trance data
    @Override
    public TranceData getTranceData() {
        return tranceData;
    }

    // Save trance data to NBT when mob is saved (e.g. world save or chunk unload)
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void saveTrance(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("PixelTrance", tranceData.writeToNbt());
    }

    // Load trance data from NBT when mob is reloaded
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void loadTrance(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PixelTrance", NbtElement.COMPOUND_TYPE)) {
            tranceData.readFromNbt(nbt.getCompound("PixelTrance"));
            PTLog.debug("Loaded trance from NBT for mob: " + this.getName().getString());
        }
    }

    // After mob tick, run trance tick to see if trance will decay
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTrance(CallbackInfo ci) {

        TranceData trance = this.getTranceData();
        trance.tick(); // Handles trance decay

        MobEntity self = (MobEntity) (Object) this;
        World world = self.getWorld();

        // Server-side only
        if (world.isClient) return;


        // Set up mob eye position and look direction
        Vec3d eyePos = self.getEyePos();
        Vec3d lookVec = self.getRotationVec(1.0F);
        Vec3d endPos = eyePos.add(lookVec.multiply(10.0));

        // Look for a player the mob is staring at
        EntityHitResult hit = ProjectileUtil.getEntityCollision(
                world,
                self,
                eyePos,
                endPos,
                self.getBoundingBox().stretch(lookVec.multiply(10.0D)).expand(1.0D),
                entity -> entity instanceof PlayerEntity && entity.isAlive() && !entity.isSpectator()
        );


        // Check if valid hypnotic target
        LivingEntity target = null;
        float focusRate = 0f;

        if (hit != null && hit.getEntity() instanceof PlayerEntity potential) {

            // PTLog.info(self.getName().getString() + " is looking at potential target: " + potential.getName().getString());

            if (FocusLockConditions.isHypnoticTarget(potential)) {
                target = potential;
                focusRate = FocusLockConditions.getFocusGainRate(potential);
            }
        }

        if (target != null) {
            // PTLog.info(self.getName().getString() + " sees valid hypnotic target: " + target.getName().getString());
        }
        trance.tickFocus(focusRate > 0f, target, focusRate);

        // Mutual gaze: gain trance if target is looking at the mob
        boolean tranceBuild = false;
        if (target != null && FocusLockConditions.isLookingAt(target, self)) {
            tranceBuild = true;
        }
        trance.tickTrance(tranceBuild);


    }


}
