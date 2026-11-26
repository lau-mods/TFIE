package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.common.crafting.fluidaware.IngredientFluidStack;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.stream.Stream;

@Mixin(value = IngredientFluidStack.class, remap = false)
public abstract class IngredientFluidStackMixin
{
    @Shadow
    @Final
    private SizedFluidIngredient fluidIngredient;

    @Inject(method = "getItems", at = @At("HEAD"), cancellable = true)
    private void tfie$getTfcFluidContainers(CallbackInfoReturnable<Stream<ItemStack>> callback)
    {
        callback.setReturnValue(Stream.of(fluidIngredient.getFluids())
                .flatMap(fluid -> Helpers.allItems(TFCTags.Items.FLUID_ITEM_INGREDIENT_EMPTY_CONTAINERS)
                        .map(item -> {
                            ItemStack container = item.getDefaultInstance();
                            IFluidHandlerItem handler = container.getCapability(Capabilities.FluidHandler.ITEM);
                            if(handler == null)
                                return null;

                            handler.fill(fluid.copyWithAmount(Integer.MAX_VALUE), FluidAction.EXECUTE);
                            FluidStack content = handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
                            if(!fluidIngredient.test(content))
                                return null;

                            ItemStack displayContainer = item.getDefaultInstance();
                            IFluidHandlerItem displayHandler = displayContainer.getCapability(Capabilities.FluidHandler.ITEM);
                            if(displayHandler == null)
                                return null;

                            int filled = displayHandler.fill(fluid.copyWithAmount(fluidIngredient.amount()),
                                    FluidAction.EXECUTE);
                            return filled == fluidIngredient.amount() ? displayHandler.getContainer() : null;
                        }))
                .filter(Objects::nonNull));
    }
}
