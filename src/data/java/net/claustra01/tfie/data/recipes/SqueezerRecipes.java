package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class SqueezerRecipes extends RecipeProvider
{
    private static final List<String> SEEDS = List.of(
            "alfalfa", "barley", "beet", "cabbage", "canola", "carrot", "cassava", "jute", "maize",
            "melon", "oat", "onion", "peanut", "pumpkin", "radish", "red_bell_pepper", "rice", "rye",
            "soybean", "squash", "tomato", "wheat", "yellow_bell_pepper");

    public SqueezerRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        var plantOil = BuiltInRegistries.FLUID.get(id("immersiveengineering:plantoil"));
        for(String seed : SEEDS)
            SqueezerRecipeBuilder.builder()
                    .input(BuiltInRegistries.ITEM.get(id("tfc:seeds/" + seed)), 1)
                    .output(plantOil, 80)
                    .setEnergy(6400)
                    .build(output, id("tfie:squeezer/" + seed));

        SqueezerRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("firmalife:wild_honeycomb")), 1)
                .output(BuiltInRegistries.ITEM.get(id("firmalife:food/raw_honey")), 1)
                .setEnergy(6400)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, id("tfie:squeezer/firmalife/wild_honeycomb"));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
