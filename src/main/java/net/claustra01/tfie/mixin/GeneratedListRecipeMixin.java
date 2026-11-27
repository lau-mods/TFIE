package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.common.crafting.GeneratedListRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = GeneratedListRecipe.class, remap = false)
public abstract class GeneratedListRecipeMixin
{
    @Shadow
    public abstract ResourceLocation getGeneratorID();

    @Inject(method = "getSubRecipes", at = @At("HEAD"), cancellable = true)
    private void tfie$excludeArcRecycling(CallbackInfoReturnable<List<Recipe<?>>> callback)
    {
        if(getGeneratorID().equals(ResourceLocation.fromNamespaceAndPath(
                "immersiveengineering", "arc_recycling_list")))
            callback.setReturnValue(List.of());
    }
}
