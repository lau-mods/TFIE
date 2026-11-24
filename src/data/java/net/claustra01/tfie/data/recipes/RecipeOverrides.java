package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.util.EmptyRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.FalseCondition;

import java.util.concurrent.CompletableFuture;

public final class RecipeOverrides extends RecipeProvider
{
    public RecipeOverrides(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        output.accept(ResourceLocation.fromNamespaceAndPath(
                "immersiveengineering", "crafting/treated_wood_horizontal"), EmptyRecipe.INSTANCE, null,
                FalseCondition.INSTANCE);
    }
}
