package net.claustra01.tfie.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class TFIECropBlockEntity extends BlockEntity
{
    public TFIECropBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.CROP.get(), pos, state);
    }
}
