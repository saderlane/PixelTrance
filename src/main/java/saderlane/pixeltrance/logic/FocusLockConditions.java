package saderlane.pixeltrance.logic;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import saderlane.pixeltrance.item.ModItems;

/*
 * FocusLockConditions provides logic to determine whether an entity
 * is considered "hypnotic" and should trigger focus gain.
*/
public class FocusLockConditions {

    public static boolean isHypnoticTarget(PlayerEntity player) {
        // Check both hands for the pocket watch
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        return mainHand.isOf(ModItems.POCKET_WATCH) || offHand.isOf(ModItems.POCKET_WATCH);
    }

    public static boolean isHypnoticTarget(LivingEntity entity) {
        // For now, only players holding the Pocket Watch are hypnotic
        if (entity instanceof PlayerEntity player) {
            return isHypnoticTarget(player);
        }

        // Later: Check if this mob has trance aura, is glowing, etc.
        return false;
    }

}
