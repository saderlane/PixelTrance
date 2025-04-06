package saderlane.pixeltrance.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.util.PTLog;

import java.lang.reflect.Type;

public class PocketWatchItem extends Item {

    public PocketWatchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            // Server side log for debugging
            PTLog.info(user.getName().getString() + " toggled focus session (server side)");
        }
        else
        {
            // Client side feedback
            user.sendMessage(Text.literal("Toggled Focus Session!"), true);
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }
}
