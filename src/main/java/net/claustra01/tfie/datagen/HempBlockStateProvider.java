package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.crop.HempCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class HempBlockStateProvider extends BlockStateProvider
{
    public HempBlockStateProvider(PackOutput output, ExistingFileHelper existingFiles)
    {
        super(output, TFIE.MOD_ID, existingFiles);
    }

    @Override
    protected void registerStatesAndModels()
    {
        for(int age = 0; age <= 4; age++)
        {
            int stage = age;
            getVariantBuilder(ModBlocks.HEMP_CROP.get()).partialState().with(HempCropBlock.AGE, age)
                    .with(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM).modelForState()
                    .modelFile(new ModelFile.UncheckedModelFile(net.minecraft.resources.ResourceLocation.parse(
                            "immersiveengineering:block/hemp/bottom" + stage))).addModel();
            getVariantBuilder(ModBlocks.HEMP_CROP.get()).partialState().with(HempCropBlock.AGE, age)
                    .with(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP).modelForState()
                    .modelFile(new ModelFile.UncheckedModelFile(net.minecraft.resources.ResourceLocation.parse(
                            "immersiveengineering:block/hemp/" + (age == 4 ? "top" : "bottom" + age))))
                    .addModel();
        }

        models().withExistingParent("block/dead_crop/hemp_young", mcLoc("block/crop"))
                .texture("crop", modLoc("block/crop/hemp_dead_young"));
        models().withExistingParent("block/dead_crop/hemp_bottom", mcLoc("block/crop"))
                .texture("crop", modLoc("block/crop/hemp_dead_bottom"));
        models().withExistingParent("block/dead_crop/hemp_top", mcLoc("block/crop"))
                .texture("crop", modLoc("block/crop/hemp_dead_top"));
        getVariantBuilder(ModBlocks.DEAD_HEMP_CROP.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(new ModelFile.UncheckedModelFile(modLoc("block/dead_crop/hemp_"
                        + (!state.getValue(DeadCropBlock.MATURE) ? "young"
                        : state.getValue(DoubleCropBlock.PART) == DoubleCropBlock.Part.TOP ? "top" : "bottom"))))
                .build());

        models().getBuilder("block/wild_crop/hemp_bottom").parent(new ModelFile.UncheckedModelFile(
                net.minecraft.resources.ResourceLocation.parse("tfc:block/wild_crop/crop")))
                .texture("crop", modLoc("block/crop/hemp_wild_bottom"));
        models().withExistingParent("block/wild_crop/hemp_top", mcLoc("block/crop"))
                .texture("crop", modLoc("block/crop/hemp_wild_top"));
        getVariantBuilder(ModBlocks.WILD_HEMP_CROP.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(new ModelFile.UncheckedModelFile(modLoc("block/"
                        + (state.getValue(WildCropBlock.MATURE) ? "wild_crop/hemp_"
                        : "dead_crop/hemp_")
                        + (state.getValue(WildDoubleCropBlock.PART) == DoubleCropBlock.Part.TOP
                        ? "top" : "bottom"))))
                .build());
    }
}
