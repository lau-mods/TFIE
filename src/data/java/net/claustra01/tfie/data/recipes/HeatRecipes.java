package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;

@SuppressWarnings("SameParameterValue")
public interface HeatRecipes extends Recipes
{
    default void heatRecipes()
    {
        add(IEItems.Ingredients.SLAG, IEBlocks.StoneDecoration.SLAG_GLASS, 380);
        add(net.minecraft.world.item.Items.BRICKS, IEBlocks.StoneDecoration.CLINKER_BRICK, 1499);
    }

    private void add(ItemLike input, ItemLike output, float temperature)
    {
        add(Ingredient.of(input), ItemStackProvider.of(output), temperature);
    }

    private void add(Ingredient input, ItemStackProvider output, float temperature)
    {
        add(new HeatingRecipe(input, output, FluidStack.EMPTY, temperature, false));
    }
}
