package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceProcess;
import net.claustra01.tfie.common.heat.FurnaceOutputHeat;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ArcFurnaceProcess.class, remap = false)
public abstract class ArcFurnaceProcessMixin
{
    @Inject(method = "getRecipeItemOutputs", at = @At("RETURN"))
    private void tfie$heatMetalOutputs(CallbackInfoReturnable<List<ItemStack>> callback)
    {
        callback.getReturnValue().forEach(FurnaceOutputHeat::apply);
    }
}
