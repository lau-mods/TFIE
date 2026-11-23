package net.claustra01.tfie.datagen;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.dries007.tfc.world.blockpredicate.ReplaceablePredicate;
import net.dries007.tfc.world.feature.BlockConfig;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.placement.ClimatePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Vec3i;
import net.minecraft.data.PackOutput;
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
import net.minecraft.core.RegistrySetBuilder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;

public final class HempWorldgenProvider extends DatapackBuiltinEntriesProvider
{
    private static final ResourceKey<ConfiguredFeature<?, ?>> PLANT_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                    "plant/wild_crop/hemp"));
    private static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CONFIGURED = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                    "plant/wild_crop/hemp_patch"));
    private static final ResourceKey<PlacedFeature> PLANT_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE, PLANT_CONFIGURED.location());
    public static final ResourceKey<PlacedFeature> PATCH_PLACED = ResourceKey.create(
            Registries.PLACED_FEATURE, PATCH_CONFIGURED.location());

    public HempWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, lookup, new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, context -> {
                    context.register(PLANT_CONFIGURED, new ConfiguredFeature<>(TFCFeatures.TALL_WILD_CROP.get(),
                            new BlockConfig<>(ModBlocks.WILD_HEMP_CROP.get())));
                    context.register(PATCH_CONFIGURED, new ConfiguredFeature<>(Feature.RANDOM_PATCH,
                            new RandomPatchConfiguration(6, 5, 1,
                                    context.lookup(Registries.PLACED_FEATURE).getOrThrow(PLANT_PLACED))));
                })
                .add(Registries.PLACED_FEATURE, context -> {
                    var configured = context.lookup(Registries.CONFIGURED_FEATURE);
                    context.register(PLANT_PLACED, new PlacedFeature(configured.getOrThrow(PLANT_CONFIGURED),
                            List.of(HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                                    BlockPredicateFilter.forPredicate(ReplaceablePredicate.INSTANCE),
                                    BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(
                                            ModBlocks.WILD_HEMP_CROP.get().defaultBlockState(), Vec3i.ZERO)))));
                    ClimatePlacement climate = ClimatePlacement.CODEC.codec().parse(JsonOps.INSTANCE,
                            JsonParser.parseString("{\"min_temperature\":10,\"max_temperature\":40,"
                                    + "\"min_groundwater\":140,\"max_groundwater\":360}"))
                            .getOrThrow();
                    context.register(PATCH_PLACED, new PlacedFeature(configured.getOrThrow(PATCH_CONFIGURED),
                            List.of(RarityFilter.onAverageOnceEvery(90), InSquarePlacement.spread(), climate)));
                }), Set.of(TFIE.MOD_ID));
    }
}
