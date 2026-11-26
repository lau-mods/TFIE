package net.claustra01.tfie.common.arc;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public final class ArcFurnaceRecipeLookup
{
    private ArcFurnaceRecipeLookup()
    {
    }

    public static List<RecipeHolder<ArcFurnaceRecipe>> recipes(Level level)
    {
        List<RecipeHolder<ArcFurnaceRecipe>> recipes = new ArrayList<>();
        for(RecipeHolder<?> holder : level.getRecipeManager().getRecipes())
            if(holder.value() instanceof ModeAwareArcFurnaceRecipe)
                recipes.add(cast(holder));
        return recipes;
    }

    public static ArcFurnaceRecipe getById(Level level, ResourceLocation id)
    {
        return level.getRecipeManager().byKey(id).map(RecipeHolder::value)
                .filter(ModeAwareArcFurnaceRecipe.class::isInstance)
                .map(ArcFurnaceRecipe.class::cast).orElse(null);
    }

    public static boolean isValidInput(Level level, ItemStack stack)
    {
        return recipes(level).stream().anyMatch(holder -> holder.value().isValidInput(stack));
    }

    public static boolean isValidAdditive(Level level, ItemStack stack)
    {
        return recipes(level).stream().anyMatch(holder -> holder.value().isValidAdditive(stack));
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<ArcFurnaceRecipe> cast(RecipeHolder<?> holder)
    {
        return (RecipeHolder<ArcFurnaceRecipe>)(RecipeHolder<?>)holder;
    }
}
