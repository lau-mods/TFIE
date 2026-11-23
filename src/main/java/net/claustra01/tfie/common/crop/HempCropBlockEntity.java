package net.claustra01.tfie.common.crop;

import net.claustra01.tfie.common.ModBlockEntities;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class HempCropBlockEntity extends CropBlockEntity
{
    public HempCropBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CROP.get(), pos, state);
    }
}
