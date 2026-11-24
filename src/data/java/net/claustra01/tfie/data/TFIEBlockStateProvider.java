package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class TFIEBlockStateProvider extends BlockStateProvider
{
    public TFIEBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        ModelFile hemp = models().cross("crop/hemp",
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "block/crop/hemp_dead_young"))
                .renderType("cutout");
        ModelFile deadHemp = models().cross("dead_crop/hemp",
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "block/crop/hemp_dead_bottom"))
                .renderType("cutout");
        ModelFile wildHemp = models().cross("wild_crop/hemp",
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "block/crop/hemp_dead_top"))
                .renderType("cutout");
        simpleBlock(ModBlocks.HEMP_CROP.get(), hemp);
        simpleBlock(ModBlocks.DEAD_HEMP_CROP.get(), deadHemp);
        simpleBlock(ModBlocks.WILD_HEMP_CROP.get(), wildHemp);
        simpleBlockItem(ModBlocks.WILD_HEMP_CROP.get(), wildHemp);
    }
}
