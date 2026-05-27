package net.saderlane.pixeltrance.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PixelTrance.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SPIRALITE_BLOCK.get())
                .add(ModBlocks.SPIRALITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_SPIRALITE_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SPIRALITE_BLOCK.get())
                .add(ModBlocks.SPIRALITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_SPIRALITE_ORE.get());
    }
}
