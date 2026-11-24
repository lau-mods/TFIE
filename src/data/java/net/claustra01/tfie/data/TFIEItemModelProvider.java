package net.claustra01.tfie.data;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
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
        withExistingParent("item/mold_sheet", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/mold_sheet"));
        withExistingParent("item/arc_furnace_blueprint",
                ResourceLocation.fromNamespaceAndPath("immersiveengineering", "item/blueprint"));
        drillhead("black_steel", "black_steel");
        drillhead("blue_steel", "blue_steel");
        drillhead("red_steel", "red_steel");
        drillhead("invar", "invar");
        drillhead("titanium", "titanium");
        drillhead("netherite", "netherite");
        drillhead("tungsten_steel", "tungsten_steel");
        withExistingParent("item/wild_crop/hemp", mcLoc("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(
                        "immersiveengineering", "item/material_hemp_fiber"));
    }

    private void drillhead(String name, String texture)
    {
        withExistingParent("item/drillhead_" + name, mcLoc("item/generated"))
                .texture("layer0", texture.contains(":")
                        ? ResourceLocation.parse(texture)
                        : modLoc("item/drillhead_" + texture));
    }
}
