package net.claustra01.tfie.common;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.crop.HempCropBlock;
import net.claustra01.tfie.common.crop.HempCropProperties;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks
{
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TFIE.MOD_ID);
    public static final DeferredBlock<HempCropBlock> HEMP_CROP =
            BLOCKS.register("crop/hemp", HempCropBlock::new);
    public static final DeferredBlock<DeadDoubleCropBlock> DEAD_HEMP_CROP =
            BLOCKS.register("dead_crop/hemp", () -> new DeadDoubleCropBlock(
                    HempCropProperties.create(), HempCropProperties.climateRange()));
    public static final DeferredBlock<WildDoubleCropBlock> WILD_HEMP_CROP =
            BLOCKS.register("wild_crop/hemp", () -> new WildDoubleCropBlock(
                    HempCropProperties.create()));

    private ModBlocks()
    {
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
