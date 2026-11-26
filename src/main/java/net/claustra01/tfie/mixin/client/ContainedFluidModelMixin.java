package net.claustra01.tfie.mixin.client;

import net.claustra01.tfie.client.model.PartialFluidContainerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.IdentityHashMap;
import java.util.Map;

@Mixin(targets = "net.dries007.tfc.client.model.ContainedFluidModel$ContainedFluidOverrideHandler", remap = false)
public abstract class ContainedFluidModelMixin
{
    @Unique
    private Map<BakedModel, BakedModel[]> tfie$partialModels;

    @Inject(method = "resolve", at = @At("RETURN"), cancellable = true)
    private void tfie$showPartialFluidLevel(BakedModel model, ItemStack stack, ClientLevel level,
                                            LivingEntity entity, int seed,
                                            CallbackInfoReturnable<BakedModel> callback)
    {
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if(handler == null)
            return;

        FluidStack fluid = handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
        int capacity = handler.getTankCapacity(0);
        if(fluid.isEmpty() || fluid.getAmount() >= capacity)
            return;

        int fluidLevel = Mth.clamp(Mth.ceil(16f * fluid.getAmount() / capacity), 1, 15);
        BakedModel resolved = callback.getReturnValue();
        if(resolved.getOverrides() != (Object)this)
            return;
        if(tfie$partialModels == null)
            tfie$partialModels = new IdentityHashMap<>();
        BakedModel[] levels = tfie$partialModels.computeIfAbsent(resolved, unused -> new BakedModel[16]);
        if(levels[fluidLevel] == null)
            levels[fluidLevel] = new PartialFluidContainerModel(resolved, fluidLevel);
        callback.setReturnValue(levels[fluidLevel]);
    }
}
