package net.claustra01.tfie.mixin;

import net.claustra01.tfie.common.arc.ArcFurnaceRecipeLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "blusunrize.immersiveengineering.common.gui.IESlot$ArcAdditive", remap = false)
public abstract class ArcFurnaceAdditiveSlotMixin
{
    @Shadow
    @Final
    private Level level;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void tfie$useCurrentRecipes(ItemStack stack, CallbackInfoReturnable<Boolean> callback)
    {
        callback.setReturnValue(!stack.isEmpty() && ArcFurnaceRecipeLookup.isValidAdditive(level, stack));
    }
}
