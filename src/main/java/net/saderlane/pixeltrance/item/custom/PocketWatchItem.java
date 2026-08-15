package net.saderlane.pixeltrance.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.saderlane.pixeltrance.component.ModDataComponentTypes;
import net.saderlane.pixeltrance.dev.PTLog;
import net.saderlane.pixeltrance.hypno.HypnoData;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class PocketWatchItem extends Item {
    public PocketWatchItem(Properties properties) {
        super(properties);
    }

    // ========== Private Variables ============
    private static final float RADIUS = 8.0f; // How far watch reaches
    private static final int PULSE_IN_TICK = 5; // Ticks between pulses
                                                    // 5 = 4 times a second
    private static final int FOCUS_GAIN = 7;
    private static final int TRANCE_GAIN = 3;

    // Get TICKING data component for the stack
    public static boolean isTicking(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.TICKING.get(), false);
    }

    // Set the TICKING data component for the stack
    public static void setTicking(ItemStack stack, boolean ticking) {
        stack.set(ModDataComponentTypes.TICKING.get(), ticking);
    }

    // When item is used on livingEntity
    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack stack,
            @NotNull Player player,
            @NotNull LivingEntity interactionTarget,
            @NotNull InteractionHand usedHand) {

        // If not a player OR is not alive
        if (interactionTarget instanceof Player || !interactionTarget.isAlive()) {
            return InteractionResult.PASS;
        }

        if (!player.level().isClientSide()) {
            setTicking(stack, true);

            PTLog.debug("[PixelTrance] Pocket watch shown to " + interactionTarget.getName().getString()
                    + " by " + player.getName().getString()
                    + " (" + usedHand.name() + ")");


            // What to do when target is shown active pocket watch
        }
        return InteractionResult.SUCCESS;
    }

    // When item is used
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            @NotNull InteractionHand usedHand) {

        ItemStack itemStack = player.getItemInHand(usedHand); // Get the item stack of the pocket watch

        if (!level.isClientSide()) { // If is not client level and target is alive
            setTicking(itemStack, !isTicking(itemStack));
            PTLog.debug("[PixelTrance] **Pocket Watch Used**");
            PTLog.debug("[PixelTrance] Ticking: " + isTicking(itemStack));
            PTLog.debug("[PixelTrance] Stack: " + itemStack.getDisplayName().getString());
            PTLog.debug("[PixelTrance] Player: " + player.getName().getString());
            PTLog.debug("[PixelTrance] UsedHand: " + usedHand.name());
        }

        return super.use(level, player, usedHand);
    }



    // Stops pocket watch ticking if it is not in the player's hand or offhand
    @Override
    public void inventoryTick(@NotNull ItemStack stack,
                              Level level,
                              @NotNull Entity entity,
                              int slotId,
                              boolean isSelected) {

        boolean ticking = isTicking(stack);
        // If not client or entity holding the watch isn't living, return
        if (!level.isClientSide() && entity instanceof LivingEntity holder) {
            boolean held = isSelected || holder.getOffhandItem() == stack;// If not held in main or offhand
            if (!held) { // If it is not held
                // AND it is ticking
                if (ticking) {
                    setTicking(stack, false); // Set ticking to false
                    PTLog.debug("[PixelTrance] " + stack.getDisplayName().getString() + " stopped ticking.");
                }
            } else if(ticking) { // If it is ticking and in the hand
                if (level.getGameTime() % PULSE_IN_TICK == 0) {
                    pulse((ServerLevel) level, holder, stack);
                }
            }
        }


    }



    private static void pulse(ServerLevel level, LivingEntity holder, ItemStack stack) {
        Vec3 origin = holder.getEyePosition(); // Get X, Y, and Z of tist's face center

        // Build a list of LivingEntities that can be candidates
        List<LivingEntity> candidates = level.getEntitiesOfClass(
                LivingEntity.class, // Get the
                AABB.ofSize(origin, RADIUS*2, RADIUS*2, RADIUS*2), // Set the range that candidates can be found in
                candidate -> isValidSubject(candidate, holder, origin) // Set as candidate if it is a valid subject
        );

        if (candidates.isEmpty()) return;

        int slots = 1; //TODO Make this variable/scale in the future based on perks

        List<LivingEntity> chosen = selectTargets(candidates, origin, slots);
        for (LivingEntity subject : candidates) {

            int subjectFocus = HypnoData.getFocus(subject);
            int subjectTrance = HypnoData.getTrance(subject);

            if (chosen.contains(subject)) {
                if (subjectFocus != HypnoData.MAX)
                {
                    HypnoData.addFocus(subject, FOCUS_GAIN);
                    PTLog.debug("[PixelTrance] Influencing " + subject.getName().getString()
                            + " (focus " + HypnoData.getFocus(subject) + ")");
                }
                if (subjectFocus == HypnoData.MAX && subjectTrance != HypnoData.MAX) {
                    HypnoData.addTrance(subject, TRANCE_GAIN);
                    PTLog.debug("[PixelTrance] Influencing " + subject.getName().getString()
                            + " (trance " + HypnoData.getTrance(subject) + ")");
                }

            }
        }
    }


    // Can probably move these to a class later for all hypno-inducing valid objects
    private static boolean isValidSubject(LivingEntity candidate, LivingEntity holder, Vec3 origin) {
        if (candidate == holder || !candidate.isAlive()) return false; // If the candidate is the holder or dead, return

        if (candidate.distanceToSqr(origin) > RADIUS * RADIUS) return false; // Get sphere(scandelous) instead of box around holder

        return true;
    }

    private static List<LivingEntity> selectTargets(List<LivingEntity> candidates, Vec3 origin, int slots) {
        candidates.sort(
                Comparator.comparingInt(HypnoData::getFocus).reversed()
                        .thenComparingDouble(candidate -> candidate.distanceToSqr(origin))
        );

        return candidates.size() > slots ? candidates.subList(0, slots) : candidates;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltipComponents,
            @NotNull TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.pixeltrance.pocket_watch.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.pixeltrance.pocket_watch"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
