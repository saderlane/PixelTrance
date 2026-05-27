package net.saderlane.pixeltrance.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.util.ModTags;

import java.util.List;

public class PocketWatchItem extends Item {
    public PocketWatchItem(Properties properties) {
        super(properties);
    }

    private static boolean ticking = false;

    // When item is used on livingEntity
    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player, LivingEntity interactionTarget,
            InteractionHand usedHand) {

        Component component = stack.get(DataComponents.CUSTOM_NAME);
        if (!(interactionTarget instanceof Player)) { // If target is not a player
            if (!player.level().isClientSide && interactionTarget.isAlive()) { // If is not client level and target is alive

                stack.is(ModTags.Items.HYPNOTIC_ITEMS);
                ticking = !ticking;

                PTLog.debug("[PixelTrance] Stack: " + stack.getDisplayName().getString());
                PTLog.debug("[PixelTrance] Player: " + player.getName().getString());
                PTLog.debug("[PixelTrance] UsedHand: " + usedHand.name());
                PTLog.debug("[PixelTrance] LivingEntity:" + interactionTarget.getName().getString());
                PTLog.debug("[PixelTrance] Ticking: " + ticking);

            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    // When item is used
    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (!player.level().isClientSide) { // If is not client level and target is alive
            ticking = !ticking;
            PTLog.debug("[PixelTrance] Ticking: " + ticking);
            PTLog.debug("[PixelTrance] Stack: " + itemStack.getDisplayName().getString());
            PTLog.debug("[PixelTrance] Player: " + player.getName().getString());
            PTLog.debug("[PixelTrance] UsedHand: " + usedHand.name());
        }

        return super.use(level, player, usedHand);
    }

    // Stops pocket watch ticking if it is not in the player's hand or offhand
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            if ((!isSelected && (entity instanceof Player && ((Player)entity).getOffhandItem() != stack))) {
                ticking = false;
                //PTLog.debug("[PixelTrance] Ticking: " + ticking);
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.pixeltrance.pocket_watch.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.pixeltrance.pocket_watch"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
