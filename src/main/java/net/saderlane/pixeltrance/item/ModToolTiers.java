package net.saderlane.pixeltrance.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.saderlane.pixeltrance.util.ModTags;

public class ModToolTiers {

    public static final Tier SPIRALITE = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_SPIRALITE_TOOL,
            600, 7f, 1.5f, 20,
            () -> Ingredient.of(ModItems.SPIRALITE.get()));
}
