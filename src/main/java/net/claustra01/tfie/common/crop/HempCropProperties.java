package net.claustra01.tfie.common.crop;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;


public final class HempCropProperties
{
    private HempCropProperties()
    {
    }

    public static ExtendedProperties create()
    {
        return ExtendedProperties.of(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .strength(0.4F)
                .sound(SoundType.CROP)
                .flammable(60, 30)
                .pushReaction(PushReaction.DESTROY);
    }

    public static Supplier<ClimateRange> climateRange()
    {
        return ClimateRange.MANAGER.getReference(
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "crop/hemp"));
    }
}
