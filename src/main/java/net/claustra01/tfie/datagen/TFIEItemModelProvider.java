package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class TFIEItemModelProvider extends ItemModelProvider
{
    public TFIEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        basicItem(ModItems.TREATED_WOOD_LUMBER.get());
        withExistingParent("item/tool_head/ie_hammer", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/tool_head/ie_hammer"));
        withExistingParent("item/tool_head/wirecutter", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/tool_head/wirecutter"));
        getBuilder("item/wild_crop/hemp").parent(new ModelFile.UncheckedModelFile(
                modLoc("block/wild_crop/hemp_bottom")));
    }
}
