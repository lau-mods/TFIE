package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class HempFeatureTagsProvider extends TagsProvider<PlacedFeature>
{
    public HempFeatureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
            ExistingFileHelper existingFiles)
    {
        super(output, Registries.PLACED_FEATURE, lookup, TFIE.MOD_ID, existingFiles);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        tag(TagKey.create(Registries.PLACED_FEATURE, ResourceLocation.parse("tfc:feature/crops")))
                .addOptional(HempWorldgenProvider.PATCH_PLACED.location());
    }
}
