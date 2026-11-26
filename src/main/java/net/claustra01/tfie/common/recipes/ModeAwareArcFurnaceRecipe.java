package net.claustra01.tfie.common.recipes;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;

import java.util.List;

public final class ModeAwareArcFurnaceRecipe extends ArcFurnaceRecipe
{
    private final int mode;

    public ModeAwareArcFurnaceRecipe(TagOutputList output, TagOutput slag,
                                     List<StackWithChance> secondaryOutputs, int time, int energy,
                                     IngredientWithSize input, List<IngredientWithSize> additives,
                                     String specialRecipeType, int mode)
    {
        super(output, slag, secondaryOutputs, time, energy, input, additives, specialRecipeType);
        this.mode = mode;
    }

    public int mode()
    {
        return mode;
    }

    @Override
    protected IERecipeSerializer<ArcFurnaceRecipe> getIESerializer()
    {
        return ModRecipes.ARC_FURNACE.get();
    }
}
