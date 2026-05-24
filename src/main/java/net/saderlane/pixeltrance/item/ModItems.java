package net.saderlane.pixeltrance.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.saderlane.pixeltrance.PixelTrance;

public class ModItems {
    // Registers all our items to MC, tied to our MOD_ID
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PixelTrance.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // Adds pocket watch item
    public static final DeferredItem<Item> POCKET_WATCH = ITEMS.register("pocket_watch",
            () -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(250)));


    // Adds Spiralite material (DEV NOTE: TEMP TEXTURE FROM Kaupenjoe's tutorial - will replace)
    public static final DeferredItem<Item> SPIRALITE = ITEMS.register("spiralite",
            () -> new Item(new Item.Properties()));
    // Adds Raw Spiralite material (DEV NOTE: TEMP TEXTURE FROM Kaupenjoe's tutorial - will replace)
    public static final DeferredItem<Item> RAW_SPIRALITE = ITEMS.register("raw_spiralite",
            () -> new Item(new Item.Properties()));

}
