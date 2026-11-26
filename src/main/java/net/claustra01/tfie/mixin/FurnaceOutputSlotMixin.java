package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import net.claustra01.tfie.common.heat.FurnaceOutputHeat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler$OutputSlot", remap = false)
public abstract class FurnaceOutputSlotMixin
{
    @Inject(method = "get", at = @At("RETURN"))
    private void tfie$heatMetalOutputs(Object recipe, CallbackInfoReturnable<ItemStack> callback)
    {
        if(recipe instanceof AlloyRecipe || recipe instanceof BlastFurnaceRecipe)
            FurnaceOutputHeat.apply(callback.getReturnValue());
    }
}
