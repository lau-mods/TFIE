package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.util.EmptyRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.FalseCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class RecipeOverrides extends RecipeProvider
{
    private static final List<String> REPLACED_METALS = List.of(
            "aluminum", "brass", "bronze", "cobalt", "constantan", "copper", "electrum", "gold", "invar",
            "iron", "lead", "nickel", "osmium", "platinum", "rose_gold", "silver", "steel", "tin",
            "tungsten", "uranium", "zinc");

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
        for(String metal : REPLACED_METALS)
            for(String shape : List.of("plate", "rod", "wire"))
                output.accept(ResourceLocation.fromNamespaceAndPath(
                        "immersiveengineering", "metalpress/" + shape + "_" + metal),
                        EmptyRecipe.INSTANCE, null, FalseCondition.INSTANCE);
        output.accept(ResourceLocation.fromNamespaceAndPath(
                "immersiveengineering", "metalpress/rod_netherite"), EmptyRecipe.INSTANCE, null,
                FalseCondition.INSTANCE);
    }
}
