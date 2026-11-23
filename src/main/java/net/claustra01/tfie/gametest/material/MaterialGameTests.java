package net.claustra01.tfie.gametest.material;

import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.condition.OreEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.claustra01.tfcm.config.TFCMConfig;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.LoggedRecipeCondition;
import net.claustra01.tfie.common.metal.MetalCatalog;
import net.claustra01.tfie.common.metal.MetalDefinition;
import net.claustra01.tfie.common.metal.MetalShape;
import net.claustra01.tfie.common.metal.MetalSource;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.util.data.Fuel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.StringWriter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MaterialGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String TEMPLATE_NAMESPACE = TFIE.MOD_ID;
    private static final Set<String> TFC_METALS = Set.of(
            "bismuth", "bismuth_bronze", "black_bronze", "black_steel", "blue_steel", "brass", "bronze",
            "cast_iron", "copper", "gold", "nickel", "red_steel", "rose_gold", "silver", "steel",
            "sterling_silver", "tin", "wrought_iron", "zinc");
    private static final Set<String> TFC_INGOT_ONLY = Set.of(
            "pig_iron", "weak_steel", "weak_blue_steel", "weak_red_steel", "high_carbon_steel",
            "high_carbon_black_steel", "high_carbon_blue_steel", "high_carbon_red_steel", "unknown");
    private static final Set<String> TFCM_METALS = Set.of(
            "compressed_iron", "platinum", "naquadah", "iridium", "osmium", "osmiridium", "mithril",
            "arcane", "refined_glowstone", "refined_obsidian", "antimony", "titanium", "cobalt", "lithium",
            "aluminum", "constantan", "invar", "electrum", "lead", "uranium", "tungsten", "solder",
            "tungsten_steel", "netherite", "dawnstone", "andesite_alloy", "draconium", "awakened_draconium",
            "boron", "thorium", "magnesium", "beryllium", "zirconium", "manganese", "molybdenum",
            "neodymium", "vanadium", "hastelloy", "high_speed_steel", "signalum", "lumium", "enderium");
    private static final Set<String> FIRMALIFE_METALS = Set.of("chromium", "stainless_steel");

    private MaterialGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void metal_sets_and_standard_outputs(GameTestHelper helper)
    {
        Map<String, MetalDefinition> definitions = MetalCatalog.all().stream()
                .collect(Collectors.toMap(MetalDefinition::id, Function.identity()));
        Set<String> expectedAll = union(TFC_METALS, TFC_INGOT_ONLY, TFCM_METALS, FIRMALIFE_METALS,
                Set.of("high_carbon_tungsten_steel"));
        helper.assertTrue(definitions.keySet().equals(expectedAll),
                "The correspondence table must contain exactly the chapter 6 materials");

        Set<String> shaped = MetalCatalog.shaped().stream().map(MetalDefinition::id).collect(Collectors.toSet());
        helper.assertTrue(shaped.equals(union(TFC_METALS, TFCM_METALS, FIRMALIFE_METALS)),
                "The maximum shaped set must be the independently listed 63 metals");
        helper.assertTrue(shaped.size() == 63, "The maximum shaped metal set must contain 63 entries");
        helper.assertTrue(TFC_INGOT_ONLY.stream().allMatch(id -> definitions.get(id).shapes()
                        .equals(Set.of(MetalShape.INGOT))),
                "The nine TFC ingot-only materials must not acquire processing shapes");
        helper.assertTrue(definitions.get("high_carbon_tungsten_steel").shapes()
                        .equals(Set.of(MetalShape.INGOT)),
                "High carbon tungsten steel must remain ingot-only");

        for(String id : TFC_METALS)
            assertOutputs(helper, definitions.get(id), "tfc");
        for(String id : TFCM_METALS)
            assertOutputs(helper, definitions.get(id), "tfcm");
        for(String id : FIRMALIFE_METALS)
            assertOutputs(helper, definitions.get(id), "firmalife");

        long active = MetalCatalog.shaped().stream().filter(MaterialGameTests::isActive).count();
        long expectedActive = ModList.get().isLoaded("firmalife") ? 63 : 61;
        if(Boolean.getBoolean("tfie.gametest.disabledContent"))
            expectedActive--;
        helper.assertTrue(active == expectedActive,
                "The active shaped set must follow FirmaLife and TFCM configuration conditions");
        helper.assertTrue(MetalCatalog.find("future_metal").isEmpty(),
                "Unknown metals must not be generated from registry tags");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void input_tags_and_load_conditions(GameTestHelper helper)
    {
        for(MetalDefinition metal : MetalCatalog.shaped())
        {
            for(MetalShape shape : MetalShape.values())
            {
                helper.assertTrue(metal.inputTag(shape).equals(ResourceLocation.fromNamespaceAndPath("c",
                                shape.tagFolder() + "/" + metal.id())),
                        "Input tags must use the c namespace and the exact metal ID");
                List<ICondition> conditions = metal.loadConditions(shape);
                helper.assertTrue(conditions.stream().anyMatch(ItemExistsCondition.class::isInstance),
                        "Every shape must check its referenced output item");
                if(metal.source() == MetalSource.TFCM)
                    helper.assertTrue(conditions.stream().anyMatch(MetalEnabledCondition.class::isInstance),
                            "TFCM metals must use tfcm:metal_enabled");
                if(metal.source() == MetalSource.FIRMALIFE)
                    helper.assertTrue(conditions.stream().anyMatch(condition -> condition instanceof ModLoadedCondition),
                            "FirmaLife metals must use a mod_loaded condition");
                if(isActive(metal))
                {
                    Item output = BuiltInRegistries.ITEM.get(metal.output(shape));
                    TagKey<Item> inputTag = TagKey.create(BuiltInRegistries.ITEM.key(), metal.inputTag(shape));
                    helper.assertTrue(output.builtInRegistryHolder().is(inputTag),
                            metal.output(shape) + " must resolve through " + metal.inputTag(shape));
                }
            }
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void shape_amounts_and_upstream_exceptions(GameTestHelper helper)
    {
        Map<MetalShape, Integer> expected = new EnumMap<>(MetalShape.class);
        expected.put(MetalShape.INGOT, 100);
        expected.put(MetalShape.PLATE, 100);
        expected.put(MetalShape.SHEET, 200);
        expected.put(MetalShape.ROD, 50);
        expected.put(MetalShape.WIRE, 25);
        expected.put(MetalShape.DOUBLE_SHEET, 400);
        for(MetalDefinition metal : MetalCatalog.shaped().stream().filter(MaterialGameTests::isActive).toList())
        {
            expected.forEach((shape, amount) -> assertHeatingAmount(helper, metal.output(shape), amount));
        }

        if(TFCMConfig.isMetalEnabled(Metal.TITANIUM))
        {
            assertHeatingAmount(helper, ResourceLocation.parse("tfcm:metal/block/titanium"), 100);
            assertHeatingAmount(helper, ResourceLocation.parse("tfcm:metal/nugget/titanium"), 10);
            var recipes = helper.getLevel().getRecipeManager();
            var nuggets = recipes.byKey(ResourceLocation.parse("tfcm:crafting/metal/nugget/titanium"))
                    .orElseThrow();
            helper.assertTrue(nuggets.value().getResultItem(helper.getLevel().registryAccess()).getCount() == 9,
                    "TFCM must retain its nine nuggets per ingot conversion");
            var ingot = recipes.byKey(ResourceLocation.parse("tfcm:crafting/metal/ingot_from_nuggets/titanium"))
                    .orElseThrow();
            helper.assertTrue(ingot.value().getIngredients().size() == 9,
                    "TFCM must retain its nine nuggets to one ingot conversion");
            helper.assertTrue(recipes.getRecipes().stream().filter(recipe -> recipe.id().getNamespace().equals("tfie"))
                            .noneMatch(recipe -> recipe.value().getIngredients().stream().anyMatch(ingredient ->
                                    ingredient.test(stack("tfcm:metal/nugget/titanium"))
                                            || ingredient.test(stack("tfcm:metal/block/titanium")))
                                    || recipe.value().getResultItem(helper.getLevel().registryAccess())
                                    .is(stack("tfcm:metal/nugget/titanium").getItem())
                                    || recipe.value().getResultItem(helper.getLevel().registryAccess())
                                    .is(stack("tfcm:metal/block/titanium").getItem())),
                    "TFIE must not add block or nugget conversions");
        }
        helper.succeed();
    }

    public static void disabled_materials_are_skipped_and_logged(GameTestHelper helper)
    {
        helper.assertTrue(Boolean.getBoolean("tfie.gametest.disabledContent"),
                "This test requires the TFCM-disabled run configuration");
        helper.assertTrue(!TFCMConfig.isMetalEnabled(Metal.TITANIUM),
                "Titanium must be disabled for the dedicated configuration");
        helper.assertTrue(!TFCMConfig.isOreEnabled("hematite"),
                "Hematite must be disabled for the dedicated configuration");
        MetalDefinition titanium = MetalCatalog.find("titanium").orElseThrow();
        helper.assertTrue(titanium.loadConditions(MetalShape.INGOT).stream()
                        .anyMatch(condition -> !condition.test(ICondition.IContext.EMPTY)),
                "A disabled metal must fail its recipe load conditions");

        StringWriter log = new StringWriter();
        var logger = (org.apache.logging.log4j.core.Logger) LogManager.getLogger(TFIE.class);
        var appender = WriterAppender.newBuilder().setName("tfie-disabled-material-log").setTarget(log)
                .setLayout(PatternLayout.createDefaultLayout()).build();
        appender.start();
        logger.addAppender(appender);
        try
        {
            for(MetalShape shape : MetalShape.values())
            {
                var metalRecipe = new LoggedRecipeCondition(ResourceLocation.fromNamespaceAndPath("tfie",
                        "metalpress/" + shape.id() + "/titanium"), "disabled metal titanium",
                        titanium.loadConditions(shape));
                helper.assertTrue(!metalRecipe.test(ICondition.IContext.EMPTY),
                        "Every titanium shape must be skipped");
                helper.assertTrue(!BuiltInRegistries.ITEM.containsKey(titanium.output(shape)),
                        "Disabled titanium output must not be registered");
                helper.assertTrue(log.toString().contains(metalRecipe.recipe().toString()),
                        "Skipped recipe log must include " + metalRecipe.recipe());
            }
            var oreRecipe = new LoggedRecipeCondition(ResourceLocation.parse("tfie:arcfurnace/briquet/hematite"),
                    "disabled ore hematite", List.of(new OreEnabledCondition("hematite")));
            helper.assertTrue(!oreRecipe.test(ICondition.IContext.EMPTY),
                    "Disabled content recipes must be skipped without substitution");
            helper.assertTrue(log.toString().contains("disabled metal titanium")
                            && log.toString().contains("tfie:arcfurnace/briquet/hematite")
                            && log.toString().contains("disabled ore hematite"),
                    "Skipped recipe logs must include each recipe ID and reason");
        }
        finally
        {
            logger.removeAppender(appender);
            appender.stop();
        }
        helper.assertTrue(MetalCatalog.all().stream().filter(metal -> !metal.id().equals("titanium"))
                        .noneMatch(metal -> metal.inputTag(MetalShape.INGOT).getPath().endsWith("/titanium")
                                || metal.output(MetalShape.INGOT).getPath().endsWith("/titanium")),
                "Other metals must not resolve to titanium");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void material_tags_fuels_and_heat(GameTestHelper helper)
    {
        assertTagged(helper, "tfc:powder/saltpeter", "c:dusts/saltpeter");
        assertTagged(helper, "tfc:powder/sulfur", "c:dusts/sulfur");
        assertTagged(helper, "immersiveengineering:coal_coke", "tfc:forge_fuel");
        assertTagged(helper, "immersiveengineering:ersatz_leather", "tfc:leather_knapping");

        Fuel coalCoke = Fuel.get(stack("immersiveengineering:coal_coke"));
        Fuel coke = Fuel.get(stack("immersiveengineering:coke"));
        helper.assertTrue(coalCoke.duration() == 3200 && close(coalCoke.temperature(), 1550.0F),
                "Coal coke must burn for 3200 ticks at 1550 C");
        helper.assertTrue(coke.duration() == 32000 && close(coke.temperature(), 1550.0F),
                "Coke must burn for 32000 ticks at 1550 C");
        helper.assertTrue(coalCoke.matches(stack("immersiveengineering:coal_coke"))
                        && coke.matches(stack("immersiveengineering:coke")),
                "Fuel entries must match their exact IE items");
        helper.assertTrue(!isTagged("immersiveengineering:coke", "tfc:forge_fuel"),
                "The coke block must not be added to the charcoal forge fuel tag");

        helper.assertTrue(close(HeatCapability.getDefinition(stack("minecraft:bricks"))
                        .heatCapacity(), 1.0F), "Bricks must have heat capacity 1.0");
        helper.assertTrue(close(HeatCapability.getDefinition(stack("immersiveengineering:slag"))
                        .heatCapacity(), 0.6F), "Slag must have heat capacity 0.6");
        helper.assertTrue(HeatCapability.has(stack("minecraft:bricks"))
                        && HeatCapability.has(stack("immersiveengineering:slag")),
                "The real items must receive the generated heat definitions");
        for(String id : List.of("minecraft:bricks", "immersiveengineering:slag"))
        {
            ItemStack heated = stack(id);
            HeatCapability.setTemperature(heated, 400);
            helper.assertTrue(close(HeatCapability.getTemperature(heated), 400),
                    id + " must retain heat on its TFC heat component");
        }

        Map<String, String> ironTags = Map.of(
                "tfc:metal/ingot/wrought_iron", "c:ingots/iron",
                "tfcm:metal/plate/wrought_iron", "c:plates/iron",
                "tfc:metal/sheet/wrought_iron", "c:sheets/iron",
                "tfc:metal/double_sheet/wrought_iron", "c:double_sheets/iron",
                "tfc:metal/rod/wrought_iron", "c:rods/iron",
                "tfcm:metal/wire/wrought_iron", "c:wires/iron");
        ironTags.forEach((item, tag) -> helper.assertTrue(!isTagged(item, tag),
                "Wrought iron must not be added to " + tag));
        helper.succeed();
    }

    private static void assertOutputs(GameTestHelper helper, MetalDefinition metal, String existingNamespace)
    {
        for(MetalShape shape : MetalShape.values())
        {
            String namespace = shape == MetalShape.PLATE || shape == MetalShape.WIRE
                    ? "tfcm" : existingNamespace;
            ResourceLocation expected = ResourceLocation.fromNamespaceAndPath(namespace,
                    "metal/" + shape.id() + "/" + metal.id());
            helper.assertTrue(metal.output(shape).equals(expected),
                    metal.id() + " has an incorrect standard output for " + shape.id());
        }
    }

    private static boolean isActive(MetalDefinition metal)
    {
        return metal.loadConditions(MetalShape.INGOT).stream()
                .allMatch(condition -> condition.test(ICondition.IContext.EMPTY));
    }

    private static void assertHeatingAmount(GameTestHelper helper, ResourceLocation id, int amount)
    {
        HeatingRecipe recipe = HeatingRecipe.getRecipe(new ItemStack(BuiltInRegistries.ITEM.get(id)));
        helper.assertTrue(recipe != null && recipe.getDisplayOutputFluid().getAmount() == amount,
                id + " must recover " + amount + " mB through TFC heating");
    }

    private static void assertTagged(GameTestHelper helper, String item, String tag)
    {
        helper.assertTrue(isTagged(item, tag),
                item + " must be a member of " + tag);
    }

    private static boolean isTagged(String item, String tag)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(item)).builtInRegistryHolder().is(
                TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.parse(tag)));
    }

    private static ItemStack stack(String item)
    {
        return new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(item)));
    }

    private static boolean close(float left, float right)
    {
        return Math.abs(left - right) < 0.0001F;
    }

    @SafeVarargs
    private static Set<String> union(Set<String>... sets)
    {
        return java.util.Arrays.stream(sets).flatMap(Set::stream).collect(Collectors.toUnmodifiableSet());
    }
}
