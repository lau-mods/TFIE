package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.data.recipes.BarrelRecipes;
import net.claustra01.tfie.data.recipes.BlueprintRecipes;
import net.claustra01.tfie.data.recipes.CraftingRecipes;
import net.claustra01.tfie.data.recipes.MetalPressRecipes;
import net.claustra01.tfie.data.recipes.RecipeOverrides;
import net.claustra01.tfie.data.recipes.SawmillRecipes;
import net.claustra01.tfie.data.providers.BuiltinBlockTags;
import net.claustra01.tfie.data.providers.BuiltinClimateRanges;
import net.claustra01.tfie.data.providers.BuiltinFuels;
import net.claustra01.tfie.data.providers.BuiltinFluidTags;
import net.claustra01.tfie.data.providers.BuiltinItemHeat;
import net.claustra01.tfie.data.providers.BuiltinItemSizes;
import net.claustra01.tfie.data.providers.BuiltinItemTags;
import net.claustra01.tfie.data.providers.HempLootTables;
import net.claustra01.tfie.data.providers.HempPlacedFeatureTags;
import net.claustra01.tfie.data.providers.HempWorldgenProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.List;

@EventBusSubscriber(modid = TFIE.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataEntryPoint
{
    private DataEntryPoint()
    {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        event.createProvider(LangProvider::new);
        event.createProvider(output -> new TFIEItemModelProvider(output, event.getExistingFileHelper()));
        event.createProvider(output -> new TFIEBlockStateProvider(output, event.getExistingFileHelper()));
        event.createProvider(GameTestStructureProvider::new);
        event.createProvider(output -> new LootTableProvider(output, HempLootTables.TABLES,
                List.of(new LootTableProvider.SubProviderEntry(ignored -> new HempLootTables(),
                        LootContextParamSets.BLOCK)), event.getLookupProvider()));
        event.createProvider(BuiltinClimateRanges::new);
        HempWorldgenProvider worldgen = event.createProvider(HempWorldgenProvider::new);
        event.createProvider(output -> new HempPlacedFeatureTags(output, worldgen.getRegistryProvider(),
                event.getExistingFileHelper()));
        event.createProvider(output -> new BuiltinBlockTags(output, event.getLookupProvider(),
                event.getExistingFileHelper()));
        event.createProvider(output -> named("TFIE sawmill recipes",
                new SawmillRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE barrel recipes",
                new BarrelRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE crafting recipes",
                new CraftingRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE recipe overrides",
                new RecipeOverrides(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE metal press recipes",
                new MetalPressRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE blueprint recipes",
                new BlueprintRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> new BuiltinItemTags(output, event.getLookupProvider(),
                event.getExistingFileHelper()));
        event.createProvider(BuiltinFuels::new);
        event.createProvider(BuiltinItemHeat::new);
        event.createProvider(BuiltinItemSizes::new);
        event.createProvider(output -> new BuiltinFluidTags(output, event.getLookupProvider(),
                event.getExistingFileHelper()));
    }

    private static DataProvider named(String name, DataProvider provider)
    {
        return new DataProvider()
        {
            @Override
            public CompletableFuture<?> run(CachedOutput output)
            {
                return provider.run(output);
            }

            @Override
            public String getName()
            {
                return name;
            }
        };
    }
}
