package saderlane.pixeltrance.logic;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.item.ModItems;

/*
 * FocusLockConditions provides logic to determine whether an entity
 * is considered "hypnotic" and should trigger focus gain.
*/
public class FocusLockConditions {

    public static boolean isHypnoticTarget(PlayerEntity player) {

        if (!(player instanceof TranceDataAccess tranceUser)) return false;

        var tranceData = tranceUser.getTranceData();

        // Check both hands for the pocket watch
        boolean holdingWatch =
                player.getMainHandStack().isOf(ModItems.POCKET_WATCH) ||
                        player.getOffHandStack().isOf(ModItems.POCKET_WATCH);

        return holdingWatch && tranceData.isFocusSessionActive();
    }

    public static boolean isHypnoticTarget(LivingEntity entity) {
        // Players holding active pocket watch trigger focus gain
        if (entity instanceof PlayerEntity player) {
            return isHypnoticTarget(player);
        }

        // Allow sheep to be hypnotic (for testing / later behavior)
        if (entity instanceof SheepEntity sheep) {
            return sheep.getColor() == DyeColor.PINK;
        }


        // Later: Check if this mob has trance aura, is glowing, etc.
        return false;
    }

}
