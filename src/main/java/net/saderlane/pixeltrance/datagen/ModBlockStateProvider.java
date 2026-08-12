package net.saderlane.pixeltrance.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.block.ModBlocks;
import net.saderlane.pixeltrance.block.SpiraliteLampBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PixelTrance.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SPIRALITE_BLOCK);
        blockWithItem(ModBlocks.SPIRALITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_SPIRALITE_ORE);

        customLamp(ModBlocks.SPIRALITE_LAMP, SpiraliteLampBlock.CLICKED);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }


    private void customLamp(DeferredBlock<?> deferredBlock, BooleanProperty clicked) {

        String blockName = deferredBlock.getRegisteredName().toLowerCase().split(":")[1];

        String blockOn = blockName+ "_on";
        String blockOff = blockName+ "_off";


        getVariantBuilder(deferredBlock.get()).forAllStates(state -> {
            if(state.getValue(clicked)) {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll(blockOn,
                        ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "block/" + blockOn)))};
            } else {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll(blockOff,
                        ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "block/" + blockOff)))};
            }
        });

        simpleBlockItem(ModBlocks.SPIRALITE_LAMP.get(), models().cubeAll(blockOn,
                ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, "block/" + blockOn)));
    }

}
