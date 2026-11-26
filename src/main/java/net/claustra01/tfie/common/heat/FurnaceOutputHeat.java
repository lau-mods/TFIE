package net.claustra01.tfie.common.heat;

import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatComponent;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.minecraft.world.item.ItemStack;

public final class FurnaceOutputHeat
{
    private FurnaceOutputHeat()
    {
    }

    public static ItemStack apply(ItemStack stack)
    {
        if(stack.isEmpty())
            return stack;
        HeatDefinition definition = HeatCapability.getDefinition(stack);
        if(definition != null && definition.forgingTemperature() > 0)
        {
            if(!HeatCapability.has(stack))
                stack.set(TFCComponents.HEAT, HeatComponent.of(definition));
            HeatCapability.setTemperature(stack, definition.forgingTemperature() / 0.6f * 0.9f);
        }
        return stack;
    }

    public static Float meltingPoint(ItemStack stack)
    {
        HeatDefinition definition = HeatCapability.getDefinition(stack);
        return definition != null && definition.forgingTemperature() > 0
                ? definition.forgingTemperature() / 0.6f
                : null;
    }
}
