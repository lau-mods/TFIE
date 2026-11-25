package net.claustra01.tfie.gametest.metal;

import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.condition.OreEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.claustra01.tfcm.common.metal.MetalForm;
import net.claustra01.tfie.TFIE;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.util.data.Fuel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MetalGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final TagKey<Item> SALTPETER =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dusts/saltpeter"));
    private static final TagKey<Item> SULFUR =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dusts/sulfur"));
    private static final TagKey<Item> FORGE_FUEL =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "forge_fuel"));
    private static final TagKey<Item> LEATHER_KNAPPING =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfc", "leather_knapping"));
    private static final TagKey<Item> IRON_INGOTS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/iron"));

    private MetalGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void metal_table_and_standard_outputs(GameTestHelper helper)
    {
        List<ExpectedMetals.Entry> metals = ExpectedMetals.available();
        boolean firmalifeLoaded = net.neoforged.fml.ModList.get().isLoaded("firmalife");
        long firmalife = metals.stream()
                .filter(entry -> entry.origin() == ExpectedMetals.Origin.FIRMALIFE).count();
        long enabledTfcm = ExpectedMetals.TFCM.stream().filter(ExpectedMetals::metalEnabled).count();
        helper.assertTrue(metals.stream()
                        .filter(entry -> entry.origin() == ExpectedMetals.Origin.TFC).count() == 19,
                "All nineteen TFC shaped metals must be present");
        helper.assertTrue(firmalife == (firmalifeLoaded ? 2 : 0),
                "FirmaLife must contribute exactly two metals");
        helper.assertTrue(metals.size() == 19 + enabledTfcm + firmalife,
                "The metal set must be exactly the TFC, enabled TFCM and installed FirmaLife metals");
        if(!"disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod")))
            helper.assertTrue(enabledTfcm == 42, "Every TFCM metal must be available by default");

        Set<String> expected = new HashSet<>();
        for(ExpectedMetals.Entry entry : metals)
            for(String shape : ExpectedMetals.SHAPES)
                expected.add(ExpectedMetals.recipeId(entry, shape));
        Set<String> actual = helper.getLevel().getRecipeManager().getRecipeIds()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::toString)
                .filter(id -> id.startsWith(TFIE.MOD_ID + ":metalpress/"))
                .collect(Collectors.toSet());
        helper.assertTrue(actual.equals(expected),
                "Metal press recipes differ from the chapter 6 metal set: missing "
                        + difference(expected, actual) + ", extra " + difference(actual, expected));

        for(ExpectedMetals.Entry entry : metals)
        {
            String sheet = ExpectedMetals.output(entry, "sheet");
            String rod = ExpectedMetals.output(entry, "rod");
            String plate = ExpectedMetals.output(entry, "plate");
            String wire = ExpectedMetals.output(entry, "wire");
            String existing = switch(entry.origin())
            {
                case TFC -> "tfc:";
                case TFCM -> "tfcm:";
                case FIRMALIFE -> "firmalife:";
            };
            helper.assertTrue(sheet.startsWith(existing) && rod.startsWith(existing),
                    "Existing shapes must keep their providing namespace for " + entry.id());
            helper.assertTrue(plate.startsWith("tfcm:") && wire.startsWith("tfcm:"),
                    "Added shapes must always come from TFCM for " + entry.id());
        }

        for(String metal : ExpectedMetals.INGOT_ONLY)
            for(String shape : ExpectedMetals.SHAPES)
                helper.assertTrue(helper.getLevel().getRecipeManager()
                                .byKey(ExpectedMetals.id("tfie:metalpress/" + shape + "/" + metal)).isEmpty(),
                        "Ingot-only TFC metal " + metal + " must not gain a " + shape + " shape");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void metal_amounts_and_compression_exception(GameTestHelper helper)
    {
        helper.assertTrue(MetalForm.PLATE.amount == 100 && MetalForm.SHEET.amount == 200
                        && MetalForm.ROD.amount == 50 && MetalForm.WIRE.amount == 25,
                "TFCM shape amounts must be Plate 100, Sheet 200, Rod 50 and Wire 25 mB");

        ItemStack plate = new ItemStack(BuiltInRegistries.ITEM.get(
                ExpectedMetals.id("tfcm:metal/plate/copper")));
        ItemStack sheet = new ItemStack(BuiltInRegistries.ITEM.get(
                ExpectedMetals.id("tfc:metal/sheet/copper")));
        HeatDefinition plateHeat = HeatCapability.getDefinition(plate);
        HeatDefinition sheetHeat = HeatCapability.getDefinition(sheet);
        helper.assertTrue(plateHeat != null && sheetHeat != null,
                "Plate and sheet must keep their TFC heat definitions");
        helper.assertTrue(Math.abs(sheetHeat.heatCapacity() - 2 * plateHeat.heatCapacity()) < 1e-4F,
                "A sheet must hold twice the metal of a plate");

        List<ResourceLocation> pressIds = helper.getLevel().getRecipeManager().getRecipeIds()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID) && id.getPath().startsWith("metalpress/"))
                .sorted()
                .toList();
        helper.assertTrue(!pressIds.isEmpty(), "The metal press recipes must be loaded");
        Map<ResourceLocation, ExpectedMetals.Entry> entries = new HashMap<>();
        for(ExpectedMetals.Entry entry : ExpectedMetals.available())
            for(String shape : ExpectedMetals.SHAPES)
                entries.put(ExpectedMetals.id(ExpectedMetals.recipeId(entry, shape)), entry);
        for(ResourceLocation id : pressIds)
        {
            ExpectedMetals.Entry entry = entries.get(id);
            helper.assertTrue(entry != null, id + " must belong to a chapter 6 metal");
            MetalPressRecipe recipe = (MetalPressRecipe)helper.getLevel().getRecipeManager()
                    .byKey(id).orElseThrow().value();
            IngredientWithSize input = recipe.getItemInputs().getFirst();
            ItemStack ingot = new ItemStack(BuiltInRegistries.ITEM.get(ExpectedMetals.id(ExpectedMetals.ingot(entry))));
            ItemStack result = recipe.output.get();
            int ingotAmount = molten(helper, ingot);
            int resultAmount = molten(helper, result);
            helper.assertTrue(ingotAmount == 100,
                    id + " must consume an ingot with 100 mB of metal, found " + ingotAmount);
            helper.assertTrue(input.getCount() * ingotAmount == result.getCount() * resultAmount,
                    id + " must preserve metal: " + input.getCount() + " x " + ingotAmount
                            + " != " + result.getCount() + " x " + resultAmount);
            helper.assertTrue(!isBlockOrNugget(ingot) && !isBlockOrNugget(result),
                    id + " must not press a block or nugget item");
        }
        helper.assertTrue(MetalForm.NUGGET.amount == 10,
                "The existing TFCM ten mB nugget conversion must stay untouched");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 400)
    public static void disabled_metal_and_ore_conditions(GameTestHelper helper)
    {
        boolean disabledRun = "disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod"));
        boolean titanium = new MetalEnabledCondition(Metal.TITANIUM).test(ICondition.IContext.EMPTY);
        boolean hematite = new OreEnabledCondition("hematite").test(ICondition.IContext.EMPTY);
        helper.assertTrue(titanium == !disabledRun, "TFCM titanium must reflect the configured run");
        helper.assertTrue(hematite == !disabledRun, "TFCM hematite must reflect the configured run");
        for(String shape : ExpectedMetals.SHAPES)
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(ExpectedMetals.id("tfie:metalpress/" + shape + "/titanium")).isPresent()
                            == titanium,
                    "Titanium " + shape + " recipes must follow the TFCM metal setting");
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(ExpectedMetals.id("tfie:metalpress/plate/copper")).isPresent(),
                "Unrelated metals must keep their recipes when another metal is disabled");
        if(!disabledRun)
        {
            helper.succeed();
            return;
        }

        StringWriter log = new StringWriter();
        var logger = (org.apache.logging.log4j.core.Logger)LogManager.getLogger(TFIE.class);
        var appender = WriterAppender.newBuilder().setName("tfie-metal-disable-log")
                .setTarget(log).setLayout(PatternLayout.createDefaultLayout()).build();
        appender.start();
        logger.addAppender(appender);
        var server = helper.getLevel().getServer();
        CompletableFuture<Void> reload = server.reloadResources(server.getPackRepository().getSelectedIds());
        whenDone(helper, reload, () ->
        {
            logger.removeAppender(appender);
            appender.stop();
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(ExpectedMetals.id("tfie:metalpress/plate/titanium")).isEmpty(),
                    "A disabled metal must not come back after a reload");
            helper.assertTrue(log.toString().contains("tfie:metalpress/plate/titanium")
                            && log.toString().contains("plate shape for titanium is unavailable"),
                    "The skipped recipe ID and reason must be logged: " + log);
            long presses = helper.getLevel().getRecipeManager().getRecipeIds()
                    .filter(id -> id.getNamespace().equals(TFIE.MOD_ID)
                            && id.getPath().startsWith("metalpress/"))
                    .count();
            helper.assertTrue(presses == 60L * 4,
                    "Disabling one metal must drop only its four recipes, found " + presses);
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void common_material_tags_and_heat(GameTestHelper helper)
    {
        helper.assertTrue(new ItemStack(BuiltInRegistries.ITEM.get(
                        ExpectedMetals.id("tfc:powder/saltpeter"))).is(SALTPETER),
                "TFC saltpeter powder must join #c:dusts/saltpeter");
        helper.assertTrue(new ItemStack(BuiltInRegistries.ITEM.get(
                        ExpectedMetals.id("tfc:powder/sulfur"))).is(SULFUR),
                "TFC sulfur powder must join #c:dusts/sulfur");
        helper.assertTrue(new ItemStack(BuiltInRegistries.ITEM.get(
                        ExpectedMetals.id("immersiveengineering:coal_coke"))).is(FORGE_FUEL),
                "IE coal coke must join #tfc:forge_fuel");
        helper.assertTrue(new ItemStack(BuiltInRegistries.ITEM.get(
                        ExpectedMetals.id("immersiveengineering:ersatz_leather"))).is(LEATHER_KNAPPING),
                "IE ersatz leather must join #tfc:leather_knapping");

        Fuel coalCoke = Fuel.get(new ItemStack(BuiltInRegistries.ITEM.get(
                ExpectedMetals.id("immersiveengineering:coal_coke"))));
        Fuel coke = Fuel.get(new ItemStack(BuiltInRegistries.ITEM.get(
                ExpectedMetals.id("immersiveengineering:coke"))));
        helper.assertTrue(coalCoke != null && coalCoke.duration() == 3200
                        && coalCoke.temperature() == 1550.0F,
                "Coal coke must burn for 3200 ticks at 1550 degrees");
        helper.assertTrue(coke != null && coke.duration() == 32000 && coke.temperature() == 1550.0F,
                "Coke must burn for 32000 ticks at 1550 degrees");

        ItemStack bricks = new ItemStack(Items.BRICKS);
        ItemStack slag = new ItemStack(BuiltInRegistries.ITEM.get(
                ExpectedMetals.id("immersiveengineering:slag")));
        HeatDefinition brickHeat = HeatCapability.getDefinition(bricks);
        HeatDefinition slagHeat = HeatCapability.getDefinition(slag);
        helper.assertTrue(brickHeat != null && brickHeat.heatCapacity() == 1.0F,
                "Bricks must hold a heat capacity of 1.0");
        helper.assertTrue(slagHeat != null && slagHeat.heatCapacity() == 0.6F,
                "IE slag must hold a heat capacity of 0.6");
        HeatCapability.setTemperature(bricks, 1000);
        helper.assertTrue(HeatCapability.getTemperature(bricks) == 1000,
                "TFC heat must store the applied temperature");

        helper.assertTrue(!new ItemStack(BuiltInRegistries.ITEM.get(
                        ExpectedMetals.id("tfc:metal/ingot/wrought_iron"))).is(IRON_INGOTS),
                "Wrought iron must not be added to the normal iron ingot tag");
        helper.succeed();
    }

    private static String difference(Set<String> left, Set<String> right)
    {
        Set<String> copy = new HashSet<>(left);
        copy.removeAll(right);
        return copy.toString();
    }

    private static int molten(GameTestHelper helper, ItemStack stack)
    {
        ItemStack single = new ItemStack(stack.getItem());
        HeatingRecipe recipe = HeatingRecipe.getRecipe(single);
        helper.assertTrue(recipe != null, "A TFC heating recipe must exist for " + stack);
        return recipe.assembleFluid(single).getAmount();
    }

    private static boolean isBlockOrNugget(ItemStack stack)
    {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        return path.contains("/block/") || path.contains("/nugget/");
    }

    private static void whenDone(GameTestHelper helper, CompletableFuture<?> future, Runnable then)
    {
        helper.runAfterDelay(1, () ->
        {
            if(future.isDone())
                then.run();
            else
                whenDone(helper, future, then);
        });
    }
}
