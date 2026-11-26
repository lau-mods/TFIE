package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeSelection;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ArcFurnaceLogic.class, remap = false)
public abstract class ArcFurnaceLogicMixin
{
    @Redirect(method = "enqueueProcesses",
            at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/api/crafting/ArcFurnaceRecipe;findRecipe(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/NonNullList;)Lnet/minecraft/world/item/crafting/RecipeHolder;"))
    private RecipeHolder<ArcFurnaceRecipe> tfie$selectModeRecipe(Level level, ItemStack input,
                                                                NonNullList<ItemStack> additives,
                                                                ArcFurnaceLogic.State state,
                                                                Level ignored)
    {
        return ArcFurnaceRecipeSelection.find(level, input, additives, state.getInventory());
    }
}
