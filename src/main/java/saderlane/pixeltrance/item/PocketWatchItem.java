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
            if (user instanceof TranceDataAccess tranceUser)
            {
                var tranceData = tranceUser.getTranceData();

                // Toggle focus session state
                boolean wasActive = tranceData.isFocusSessionActive();
                tranceData.setFocusSessionActive(!wasActive);

                // Server side log for debugging
                PTLog.info(user.getName().getString() + " toggled focus session to: " + (!wasActive));
            }
        }
        else
        {
            if (user instanceof TranceDataAccess tranceUser) {
                boolean state = tranceUser.getTranceData().isFocusSessionActive();
                user.sendMessage(Text.literal(
                        state ? "Focus Session: Activated" : "Focus Session: Deactivated"
                ), true);
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
    }
}
