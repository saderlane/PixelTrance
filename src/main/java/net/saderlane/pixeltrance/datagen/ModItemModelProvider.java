package net.saderlane.pixeltrance.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PixelTrance.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        basicItem(ModItems.SPIRALITE.get());
        basicItem(ModItems.RAW_SPIRALITE.get());


        handheldItem(ModItems.POCKET_WATCH);
        handheldItem(ModItems.SPIRALITE_SWORD);
        handheldItem(ModItems.SPIRALITE_PICKAXE);
        handheldItem(ModItems.SPIRALITE_SHOVEL);
        handheldItem(ModItems.SPIRALITE_AXE);
        handheldItem(ModItems.SPIRALITE_HOE);

    }

    private ItemModelBuilder handheldItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID,"item/" + item.getId().getPath()));
    }
}
