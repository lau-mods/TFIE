package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.BiFunction;

@Mixin(targets = "blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic$State",
        remap = false)
public abstract class ArcFurnaceStateMixin
{
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target =
            "Lblusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcessor$InMachineProcessor;"+
                    "<init>(ILjava/util/function/IntToDoubleFunction;ILjava/lang/Runnable;Ljava/lang/Runnable;"+
                    "Ljava/util/function/BiFunction;)V"), index = 5)
    private BiFunction<Level, ResourceLocation, ArcFurnaceRecipe> tfie$useCurrentRecipes(
            BiFunction<Level, ResourceLocation, ArcFurnaceRecipe> original)
    {
        return ArcFurnaceRecipeLookup::getById;
    }
}
