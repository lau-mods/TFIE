package net.claustra01.tfie.common;

import blusunrize.immersiveengineering.common.register.IEItems;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;

public final class AnvilSlag
{
    private static final ResourceLocation HIGH_CARBON_STEEL =
            ResourceLocation.fromNamespaceAndPath("tfc", "anvil/metal/ingot/high_carbon_steel");

    private AnvilSlag()
    {
    }

    public static void giveIfCompleted(AnvilRecipe recipe, boolean complete, ServerPlayer player, BlockPos pos)
    {
        if(complete && HIGH_CARBON_STEEL.equals(AnvilRecipe.getId(recipe)))
        {
            ItemStack slag = new ItemStack(IEItems.Ingredients.SLAG.get());
            player.getInventory().add(slag);
            if(!slag.isEmpty())
                Containers.dropItemStack(player.level(), pos.getX() + 0.5,
                        pos.getY() + 1, pos.getZ() + 0.5, slag);
        }
    }
}
