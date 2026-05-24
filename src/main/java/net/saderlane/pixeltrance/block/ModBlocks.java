package net.saderlane.pixeltrance.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(PixelTrance.MOD_ID);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    // TODO: REMOVE BORROWED BLOCK TEXTURES
    // Spiralite Block
    public static final DeferredBlock<Block> SPIRALITE_BLOCK = registerBlock("spiralite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5.0F, 6.0F) // How hard it is to break
                    .requiresCorrectToolForDrops() // Requires the correct tool
                    .sound(SoundType.AMETHYST) // Sound it makes breaking
                    .mapColor(MapColor.COLOR_MAGENTA) // Map Color
            ));

    // Spiralite Ore
    public static final DeferredBlock<Block> SPIRALITE_ORE = registerBlock("spiralite_ore",
            () -> new DropExperienceBlock(UniformInt.of(0,1),
                    BlockBehaviour.Properties.of()
                            .strength(3.0F, 3.0F) // How hard it is to break
                            .requiresCorrectToolForDrops() // Requires the correct tool
                            .sound(SoundType.STONE) // Sound it makes breaking
                            .mapColor(MapColor.COLOR_MAGENTA) // Map Color
            ));
    // Deepslate spiralite ore
    public static final DeferredBlock<Block> DEEPSLATE_SPIRALITE_ORE = registerBlock("deepslate_spiralite_ore",
            () -> new DropExperienceBlock(UniformInt.of(2,4),
                    BlockBehaviour.Properties.of()
                            .strength(4.5F, 3.0F) // How hard it is to break
                            .requiresCorrectToolForDrops() // Requires the correct tool
                            .sound(SoundType.DEEPSLATE) // Sound it makes breaking
                            .mapColor(MapColor.COLOR_MAGENTA) // Map Color
            ));




    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block); // Create and register block
        registerBlockItem(name, toReturn); // Register block item
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
