package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.data.providers.BuiltinBlockTags;
import net.claustra01.tfie.data.providers.BuiltinClimateRanges;
import net.claustra01.tfie.data.providers.BuiltinFertilizers;
import net.claustra01.tfie.data.providers.BuiltinFluidTags;
import net.claustra01.tfie.data.providers.BuiltinFuels;
import net.claustra01.tfie.data.providers.BuiltinItemTags;
import net.claustra01.tfie.data.providers.BuiltinItemHeat;
import net.claustra01.tfie.data.providers.BuiltinItemSizes;
import net.claustra01.tfie.data.providers.BuiltinRecipes;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = TFIE.MOD_ID)
public class DataEntryPoint
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        final PackOutput output = event.getGenerator().getPackOutput();
        final var lookup = event.getLookupProvider();

        add(event, new BuiltinItemHeat(output, lookup));
        add(event, new BuiltinItemSizes(output, lookup));
        add(event, new BuiltinRecipes(output, lookup));

        final var blockTags = add(event, new BuiltinBlockTags(event, lookup)).contentsGetter();

        add(event, new BuiltinItemTags(event, lookup, blockTags));
        add(event, new BuiltinFluidTags(event, lookup));

        add(event, new BuiltinFertilizers(output, lookup));
        add(event, new BuiltinFuels(output, lookup));
        add(event, new BuiltinClimateRanges(output, lookup));
    }

    private static <T extends DataProvider> T add(GatherDataEvent event, T provider)
    {
        return event.getGenerator().addProvider(true, provider);
    }
}
