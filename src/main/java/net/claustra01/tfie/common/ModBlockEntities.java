package net.claustra01.tfie.common;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities
{
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TFIE.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CropBlockEntity>> CROP =
            BLOCK_ENTITIES.register("crop", () -> BlockEntityType.Builder.of(
                    ModBlockEntities::createCrop, ModBlocks.HEMP_CROP.get()).build(null));

    private ModBlockEntities()
    {
    }

    private static CropBlockEntity createCrop(BlockPos pos, BlockState state)
    {
        return new CropBlockEntity(CROP.get(), pos, state);
    }

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
