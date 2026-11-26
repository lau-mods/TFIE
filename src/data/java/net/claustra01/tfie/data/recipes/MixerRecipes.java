package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.data.recipes.builder.MixerRecipeBuilder;
import net.dries007.tfc.common.recipes.ingredients.AndIngredient;
import net.dries007.tfc.common.recipes.ingredients.NotRottenIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class MixerRecipes extends RecipeProvider
{
    public MixerRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        MixerRecipeBuilder.builder()
                .input(ItemTags.create(id("c:dusts/sulfur")), 1)
                .input(Ingredient.of(
                        BuiltInRegistries.ITEM.get(id("tfc:powder/native_copper")),
                        BuiltInRegistries.ITEM.get(id("tfc:powder/malachite")),
                        BuiltInRegistries.ITEM.get(id("tfc:powder/tetrahedrite"))), 1)
                .fluidInput(fluidTag("c:ethanol"), 500)
                .output(fluid("immersiveengineering:herbicide"), 500)
                .setEnergy(3200)
                .build(output, id("tfie:mixer/herbicide"));
        MixerRecipeBuilder.builder()
                .input(Ingredient.of(
                        BuiltInRegistries.ITEM.get(id("tfc:powder/flux")),
                        BuiltInRegistries.ITEM.get(id("tfc:powder/lime"))), 1)
                .fluidInput(fluidTag("tfc:fresh_water"), 500)
                .output(fluid("tfc:limewater"), 500)
                .setEnergy(800)
                .build(output, id("tfie:mixer/limewater"));
        MixerRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id("tfc:powder/salt")), 1)
                .fluidInput(fluidTag("tfc:fresh_water"), 125)
                .output(fluid("tfc:salt_water"), 125)
                .setEnergy(800)
                .build(output, id("tfie:mixer/salt_water"));

        firmalife(output, "olive_oil_water", "tfc:olive_paste", "tfc:olive_oil_water");
        firmalife(output, "tallow", "tfc:blubber", "tfc:tallow");
        firmalife(output, "lye", "tfc:powder/wood_ash", "tfc:lye");

        Ingredient butter = AndIngredient.of(
                Ingredient.of(BuiltInRegistries.ITEM.get(id("firmalife:food/butter"))),
                NotRottenIngredient.INSTANCE);
        output.withConditions(new ModLoadedCondition("firmalife")).accept(
                id("tfie:mixer/firmalife/caramel"),
                new MixerRecipe(
                        new FluidStack(fluid("firmalife:caramel"), 1000),
                        SizedFluidIngredient.of(fluid("firmalife:sugar_water"), 1000),
                        List.of(new IngredientWithSize(butter, 1)),
                        3200),
                null);
    }

    private static void firmalife(RecipeOutput output, String path, String input, String result)
    {
        MixerRecipeBuilder.builder()
                .input(BuiltInRegistries.ITEM.get(id(input)), 1)
                .fluidInput(FluidTags.WATER, 200)
                .output(fluid(result), 200)
                .setEnergy(800)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, id("tfie:mixer/firmalife/" + path));
    }

    private static Fluid fluid(String value)
    {
        return BuiltInRegistries.FLUID.get(id(value));
    }

    private static TagKey<Fluid> fluidTag(String value)
    {
        return TagKey.create(net.minecraft.core.registries.Registries.FLUID, id(value));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
