package net.claustra01.tfie.common.crop;

import blusunrize.immersiveengineering.common.register.IEItems;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.ModBlockEntities;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class HempCropBlock extends DoubleCropBlock
{
    public static final IntegerProperty AGE = TFCBlockStateProperties.getAgeProperty(4);

    public HempCropBlock()
    {
        super(HempCropProperties.create()
                        .blockEntity(ModBlockEntities.CROP)
                        .serverTicks(CropBlockEntity::serverTickBottomPartOnly),
                3, 4, ModBlocks.DEAD_HEMP_CROP, IEItems.Misc.HEMP_SEEDS,
                0.6F, 0.2F, 0.6F, HempCropProperties.climateRange());
    }

    @Override
    public IntegerProperty getAgeProperty()
    {
        return AGE;
    }
}
