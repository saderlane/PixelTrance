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
import saderlane.pixeltrance.api.HypnoticSource;
import saderlane.pixeltrance.api.TranceDataAccess;
import saderlane.pixeltrance.logic.FocusLockConditions;
import saderlane.pixeltrance.util.PTLog;

import java.lang.reflect.Type;

public class PocketWatchItem extends Item implements HypnoticSource {

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

    @Override
    public float getFocusStrength() {
        return 0.6f;
    }

    @Override
    public int getFocusInterval() {
        return 2; // every 2 ticks
    }

    @Override
    public float getTranceStrength() {
        return 0.4f;
    }

    @Override
    public int getTranceInterval() {
        return 2;
    }

    @Override
    public boolean shouldAffect(LivingEntity observer, LivingEntity source) {
        // Only affect if source is looking at observer and is in a focus session
        if (!(source instanceof TranceDataAccess tranceUser)) return false;
        var trance = tranceUser.getTranceData();
        if (!trance.isFocusSessionActive()) return false;

        // Require eye contact to apply trance (for player targets)
        return observer.squaredDistanceTo(source) <= 100
                && FocusLockConditions.isLookingAt(source, observer);
    }

}
