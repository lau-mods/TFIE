package net.claustra01.tfie.common.items;

import java.util.Locale;
import java.util.function.Supplier;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.items.TFCItems.ItemId;

@SuppressWarnings("unused")
public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TFIE.MOD_ID);

    public static final ItemId WIRECUTTER_HEAD = register("tool_head/wirecutter");

    public static final ItemId HAMMER_HEAD = register("tool_head/ie_hammer");

    public static final ItemId MOLD_SHEET = register("mold_sheet",
        () -> new Item(new Item.Properties().stacksTo(1)));
    public static final ItemId MOLD_BLOCK = register("mold_block",
        () -> new Item(new Item.Properties().stacksTo(1)));

    public static final ItemId DRILLHEAD_BLACK_STEEL = register("drillhead_black_steel", () -> new Drillhead(Drillhead.BLACK_STEEL));
    public static final ItemId DRILLHEAD_BLUE_STEEL = register("drillhead_blue_steel", () -> new Drillhead(Drillhead.BLUE_STEEL));
    public static final ItemId DRILLHEAD_RED_STEEL = register("drillhead_red_steel", () -> new Drillhead(Drillhead.RED_STEEL));

    public static final ItemId TREATED_WOOD_LUMBER = register("treated_wood_lumber");

    private static ItemId register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static ItemId register(String name, Supplier<Item> item)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }
}
