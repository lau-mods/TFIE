package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
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
        ModelFile[] bottoms = new ModelFile[5];
        for(int age = 0; age <= 4; age++)
            bottoms[age] = crop("crop/hemp_age_" + age, ie("block/hemp/bottom" + age));

        ModelFile cropTop = crop("crop/hemp_age_4_top", ie("block/hemp/top0"));
        ModelFile empty = models().getExistingFile(mcLoc("block/air"));
        ModelFile deadYoung = crop("dead_crop/hemp_young", modLoc("block/crop/hemp_dead_young"));
        ModelFile deadBottom = crop("dead_crop/hemp_bottom", modLoc("block/crop/hemp_dead_bottom"));
        ModelFile deadTop = crop("dead_crop/hemp_top", modLoc("block/crop/hemp_dead_top"));
        ModelFile wildBottom = crop("wild_crop/hemp_bottom", ie("block/hemp/bottom4"));
        ModelFile wildTop = crop("wild_crop/hemp_top", ie("block/hemp/top0"));

        IntegerProperty ageProperty = ModBlocks.HEMP_CROP.get().getAgeProperty();
        VariantBlockStateBuilder crop = getVariantBuilder(ModBlocks.HEMP_CROP.get());
        for(int age = 0; age <= 4; age++)
        {
            crop.partialState().with(ageProperty, age)
                    .with(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                    .modelForState().modelFile(bottoms[age]).addModel();
            crop.partialState().with(ageProperty, age)
                    .with(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP)
                    .modelForState().modelFile(age == 4 ? cropTop : empty).addModel();
        }

        VariantBlockStateBuilder dead = getVariantBuilder(ModBlocks.DEAD_HEMP_CROP.get());
        dead.partialState().with(DeadCropBlock.MATURE, false)
                .modelForState().modelFile(deadYoung).addModel();
        dead.partialState().with(DeadCropBlock.MATURE, true)
                .with(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                .modelForState().modelFile(deadBottom).addModel();
        dead.partialState().with(DeadCropBlock.MATURE, true)
                .with(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.TOP)
                .modelForState().modelFile(deadTop).addModel();

        VariantBlockStateBuilder wild = getVariantBuilder(ModBlocks.WILD_HEMP_CROP.get());
        wild.partialState().with(WildCropBlock.MATURE, true)
                .with(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                .modelForState().modelFile(wildBottom).addModel();
        wild.partialState().with(WildCropBlock.MATURE, true)
                .with(WildDoubleCropBlock.PART, DoubleCropBlock.Part.TOP)
                .modelForState().modelFile(wildTop).addModel();
        wild.partialState().with(WildCropBlock.MATURE, false)
                .with(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                .modelForState().modelFile(deadBottom).addModel();
        wild.partialState().with(WildCropBlock.MATURE, false)
                .with(WildDoubleCropBlock.PART, DoubleCropBlock.Part.TOP)
                .modelForState().modelFile(deadTop).addModel();
    }

    private ModelFile crop(String name, ResourceLocation texture)
    {
        return models().withExistingParent(name, mcLoc("block/crop"))
                .texture("crop", texture)
                .renderType("cutout");
    }

    private static ResourceLocation ie(String path)
    {
        return ResourceLocation.fromNamespaceAndPath("immersiveengineering", path);
    }
}
