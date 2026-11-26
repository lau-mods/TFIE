package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceInputHandler;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ArcFurnaceInputHandler.class, remap = false)
public abstract class ArcFurnaceInputHandlerMixin
{
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void tfie$rejectAutomatedBlueprint(int slot, ItemStack stack, boolean simulate,
                                               CallbackInfoReturnable<ItemStack> callback)
    {
        if(stack.is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
            callback.setReturnValue(stack);
    }
}
