package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcess;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext;
import net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess;
import net.claustra01.tfie.common.heat.FurnaceOutputCapacity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MultiblockProcess.class, remap = false)
public abstract class MultiblockProcessMixin<R extends MultiblockRecipe, C extends ProcessContext<R>>
{
    @Shadow
    protected abstract List<ItemStack> getRecipeItemOutputs(Level level, C context);

    @Inject(method = "canProcess", at = @At("HEAD"), cancellable = true)
    private void tfie$requireAllArcOutputs(C context, Level level,
                                           CallbackInfoReturnable<Boolean> callback)
    {
        if(!((Object)this instanceof ArcFurnaceProcess)
                || !(context instanceof ProcessContext.ProcessContextInMachine<R> machine)
                || !(context instanceof ArcFurnaceLogic.State state))
            return;
        MultiblockProcess<R, C> process = (MultiblockProcess<R, C>)(Object)this;
        if((Object)this instanceof ArcFurnaceProcessSnapshotAccess snapshot
                && !snapshot.tfie$matchesCurrentRecipe(level))
        {
            callback.setReturnValue(false);
            return;
        }
        R recipe = process.getRecipe(level);
        if(recipe == null)
        {
            callback.setReturnValue(false);
            return;
        }
        int maxTicks = process.getMaxTicks(level);
        int energyPerTick = recipe.getTotalProcessEnergy() / maxTicks;
        if(context.getEnergy().extractEnergy(energyPerTick, true) != energyPerTick
                || !state.hasElectrodes())
        {
            callback.setReturnValue(false);
            return;
        }
        ItemStack slag = recipe instanceof ArcFurnaceRecipe arcRecipe
                ? arcRecipe.slag.get() : ItemStack.EMPTY;
        if(process.processTick + 1 >= maxTicks
                && (!FurnaceOutputCapacity.canFitArcOutputs(machine.getInventory(), machine.getOutputSlots(),
                getRecipeItemOutputs(level, context), ArcFurnaceLogic.SLAG_SLOT, slag)
                || !context.additionalCanProcessCheck(process, level)))
        {
            callback.setReturnValue(false);
            return;
        }
        callback.setReturnValue(true);
    }
}
