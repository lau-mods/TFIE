package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.FermenterRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class FermenterRecipes extends RecipeProvider
{
    private static final List<String> FOODS = List.of(
            "banana", "beet", "blackberry", "blueberry", "bunchberry", "cassava", "cherry", "cloudberry",
            "cranberry", "elderberry", "gooseberry", "green_apple", "lemon", "melon_slice", "olive",
            "orange", "peach", "plum", "potato", "pumpkin_chunks", "raspberry", "red_apple", "snowberry",
            "strawberry", "sugarcane", "wintergreen_berry");

    public FermenterRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        var ethanol = BuiltInRegistries.FLUID.get(id("immersiveengineering:ethanol"));
        for(String food : FOODS)
            FermenterRecipeBuilder.builder()
                    .input(BuiltInRegistries.ITEM.get(id("tfc:food/" + food)), 1)
                    .output(ethanol, 80)
                    .setEnergy(6400)
                    .build(output, id("tfie:fermenter/" + food));

        FermenterRecipeBuilder.builder()
                .input(ItemTags.create(id("c:foods/grain")), 1)
                .output(ethanol, 80)
                .setEnergy(6400)
                .build(output, id("tfie:fermenter/grains"));

        FermenterRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("firmalife:food/raw_honey")), 1)
                .output(ethanol, 250)
                .setEnergy(6400)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, id("tfie:fermenter/raw_honey"));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
