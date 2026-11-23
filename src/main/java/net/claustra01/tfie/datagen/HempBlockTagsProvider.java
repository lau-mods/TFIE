package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class HempBlockTagsProvider extends BlockTagsProvider
{
    public HempBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
            ExistingFileHelper existingFiles)
    {
        super(output, lookup, TFIE.MOD_ID, existingFiles);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        tag(BlockTags.CROPS).add(ModBlocks.HEMP_CROP.get(), ModBlocks.DEAD_HEMP_CROP.get(),
                ModBlocks.WILD_HEMP_CROP.get());
        tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.HEMP_CROP.get(),
                ModBlocks.DEAD_HEMP_CROP.get(), ModBlocks.WILD_HEMP_CROP.get());
        tag(TagKey.create(Registries.BLOCK, ResourceLocation.parse("tfc:can_be_snow_piled")))
                .add(ModBlocks.WILD_HEMP_CROP.get());
    }
}
