package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class CraftingRecipes extends RecipeProvider
{
    public CraftingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("immersiveengineering", "treated_wood_horizontal")))
                .define('X', ModItems.TREATED_WOOD_LUMBER.get())
                .pattern("XX")
                .pattern("XX")
                .unlockedBy("has_treated_lumber", has(ModItems.TREATED_WOOD_LUMBER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        TFIE.MOD_ID, "crafting/treated_wood_horizontal"));
    }
}
