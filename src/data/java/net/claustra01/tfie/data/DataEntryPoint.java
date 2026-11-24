package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.data.recipes.BarrelRecipes;
import net.claustra01.tfie.data.recipes.CraftingRecipes;
import net.claustra01.tfie.data.recipes.RecipeOverrides;
import net.claustra01.tfie.data.recipes.SawmillRecipes;
import net.claustra01.tfie.data.tags.TreatedLumberFluidTags;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

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
        event.createProvider(output -> named("TFIE sawmill recipes",
                new SawmillRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE barrel recipes",
                new BarrelRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE crafting recipes",
                new CraftingRecipes(output, event.getLookupProvider())));
        event.createProvider(output -> named("TFIE recipe overrides",
                new RecipeOverrides(output, event.getLookupProvider())));
        event.createProvider(output -> new TreatedLumberFluidTags(output, event.getLookupProvider(),
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
