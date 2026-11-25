package net.claustra01.tfie.common;

import net.claustra01.tfie.common.blocks.HempCropBlock;
import net.claustra01.tfie.util.IECrop;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.claustra01.tfie.TFIE;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks
{
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TFIE.MOD_ID);

    public static final DeferredBlock<HempCropBlock> HEMP_CROP = BLOCKS.register("crop/hemp", () ->
            new HempCropBlock(ExtendedProperties.of(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .strength(0.4F)
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .flammable(60, 30)
                    .blockEntity(ModBlockEntities.CROP)
                    .serverTicks(CropBlockEntity::serverTickBottomPartOnly)));
    public static final DeferredBlock<DeadDoubleCropBlock> DEAD_HEMP_CROP = BLOCKS.register("dead_crop/hemp", () ->
            new DeadDoubleCropBlock(ExtendedProperties.of(MapColor.PLANT)
                    .noCollission()
                    .strength(0.4F)
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .flammable(60, 30), IECrop::climateRange));
    public static final DeferredBlock<WildDoubleCropBlock> WILD_HEMP_CROP = BLOCKS.register("wild_crop/hemp", () ->
            new WildDoubleCropBlock(ExtendedProperties.of(MapColor.PLANT)
                    .noCollission()
                    .randomTicks()
                    .strength(0.4F)
                    .sound(SoundType.CROP)
                    .pushReaction(PushReaction.DESTROY)
                    .flammable(60, 30)
                    .offsetType(OffsetType.XZ)));

    private ModBlocks()
    {
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
