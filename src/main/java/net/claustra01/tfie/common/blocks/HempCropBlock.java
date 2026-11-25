package net.claustra01.tfie.common.blocks;

import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.util.IECrop;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class HempCropBlock extends DoubleCropBlock
{
    public HempCropBlock(ExtendedProperties properties)
    {
        super(properties, 3, 4, ModBlocks.DEAD_HEMP_CROP, IECrop::seed,
                0.6F, 0.2F, 0.6F, IECrop::climateRange);
    }

    @Override
    public IntegerProperty getAgeProperty()
    {
        return TFCBlockStateProperties.AGE_4;
    }
}
