package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FurnaceHandler.class, remap = false)
public abstract class FurnaceHandlerMixin<R extends IESerializableRecipe>
{
    @Shadow
    private int process;

    @Shadow
    private int processMax;

    @Shadow
    private boolean isAnyInputEmpty(net.neoforged.neoforge.items.IItemHandler inventory)
    {
        throw new AssertionError();
    }

    @Shadow
    private R getRecipe(FurnaceHandler.IFurnaceEnvironment<R> environment)
    {
        throw new AssertionError();
    }

    @Inject(method = "tickServer", at = @At("HEAD"), cancellable = true)
    private void tfie$waitForOutputCapacity(
            IMultiblockContext<? extends FurnaceHandler.IFurnaceEnvironment<R>> context,
            CallbackInfoReturnable<Boolean> callback
    )
    {
        FurnaceHandler.IFurnaceEnvironment<R> environment = context.getState();
        if(processMax > 0
                &&process > 0
                &&process <= environment.getProcessSpeed(context.getLevel())
                &&!isAnyInputEmpty(environment.getInventory())
                &&getRecipe(environment)==null)
            callback.setReturnValue(false);
    }

    @Redirect(method = "getRecipe", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"),
            remap = true)
    private boolean tfie$compareOutputComponents(ItemStack existing, ItemStack output)
    {
        return ItemStack.isSameItemSameComponents(existing, output);
    }
}
