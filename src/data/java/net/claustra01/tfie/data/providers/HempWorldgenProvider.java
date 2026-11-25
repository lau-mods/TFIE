package net.claustra01.tfie.data.providers;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.dries007.tfc.world.blockpredicate.ReplaceablePredicate;
import net.dries007.tfc.world.feature.BlockConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.placement.ClimatePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class HempWorldgenProvider extends DatapackBuiltinEntriesProvider
{
    public static final ResourceKey<PlacedFeature> PATCH_PLACED = placedKey("plant/wild_crop/hemp_patch");

    private static final ResourceKey<ConfiguredFeature<?, ?>> INDIVIDUAL_CONFIGURED =
            configuredKey("plant/wild_crop/hemp");
    private static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CONFIGURED =
            configuredKey("plant/wild_crop/hemp_patch");
    private static final ResourceKey<PlacedFeature> INDIVIDUAL_PLACED =
            placedKey("plant/wild_crop/hemp");
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, HempWorldgenProvider::configuredFeatures)
            .add(Registries.PLACED_FEATURE, HempWorldgenProvider::placedFeatures);

    public HempWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, BUILDER, Set.of(TFIE.MOD_ID));
    }

    private static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context)
    {
        context.register(INDIVIDUAL_CONFIGURED, new ConfiguredFeature<>(TFCFeatures.TALL_WILD_CROP.get(),
                new BlockConfig<>(ModBlocks.WILD_HEMP_CROP.get())));
        context.register(PATCH_CONFIGURED, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(6, 5, 1,
                        context.lookup(Registries.PLACED_FEATURE).getOrThrow(INDIVIDUAL_PLACED))));
    }

    private static void placedFeatures(BootstrapContext<PlacedFeature> context)
    {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(INDIVIDUAL_PLACED, new PlacedFeature(configured.getOrThrow(INDIVIDUAL_CONFIGURED),
                List.of(
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BlockPredicateFilter.forPredicate(ReplaceablePredicate.INSTANCE),
                        BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(
                                ModBlocks.WILD_HEMP_CROP.get().defaultBlockState(), BlockPos.ZERO)))));
        context.register(PATCH_PLACED, new PlacedFeature(configured.getOrThrow(PATCH_CONFIGURED),
                List.of(
                        RarityFilter.onAverageOnceEvery(90),
                        InSquarePlacement.spread(),
                        new ClimatePlacement(10.0F, 40.0F, 140.0F, 360.0F,
                                -1.0F, 1.0F, false, 0, 4, List.of(),
                                -64, 320, false, false))));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String path)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path));
    }

    private static ResourceKey<PlacedFeature> placedKey(String path)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path));
    }
}
