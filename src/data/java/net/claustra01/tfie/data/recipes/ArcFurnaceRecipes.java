package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import net.claustra01.tfcm.common.metal.Metal;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ArcFurnaceRecipes extends RecipeProvider
{
    private record Briquet(String ore, String output, String metal, boolean firmalife)
    {
    }

    private static final List<Briquet> BRIQUETS = List.of(
            new Briquet("native_copper", "tfc:metal/ingot/copper", null, false),
            new Briquet("native_gold", "tfc:metal/ingot/gold", null, false),
            new Briquet("hematite", "tfc:metal/ingot/cast_iron", null, false),
            new Briquet("native_silver", "tfc:metal/ingot/silver", null, false),
            new Briquet("cassiterite", "tfc:metal/ingot/tin", null, false),
            new Briquet("bismuthinite", "tfc:metal/ingot/bismuth", null, false),
            new Briquet("garnierite", "tfc:metal/ingot/nickel", null, false),
            new Briquet("malachite", "tfc:metal/ingot/copper", null, false),
            new Briquet("magnetite", "tfc:metal/ingot/cast_iron", null, false),
            new Briquet("limonite", "tfc:metal/ingot/cast_iron", null, false),
            new Briquet("sphalerite", "tfc:metal/ingot/zinc", null, false),
            new Briquet("tetrahedrite", "tfc:metal/ingot/copper", null, false),
            new Briquet("galena", "tfcm:metal/ingot/lead", "lead", false),
            new Briquet("magnesite", "tfcm:metal/ingot/magnesium", "magnesium", false),
            new Briquet("bauxite", "tfcm:metal/ingot/aluminum", "aluminum", false),
            new Briquet("uraninite", "tfcm:metal/ingot/uranium", "uranium", false),
            new Briquet("chromite", "firmalife:metal/ingot/chromium", null, true),
            new Briquet("rutile", "tfcm:metal/ingot/titanium", "titanium", false),
            new Briquet("zircon", "tfcm:metal/ingot/zirconium", "zirconium", false),
            new Briquet("cobaltite", "tfcm:metal/ingot/cobalt", "cobalt", false),
            new Briquet("stibnite", "tfcm:metal/ingot/antimony", "antimony", false),
            new Briquet("native_platinum", "tfcm:metal/ingot/platinum", "platinum", false),
            new Briquet("wolframite", "tfcm:metal/ingot/tungsten", "tungsten", false),
            new Briquet("pyrolusite", "tfcm:metal/ingot/manganese", "manganese", false),
            new Briquet("monazite", "tfcm:metal/ingot/neodymium", "neodymium", false),
            new Briquet("thorianite", "tfcm:metal/ingot/thorium", "thorium", false),
            new Briquet("native_iridium", "tfcm:metal/ingot/iridium", "iridium", false),
            new Briquet("native_osmium", "tfcm:metal/ingot/osmium", "osmium", false),
            new Briquet("spodumene", "tfcm:metal/ingot/lithium", "lithium", false),
            new Briquet("carobbiite", "tfcm:metal/ingot/beryllium", "beryllium", false),
            new Briquet("native_naquadah", "tfcm:metal/ingot/naquadah", "naquadah", false),
            new Briquet("mithril_matrix", "tfcm:metal/ingot/mithril", "mithril", false),
            new Briquet("borax", "tfcm:metal/ingot/boron", "boron", false));

    public ArcFurnaceRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        for(Briquet row : BRIQUETS)
        {
            ResourceLocation input = id("tfcm:ore_processing/briquet/" + row.ore);
            ResourceLocation result = id(row.output);
            ResourceLocation recipeId = id("tfie:arcfurnace/briquet/" + row.ore);
            add(output, recipeId, 0, input(input, 1), List.of(), result, 1,
                    TagOutput.EMPTY, 51200, 100, row.firmalife ? "firmalife" : null,
                    List.of(input, result), metals(row.metal), row.ore);
        }

        alloy(output, "bismuth_bronze", 3, "copper", 3,
                List.of(add("zinc", 1), add("bismuth", 1)), 5, 128000, 250);
        alloy(output, "black_bronze", 4, "copper", 2,
                List.of(add("silver", 1), add("gold", 1)), 4, 102400, 200);
        alloy(output, "brass", 1, "copper", 9, List.of(add("zinc", 1)), 10, 256000, 500);
        alloy(output, "bronze", 2, "copper", 9, List.of(add("tin", 1)), 10, 128000, 250);
        alloy(output, "rose_gold", 1, "gold", 3, List.of(add("copper", 1)), 4, 51200, 100);
        alloy(output, "sterling_silver", 1, "silver", 3,
                List.of(add("copper", 2)), 5, 128000, 250);
        alloy(output, "weak_steel", 1, "steel", 6,
                List.of(add("nickel", 2), add("black_bronze", 2)), 10, 256000, 500);
        alloy(output, "weak_blue_steel", 1, "black_steel", 10,
                List.of(add("steel", 5), add("bismuth_bronze", 2), add("sterling_silver", 3)),
                20, 512000, 1000);
        alloy(output, "weak_red_steel", 2, "black_steel", 10,
                List.of(add("steel", 5), add("brass", 2), add("rose_gold", 3)),
                20, 512000, 1000);
        alloy(output, "constantan", 5, "copper", 1, List.of(add("nickel", 1)), 2, 51200, 100);
        alloy(output, "electrum", 2, "gold", 1, List.of(add("silver", 1)), 2, 51200, 100);
        alloy(output, "invar", 2, "wrought_iron", 2, List.of(add("nickel", 1)), 3, 51200, 100);
        alloy(output, "osmiridium", 1, "osmium", 2, List.of(add("iridium", 1)), 3, 76800, 150);
        alloy(output, "solder", 1, "antimony", 2,
                List.of(add("bismuth", 1), add("tin", 1)), 4, 102400, 200);
        alloy(output, "hastelloy", 2, "nickel", 7,
                List.of(add("chromium", 3), add("molybdenum", 2), add("wrought_iron", 1), add("tungsten", 1)),
                14, 358400, 700);
        alloy(output, "high_speed_steel", 2, "steel", 10,
                List.of(add("tungsten", 1), add("vanadium", 1), add("molybdenum", 1), add("manganese", 1)),
                14, 358400, 700);
        andesite(output, "andesite_alloy_from_zinc", 1, "zinc");
        andesite(output, "andesite_alloy_from_tin", 2, "tin");

        add(output, id("tfie:arcfurnace/steel"), 1, tag("ingots/wrought_iron", 1),
                List.of(tag("dusts/coal_coke", 1)), id("tfc:metal/ingot/steel"), 1,
                new TagOutput(item("immersiveengineering:slag")), 204800, 400, null,
                List.of(id("tfc:metal/ingot/steel"), id("immersiveengineering:slag")), List.of(), null);
        add(output, id("tfie:arcfurnace/insulating_glass"), 0, tag("glass_blocks", 2),
                List.of(input(id("tfcm:metal/dust/wrought_iron"), 1)),
                id("immersiveengineering:insulating_glass"), 2, TagOutput.EMPTY, 51200, 100,
                null, List.of(id("tfcm:metal/dust/wrought_iron"), id("immersiveengineering:insulating_glass")),
                List.of(), null);
    }

    private static void alloy(RecipeOutput output, String resultMetal, int mode, String mainMetal,
                              int mainCount, List<IngredientWithSize> additives, int resultCount,
                              int energy, int time)
    {
        String namespace = List.of("weak_steel", "weak_blue_steel", "weak_red_steel", "bismuth_bronze",
                "black_bronze", "brass", "bronze", "rose_gold", "sterling_silver").contains(resultMetal)
                ? "tfc" : "tfcm";
        String mod = resultMetal.equals("hastelloy") ? "firmalife" : null;
        ResourceLocation result = id(namespace + ":metal/ingot/" + resultMetal);
        List<ResourceLocation> items = new ArrayList<>();
        items.add(result);
        List<String> enabledMetals = new ArrayList<>(List.of(mainMetal, resultMetal));
        enabledMetals.addAll(switch(resultMetal)
        {
            case "bismuth_bronze" -> List.of("zinc", "bismuth");
            case "black_bronze" -> List.of("silver", "gold");
            case "brass" -> List.of("zinc");
            case "bronze" -> List.of("tin");
            case "rose_gold", "sterling_silver" -> List.of("copper");
            case "weak_steel" -> List.of("nickel", "black_bronze");
            case "weak_blue_steel" -> List.of("steel", "bismuth_bronze", "sterling_silver");
            case "weak_red_steel" -> List.of("steel", "brass", "rose_gold");
            case "constantan", "invar" -> List.of("nickel");
            case "electrum" -> List.of("silver");
            case "osmiridium" -> List.of("iridium");
            case "solder" -> List.of("bismuth", "tin");
            case "hastelloy" -> List.of("chromium", "molybdenum", "wrought_iron", "tungsten");
            case "high_speed_steel" -> List.of("tungsten", "vanadium", "molybdenum", "manganese");
            default -> List.of();
        });
        add(output, id("tfie:arcfurnace/alloy/" + resultMetal), mode, tag("ingots/" + mainMetal, mainCount),
                additives, result, resultCount, TagOutput.EMPTY, energy, time, mod, items,
                metals(enabledMetals.toArray(String[]::new)), null);
    }

    private static void andesite(RecipeOutput output, String path, int mode, String additive)
    {
        ResourceLocation loose = id("tfc:rock/loose/andesite");
        ResourceLocation result = id("tfcm:metal/ingot/andesite_alloy");
        add(output, id("tfie:arcfurnace/alloy/" + path), mode, input(loose, 1),
                List.of(tag("nuggets/" + additive, 1)), result, 1, TagOutput.EMPTY, 25600, 50,
                null, List.of(loose, result), metals("andesite_alloy", additive), null);
    }

    private static void add(RecipeOutput output, ResourceLocation id, int mode,
                            IngredientWithSize input, List<IngredientWithSize> additives,
                            ResourceLocation result, int count, TagOutput slag, int energy, int time,
                            String mod, List<ResourceLocation> items, List<Metal> metals, String ore)
    {
        ModeAwareArcFurnaceRecipe recipe = new ModeAwareArcFurnaceRecipe(
                new TagOutputList(new TagOutput(item(result.toString()), count)), slag, List.of(),
                time, energy, input, additives, "", mode);
        output.accept(id, recipe, null, TFIEConditions.recipeForMetals(id,
                "Arc Furnace materials are unavailable", mod, items, List.of(), metals, ore));
    }

    private static IngredientWithSize add(String metal, int count)
    {
        return tag("ingots/" + metal, count);
    }

    private static IngredientWithSize tag(String path, int count)
    {
        return new IngredientWithSize(TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", path)), count);
    }

    private static IngredientWithSize input(ResourceLocation item, int count)
    {
        return new IngredientWithSize(Ingredient.of(BuiltInRegistries.ITEM.get(item)), count);
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }

    private static List<Metal> metals(String... ids)
    {
        return Arrays.stream(ids).filter(value -> value != null).map(ArcFurnaceRecipes::metal)
                .filter(value -> value != null).distinct().toList();
    }

    private static Metal metal(String id)
    {
        for(Metal metal : Metal.values())
            if(metal.id().equals(id))
                return metal;
        return null;
    }
}
