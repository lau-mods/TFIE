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
        event.createProvider(output -> new TFIEItemModelProvider(output, event.getExistingFileHelper()));
        event.createProvider(GameTestStructureProvider::new);
        event.createProvider(MaterialDataProvider::new);
        event.createProvider((output, lookup) -> new MaterialItemTagsProvider(output, lookup,
                event.getExistingFileHelper()));
    }
}
