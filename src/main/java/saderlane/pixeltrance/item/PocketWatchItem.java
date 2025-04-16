package saderlane.pixeltrance.item;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import saderlane.pixeltrance.api.Inducer;
import saderlane.pixeltrance.api.ActivatableInducer;
import saderlane.pixeltrance.network.ItemActivationS2CPacket;
import saderlane.pixeltrance.util.PTLog;


public class PocketWatchItem extends Item implements Inducer, ActivatableInducer {

    private static final String ACTIVE_KEY = "Active";

    public PocketWatchItem(Settings settings) {
        super(settings);
    }

    // === Inducer: Focus Properties ===
    @Override
    public float getFocusRate() {
        return 1f; // Builds  focus per interval
    }

    @Override
    public int getFocusInterval() {
        return 20; // Every X ticks
    }

    // === Inducer: Trance Properties ===
    @Override
    public float getTranceRate() {
        return 1f;
    }

    @Override
    public int getTranceInterval() {
        return 20;
    }


    // === Inducer: Custom Properties ===
    @Override
    public boolean requiresLineOfSight() {
        return true;
    }

    // === Item: Activation + Use ===

    // Handles right-click usage. Toggles activation on the server.
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);


            boolean newState = toggleActivation(stack, user);

            if (user instanceof ServerPlayerEntity serverPlayer) {
                ItemActivationS2CPacket.send(serverPlayer, hand, newState);
            }

            PTLog.info("Pocket Watch toggled on server: " + (newState ? "Active" : "Inactive"));
        }

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

    // Returns whether this pocket watch is currently active.
    @Override
    public boolean isActivated(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getBoolean(ACTIVE_KEY);
    }

    // Toggles the active state in NBT and returns the new state.
    @Override
    public boolean toggleActivation(ItemStack stack, LivingEntity user) {
        boolean wasActive = isActivated(stack);
        boolean newState = !wasActive;
        stack.getOrCreateNbt().putBoolean(ACTIVE_KEY, newState);
        return newState;
    }
}

