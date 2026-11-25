package net.claustra01.tfie.gametest.metal;

import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Chapter 6 expectations, kept independent of the production metal table.
 */
public final class ExpectedMetals
{
    public enum Origin
    {
        TFC,
        TFCM,
        FIRMALIFE
    }

    public record Entry(String id, Origin origin)
    {
    }

    public static final List<String> TFC = List.of(
            "bismuth", "bismuth_bronze", "black_bronze", "black_steel", "blue_steel", "brass", "bronze",
            "cast_iron", "copper", "gold", "nickel", "red_steel", "rose_gold", "silver", "steel",
            "sterling_silver", "tin", "wrought_iron", "zinc");

    public static final List<String> TFCM = List.of(
            "compressed_iron", "platinum", "naquadah", "iridium", "osmium", "osmiridium", "mithril",
            "arcane", "refined_glowstone", "refined_obsidian", "antimony", "titanium", "cobalt",
            "lithium", "aluminum", "constantan", "invar", "electrum", "lead", "uranium", "tungsten",
            "solder", "tungsten_steel", "netherite", "dawnstone", "andesite_alloy", "draconium",
            "awakened_draconium", "boron", "thorium", "magnesium", "beryllium", "zirconium",
            "manganese", "molybdenum", "neodymium", "vanadium", "hastelloy", "high_speed_steel",
            "signalum", "lumium", "enderium");

    public static final List<String> FIRMALIFE = List.of("chromium", "stainless_steel");

    public static final List<String> INGOT_ONLY = List.of(
            "pig_iron", "weak_steel", "weak_blue_steel", "weak_red_steel", "high_carbon_steel",
            "high_carbon_black_steel", "high_carbon_blue_steel", "high_carbon_red_steel", "unknown");

    public static final List<String> SHAPES = List.of("plate", "sheet", "rod", "wire");

    private ExpectedMetals()
    {
    }

    /**
     * Every metal whose four shapes are present in this run.
     */
    public static List<Entry> available()
    {
        List<Entry> entries = new ArrayList<>();
        TFC.forEach(id -> entries.add(new Entry(id, Origin.TFC)));
        for(String id : TFCM)
        {
            if(!metalEnabled(id))
                continue;
            Entry entry = new Entry(id, Origin.TFCM);
            if(outputExists(entry))
                entries.add(entry);
        }
        if(ModList.get().isLoaded("firmalife"))
            for(String id : FIRMALIFE)
            {
                Entry entry = new Entry(id, Origin.FIRMALIFE);
                if(outputExists(entry))
                    entries.add(entry);
            }
        return entries;
    }

    public static boolean metalEnabled(String id)
    {
        return new MetalEnabledCondition(Metal.valueOf(id.toUpperCase(Locale.ROOT)))
                .test(ICondition.IContext.EMPTY);
    }

    private static boolean outputExists(Entry entry)
    {
        for(String shape : SHAPES)
            if(!BuiltInRegistries.ITEM.containsKey(id(output(entry, shape))))
                return false;
        return true;
    }

    public static String output(Entry entry, String shape)
    {
        String namespace = switch(entry.origin())
        {
            case TFC -> shape.equals("sheet") || shape.equals("rod") ? "tfc" : "tfcm";
            case TFCM -> "tfcm";
            case FIRMALIFE -> shape.equals("sheet") || shape.equals("rod") ? "firmalife" : "tfcm";
        };
        return namespace + ":metal/" + shape + "/" + entry.id();
    }

    public static String ingot(Entry entry)
    {
        String namespace = switch(entry.origin())
        {
            case TFC -> "tfc";
            case TFCM -> "tfcm";
            case FIRMALIFE -> "firmalife";
        };
        return namespace + ":metal/ingot/" + entry.id();
    }

    public static String recipeId(Entry entry, String shape)
    {
        return "tfie:metalpress/" + shape + "/" + entry.id();
    }

    public static int ingots(String shape)
    {
        return shape.equals("sheet") ? 2 : 1;
    }

    public static int results(String shape)
    {
        return switch(shape)
        {
            case "rod" -> 2;
            case "wire" -> 4;
            default -> 1;
        };
    }

    public static String mold(String shape)
    {
        return shape.equals("sheet") ? "tfie:mold_sheet" : "immersiveengineering:mold_" + shape;
    }

    public static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
