package net.claustra01.tfie.data.providers;

import java.util.concurrent.CompletableFuture;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.claustra01.tfie.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;

public class BuiltinItemHeat extends DataManagerProvider<HeatDefinition> implements Accessors
{
    public BuiltinItemHeat(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(HeatCapability.MANAGER, output, lookup);
    }

    @Override
    protected void addData(HolderLookup.Provider provider)
    {
        add(IEItems.Ingredients.SLAG, 0.6f);
        add(net.minecraft.world.item.Items.BRICKS, 1.0f);
    }

    private void add(ItemLike item, float heatCapacity)
    {
        add(Ingredient.of(item), heatCapacity);
    }

    private void add(Ingredient item, float heatCapacity)
    {
        add(nameOf(item), new HeatDefinition(item, heatCapacity, 0f, 0f));
    }
}
