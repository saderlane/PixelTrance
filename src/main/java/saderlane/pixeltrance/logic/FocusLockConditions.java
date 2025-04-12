package saderlane.pixeltrance.logic;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3d;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.item.ModItems;

/*
 * FocusLockConditions provides logic to determine whether an entity
 * is considered "hypnotic" and should trigger focus gain.
*/
public class FocusLockConditions {

    // Checks if hypnotic target is a player holding a pocket watch
    public static boolean isHypnoticTarget(PlayerEntity player) {

        if (!(player instanceof TranceDataAccess tranceUser)) return false;

        var tranceData = tranceUser.getTranceData();

        // Check both hands for the pocket watch
        boolean holdingWatch =
                player.getMainHandStack().isOf(ModItems.POCKET_WATCH) ||
                        player.getOffHandStack().isOf(ModItems.POCKET_WATCH);

        return holdingWatch && tranceData.isFocusSessionActive();
    }

    // Returns true if the entity is considered a hypnotic target
    //  Triggers focus gain when another entity looks at this target
    public static boolean isHypnoticTarget(LivingEntity entity) {
        // Players holding active pocket watch trigger focus gain
        if (entity instanceof PlayerEntity player) {
            return isHypnoticTarget(player);
        }

        // Allow sheep to be hypnotic (for testing / later behavior)
        if (entity instanceof SheepEntity sheep) {
            return sheep.getColor() == DyeColor.PINK;
        }

        if (entity instanceof GlowSquidEntity squid) {
            return true;
        }


        // Later: Check if this mob has trance aura, is glowing, etc.
        return false;
    }

    // Determines if the observer is looking at the target
    public static boolean isLookingAt(LivingEntity observer, LivingEntity target) {
        if (observer == null || target == null) return false;

        Vec3d lookVec = observer.getRotationVec(1.0f).normalize();
        Vec3d directionToTarget = target.getEyePos().subtract(observer.getEyePos()).normalize();

        return lookVec.dotProduct(directionToTarget) > 0.95;
    }



    // Returns how quickly this entity should build focus when observed.
     //     The result is passed into tickFocus() to control buildup rate.
    public static float getFocusGainRate(LivingEntity entity) {
        if (entity instanceof PlayerEntity player && isHypnoticTarget(player)) {
            return 0.6f; // Pocket Watch user
        }

        if (entity instanceof SheepEntity sheep && sheep.getColor() == DyeColor.PINK) {
            return 0.3f; // Pink sheep: slower buildup
        }
        if (entity instanceof GlowSquidEntity squid) {
            return 0.5f;
        }

        return 0f;
    }


}
