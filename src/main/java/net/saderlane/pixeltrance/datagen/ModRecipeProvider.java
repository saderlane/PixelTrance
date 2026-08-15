package net.saderlane.pixeltrance.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.saderlane.pixeltrance.PixelTrance;
import net.saderlane.pixeltrance.block.ModBlocks;
import net.saderlane.pixeltrance.item.ModItems;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> SPIRALITE_SMELTABLES = List.of(ModItems.RAW_SPIRALITE,
                ModBlocks.SPIRALITE_ORE, ModBlocks.DEEPSLATE_SPIRALITE_ORE);

        // Recipe for Spiralite Block
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SPIRALITE_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.SPIRALITE.get())
                .unlockedBy("has_spiralite", has(ModItems.SPIRALITE)).save(recipeOutput);

        // Recipe for Spiralite from Spiralite Block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SPIRALITE.get(), 9)
                .requires(ModBlocks.SPIRALITE_BLOCK)
                .unlockedBy("has_spiralite_block", has(ModBlocks.SPIRALITE_BLOCK))
                .save(recipeOutput);
                        //, "pixeltrance:spiralite"); // This is only needed if 2 recipes return the same thing, make sure the ID is different


        // Recipe for Spiralite Lamp
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SPIRALITE_LAMP.get())
                .pattern("SSS")
                .pattern("SRS")
                .pattern("SSS")
                .define('S', ModItems.SPIRALITE.get())
                .define('R', Items.REDSTONE_LAMP)
                .unlockedBy("has_spiralite", has(ModItems.SPIRALITE)).save(recipeOutput);



        // Recipe for Pocket Watch
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.POCKET_WATCH.get())
                .pattern("gCg")
                .pattern("gSg")
                .pattern("ggg")
                .define('S', ModItems.SPIRALITE.get())
                .define('g', Items.GOLD_INGOT)
                .define('C', Items.CLOCK)
                .unlockedBy("has_spiralite", has(ModItems.SPIRALITE)).save(recipeOutput);


        toolBuilder("sword",
                RecipeCategory.TOOLS, ModItems.SPIRALITE_SWORD.get(), Items.STICK, ModItems.SPIRALITE.get(), recipeOutput);
        toolBuilder("pickaxe",
                RecipeCategory.TOOLS, ModItems.SPIRALITE_PICKAXE.get(), Items.STICK, ModItems.SPIRALITE.get(), recipeOutput);
        toolBuilder("shovel",
                RecipeCategory.TOOLS, ModItems.SPIRALITE_SHOVEL.get(), Items.STICK, ModItems.SPIRALITE.get(), recipeOutput);
        toolBuilder("axe",
                RecipeCategory.TOOLS, ModItems.SPIRALITE_AXE.get(), Items.STICK, ModItems.SPIRALITE.get(), recipeOutput);
        toolBuilder("hoe",
                RecipeCategory.TOOLS, ModItems.SPIRALITE_HOE.get(), Items.STICK, ModItems.SPIRALITE.get(), recipeOutput);



        oreSmelting(recipeOutput, SPIRALITE_SMELTABLES, RecipeCategory.MISC, ModItems.SPIRALITE.get(), 0.25f, 200, "spiralite");
        oreBlasting(recipeOutput, SPIRALITE_SMELTABLES, RecipeCategory.MISC, ModItems.SPIRALITE.get(), 0.25f, 100, "spiralite");
    }

    protected static void toolBuilder(String toolType, RecipeCategory category, Item item, Item itemBase, Item material, RecipeOutput recipeOutput) {

        switch (toolType) {
            case "sword": // Build sword
                ShapedRecipeBuilder.shaped(category, item)
                        .pattern(" M ")
                        .pattern(" M ")
                        .pattern(" b ")
                        .define('M', material)
                        .define('b', itemBase)
                        .unlockedBy("has_spiralite", has(material)).save(recipeOutput);
                break;
            case "pickaxe": // Build pickaxe
                ShapedRecipeBuilder.shaped(category, item)
                        .pattern("MMM")
                        .pattern(" b ")
                        .pattern(" b ")
                        .define('M', material)
                        .define('b', itemBase)
                        .unlockedBy("has_spiralite", has(material)).save(recipeOutput);
                break;
            case "shovel": // Build shovel
                ShapedRecipeBuilder.shaped(category, item)
                        .pattern(" M ")
                        .pattern(" b ")
                        .pattern(" b ")
                        .define('M', material)
                        .define('b', itemBase)
                        .unlockedBy("has_spiralite", has(material)).save(recipeOutput);
                break;
            case "axe": // Build axe
                ShapedRecipeBuilder.shaped(category, item)
                        .pattern("MM ")
                        .pattern("Mb ")
                        .pattern(" b ")
                        .define('M', material)
                        .define('b', itemBase)
                        .unlockedBy("has_spiralite", has(material)).save(recipeOutput);
                break;
            case "hoe": // Build hoe
                ShapedRecipeBuilder.shaped(category, item)
                        .pattern("MM ")
                        .pattern(" b ")
                        .pattern(" b ")
                        .define('M', material)
                        .define('b', itemBase)
                        .unlockedBy("has_spiralite", has(material)).save(recipeOutput);
                break;

        }

    }


    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, PixelTrance.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
