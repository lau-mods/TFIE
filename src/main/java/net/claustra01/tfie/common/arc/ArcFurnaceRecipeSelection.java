package net.claustra01.tfie.common.arc;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.function.IntFunction;

public final class ArcFurnaceRecipeSelection
{
    private ArcFurnaceRecipeSelection()
    {
    }

    public static RecipeHolder<ArcFurnaceRecipe> find(Level level, ItemStack input,
                                                      NonNullList<ItemStack> additives,
                                                      IItemHandler inventory)
    {
        BlueprintState blueprint = blueprintState(inventory::getStackInSlot);
        if(blueprint.stopReason() != StopReason.NONE)
            return null;

        RecipeHolder<ArcFurnaceRecipe> candidate = null;
        for(RecipeHolder<ArcFurnaceRecipe> holder : ArcFurnaceRecipeLookup.recipes(level))
        {
            if(!(holder.value() instanceof ModeAwareArcFurnaceRecipe recipe) || !recipe.input.test(input))
                continue;
            if(recipe.mode() != 0 && (blueprint.count() != 1 || recipe.mode() != blueprint.mode()))
                continue;
            if(candidate != null)
                return null;
            candidate = holder;
        }
        return candidate != null && candidate.value().matches(input, additives) ? candidate : null;
    }

    public static StopReason stopReason(Level level, IntFunction<ItemStack> inventory)
    {
        BlueprintState blueprint = blueprintState(inventory);
        if(blueprint.stopReason() != StopReason.NONE)
            return blueprint.stopReason();
        for(int slot = 0; slot < 12; slot++)
        {
            ItemStack input = inventory.apply(slot);
            if(input.isEmpty() || input.is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
                continue;
            int candidates = 0;
            for(RecipeHolder<ArcFurnaceRecipe> holder : ArcFurnaceRecipeLookup.recipes(level))
                if(holder.value() instanceof ModeAwareArcFurnaceRecipe recipe
                        && recipe.input.test(input)
                        && (recipe.mode() == 0 || blueprint.count() == 1 && recipe.mode() == blueprint.mode())
                        && ++candidates > 1)
                    return StopReason.DUPLICATE_RECIPE;
        }
        return StopReason.NONE;
    }

    private static BlueprintState blueprintState(IntFunction<ItemStack> inventory)
    {
        int count = 0;
        int mode = 0;
        for(int slot = 0; slot < 12; slot++)
        {
            ItemStack stack = inventory.apply(slot);
            if(stack.is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
            {
                count += stack.getCount();
                mode = stack.getOrDefault(ModDataComponents.ARC_FURNACE_MODE, 1);
            }
        }
        StopReason reason = count > 1 ? StopReason.MULTIPLE_BLUEPRINTS
                : count == 1 && (mode < 1 || mode > 8) ? StopReason.INVALID_MODE : StopReason.NONE;
        return new BlueprintState(count, mode, reason);
    }

    private record BlueprintState(int count, int mode, StopReason stopReason)
    {
    }

    public enum StopReason
    {
        NONE(""),
        MULTIPLE_BLUEPRINTS("gui.tfie.arc_furnace.invalid_blueprints"),
        INVALID_MODE("gui.tfie.arc_furnace.invalid_mode"),
        DUPLICATE_RECIPE("gui.tfie.arc_furnace.duplicate_recipe");

        private final String translationKey;

        StopReason(String translationKey)
        {
            this.translationKey = translationKey;
        }

        public String translationKey()
        {
            return translationKey;
        }
    }
}
