package net.claustra01.tfie.common.arc;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.dries007.tfc.common.component.TFCComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.StringJoiner;

public final class ArcFurnaceRecipeSnapshot
{
    private ArcFurnaceRecipeSnapshot()
    {
    }

    public static String create(ArcFurnaceRecipe recipe)
    {
        StringJoiner snapshot = new StringJoiner("|");
        snapshot.add("mode=" + (recipe instanceof ModeAwareArcFurnaceRecipe modeRecipe
                ? modeRecipe.mode() : -1));
        snapshot.add("time=" + recipe.getTotalProcessTime());
        snapshot.add("energy=" + recipe.getTotalProcessEnergy());
        snapshot.add("input=" + recipe.input.serialize());
        for(IngredientWithSize additive : recipe.additives)
            snapshot.add("additive=" + additive.serialize());
        for(ItemStack output : recipe.getBaseOutputs())
            snapshot.add("output=" + stack(output));
        snapshot.add("slag=" + stack(recipe.slag.get()));
        for(StackWithChance secondary : recipe.secondaryOutputs)
            snapshot.add("secondary=" + stack(secondary.stack().get()) + '@' + secondary.chance()
                    + '@' + secondary.conditions());
        snapshot.add("special=" + recipe.specialRecipeType);
        return snapshot.toString();
    }

    private static String stack(ItemStack stack)
    {
        if(stack.isEmpty())
            return "empty";
        ItemStack snapshotStack = stack.copy();
        snapshotStack.remove(TFCComponents.HEAT);
        return BuiltInRegistries.ITEM.getKey(stack.getItem()) + "*" + stack.getCount()
                + snapshotStack.getComponentsPatch();
    }
}
