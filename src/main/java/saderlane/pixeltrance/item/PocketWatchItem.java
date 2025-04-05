package saderlane.pixeltrance.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.util.PTLog;

public class PocketWatchItem extends Item {

    public PocketWatchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (!user.getWorld().isClient)
        {
            PTLog.info("Pocket Watch used on: " + target.getName().getString());

            if (target instanceof TranceDataAccess tranceTarget)
            {
                tranceTarget.getTranceData().addTrance(10);
                PTLog.info("Added trance via Pocket Watch to " + target.getName().getString());
            }
            else
            {
                PTLog.warn("Target is NOT trance-compatible: " + target.getType().toString());
            }
        }

        // Always return success (true if client-side for animation)
        return ActionResult.SUCCESS;
    }
}
