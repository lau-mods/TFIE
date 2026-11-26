package net.claustra01.tfie.mixin;

import net.claustra01.tfie.common.arc.ArcFurnaceRecipeLookup;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "blusunrize.immersiveengineering.common.gui.IESlot$ArcInput", remap = false)
public abstract class ArcFurnaceInputSlotMixin
{
    @Shadow
    @Final
    private Level level;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void tfie$allowOneManualBlueprint(ItemStack stack,
                                              CallbackInfoReturnable<Boolean> callback)
    {
        if(!stack.is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
        {
            callback.setReturnValue(!stack.isEmpty() && ArcFurnaceRecipeLookup.isValidInput(level, stack));
            return;
        }
        SlotItemHandler slot = (SlotItemHandler)(Object)this;
        IItemHandler inventory = slot.getItemHandler();
        int target = slot.getContainerSlot();
        for(int index = 0; index < 12; index++)
            if(index != target && inventory.getStackInSlot(index).is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
            {
                callback.setReturnValue(false);
                return;
            }
        callback.setReturnValue(true);
    }
}
