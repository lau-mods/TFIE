package net.claustra01.tfie.mixin;

import net.claustra01.tfie.common.AnvilSlag;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilBlockEntity.class)
public abstract class AnvilBlockEntityMixin
{
    @Redirect(method = "work", at = @At(value = "INVOKE", target =
            "Lnet/dries007/tfc/common/recipes/AnvilRecipe;checkComplete" +
                    "(Lnet/dries007/tfc/common/recipes/AnvilRecipe$Inventory;)Z"))
    private boolean tfie$giveHighCarbonSteelSlag(AnvilRecipe recipe, AnvilRecipe.Inventory inventory,
                                                  ServerPlayer player, ForgeStep step)
    {
        boolean complete = recipe.checkComplete(inventory);
        AnvilBlockEntity anvil = (AnvilBlockEntity) (Object) this;
        AnvilSlag.giveIfCompleted(recipe, complete, player, anvil.getBlockPos());
        return complete;
    }
}
