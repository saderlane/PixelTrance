package saderlane.pixeltrance.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import saderlane.pixeltrance.util.PTLog;

public class ModItems {
    // The actual item instance: registers as "pixeltrance:pocket_watch"
    public static final Item POCKET_WATCH = register("pocket_watch", new PocketWatchItem(
            new Item.Settings().maxCount(1) // Only one pocket watch per slot
    ));

    // Register the item with the game
    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("pixeltrance", name), item);
    }

    // Called once from mod initializer
    public static void init() {
        PTLog.info("Registering mod items");
    }
}
