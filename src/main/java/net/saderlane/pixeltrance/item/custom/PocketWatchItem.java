package net.saderlane.pixeltrance.item.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class PocketWatchItem extends Item {
    public PocketWatchItem(Properties properties) {
        super(properties);
    }

    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean ticking = false;

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player, LivingEntity interactionTarget,
            InteractionHand usedHand) {

        Component component = stack.get(DataComponents.CUSTOM_NAME);
        if (!(interactionTarget instanceof Player)) { // If target is not a player
            if (!player.level().isClientSide && interactionTarget.isAlive()) { // If is not client level and target is alive

                ticking = !ticking;

                LOGGER.debug("[PixelTrance] Stack: " + stack.getDisplayName().getString());
                LOGGER.debug("[PixelTrance] Player: " + player.getName().getString());
                LOGGER.debug("[PixelTrance] UsedHand: " + usedHand.name());
                LOGGER.debug("[PixelTrance] LivingEntity:" + interactionTarget.getName().getString());
                LOGGER.debug("[PixelTrance] Ticking: " + ticking);

            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (!player.level().isClientSide) { // If is not client level and target is alive
            ticking = !ticking;
            LOGGER.debug("[PixelTrance] Ticking: " + ticking);
            LOGGER.debug("[PixelTrance] Stack: " + itemStack.getDisplayName().getString());
            LOGGER.debug("[PixelTrance] Player: " + player.getName().getString());
            LOGGER.debug("[PixelTrance] UsedHand: " + usedHand.name());
        }

        return super.use(level, player, usedHand);
    }
}
