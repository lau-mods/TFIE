package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceProcess;
import net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeLookup;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeSnapshot;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(value = ArcFurnaceProcess.class, remap = false)
public abstract class ArcFurnaceProcessPersistenceMixin implements ArcFurnaceProcessSnapshotAccess
{
    @Unique
    private static final String TFIE_RECIPE_SNAPSHOT = "tfie_recipe_snapshot";

    @Unique
    private String tfie$recipeSnapshot = "";

    @Inject(method = "<init>(Ljava/util/function/BiFunction;Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("RETURN"))
    private void tfie$loadRecipeSnapshot(BiFunction<?, ?, ?> recipeGetter, CompoundTag data,
                                         CallbackInfo callback)
    {
        tfie$recipeSnapshot = data.getString(TFIE_RECIPE_SNAPSHOT);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/item/crafting/RecipeHolder;J[I)V",
            at = @At("RETURN"))
    private void tfie$captureRecipeSnapshot(RecipeHolder<ArcFurnaceRecipe> holder, long seed,
                                             int[] inputSlots, CallbackInfo callback)
    {
        tfie$recipeSnapshot = ArcFurnaceRecipeSnapshot.create(holder.value());
    }

    @Inject(method = "writeExtraDataToNBT", at = @At("TAIL"))
    private void tfie$saveRecipeSnapshot(CompoundTag data, HolderLookup.Provider provider,
                                         CallbackInfo callback)
    {
        data.putString(TFIE_RECIPE_SNAPSHOT, tfie$recipeSnapshot);
    }

    @Override
    public boolean tfie$matchesCurrentRecipe(Level level)
    {
        ArcFurnaceProcess process = (ArcFurnaceProcess)(Object)this;
        ArcFurnaceRecipe current = ArcFurnaceRecipeLookup.getById(level, process.getRecipeId());
        return current != null && tfie$recipeSnapshot.equals(ArcFurnaceRecipeSnapshot.create(current));
    }
}
