package net.claustra01.tfie.mixin.compat.jei;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "blusunrize.immersiveengineering.common.util.compat.jei.arcfurnace.ArcFurnaceRecipeCategory",
        remap = false)
public abstract class ArcFurnaceRecipeCategoryMixin
{
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lblusunrize/immersiveengineering/api/crafting/ArcFurnaceRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At("TAIL"))
    private void tfie$addModeBlueprint(IRecipeLayoutBuilder builder, ArcFurnaceRecipe recipe,
                                       IFocusGroup focuses, CallbackInfo callback)
    {
        if(recipe instanceof ModeAwareArcFurnaceRecipe modeRecipe && modeRecipe.mode() > 0)
        {
            ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
            blueprint.set(ModDataComponents.ARC_FURNACE_MODE, modeRecipe.mode());
            builder.addSlot(RecipeIngredientRole.CATALYST, 2, 38)
                    .setStandardSlotBackground()
                    .addItemStack(blueprint);
        }
    }
}
