package net.claustra01.tfie.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class TFIEJeiPlugin implements IModPlugin
{
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "jei");

    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration)
    {
        registration.registerSubtypeInterpreter(ModItems.ARC_FURNACE_BLUEPRINT.get(),
                (stack, context) -> context == UidContext.Ingredient
                        ? String.valueOf(stack.getOrDefault(ModDataComponents.ARC_FURNACE_MODE, 1))
                        : "");
    }
}
