package net.claustra01.tfie.common;

import blusunrize.immersiveengineering.common.items.DrillheadItem;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems
{
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TFIE.MOD_ID);

    public static final DeferredItem<Item> TREATED_WOOD_LUMBER =
            ITEMS.registerSimpleItem("treated_wood_lumber");
    public static final DeferredItem<Item> IE_HAMMER_HEAD =
            ITEMS.registerSimpleItem("tool_head/ie_hammer");
    public static final DeferredItem<Item> WIRECUTTER_HEAD =
            ITEMS.registerSimpleItem("tool_head/wirecutter");
    public static final DeferredItem<Item> MOLD_SHEET =
            ITEMS.registerSimpleItem("mold_sheet", new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> ARC_FURNACE_BLUEPRINT =
            ITEMS.register("arc_furnace_blueprint", () -> new Item(
                    new Item.Properties().stacksTo(1).component(ModDataComponents.ARC_FURNACE_MODE, 1)));

    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_BLACK_STEEL =
            drillhead("black_steel", 3);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_BLUE_STEEL =
            drillhead("blue_steel", 5);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_RED_STEEL =
            drillhead("red_steel", 5);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_INVAR =
            drillhead("invar", 2);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_TITANIUM =
            drillhead("titanium", 3);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_NETHERITE =
            drillhead("netherite", 5);
    public static final DeferredItem<TFIEDrillheadItem> DRILLHEAD_TUNGSTEN_STEEL =
            drillhead("tungsten_steel", 5);

    public static final DeferredItem<BlockItem> WILD_HEMP_CROP =
            ITEMS.registerSimpleBlockItem(ModBlocks.WILD_HEMP_CROP);

    private ModItems()
    {
    }

    private static DeferredItem<TFIEDrillheadItem> drillhead(String metal, int size)
    {
        TagKey<Item> repairMaterial = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "ingots/" + metal));
        DrillheadItem.DrillHeadPerm properties = new DrillheadItem.DrillHeadPerm(
                metal, repairMaterial, size, 1, Tiers.DIAMOND, 10, 7, 10000,
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_" + metal));
        return ITEMS.register("drillhead_" + metal, () -> new TFIEDrillheadItem(properties));
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
