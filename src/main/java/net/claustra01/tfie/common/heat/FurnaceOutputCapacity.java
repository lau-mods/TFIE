package net.claustra01.tfie.common.heat;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FurnaceOutputCapacity
{
    private FurnaceOutputCapacity()
    {
    }

    public static boolean canFitAll(IItemHandler inventory, int[] slots, List<ItemStack> outputs)
    {
        List<ItemStack> simulated = new ArrayList<>(slots.length);
        for(int slot : slots)
            simulated.add(inventory.getStackInSlot(slot).copy());
        for(ItemStack output : outputs)
        {
            if(output.isEmpty())
                continue;
            int remaining = output.getCount();
            for(int index = 0; index < slots.length && remaining > 0; index++)
            {
                ItemStack present = simulated.get(index);
                if(!present.isEmpty() && ItemStack.isSameItemSameComponents(present, output))
                {
                    int limit = Math.min(inventory.getSlotLimit(slots[index]), present.getMaxStackSize());
                    int accepted = Math.min(remaining, limit - present.getCount());
                    present.grow(accepted);
                    remaining -= accepted;
                }
            }
            for(int index = 0; index < slots.length && remaining > 0; index++)
            {
                if(simulated.get(index).isEmpty())
                {
                    int accepted = Math.min(remaining,
                            Math.min(inventory.getSlotLimit(slots[index]), output.getMaxStackSize()));
                    simulated.set(index, output.copyWithCount(accepted));
                    remaining -= accepted;
                }
            }
            if(remaining > 0)
                return false;
        }
        return true;
    }

    public static boolean canFitArcOutputs(IItemHandler inventory, int[] outputSlots,
                                           List<ItemStack> outputs, int slagSlot, ItemStack slag)
    {
        int[] normalSlots = Arrays.stream(outputSlots).filter(slot -> slot != slagSlot).toArray();
        List<ItemStack> normalOutputs = new ArrayList<>(outputs);
        if(!slag.isEmpty())
            for(int index = normalOutputs.size() - 1; index >= 0; index--)
            {
                ItemStack output = normalOutputs.get(index);
                if(ItemStack.isSameItemSameComponents(output, slag) && output.getCount() == slag.getCount())
                {
                    normalOutputs.remove(index);
                    break;
                }
            }
        return canFitAll(inventory, normalSlots, normalOutputs)
                && canFitAll(inventory, new int[]{slagSlot}, List.of(slag));
    }
}
