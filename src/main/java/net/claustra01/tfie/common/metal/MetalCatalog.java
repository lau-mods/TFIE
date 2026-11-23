package net.claustra01.tfie.common.metal;

import net.claustra01.tfcm.common.metal.Metal;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class MetalCatalog
{
    private static final Set<MetalShape> SHAPED = Set.of(
            MetalShape.INGOT, MetalShape.PLATE, MetalShape.SHEET,
            MetalShape.ROD, MetalShape.WIRE, MetalShape.DOUBLE_SHEET);
    private static final Set<MetalShape> INGOT_ONLY = Set.of(MetalShape.INGOT);
    private static final List<Metal> TFCM_METALS = List.of(
            Metal.COMPRESSED_IRON, Metal.PLATINUM, Metal.NAQUADAH, Metal.IRIDIUM, Metal.OSMIUM,
            Metal.OSMIRIDIUM, Metal.MITHRIL, Metal.ARCANE, Metal.REFINED_GLOWSTONE,
            Metal.REFINED_OBSIDIAN, Metal.ANTIMONY, Metal.TITANIUM, Metal.COBALT, Metal.LITHIUM,
            Metal.ALUMINUM, Metal.CONSTANTAN, Metal.INVAR, Metal.ELECTRUM, Metal.LEAD, Metal.URANIUM,
            Metal.TUNGSTEN, Metal.SOLDER, Metal.TUNGSTEN_STEEL, Metal.NETHERITE, Metal.DAWNSTONE,
            Metal.ANDESITE_ALLOY, Metal.DRACONIUM, Metal.AWAKENED_DRACONIUM, Metal.BORON,
            Metal.THORIUM, Metal.MAGNESIUM, Metal.BERYLLIUM, Metal.ZIRCONIUM, Metal.MANGANESE,
            Metal.MOLYBDENUM, Metal.NEODYMIUM, Metal.VANADIUM, Metal.HASTELLOY,
            Metal.HIGH_SPEED_STEEL, Metal.SIGNALUM, Metal.LUMIUM, Metal.ENDERIUM);
    private static final Map<String, MetalDefinition> METALS = createMetals();

    private MetalCatalog()
    {
    }

    public static List<MetalDefinition> all()
    {
        return List.copyOf(METALS.values());
    }

    public static List<MetalDefinition> shaped()
    {
        return METALS.values().stream().filter(metal -> metal.shapes().size() > 1).toList();
    }

    public static Optional<MetalDefinition> find(String id)
    {
        return Optional.ofNullable(METALS.get(id));
    }

    private static Map<String, MetalDefinition> createMetals()
    {
        Map<String, MetalDefinition> metals = new LinkedHashMap<>();
        add(metals, MetalSource.TFC, SHAPED, null,
                "bismuth", "bismuth_bronze", "black_bronze", "black_steel", "blue_steel", "brass",
                "bronze", "cast_iron", "copper", "gold", "nickel", "red_steel", "rose_gold", "silver",
                "steel", "sterling_silver", "tin", "wrought_iron", "zinc");
        add(metals, MetalSource.TFC, INGOT_ONLY, null,
                "pig_iron", "weak_steel", "weak_blue_steel", "weak_red_steel", "high_carbon_steel",
                "high_carbon_black_steel", "high_carbon_blue_steel", "high_carbon_red_steel", "unknown");
        for(Metal metal : TFCM_METALS)
            metals.put(metal.id(), new MetalDefinition(metal.id(), MetalSource.TFCM, SHAPED, metal));
        metals.put("high_carbon_tungsten_steel", new MetalDefinition("high_carbon_tungsten_steel",
                MetalSource.TFCM, INGOT_ONLY, Metal.TUNGSTEN_STEEL));
        add(metals, MetalSource.FIRMALIFE, SHAPED, null, "chromium", "stainless_steel");
        return Collections.unmodifiableMap(metals);
    }

    private static void add(Map<String, MetalDefinition> metals, MetalSource source, Set<MetalShape> shapes,
            Metal tfcmMetal, String... ids)
    {
        Arrays.stream(ids).forEach(id -> metals.put(id, new MetalDefinition(id, source, shapes, tfcmMetal)));
    }
}
