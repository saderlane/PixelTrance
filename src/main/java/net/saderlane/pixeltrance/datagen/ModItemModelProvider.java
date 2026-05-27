package net.saderlane.pixeltrance.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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


        basicItem(ModItems.POCKET_WATCH.get());

    }
}
