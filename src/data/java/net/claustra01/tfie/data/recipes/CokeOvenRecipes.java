package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.CokeOvenRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public final class CokeOvenRecipes extends RecipeProvider
{
    public CokeOvenRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        add(output, "bituminous_coal");
        add(output, "lignite");
    }

    private static void add(RecipeOutput output, String coal)
    {
        CokeOvenRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("tfc:ore/" + coal)), 16)
                .output(ItemTags.create(id("c:coal_coke")), 1)
                .creosoteAmount(500)
                .setTime(6000)
                .build(output, id("tfie:cokeoven/" + coal));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
