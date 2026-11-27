package net.claustra01.tfie.gametest.ledger;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.ArcRecyclingRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceFuel;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.crafting.SawmillRecipe;
import blusunrize.immersiveengineering.common.crafting.GeneratedListRecipe;
import net.claustra01.tfcm.common.metal.MetalForm;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RecipeLedgerGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final List<String> ORES = List.of(
            "aluminum", "ardite", "coal", "cobalt", "copper", "diamond", "emerald", "fluorite",
            "gold", "iron", "lapis", "lead", "nickel", "osmium", "platinum", "quartz", "redstone",
            "silver", "tin", "tungsten", "uranium", "zinc");
    private static final List<String> METAL_ORES = List.of(
            "aluminum", "ardite", "cobalt", "copper", "gold", "iron", "lead", "nickel", "osmium",
            "platinum", "silver", "tin", "tungsten", "uranium", "zinc");
    private static final List<String> ARC_DUSTS = List.of(
            "aluminum", "ardite", "brass", "bronze", "cobalt", "constantan", "copper", "electrum",
            "gold", "invar", "iron", "lead", "manyullyn", "nickel", "osmium", "platinum",
            "rose_gold", "silver", "steel", "tin", "tungsten", "uranium", "zinc");
    private static final List<String> PRESS_METALS = List.of(
            "aluminum", "brass", "bronze", "cobalt", "constantan", "copper", "electrum", "gold",
            "invar", "iron", "lead", "nickel", "osmium", "platinum", "rose_gold", "silver", "steel",
            "tin", "tungsten", "uranium", "zinc");

    private RecipeLedgerGameTests()
    {
    }

    public record CrusherLedgerInput(ResourceLocation removedId, ItemStack input)
    {
    }

    public record MetalPressLedgerInput(ResourceLocation removedId, ItemStack mold, ItemStack input)
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void crusher_ledger_and_processing_boundary(GameTestHelper helper)
    {
        List<ResourceLocation> removed = crusherLedger();
        helper.assertTrue(removed.size() == 54, "The independent Crusher ledger must contain 54 IDs");
        assertAbsent(helper, removed);
        assertCrusherLedgerLookup(helper, Set.copyOf(removed));

        helper.assertTrue(CrusherRecipe.findRecipe(helper.getLevel(), new ItemStack(Items.WHITE_WOOL)) == null,
                "Wool must not start a Crusher process");
        RecipeHolder<CrusherRecipe> ingot = CrusherRecipe.findRecipe(helper.getLevel(),
                item("tfc:metal/ingot/copper"));
        helper.assertTrue(ingot != null && ingot.id().equals(id("immersiveengineering:crusher/ingot_copper")),
                "Ingot crushing must not be removed with ore crushing");
        helper.assertTrue(CrusherRecipe.findRecipe(helper.getLevel(), item("tfc:metal/ingot/wrought_iron")) == null,
                "TFIE must not add a Wrought Iron Crusher route");

        assertCrusherControls(helper);
        long nonMetal = helper.getLevel().getRecipeManager().getRecipeIds()
                .filter(recipeId -> recipeId.getNamespace().equals(TFIE.MOD_ID)
                        && recipeId.getPath().startsWith("crusher/"))
                .count();
        helper.assertTrue(nonMetal == 24, "TFIE must retain exactly 24 non-metal Crusher recipes");
        helper.assertTrue(helper.getLevel().getRecipeManager().getRecipes().stream()
                        .anyMatch(holder -> holder.id().getNamespace().equals("tfcm")
                                && holder.value() instanceof CrusherRecipe),
                "TFCM's ore-processing Crusher recipes must remain loaded");
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(id("immersiveengineering:crusher/nether_quartz")).isEmpty(),
                "The nonexistent nether_quartz path must not be fabricated");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void arc_static_and_recycling_ledger(GameTestHelper helper)
    {
        List<ResourceLocation> removed = arcLedger();
        helper.assertTrue(removed.size() == 78, "The independent static Arc ledger must contain 78 IDs");
        assertAbsent(helper, removed);
        assertArcLedgerLookup(helper, Set.copyOf(removed));
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(id("immersiveengineering:arc_recycling_list")).isEmpty(),
                "The Arc Recycling generated-list entry must be disabled");
        assertNoArcRecycling(helper);

        for(String control : List.of(
                "tfie:arcfurnace/briquet/native_copper", "tfie:arcfurnace/steel",
                "tfie:arcfurnace/insulating_glass", "tfie:arcfurnace/alloy/rose_gold"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(control)).isPresent(),
                    "The adopted Arc control must remain: " + control);
        assertGeneratedListControl(helper, "mixer_potion_list");
        assertGeneratedListControl(helper, "potion_bottling_list");
        helper.assertTrue(helper.getLevel().getRecipeManager().getRecipes().stream()
                        .anyMatch(holder -> holder.id().getNamespace().equals("tfcm")
                                && holder.value() instanceof HeatingRecipe),
                "TFCM heating recipes must not be removed with Arc recipes");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void furnace_and_alloy_replacement_ledger(GameTestHelper helper)
    {
        assertAbsent(helper, List.of(
                id("immersiveengineering:blastfurnace/steel"),
                id("immersiveengineering:blastfurnace/steel_block"),
                id("immersiveengineering:blastfurnace/fuel_coke_block"),
                id("immersiveengineering:blastfurnace/fuel_charcoal_block")));
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("immersiveengineering:coal_coke")) == 1200,
                "Single Coke must remain a 1200 tick blast-furnace fuel");
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        new ItemStack(Items.CHARCOAL)) == 300,
                "Single Charcoal must remain a 300 tick blast-furnace fuel");
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("immersiveengineering:coke")) == 0
                        && BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("minecraft:charcoal")) == 300
                        && BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("minecraft:coal_block")) == 0,
                "Blast Furnace block fuels must remain rejected");
        helper.assertTrue(BlastFurnaceRecipe.findRecipe(helper.getLevel(),
                        item("minecraft:iron_ingot"), null) == null
                        && BlastFurnaceRecipe.findRecipe(helper.getLevel(),
                        item("minecraft:iron_block"), null) == null,
                "Iron Ingot and Block must not find an alternative Blast Furnace steel route");
        for(String ore : List.of("hematite", "magnetite", "limonite"))
        {
            if(ore.equals("hematite")
                    && "disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod")))
            {
                helper.assertTrue(helper.getLevel().getRecipeManager()
                                .byKey(id("tfie:blastfurnace/briquet/hematite")).isEmpty(),
                        "A disabled Hematite series must omit its Blast Furnace replacement");
                continue;
            }
            BlastFurnaceRecipe recipe = BlastFurnaceRecipe.findRecipe(helper.getLevel(),
                    item("tfcm:ore_processing/briquet/" + ore), null);
            helper.assertTrue(recipe != null && recipe.time == 1200
                            && recipe.output.get().is(itemType("tfc:raw_iron_bloom"))
                            && recipe.slag.get().isEmpty(),
                    ore + " must process only to a slag-free Raw Iron Bloom");
        }

        List<String> standardAlloys = List.of(
                "brass", "bronze", "constantan", "electrum", "invar", "rose_gold", "manyullyn",
                "insulating_glass");
        for(String path : standardAlloys)
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(id("immersiveengineering:alloysmelter/" + path)).isEmpty(),
                    "The IE Alloy Kiln source must be removed: " + path);
        for(String path : List.of(
                "brass", "bronze", "constantan", "electrum", "invar", "andesite_alloy_from_zinc",
                "andesite_alloy_from_tin", "insulating_glass"))
        {
            Recipe<?> recipe = helper.getLevel().getRecipeManager()
                    .byKey(id("tfie:alloysmelter/" + path)).orElseThrow().value();
            helper.assertTrue(recipe instanceof AlloyRecipe alloy && alloy.time == 200,
                    path + " must be an adopted 200 tick Alloy Kiln recipe");
        }
        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id("tfie:alloysmelter/rose_gold")).isEmpty()
                        && helper.getLevel().getRecipeManager()
                        .byKey(id("tfie:alloysmelter/manyullyn")).isEmpty(),
                "Rose Gold and Manyullyn must not receive TFIE Alloy Kiln replacements");
        AlloyRecipe brass = AlloyRecipe.findRecipe(helper.getLevel(),
                itemWithCount("tfc:metal/ingot/copper", 9), item("tfc:metal/ingot/zinc"), null);
        helper.assertTrue(brass != null && brass.output.get().is(itemType("tfc:metal/ingot/brass"))
                        && brass.output.get().getCount() == 10,
                "The Alloy Kiln must select the adopted 9:1 Brass recipe");
        for(String path : List.of("alloybrick", "blastbrick", "blastbrick_reinforced"))
            assertCraftingControl(helper, "immersiveengineering:crafting/" + path);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void shape_and_crafting_removal_ledger(GameTestHelper helper)
    {
        List<ResourceLocation> press = metalPressLedger();
        List<ResourceLocation> crafting = craftingShapeLedger();
        helper.assertTrue(press.size() == 64, "The independent Metal Press ledger must contain 64 IDs");
        helper.assertTrue(crafting.size() == 33,
                "The independent manual shape ledger must contain 8 plate/rod, 8 nugget, 16 storage and 1 gunpowder IDs");
        assertAbsent(helper, press);
        assertAbsent(helper, crafting);
        assertMetalPressLedgerLookup(helper, Set.copyOf(press));
        assertRemovedCraftingInputs(helper);
        assertAbsent(helper, List.of(
                id("tfie:blueprint/mold_block"), id("tfie:metalpress/block_steel"),
                id("tfie:metalpress/block_uranium")));
        helper.assertTrue(!BuiltInRegistries.ITEM.containsKey(id("tfie:mold_block")),
                "The retired Block Mold item must not be registered");

        RecipeHolder<MetalPressRecipe> wire = MetalPressRecipe.findRecipe(
                item("immersiveengineering:mold_wire"), item("tfc:metal/ingot/copper"), helper.getLevel());
        helper.assertTrue(wire != null && wire.id().equals(id("tfie:metalpress/wire/copper"))
                        && wire.value().output.get().getCount() == 4,
                "Copper wire must use the TFIE one-Ingot-to-four-Wire route");
        RecipeHolder<MetalPressRecipe> sheet = MetalPressRecipe.findRecipe(
                item("tfie:mold_sheet"), itemWithCount("tfc:metal/ingot/copper", 2), helper.getLevel());
        helper.assertTrue(sheet != null && sheet.id().equals(id("tfie:metalpress/sheet/copper")),
                "The Sheet mold and its processing route must remain");
        helper.assertTrue(MetalForm.NUGGET.amount == 10,
                "TFCM's existing ten mB Nugget definition must remain unchanged");
        boolean nuggetConversion = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().getNamespace().equals("tfcm"))
                .map(RecipeHolder::value)
                .filter(recipe -> recipe instanceof CraftingRecipe)
                .map(CraftingRecipe.class::cast)
                .anyMatch(recipe -> recipe.matches(CraftingInput.of(1, 1,
                                List.of(item("tfc:metal/ingot/copper"))), helper.getLevel())
                        && recipe.getResultItem(helper.getLevel().registryAccess())
                        .is(itemType("tfcm:metal/nugget/copper"))
                        && recipe.getResultItem(helper.getLevel().registryAccess()).getCount() == 9);
        helper.assertTrue(nuggetConversion, "TFCM's existing one-Ingot-to-nine-Nugget conversion must remain");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void startup_controls_and_disabled_materials(GameTestHelper helper)
    {
        assertUntouchedControls(helper);
        assertAllLedgerEntriesAbsent(helper, false);
        assertNoArcRecycling(helper);
        if("disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod")))
        {
            assertDisabledMaterialRecipes(helper);
        }
        helper.succeed();
    }

    public static void assertReloadedLedger(GameTestHelper helper)
    {
        assertAllLedgerEntriesAbsent(helper, true);
        assertNoArcRecycling(helper);
        assertUntouchedControls(helper);
        assertReloadedAdoptedControls(helper);
        if("disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod")))
            assertDisabledMaterialRecipes(helper);
    }

    public static void assertNamespaceControl(GameTestHelper helper)
    {
        Recipe<?> value = helper.getLevel().getRecipeManager()
                .byKey(id("ledger_test:crusher/wool")).orElseThrow().value();
        helper.assertTrue(value instanceof CraftingRecipe recipe
                        && recipe.matches(CraftingInput.of(1, 1, List.of(new ItemStack(Items.STONE))),
                        helper.getLevel())
                        && recipe.getResultItem(helper.getLevel().registryAccess()).is(Items.COBBLESTONE),
                "A test datapack recipe with the removed path in another namespace must survive and process");
    }

    public static void assertNamespaceControlRemoved(GameTestHelper helper)
    {
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(id("ledger_test:crusher/wool")).isEmpty(),
                "The temporary namespace-control datapack must be removed after the test");
    }

    private static List<ResourceLocation> crusherLedger()
    {
        List<ResourceLocation> ids = new ArrayList<>();
        ORES.forEach(metal -> ids.add(id("immersiveengineering:crusher/ore_" + metal)));
        METAL_ORES.forEach(metal ->
        {
            ids.add(id("immersiveengineering:crusher/raw_ore_" + metal));
            ids.add(id("immersiveengineering:crusher/raw_block_" + metal));
        });
        ids.add(id("immersiveengineering:crusher/nether_gold"));
        ids.add(id("immersiveengineering:crusher/wool"));
        return List.copyOf(ids);
    }

    private static List<ResourceLocation> arcLedger()
    {
        List<ResourceLocation> ids = new ArrayList<>();
        METAL_ORES.forEach(metal ->
        {
            ids.add(id("immersiveengineering:arcfurnace/ore_" + metal));
            ids.add(id("immersiveengineering:arcfurnace/raw_ore_" + metal));
            ids.add(id("immersiveengineering:arcfurnace/raw_block_" + metal));
        });
        ARC_DUSTS.forEach(metal -> ids.add(id("immersiveengineering:arcfurnace/dust_" + metal)));
        List.of("brass", "bronze", "constantan", "electrum", "invar", "manyullyn", "rose_gold")
                .forEach(metal -> ids.add(id("immersiveengineering:arcfurnace/alloy_" + metal)));
        ids.add(id("immersiveengineering:arcfurnace/netherite_scrap"));
        ids.add(id("immersiveengineering:arcfurnace/steel"));
        ids.add(id("immersiveengineering:arcfurnace/insulating_glass"));
        return List.copyOf(ids);
    }

    private static List<ResourceLocation> metalPressLedger()
    {
        List<ResourceLocation> ids = new ArrayList<>();
        PRESS_METALS.forEach(metal -> List.of("plate", "rod", "wire").forEach(shape ->
                ids.add(id("immersiveengineering:metalpress/" + shape + "_" + metal))));
        ids.add(id("immersiveengineering:metalpress/rod_netherite"));
        return List.copyOf(ids);
    }

    private static List<ResourceLocation> craftingShapeLedger()
    {
        List<ResourceLocation> ids = new ArrayList<>();
        List.of("copper", "gold", "iron", "nickel", "silver", "steel").forEach(metal ->
                ids.add(id("immersiveengineering:crafting/plate_" + metal + "_hammering")));
        List.of("iron", "steel").forEach(metal ->
                ids.add(id("immersiveengineering:crafting/stick_" + metal)));
        List.of(
                "copper_ingot_to_nugget_copper", "nugget_copper_to_copper_ingot",
                "ingot_nickel_to_nugget_nickel", "nugget_nickel_to_ingot_nickel",
                "ingot_silver_to_nugget_silver", "nugget_silver_to_ingot_silver",
                "ingot_steel_to_nugget_steel", "nugget_steel_to_ingot_steel")
                .forEach(path -> ids.add(id("immersiveengineering:crafting/" + path)));
        List.of("aluminum", "constantan", "electrum", "lead", "nickel", "silver", "steel", "uranium")
                .forEach(metal ->
                {
                    ids.add(id("immersiveengineering:crafting/ingot_" + metal + "_to_storage_" + metal));
                    ids.add(id("immersiveengineering:crafting/storage_" + metal + "_to_ingot_" + metal));
                });
        ids.add(id("immersiveengineering:crafting/gunpowder_from_dusts"));
        return List.copyOf(ids);
    }

    private static void assertCrusherLedgerLookup(GameTestHelper helper, Set<ResourceLocation> removed)
    {
        List<CrusherLedgerInput> inputs = crusherLedgerInputs();
        helper.assertTrue(inputs.size() == 54,
                "Every Crusher ledger ID must have an independently mapped input");
        for(CrusherLedgerInput input : inputs)
            assertCrusherInputDoesNotSelect(helper, input.input(), removed, input.removedId().getPath());
    }

    public static List<CrusherLedgerInput> crusherLedgerInputs()
    {
        List<CrusherLedgerInput> inputs = new ArrayList<>();
        for(String ore : ORES)
            inputs.add(new CrusherLedgerInput(id("immersiveengineering:crusher/ore_" + ore),
                    firstInTag("c:ores/" + ore)));
        for(String ore : METAL_ORES)
        {
            inputs.add(new CrusherLedgerInput(id("immersiveengineering:crusher/raw_ore_" + ore),
                    firstInTag("c:raw_materials/" + ore)));
            inputs.add(new CrusherLedgerInput(id("immersiveengineering:crusher/raw_block_" + ore),
                    firstInTag("c:storage_blocks/raw_" + ore)));
        }
        inputs.add(new CrusherLedgerInput(id("immersiveengineering:crusher/nether_gold"),
                new ItemStack(Items.NETHER_GOLD_ORE)));
        inputs.add(new CrusherLedgerInput(id("immersiveengineering:crusher/wool"),
                new ItemStack(Items.WHITE_WOOL)));
        return List.copyOf(inputs);
    }

    public static List<ItemStack> crusherControlInputs()
    {
        List<ItemStack> inputs = new ArrayList<>(List.of(
                item("minecraft:charcoal"), item("tfc:rock/loose/limestone"), item("tfc:food/olive")));
        for(String shape : List.of("cut", "raw", "smooth"))
            for(String color : List.of("black", "brown", "green", "pink", "red", "white", "yellow"))
                inputs.add(item("tfc:" + shape + "_sandstone/" + color));
        return List.copyOf(inputs);
    }

    public static List<ItemStack> arcRejectedInputs()
    {
        return List.of(firstInTag("c:ores/iron"), firstInTag("c:raw_materials/iron"),
                firstInTag("c:dusts/iron"), new ItemStack(Items.ANCIENT_DEBRIS),
                item("immersiveengineering:plate_steel"));
    }

    private static void assertCrusherInputDoesNotSelect(GameTestHelper helper, ItemStack input,
                                                         Set<ResourceLocation> removed, String path)
    {
        if(input.isEmpty())
            return;
        RecipeHolder<CrusherRecipe> selected = CrusherRecipe.findRecipe(helper.getLevel(), input);
        helper.assertTrue(selected == null || !removed.contains(selected.id()),
                "Crusher input selected removed route " + path + ": "
                        + (selected == null ? "none" : selected.id()));
    }

    private static void assertCrusherControls(GameTestHelper helper)
    {
        assertCrusherControl(helper, "charcoal", item("minecraft:charcoal"), "tfc:powder/charcoal", 4);
        assertCrusherControl(helper, "fluxstone", item("tfc:rock/loose/limestone"), "tfc:powder/flux", 2);
        assertCrusherControl(helper, "olive", item("tfc:food/olive"), "tfc:olive_paste", 2);
        for(String shape : List.of("cut", "raw", "smooth"))
            for(String color : List.of("black", "brown", "green", "pink", "red", "white", "yellow"))
                assertCrusherControl(helper, "sandstone/" + shape + color,
                        item("tfc:" + shape + "_sandstone/" + color), "tfc:sand/" + color, 2);

        RecipeHolder<?> tfcm = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().getNamespace().equals("tfcm"))
                .filter(holder -> holder.value() instanceof CrusherRecipe)
                .filter(holder -> ((CrusherRecipe)holder.value()).input.getItems().length > 0)
                .findFirst().orElseThrow();
        ItemStack input = ((CrusherRecipe)tfcm.value()).input.getItems()[0].copy();
        RecipeHolder<CrusherRecipe> selected = CrusherRecipe.findRecipe(helper.getLevel(), input);
        helper.assertTrue(selected != null && selected.id().getNamespace().equals("tfcm")
                        && !selected.value().getActualItemOutputs().isEmpty(),
                "A TFCM ore input must still select and execute a TFCM Crusher recipe");
    }

    private static void assertCrusherControl(GameTestHelper helper, String path, ItemStack input,
                                               String outputId, int count)
    {
        RecipeHolder<CrusherRecipe> selected = CrusherRecipe.findRecipe(helper.getLevel(), input);
        helper.assertTrue(selected != null && selected.id().equals(id("tfie:crusher/" + path)),
                "Crusher control did not select its adopted recipe: " + path);
        ItemStack output = selected.value().getActualItemOutputs().getFirst();
        helper.assertTrue(output.is(itemType(outputId)) && output.getCount() == count,
                "Crusher control produced the wrong main output: " + path);
    }

    private static void assertArcLedgerLookup(GameTestHelper helper, Set<ResourceLocation> removed)
    {
        int mapped = 0;
        for(String ore : METAL_ORES)
        {
            assertArcInputDoesNotSelect(helper, firstInTag("c:ores/" + ore), removed, "ore_" + ore);
            assertArcInputDoesNotSelect(helper, firstInTag("c:raw_materials/" + ore), removed,
                    "raw_ore_" + ore);
            assertArcInputDoesNotSelect(helper, firstInTag("c:storage_blocks/raw_" + ore), removed,
                    "raw_block_" + ore);
            mapped += 3;
        }
        for(String metal : ARC_DUSTS)
        {
            assertArcInputDoesNotSelect(helper, firstInTag("c:dusts/" + metal), removed,
                    "dust_" + metal);
            mapped++;
        }
        for(String metal : List.of("brass", "bronze", "constantan", "electrum", "invar", "manyullyn",
                "rose_gold"))
        {
            String main = switch(metal)
            {
                case "electrum", "rose_gold" -> "gold";
                case "invar" -> "iron";
                case "manyullyn" -> "cobalt";
                default -> "copper";
            };
            assertArcInputDoesNotSelect(helper, firstInTag("c:ingots/" + main), removed,
                    "alloy_" + metal);
            mapped++;
        }
        assertArcInputDoesNotSelect(helper, new ItemStack(Items.ANCIENT_DEBRIS), removed, "netherite_scrap");
        assertArcInputDoesNotSelect(helper, firstInTag("c:ingots/iron"), removed, "steel");
        assertArcInputDoesNotSelect(helper, new ItemStack(Items.GLASS), removed, "insulating_glass");
        mapped += 3;
        helper.assertTrue(mapped == 78, "Every static Arc ledger ID must have an independently mapped input");
    }

    private static void assertArcInputDoesNotSelect(GameTestHelper helper, ItemStack input,
                                                     Set<ResourceLocation> removed, String path)
    {
        if(input.isEmpty())
            return;
        RecipeHolder<ArcFurnaceRecipe> selected = ArcFurnaceRecipe.findRecipe(helper.getLevel(), input,
                NonNullList.create());
        helper.assertTrue(selected == null || !removed.contains(selected.id()),
                "Arc input selected removed route " + path + ": "
                        + (selected == null ? "none" : selected.id()));
    }

    private static void assertMetalPressLedgerLookup(GameTestHelper helper, Set<ResourceLocation> removed)
    {
        List<MetalPressLedgerInput> inputs = metalPressLedgerInputs();
        helper.assertTrue(inputs.size() == 64,
                "Every Metal Press ledger ID must have an independently mapped input and mold");
        for(MetalPressLedgerInput input : inputs)
        {
            if(input.input().isEmpty())
                continue;
            RecipeHolder<MetalPressRecipe> selected = MetalPressRecipe.findRecipe(
                    input.mold(), input.input(), helper.getLevel());
            helper.assertTrue(selected == null || !removed.contains(selected.id()),
                    "Metal Press selected a removed IE route: " + input.removedId());
        }
    }

    public static List<MetalPressLedgerInput> metalPressLedgerInputs()
    {
        List<MetalPressLedgerInput> inputs = new ArrayList<>();
        for(String metal : PRESS_METALS)
            for(String shape : List.of("plate", "rod", "wire"))
                inputs.add(new MetalPressLedgerInput(
                        id("immersiveengineering:metalpress/" + shape + "_" + metal),
                        item("immersiveengineering:mold_" + shape), firstInTag("c:ingots/" + metal)));
        inputs.add(new MetalPressLedgerInput(id("immersiveengineering:metalpress/rod_netherite"),
                item("immersiveengineering:mold_rod"), new ItemStack(Items.NETHERITE_INGOT)));
        return List.copyOf(inputs);
    }

    private static void assertRemovedCraftingInputs(GameTestHelper helper)
    {
        for(String metal : List.of("copper", "gold", "iron", "nickel", "silver", "steel"))
        {
            ItemStack ingot = firstInTag("c:ingots/" + metal);
            if(!ingot.isEmpty())
                assertCraftingDoesNotOutput(helper, CraftingInput.of(2, 1,
                                List.of(ingot, item("immersiveengineering:hammer"))),
                        "immersiveengineering:plate_" + metal,
                        "Hand hammering must not retain the removed IE Plate route: " + metal);
        }
        for(String metal : List.of("iron", "steel"))
        {
            ItemStack ingot = firstInTag("c:ingots/" + metal);
            if(!ingot.isEmpty())
                assertCraftingDoesNotOutput(helper, CraftingInput.of(1, 2, List.of(ingot.copy(), ingot.copy())),
                        "immersiveengineering:stick_" + metal,
                        "Hand crafting must not retain the removed IE Rod route: " + metal);
        }
        for(String metal : List.of("copper", "nickel", "silver", "steel"))
        {
            ItemStack ingot = firstInTag("c:ingots/" + metal);
            if(!ingot.isEmpty())
                assertCraftingDoesNotOutput(helper, CraftingInput.of(1, 1, List.of(ingot)),
                        "immersiveengineering:nugget_" + metal,
                        "The removed IE Ingot-to-Nugget output must not remain: " + metal);
        }
        for(String metal : List.of("aluminum", "constantan", "electrum", "lead", "nickel", "silver", "steel",
                "uranium"))
        {
            ItemStack ingot = firstInTag("c:ingots/" + metal);
            if(ingot.isEmpty())
                continue;
            List<ItemStack> grid = new ArrayList<>();
            for(int slot = 0; slot < 9; slot++)
                grid.add(ingot.copy());
            CraftingInput input = CraftingInput.of(3, 3, grid);
            boolean ieStorage = helper.getLevel().getRecipeManager().getRecipes().stream()
                    .filter(holder -> holder.value() instanceof CraftingRecipe)
                    .map(holder -> (CraftingRecipe)holder.value())
                    .filter(recipe -> recipe.matches(input, helper.getLevel()))
                    .map(recipe -> recipe.assemble(input, helper.getLevel().registryAccess()))
                    .filter(result -> !result.isEmpty())
                    .map(result -> BuiltInRegistries.ITEM.getKey(result.getItem()))
                    .anyMatch(resultId -> resultId.getNamespace().equals("immersiveengineering")
                            && resultId.getPath().equals("storage_" + metal));
            helper.assertTrue(!ieStorage, "Nine Ingots must not provide an alternative IE storage block: " + metal);
        }
        List<ItemStack> gunpowder = List.of(
                firstInTag("c:dusts/saltpeter"), firstInTag("c:dusts/saltpeter"),
                firstInTag("c:dusts/saltpeter"), firstInTag("c:dusts/saltpeter"),
                firstInTag("c:dusts/sulfur"), item("minecraft:charcoal"));
        if(gunpowder.stream().noneMatch(ItemStack::isEmpty))
            assertCraftingDoesNotOutput(helper, CraftingInput.of(3, 2, gunpowder), "minecraft:gunpowder",
                    "The removed dust Gunpowder route must not have an alternative crafting result");
    }

    private static void assertCraftingDoesNotOutput(GameTestHelper helper, CraftingInput input, String outputId,
                                                     String message)
    {
        boolean found = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.value() instanceof CraftingRecipe)
                .map(holder -> (CraftingRecipe)holder.value())
                .filter(recipe -> recipe.matches(input, helper.getLevel()))
                .map(recipe -> recipe.assemble(input, helper.getLevel().registryAccess()))
                .anyMatch(output -> output.is(itemType(outputId)));
        helper.assertTrue(!found, message);
    }

    private static void assertDisabledMaterialRecipes(GameTestHelper helper)
    {
        for(String shape : List.of("plate", "sheet", "rod", "wire"))
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(id("tfie:metalpress/" + shape + "/titanium")).isEmpty(),
                    "Disabled Titanium must omit its TFIE " + shape + " replacement");
        for(String recipe : List.of("tfie:arcfurnace/briquet/hematite", "tfie:arcfurnace/briquet/rutile",
                "tfie:blastfurnace/briquet/hematite", "tfie:anvil/drillhead_titanium"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(recipe)).isEmpty(),
                    "Disabled material replacement remains loaded: " + recipe);
        for(String shape : List.of("plate", "rod", "wire"))
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(id("immersiveengineering:metalpress/" + shape + "_titanium")).isEmpty(),
                    "Disabled Titanium must not restore an IE " + shape + " fallback");
        ItemStack titanium = optionalItem("tfcm:metal/ingot/titanium");
        if(!titanium.isEmpty())
            for(String shape : List.of("plate", "rod", "wire"))
                helper.assertTrue(MetalPressRecipe.findRecipe(item("immersiveengineering:mold_" + shape), titanium,
                                helper.getLevel()) == null,
                        "Disabled Titanium must not be machine-processable as " + shape);
        for(String ore : List.of("hematite", "rutile"))
        {
            ItemStack briquet = optionalItem("tfcm:ore_processing/briquet/" + ore);
            if(!briquet.isEmpty())
                helper.assertTrue(ArcFurnaceRecipe.findRecipe(helper.getLevel(), briquet,
                                NonNullList.create()) == null,
                        "Disabled ore or output metal must not be processable in the Arc: " + ore);
        }
        ItemStack hematite = optionalItem("tfcm:ore_processing/briquet/hematite");
        if(!hematite.isEmpty())
            helper.assertTrue(BlastFurnaceRecipe.findRecipe(helper.getLevel(), hematite, null) == null,
                    "Disabled Hematite must not be processable in the Blast Furnace");
    }

    private static void assertReloadedAdoptedControls(GameTestHelper helper)
    {
        assertCrusherControls(helper);
        for(String path : List.of("brass", "bronze", "constantan", "electrum", "invar",
                "andesite_alloy_from_zinc", "andesite_alloy_from_tin", "insulating_glass"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id("tfie:alloysmelter/" + path)).isPresent(),
                    "Reload removed an adopted Alloy Kiln control: " + path);
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("immersiveengineering:coal_coke")) == 1200
                        && BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(), new ItemStack(Items.CHARCOAL)) == 300,
                "Reload changed the adopted single-item Blast Furnace fuels");
        RecipeHolder<MetalPressRecipe> wire = MetalPressRecipe.findRecipe(item("immersiveengineering:mold_wire"),
                item("tfc:metal/ingot/copper"), helper.getLevel());
        RecipeHolder<MetalPressRecipe> sheet = MetalPressRecipe.findRecipe(item("tfie:mold_sheet"),
                itemWithCount("tfc:metal/ingot/copper", 2), helper.getLevel());
        helper.assertTrue(wire != null && wire.id().equals(id("tfie:metalpress/wire/copper"))
                        && sheet != null && sheet.id().equals(id("tfie:metalpress/sheet/copper")),
                "Reload changed the adopted Wire or Sheet Metal Press controls");
    }

    private static void assertAllLedgerEntriesAbsent(GameTestHelper helper, boolean allowRecyclingWrapper)
    {
        assertAbsent(helper, crusherLedger());
        assertAbsent(helper, arcLedger());
        assertAbsent(helper, metalPressLedger());
        assertAbsent(helper, craftingShapeLedger());
        List<ResourceLocation> removed = new ArrayList<>(List.of(
                id("immersiveengineering:blastfurnace/steel"),
                id("immersiveengineering:blastfurnace/steel_block"),
                id("immersiveengineering:blastfurnace/fuel_coke_block"),
                id("immersiveengineering:blastfurnace/fuel_charcoal_block"),
                id("tfie:blueprint/mold_block"), id("tfie:metalpress/block_steel"),
                id("tfie:metalpress/block_uranium")));
        List.of("brass", "bronze", "constantan", "electrum", "invar", "rose_gold", "manyullyn",
                "insulating_glass").forEach(path -> removed.add(
                id("immersiveengineering:alloysmelter/" + path)));
        if(!allowRecyclingWrapper)
            removed.add(id("immersiveengineering:arc_recycling_list"));
        assertAbsent(helper, removed);
    }

    private static void assertAbsent(GameTestHelper helper, List<ResourceLocation> ids)
    {
        for(ResourceLocation recipeId : ids)
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(recipeId).isEmpty(),
                    "Removed ledger recipe is still loaded: " + recipeId);
    }

    private static void assertNoArcRecycling(GameTestHelper helper)
    {
        List<RecipeHolder<ArcFurnaceRecipe>> recipes = ArcFurnaceRecipe.RECIPES.getRecipes(helper.getLevel());
        helper.assertTrue(recipes.stream().noneMatch(holder -> holder.value() instanceof ArcRecyclingRecipe
                        || holder.value().isSpecialType(ArcRecyclingRecipe.SPECIAL_TYPE)),
                "Arc lookup must not expose generated recycling recipes");
        helper.assertTrue(helper.getLevel().getRecipeManager().getRecipes().stream().noneMatch(holder ->
                        holder.value() instanceof ArcRecyclingRecipe
                                || holder.value() instanceof ArcFurnaceRecipe arc
                                && arc.isSpecialType(ArcRecyclingRecipe.SPECIAL_TYPE)),
                "The recipe manager must not expose an Arc Recycling recipe to JEI");
        helper.getLevel().getRecipeManager().byKey(id("immersiveengineering:arc_recycling_list"))
                .ifPresent(holder ->
                {
                    helper.assertTrue(holder.value() instanceof GeneratedListRecipe<?, ?>,
                            "The reload probe must retain the upstream generated-list wrapper");
                    GeneratedListRecipe<?, ?> generated = (GeneratedListRecipe<?, ?>)holder.value();
                    helper.assertTrue(generated.getSubRecipes().isEmpty(),
                            "The mixin must empty Arc Recycling even when a datapack re-enables its wrapper");
                });
    }

    private static void assertGeneratedListControl(GameTestHelper helper, String path)
    {
        Recipe<?> recipe = helper.getLevel().getRecipeManager()
                .byKey(id("immersiveengineering:" + path)).orElseThrow().value();
        helper.assertTrue(recipe instanceof GeneratedListRecipe<?, ?>,
                "The unrelated generated-list control must remain: " + path);
        GeneratedListRecipe<?, ?> generated = (GeneratedListRecipe<?, ?>)recipe;
        List<? extends Recipe<?>> subRecipes = generated.getSubRecipes();
        helper.assertTrue(generated.getGeneratorID().equals(id("immersiveengineering:" + path))
                        && !subRecipes.isEmpty()
                        && subRecipes.stream().allMatch(subRecipe -> path.equals("mixer_potion_list")
                        ? subRecipe instanceof MixerRecipe : subRecipe instanceof BottlingMachineRecipe),
                "The unrelated generated-list control must retain its potion subrecipes: " + path);
        if(path.equals("mixer_potion_list"))
        {
            MixerRecipe potion = (MixerRecipe)subRecipes.getFirst();
            NonNullList<ItemStack> inputs = NonNullList.create();
            potion.itemInputs.forEach(input -> inputs.add(input.getMatchingStacks()[0].copy()));
            RecipeHolder<MixerRecipe> selected = MixerRecipe.findRecipe(helper.getLevel(),
                    potion.fluidInput.getFluids()[0].copy(), inputs);
            helper.assertTrue(selected != null && !selected.value().getFluidOutput(
                            potion.fluidInput.getFluids()[0].copy(), inputs).isEmpty(),
                    "The Mixer potion generator must retain an executable recipe");
        }
        else
        {
            BottlingMachineRecipe potion = (BottlingMachineRecipe)subRecipes.getFirst();
            ItemStack[] inputs = potion.inputs.stream()
                    .map(input -> input.getMatchingStacks()[0].copy()).toArray(ItemStack[]::new);
            RecipeHolder<BottlingMachineRecipe> selected = BottlingMachineRecipe.findRecipe(helper.getLevel(),
                    potion.fluidInput.getFluids()[0].copy(), inputs);
            helper.assertTrue(selected != null && !selected.value().getDisplayStacks(inputs).isEmpty(),
                    "The Bottling Machine potion generator must retain an executable recipe");
        }
    }

    private static void assertUntouchedControls(GameTestHelper helper)
    {
        for(String path : List.of(
                "gear_copper", "blaze_rod", "bullet_casing", "melon"))
            assertMetalPressControl(helper, "immersiveengineering:metalpress/" + path);
        for(String metal : List.of("ardite", "manyullyn"))
            for(String shape : List.of("plate", "rod", "wire", "gear"))
            {
                String path = shape + "_" + metal;
                assertNoTfieRecipeOverride(helper, "metalpress/" + path);
                helper.assertTrue(helper.getLevel().getRecipeManager()
                                .byKey(id("tfie:metalpress/" + shape + "/" + metal)).isEmpty(),
                        "TFIE must not add an optional upstream Metal Press replacement: " + shape + "_" + metal);
                ItemStack ingot = firstInTag("c:ingots/" + metal);
                ItemStack output = firstInTag("c:" + (shape.equals("gear") ? "gears" : shape + "s")
                        + "/" + metal);
                if(!ingot.isEmpty() && !output.isEmpty())
                    assertMetalPressControl(helper, "immersiveengineering:metalpress/" + path);
            }
        for(String metal : List.of(
                "aluminum", "ardite", "brass", "bronze", "cobalt", "constantan", "copper",
                "electrum", "gold", "invar", "iron", "lead", "manyullyn", "nickel", "osmium",
                "platinum", "rose_gold", "silver", "steel", "tin", "tungsten", "uranium", "zinc"))
        {
            assertNoTfieRecipeOverride(helper, "metalpress/gear_" + metal);
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(id("tfie:metalpress/gear/" + metal)).isEmpty(),
                    "TFIE must not add a Gear replacement: " + metal);
            if(!firstInTag("c:ingots/" + metal).isEmpty()
                    && !firstInTag("c:gears/" + metal).isEmpty())
                assertMetalPressControl(helper, "immersiveengineering:metalpress/gear_" + metal);
        }
        for(String path : List.of(
                "raw_lead_to_raw_block_lead", "raw_block_lead_to_raw_lead",
                "storage_aluminum_to_slab", "storage_aluminum_from_slab"))
            assertCraftingControl(helper, "immersiveengineering:crafting/" + path);

        List<RecipeHolder<?>> sawmill = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().getNamespace().equals(TFIE.MOD_ID)
                        && holder.id().getPath().startsWith("sawmill/"))
                .toList();
        helper.assertTrue(!sawmill.isEmpty(), "TFIE's non-Beneath sawmill recipes must remain loaded");
        for(RecipeHolder<?> holder : sawmill)
        {
            SawmillRecipe recipe = (SawmillRecipe)holder.value();
            for(ItemStack input : recipe.input.getItems())
            {
                helper.assertTrue(!BuiltInRegistries.ITEM.getKey(input.getItem()).getNamespace().equals("beneath"),
                        "TFIE sawmill input must not use Beneath: " + holder.id());
                SawmillRecipe selected = SawmillRecipe.findRecipe(helper.getLevel(), input);
                helper.assertTrue(selected == recipe && !selected.output.get().isEmpty(),
                        "A TFIE sawmill input must select and process its surviving recipe: " + holder.id());
            }
            for(ItemStack output : recipe.getActualItemOutputs())
                if(!output.isEmpty())
                    helper.assertTrue(!BuiltInRegistries.ITEM.getKey(output.getItem()).getNamespace().equals("beneath"),
                            "TFIE sawmill output must not use Beneath: " + holder.id());
        }
        if(ModList.get().isLoaded("tfcoverdims"))
        {
            helper.assertTrue(sawmill.stream().map(RecipeHolder::value).map(SawmillRecipe.class::cast)
                            .flatMap(recipe -> java.util.Arrays.stream(recipe.input.getItems()))
                            .anyMatch(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())
                                    .getNamespace().equals("tfcoverdims")),
                    "Overdims sawmill inputs must use the tfcoverdims namespace");
            helper.assertTrue(sawmill.stream().map(RecipeHolder::value).map(SawmillRecipe.class::cast)
                            .flatMap(recipe -> recipe.getActualItemOutputs().stream())
                            .filter(stack -> !stack.isEmpty())
                            .anyMatch(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())
                                    .getNamespace().equals("tfcoverdims")),
                    "Overdims sawmill outputs must use the tfcoverdims namespace");
        }
    }

    private static void assertNoTfieRecipeOverride(GameTestHelper helper, String path)
    {
        ResourceLocation resource = id("immersiveengineering:recipe/" + path + ".json");
        boolean overridden = helper.getLevel().getServer().getResourceManager().getResourceStack(resource).stream()
                .anyMatch(entry -> entry.sourcePackId().contains(TFIE.MOD_ID));
        helper.assertTrue(!overridden,
                "TFIE's generated removal resources must not override the untouched recipe: " + path);
    }

    private static void assertMetalPressControl(GameTestHelper helper, String recipeId)
    {
        Recipe<?> value = helper.getLevel().getRecipeManager().byKey(id(recipeId)).orElseThrow().value();
        helper.assertTrue(value instanceof MetalPressRecipe, "Metal Press control has the wrong type: " + recipeId);
        MetalPressRecipe recipe = (MetalPressRecipe)value;
        ItemStack[] matching = recipe.getItemInputs().getFirst().getMatchingStacks();
        helper.assertTrue(matching.length > 0, "Metal Press control has no registered input: " + recipeId);
        RecipeHolder<MetalPressRecipe> selected = MetalPressRecipe.findRecipe(new ItemStack(recipe.mold),
                matching[0], helper.getLevel());
        helper.assertTrue(selected != null && selected.id().equals(id(recipeId))
                        && !selected.value().getActualItemOutputs().isEmpty(),
                "Metal Press control did not select and process: " + recipeId);
    }

    private static void assertCraftingControl(GameTestHelper helper, String recipeId)
    {
        Recipe<?> value = helper.getLevel().getRecipeManager().byKey(id(recipeId)).orElseThrow().value();
        helper.assertTrue(value instanceof CraftingRecipe, "Crafting control has the wrong type: " + recipeId);
        CraftingRecipe recipe = (CraftingRecipe)value;
        CraftingInput input = craftingInput(recipe);
        helper.assertTrue(recipe.matches(input, helper.getLevel())
                        && !recipe.assemble(input, helper.getLevel().registryAccess()).isEmpty(),
                "Crafting control did not match and assemble: " + recipeId);
    }

    private static CraftingInput craftingInput(CraftingRecipe recipe)
    {
        List<Ingredient> ingredients = recipe.getIngredients();
        int width = recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : Math.min(3, ingredients.size());
        int height = recipe instanceof ShapedRecipe shaped ? shaped.getHeight()
                : Math.max(1, (ingredients.size()+2)/3);
        List<ItemStack> stacks = new ArrayList<>();
        for(Ingredient ingredient : ingredients)
            stacks.add(ingredient.isEmpty() ? ItemStack.EMPTY : ingredient.getItems()[0].copy());
        while(stacks.size() < width*height)
            stacks.add(ItemStack.EMPTY);
        return CraftingInput.of(width, height, stacks);
    }

    private static ItemStack firstInTag(String value)
    {
        TagKey<Item> tag = TagKey.create(Registries.ITEM, id(value));
        return BuiltInRegistries.ITEM.getTag(tag).flatMap(items -> items.stream().findFirst())
                .map(item -> new ItemStack(item.value())).orElse(ItemStack.EMPTY);
    }

    private static ItemStack item(String value)
    {
        return new ItemStack(itemType(value));
    }

    private static ItemStack optionalItem(String value)
    {
        return BuiltInRegistries.ITEM.getOptional(id(value)).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    private static ItemStack itemWithCount(String value, int count)
    {
        return new ItemStack(itemType(value), count);
    }

    private static Item itemType(String value)
    {
        return BuiltInRegistries.ITEM.getOptional(id(value)).orElseThrow();
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
