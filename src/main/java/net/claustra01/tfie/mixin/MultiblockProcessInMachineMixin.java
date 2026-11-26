package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.MultiblockProcessInMachine;
import blusunrize.immersiveengineering.common.blocks.multiblocks.process.ProcessContext.ProcessContextInMachine;
import net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MultiblockProcessInMachine.class, remap = false)
public abstract class MultiblockProcessInMachineMixin<R extends MultiblockRecipe>
{
    @Inject(method = "doProcessTick", at = @At("HEAD"), cancellable = true)
    private void tfie$waitForOriginalArcRecipe(ProcessContextInMachine<R> context,
                                                IMultiblockLevel level, CallbackInfo callback)
    {
        if((Object)this instanceof ArcFurnaceProcessSnapshotAccess snapshot
                && !snapshot.tfie$matchesCurrentRecipe(level.getRawLevel()))
            callback.cancel();
    }
}
