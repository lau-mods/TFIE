package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CokeOvenLogic.class, remap = false)
public abstract class CokeOvenLogicMixin
{
    @Redirect(method = "tickServer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getCount()I", ordinal = 0), require = 1)
    private int useRemainingOutputCapacity(ItemStack output)
    {
        return output.getMaxStackSize() - output.getCount();
    }
}
