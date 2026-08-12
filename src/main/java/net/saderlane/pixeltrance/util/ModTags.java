package net.saderlane.pixeltrance.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.saderlane.pixeltrance.PixelTrance;

public class ModTags {
    public static class Blocks {

        public  static final TagKey<Block> INCORRECT_FOR_SPIRALITE_TOOL = createTag("incorrect_for_spiralite_tool");
        public  static final TagKey<Block> NEEDS_SPIRALITE_TOOL = createTag("needs_spiralite_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> HYPNOTIC_ITEMS = createTag("hypnotic_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PixelTrance.MOD_ID, name));
        }
    }
}

