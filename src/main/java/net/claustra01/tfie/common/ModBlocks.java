package net.claustra01.tfie.common;

import net.claustra01.tfie.TFIE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks
{
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TFIE.MOD_ID);
    private static final BlockBehaviour.Properties CROP_PROPERTIES = BlockBehaviour.Properties.of()
            .noCollission()
            .strength(0.4F)
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY);

    public static final DeferredBlock<TFIECropBlock> HEMP_CROP =
            BLOCKS.register("crop/hemp", () -> new TFIECropBlock(CROP_PROPERTIES));
    public static final DeferredBlock<Block> DEAD_HEMP_CROP =
            BLOCKS.register("dead_crop/hemp", () -> new Block(CROP_PROPERTIES));
    public static final DeferredBlock<Block> WILD_HEMP_CROP =
            BLOCKS.register("wild_crop/hemp", () -> new Block(CROP_PROPERTIES));

    private ModBlocks()
    {
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
