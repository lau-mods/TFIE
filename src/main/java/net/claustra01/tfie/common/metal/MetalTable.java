package net.claustra01.tfie.common.metal;

import net.claustra01.tfcm.common.metal.Metal;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * The chapter 6 metal set: TFC's shaped metals, every TFCM metal, and FirmaLife's two metals.
 * Only the shapes listed here are pressed; no other metal or shape is derived at runtime.
 */
public final class MetalTable
{
    public enum Origin
    {
        TFC,
        TFCM,
        FIRMALIFE
    }

    public record MetalEntry(String id, Origin origin, Metal tfcmMetal)
    {
    }

    public enum Shape
    {
        PLATE("plate", 1, 1, "immersiveengineering:mold_plate"),
        SHEET("sheet", 2, 1, "tfie:mold_sheet"),
        ROD("rod", 1, 2, "immersiveengineering:mold_rod"),
        WIRE("wire", 1, 4, "immersiveengineering:mold_wire");

        public final String id;
        public final int ingots;
        public final int results;
        public final ResourceLocation mold;

        Shape(String id, int ingots, int results, String mold)
        {
            this.id = id;
            this.ingots = ingots;
            this.results = results;
            this.mold = ResourceLocation.parse(mold);
        }
    }

    public static final List<String> TFC_METAL_IDS = List.of(
            "bismuth", "bismuth_bronze", "black_bronze", "black_steel", "blue_steel", "brass", "bronze",
            "cast_iron", "copper", "gold", "nickel", "red_steel", "rose_gold", "silver", "steel",
            "sterling_silver", "tin", "wrought_iron", "zinc");

    public static final List<String> FIRMALIFE_METAL_IDS = List.of("chromium", "stainless_steel");

    private MetalTable()
    {
    }

    public static List<MetalEntry> entries()
    {
        List<MetalEntry> entries = new ArrayList<>();
        TFC_METAL_IDS.forEach(id -> entries.add(new MetalEntry(id, Origin.TFC, null)));
        for(Metal metal : Metal.values())
            entries.add(new MetalEntry(metal.id(), Origin.TFCM, metal));
        FIRMALIFE_METAL_IDS.forEach(id -> entries.add(new MetalEntry(id, Origin.FIRMALIFE, null)));
        return List.copyOf(entries);
    }

    /**
     * Existing TFC and FirmaLife parts stay in their own namespace; TFCM supplies the added shapes.
     */
    public static ResourceLocation itemId(MetalEntry metal, Shape shape)
    {
        String namespace = switch(metal.origin())
        {
            case TFC -> shape == Shape.SHEET || shape == Shape.ROD ? "tfc" : "tfcm";
            case TFCM -> "tfcm";
            case FIRMALIFE -> shape == Shape.SHEET || shape == Shape.ROD ? "firmalife" : "tfcm";
        };
        return ResourceLocation.fromNamespaceAndPath(namespace, "metal/" + shape.id + "/" + metal.id());
    }

    public static ResourceLocation recipeId(MetalEntry metal, Shape shape)
    {
        return ResourceLocation.fromNamespaceAndPath("tfie", "metalpress/" + shape.id + "/" + metal.id());
    }
}
