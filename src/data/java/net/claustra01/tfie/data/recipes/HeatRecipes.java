package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.concurrent.CompletableFuture;

public final class HeatRecipes extends RecipeProvider
{
    public HeatRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        add(output, "clinker_brick", "minecraft:bricks", "immersiveengineering:clinker_brick", 1499);
        add(output, "slag_glass", "immersiveengineering:slag", "immersiveengineering:slag_glass", 380);
    }

    private static void add(RecipeOutput output, String path, String input, String result, float temperature)
    {
        HeatingRecipe recipe = new HeatingRecipe(
                Ingredient.of(BuiltInRegistries.ITEM.get(id(input))),
                ItemStackProvider.of(BuiltInRegistries.ITEM.get(id(result))),
                FluidStack.EMPTY, temperature, false);
        output.accept(id(TFIE.MOD_ID + ":heating/" + path), (Recipe<?>)recipe, null);
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
