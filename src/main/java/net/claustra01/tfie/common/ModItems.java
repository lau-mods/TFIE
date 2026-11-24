package net.claustra01.tfie.common;

import blusunrize.immersiveengineering.common.items.DrillheadItem;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.dries007.tfc.common.TFCTiers;
import net.claustra01.tfcm.common.tool.ToolMetal;
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

    public static final DeferredItem<DrillheadItem> DRILLHEAD_BLACK_STEEL =
            drillhead("black_steel", 3, TFCTiers.BLACK_STEEL, 12, 8, 16000,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_black_steel"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_BLUE_STEEL =
            drillhead("blue_steel", 5, TFCTiers.BLUE_STEEL, 15, 10, 20000,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_blue_steel"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_RED_STEEL =
            drillhead("red_steel", 5, TFCTiers.RED_STEEL, 15, 10, 20000,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_red_steel"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_INVAR =
            drillhead("invar", 2, ToolMetal.INVAR.toolTier(), 10, 7, 11000,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_invar"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_TITANIUM =
            drillhead("titanium", 3, ToolMetal.TITANIUM.toolTier(), 13, 9, 16500,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_titanium"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_NETHERITE =
            drillhead("netherite", 5, ToolMetal.NETHERITE.toolTier(), 16, 11, 32500,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_netherite"));
    public static final DeferredItem<DrillheadItem> DRILLHEAD_TUNGSTEN_STEEL =
            drillhead("tungsten_steel", 5, ToolMetal.TUNGSTEN_STEEL.toolTier(), 18, 12, 40625,
                    ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "item/drill_tungsten_steel"));

    public static final DeferredItem<BlockItem> WILD_HEMP_CROP =
            ITEMS.registerSimpleBlockItem(ModBlocks.WILD_HEMP_CROP);

    private ModItems()
    {
    }

    private static DeferredItem<DrillheadItem> drillhead(String metal, int size, Tier tier,
                                                         int speed, int attack, int durability,
                                                         ResourceLocation texture)
    {
        TagKey<Item> repairMaterial = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "ingots/" + metal));
        DrillheadItem.DrillHeadPerm properties = new DrillheadItem.DrillHeadPerm(
                metal, repairMaterial, size, 1, tier, speed, attack, durability, texture);
        return ITEMS.register("drillhead_" + metal, () -> new DrillheadItem(properties));
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
