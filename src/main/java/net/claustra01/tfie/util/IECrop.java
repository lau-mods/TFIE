package net.claustra01.tfie.util;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class IECrop
{
    public static final ResourceLocation CLIMATE_RANGE =
            ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "crop/hemp");

    private IECrop()
    {
    }

    public static Item seed()
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(
                "immersiveengineering", "seed"));
    }

    public static ClimateRange climateRange()
    {
        return ClimateRange.MANAGER.getCheckedReference(CLIMATE_RANGE).get();
    }
}
