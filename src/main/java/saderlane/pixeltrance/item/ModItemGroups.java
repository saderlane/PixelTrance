package saderlane.pixeltrance.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    private static final RegistryKey<ItemGroup> PIXELTRANCE_GROUP = RegistryKey.of(Registries.ITEM_GROUP.getKey(), new Identifier("pixeltrance", "main"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, PIXELTRANCE_GROUP, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.pixeltrance.main"))
                .icon(() -> new ItemStack(ModItems.POCKET_WATCH))
                .entries((context, entries) -> {
                    entries.add(ModItems.POCKET_WATCH);
                    // add more here later
                })
                .build());
    }

}
