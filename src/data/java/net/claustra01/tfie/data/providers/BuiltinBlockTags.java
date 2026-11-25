package net.claustra01.tfie.data.providers;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class BuiltinBlockTags extends BlockTagsProvider
{
    public BuiltinBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                            ExistingFileHelper existingFileHelper)
    {
        super(output, registries, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries)
    {
        tag(BlockTags.CROPS).add(ModBlocks.HEMP_CROP.get(), ModBlocks.DEAD_HEMP_CROP.get(),
                ModBlocks.WILD_HEMP_CROP.get());
        tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.HEMP_CROP.get(), ModBlocks.DEAD_HEMP_CROP.get(),
                ModBlocks.WILD_HEMP_CROP.get());
        tag(TFCTags.Blocks.CAN_BE_SNOW_PILED).add(ModBlocks.WILD_HEMP_CROP.get());
    }
}
