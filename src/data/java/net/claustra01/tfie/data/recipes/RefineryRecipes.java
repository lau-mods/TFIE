package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

public final class RefineryRecipes extends RecipeProvider
{
    public RefineryRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        RefineryRecipeBuilder.builder()
                .input(SizedFluidIngredient.of(fluid("tfc:vinegar"), 1))
                .input(SizedFluidIngredient.of(fluid("tfc:salt_water"), 9))
                .output(fluid("tfc:brine"), 10)
                .setEnergy(50)
                .build(output, id("tfie:refinery/brine"));
        RefineryRecipeBuilder.builder()
                .input(SizedFluidIngredient.of(fluid("tfc:vinegar"), 1))
                .input(fluidTag("c:milk"), 9)
                .output(fluid("tfc:milk_vinegar"), 10)
                .setEnergy(50)
                .addCondition(new NotCondition(new ModLoadedCondition("firmalife")))
                .build(output, id("tfie:refinery/milk_vinegar"));
        RefineryRecipeBuilder.builder()
                .input(SizedFluidIngredient.of(fluid("tfc:vinegar"), 1))
                .input(fluidTag("firmalife:milks"), 9)
                .output(fluid("tfc:milk_vinegar"), 10)
                .setEnergy(50)
                .addCondition(new ModLoadedCondition("firmalife"))
                .build(output, id("tfie:refinery/firmalife/milk_vinegar"));
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
