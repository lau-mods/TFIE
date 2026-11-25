package net.claustra01.tfie.data.providers;

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

public final class HempPlacedFeatureTags extends TagsProvider<PlacedFeature>
{
    private static final TagKey<PlacedFeature> CROPS = TagKey.create(Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath("tfc", "feature/crops"));

    public HempPlacedFeatureTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                                 ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.PLACED_FEATURE, registries, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries)
    {
        tag(CROPS).add(HempWorldgenProvider.PATCH_PLACED);
    }
}
