package net.claustra01.tfie.datagen;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class TFIEDataGen
{
    private TFIEDataGen()
    {
    }

    public static void gatherData(GatherDataEvent event)
    {
        event.createProvider(LangProvider::new);
        event.createProvider(GameTestStructureProvider::new);
        event.createProvider(MaterialDataProvider::new);
        event.createProvider(HempDataProvider::new);
        event.createProvider(output -> new HempBlockStateProvider(output, event.getExistingFileHelper()));
        event.createProvider(output -> new TFIEItemModelProvider(output, event.getExistingFileHelper()));
        event.createProvider((output, lookup) -> new HempBlockTagsProvider(output, lookup,
                event.getExistingFileHelper()));
        event.createProvider((output, lookup) -> new HempFeatureTagsProvider(output, lookup,
                event.getExistingFileHelper()));
        event.createProvider(HempLootProvider::new);
        event.createProvider(HempWorldgenProvider::new);
        event.createProvider((output, lookup) -> new MaterialItemTagsProvider(output, lookup,
                event.getExistingFileHelper()));
    }
}
