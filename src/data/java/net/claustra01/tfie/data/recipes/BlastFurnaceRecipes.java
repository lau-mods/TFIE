package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class BlastFurnaceRecipes extends RecipeProvider
{
    public BlastFurnaceRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ResourceLocation bloom = ResourceLocation.parse("tfc:raw_iron_bloom");
        for(String ore : List.of("hematite", "magnetite", "limonite"))
        {
            ResourceLocation input = ResourceLocation.parse("tfcm:ore_processing/briquet/" + ore);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                    "blastfurnace/briquet/" + ore);
            BlastFurnaceRecipe recipe = new BlastFurnaceRecipe(
                    new TagOutput(new ItemStack(BuiltInRegistries.ITEM.get(bloom))),
                    new IngredientWithSize(Ingredient.of(BuiltInRegistries.ITEM.get(input))),
                    1200, TagOutput.EMPTY);
            output.accept(id, recipe, null, TFIEConditions.recipe(id,
                    "Blast Furnace briquet is unavailable", null, List.of(input, bloom),
                    List.of(), null, ore));
        }
    }
}
