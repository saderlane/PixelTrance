package net.saderlane.pixeltrance.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.item.custom.PocketWatchItem;

import java.util.List;
// TODO: REMOVE BORROWED ITEM TEXTURES
public class ModItems {
    // Registers all our items to MC, tied to our MOD_ID
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PixelTrance.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Adds pocket watch item
    public static final DeferredItem<Item> POCKET_WATCH = ITEMS.register("pocket_watch",
            () -> new PocketWatchItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(250)));

    public static final DeferredItem<Item> SPIRALITE_SWORD = ITEMS.register("spiralite_sword",
            () -> new SwordItem(ModToolTiers.SPIRALITE,
                    new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.SPIRALITE, 2.5f, -2.4f))));
    public static final DeferredItem<Item> SPIRALITE_PICKAXE = ITEMS.register("spiralite_pickaxe",
            () -> new PickaxeItem(ModToolTiers.SPIRALITE,
                    new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.SPIRALITE, 1f, -2.8f))));
    public static final DeferredItem<Item> SPIRALITE_SHOVEL = ITEMS.register("spiralite_shovel",
            () -> new ShovelItem(ModToolTiers.SPIRALITE,
                    new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.SPIRALITE, 1.5f, -3f))));
    public static final DeferredItem<Item> SPIRALITE_AXE = ITEMS.register("spiralite_axe",
            () -> new AxeItem(ModToolTiers.SPIRALITE,
                    new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.SPIRALITE, 6f, -3.1f))));
    public static final DeferredItem<Item> SPIRALITE_HOE = ITEMS.register("spiralite_hoe",
            () -> new HoeItem(ModToolTiers.SPIRALITE,
                    new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.SPIRALITE, -2f, -1f))));



    // Adds Spiralite material
    public static final DeferredItem<Item> SPIRALITE = ITEMS.register("spiralite",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.pixeltrance.spiralite.tooltip"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    // Adds Raw Spiralite material (DEV NOTE: TEMP TEXTURE FROM Kaupenjoe's tutorial - will replace)
    public static final DeferredItem<Item> RAW_SPIRALITE = ITEMS.register("raw_spiralite",
            () -> new Item(new Item.Properties()));

}
