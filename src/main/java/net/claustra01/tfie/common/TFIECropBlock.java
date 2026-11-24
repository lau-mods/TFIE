package net.claustra01.tfie.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.dries007.tfc.common.blockentities.CropBlockEntity;

public final class TFIECropBlock extends Block implements EntityBlock
{
    public TFIECropBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CropBlockEntity(ModBlockEntities.CROP.get(), pos, state);
    }
}
