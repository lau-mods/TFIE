package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.util.CachedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = ClocheBlockEntity.class, remap = false)
public abstract class ClocheBlockEntityMixin
{
    @Shadow
    @Final
    public FluidTank tank;

    @Shadow
    public int fertilizerAmount;

    @Shadow
    @Final
    @Mutable
    public Supplier<ClocheRecipe> cachedRecipe;

    @Unique
    private FluidStack tfie$activeFluid = FluidStack.EMPTY;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void tfie$matchRecipeForActiveDose(CallbackInfo callback)
    {
        ClocheBlockEntity cloche = (ClocheBlockEntity)(Object)this;
        cachedRecipe = CachedRecipe.cached(
                ClocheRecipe::findRecipe,
                cloche::getLevel,
                () -> cloche.getInventory().get(ClocheBlockEntity.SLOT_SEED),
                () -> cloche.getInventory().get(ClocheBlockEntity.SLOT_SOIL),
                () -> fertilizerAmount > 0&&!tfie$activeFluid.isEmpty()
                        ? tfie$activeFluid
                        : tank.getFluid());
    }

    @Redirect(method = "tickServer", at = @At(value = "INVOKE", target =
            "Lnet/neoforged/neoforge/fluids/capability/templates/FluidTank;drain(" +
                    "ILnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)" +
                    "Lnet/neoforged/neoforge/fluids/FluidStack;"))
    private FluidStack tfie$rememberDoseFluid(FluidTank tank, int amount,
                                               FluidAction action)
    {
        tfie$activeFluid = tank.getFluid().copyWithAmount(1);
        return tank.drain(amount, action);
    }

    @Inject(method = "readCustomNBT", at = @At("TAIL"))
    private void tfie$readDoseFluid(CompoundTag tag, boolean descPacket,
                                    HolderLookup.Provider provider, CallbackInfo callback)
    {
        tfie$activeFluid = FluidStack.parseOptional(provider, tag.getCompound("tfieActiveFluid"));
    }

    @Inject(method = "writeCustomNBT", at = @At("TAIL"))
    private void tfie$writeDoseFluid(CompoundTag tag, boolean descPacket,
                                     HolderLookup.Provider provider, CallbackInfo callback)
    {
        tag.put("tfieActiveFluid", tfie$activeFluid.saveOptional(provider));
    }
}
