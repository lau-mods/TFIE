package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class CrusherRecipes extends RecipeProvider
{
    private static final List<String> COLORS = List.of(
            "black", "brown", "green", "pink", "red", "white", "yellow");

    public CrusherRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        CrusherRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("minecraft:charcoal")))
                .output(BuiltInRegistries.ITEM.get(id("tfc:powder/charcoal")), 4)
                .setEnergy(1600)
                .build(output, id("tfie:crusher/charcoal"));
        CrusherRecipeBuilder.builder()
                .input(ItemTags.create(id("tfc:fluxstone")))
                .output(BuiltInRegistries.ITEM.get(id("tfc:powder/flux")), 2)
                .setEnergy(1600)
                .build(output, id("tfie:crusher/fluxstone"));
        CrusherRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("tfc:food/olive")))
                .output(BuiltInRegistries.ITEM.get(id("tfc:olive_paste")), 2)
                .setEnergy(800)
                .build(output, id("tfie:crusher/olive"));

        for(String shape : List.of("cut", "raw", "smooth"))
            for(String color : COLORS)
                CrusherRecipeBuilder.builder()
                        .input(Ingredient.of(BuiltInRegistries.ITEM.get(id(
                                "tfc:" + shape + "_sandstone/" + color))))
                        .output(BuiltInRegistries.ITEM.get(id("tfc:sand/" + color)), 2)
                        .addSecondary(BuiltInRegistries.ITEM.get(id("tfc:powder/saltpeter")), 0.5F)
                        .setEnergy(3200)
                        .build(output, id("tfie:crusher/sandstone/" + shape + color));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
