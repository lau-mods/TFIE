package net.claustra01.tfie.gametest.crafting;

import blusunrize.immersiveengineering.api.IEApiDataComponents;
import net.claustra01.tfie.TFIE;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CraftingGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String WROUGHT_INGOT = "tfc:metal/ingot/wrought_iron";
    private static final String WROUGHT_ROD = "tfc:metal/rod/wrought_iron";
    private static final String WROUGHT_PLATE = "tfcm:metal/plate/wrought_iron";
    private static final List<ExpectedRecipe> GENERAL_RECIPES = List.of(
            recipe("basic_engineering", 4, rows("iwi", "w w", "iwi"),
                    keys('i', WROUGHT_INGOT, 'w', "immersiveengineering:treated_wood_horizontal")),
            recipe("coil_lv", 1, rows("www", "wiw", "www"),
                    keys('i', WROUGHT_INGOT, 'w', "immersiveengineering:wirecoil_copper")),
            recipe("coil_mv", 1, rows("www", "wiw", "www"),
                    keys('i', WROUGHT_INGOT, 'w', "immersiveengineering:wirecoil_electrum")),
            recipe("coil_hv", 1, rows("www", "wiw", "www"),
                    keys('i', WROUGHT_INGOT, 'w', "immersiveengineering:wirecoil_steel")),
            recipe("dynamo", 1, rows("rcr", "ili"), keys(
                    'c', "immersiveengineering:component_iron", 'i', WROUGHT_INGOT,
                    'l', "immersiveengineering:coil_lv", 'r', "minecraft:redstone")),
            recipe("windmill", 1, rows("ppp", "pip", "ppp"),
                    keys('i', WROUGHT_INGOT, 'p', "immersiveengineering:windmill_blade")),
            recipe("current_transformer", 1, rows(" m ", " b ", "iei"), keys(
                    'b', "minecraft:terracotta", 'e', "immersiveengineering:component_electronic",
                    'i', WROUGHT_INGOT, 'm', "immersiveengineering:voltmeter")),
            recipe("electromagnet", 1, rows("pcp", "wiw", "pwp"), keys(
                    'c', "immersiveengineering:component_electronic", 'i', WROUGHT_INGOT,
                    'p', "tfc:metal/rod/steel", 'w', "immersiveengineering:wirecoil_copper")),
            recipe("floodlight", 1, rows("sii", "pes", "sci"), keys(
                    'c', "immersiveengineering:component_iron", 'e', "immersiveengineering:light_bulb",
                    'i', WROUGHT_INGOT, 'p', "minecraft:glass_pane", 's', "tfcm:metal/plate/silver")),
            recipe("transformer", 1, rows("lm", "eb", "ii"), keys(
                    'b', "immersiveengineering:coil_mv", 'e', "immersiveengineering:component_electronic",
                    'i', WROUGHT_INGOT, 'l', "immersiveengineering:connector_lv",
                    'm', "immersiveengineering:connector_mv")),
            recipe("transformer_hv", 1, rows("mh", "eb", "ii"), keys(
                    'b', "immersiveengineering:coil_hv", 'e', "immersiveengineering:component_electronic",
                    'h', "immersiveengineering:connector_hv", 'i', WROUGHT_INGOT,
                    'm', "immersiveengineering:connector_mv")),
            recipe("conveyor_basic", 8, rows("lll", "iri"),
                    keys('i', WROUGHT_INGOT, 'l', "minecraft:leather", 'r', "minecraft:redstone")),
            recipe("conveyor_splitter", 3, rows("cic", " c "),
                    keys('c', "immersiveengineering:conveyor_basic", 'i', WROUGHT_INGOT)),
            recipe("pipe_valve", 1, rows("pc", "sr"), keys(
                    'c', "immersiveengineering:component_iron", 'p', "immersiveengineering:fluid_pipe",
                    'r', "minecraft:redstone", 's', WROUGHT_ROD)),
            recipe("hatch", 1, rows("rr", "pp"), keys('p', WROUGHT_PLATE, 'r', WROUGHT_ROD)),
            recipe("treated_post", 1, rows("f", "f", "s"),
                    keys('f', "immersiveengineering:treated_fence", 's', "minecraft:stone_bricks")),
            recipe("steel_post", 1, rows("f", "f", "s"),
                    keys('f', "immersiveengineering:steel_fence", 's', "minecraft:stone_bricks")),
            recipe("alu_post", 1, rows("f", "f", "s"),
                    keys('f', "immersiveengineering:alu_fence", 's', "minecraft:stone_bricks")),
            recipe("craftingtable", 1, rows("sss", "rcr", "r r"), keys(
                    'c', "minecraft:crafting_table", 'r', "immersiveengineering:stick_treated",
                    's', "tfie:treated_wood_lumber")),
            recipe("workbench", 1, rows("iss", "c f"), keys(
                    'c', "immersiveengineering:craftingtable", 'f', "immersiveengineering:treated_fence",
                    'i', WROUGHT_INGOT, 's', "immersiveengineering:slab_treated_wood_horizontal")),
            recipe("hammer", 1, rows("h", "s"),
                    keys('h', "tfie:tool_head/ie_hammer", 's', "minecraft:stick")),
            recipe("wirecutter", 1, rows("si", " s"),
                    keys('i', "tfie:tool_head/wirecutter", 's', "minecraft:stick")),
            recipe("screwdriver", 1, rows(" i", "s "), keys('i', WROUGHT_ROD, 's', "minecraft:stick")),
            recipe("earmuffs", 1, rows(" S ", "S S", "W W"),
                    keys('S', WROUGHT_ROD, 'W', "minecraft:white_wool")),
            recipe("speedloader", 1, rows("sd", "dc"), keys(
                    'c', "immersiveengineering:component_iron", 'd', "immersiveengineering:plate_duroplast",
                    's', WROUGHT_ROD)),
            recipe("toolupgrade_buzzsaw_spareblades", 1, rows("rht", "rt "), keys(
                    'h', "immersiveengineering:hemp_fiber", 'r', WROUGHT_ROD,
                    't', "immersiveengineering:treated_wood_horizontal")),
            recipe("toolupgrade_powerpack_magnet", 1, rows("rer", "wiw", " w "), keys(
                    'e', "immersiveengineering:component_electronic_adv", 'i', WROUGHT_INGOT,
                    'r', "tfc:metal/rod/steel", 'w', "immersiveengineering:wirecoil_copper")),
            recipe("toolupgrade_shield_magnet", 1, rows("  l", "lc ", "lil"), keys(
                    'c', "immersiveengineering:coil_lv", 'i', WROUGHT_INGOT, 'l', "minecraft:leather")),
            recipe("toolupgrade_shield_shock", 1, rows("crc", "crc", "crc"),
                    keys('c', "immersiveengineering:connector_lv", 'r', WROUGHT_ROD)),
            recipe("balloon", 2, rows(" f ", "ftf", " s "), keys(
                    'f', "immersiveengineering:hemp_fabric",
                    's', "immersiveengineering:slab_treated_wood_horizontal", 't', "minecraft:torch")),
            recipe("cushion", 3, rows("fff", "f f", "fff"),
                    keys('f', "immersiveengineering:hemp_fabric")),
            recipe("strip_curtain", 3, rows("sss", "fff", "fff"),
                    keys('f', "immersiveengineering:hemp_fabric", 's', "tfc:metal/rod/steel")),
            recipe("wirecoil_structure_rope", 4, rows(" w ", "wsw", " w "),
                    keys('s', "minecraft:stick", 'w', "immersiveengineering:hemp_fiber")),
            recipe("gunpowder_barrel", 1, rows("gfg", "gbg"), keys(
                    'b', "immersiveengineering:wooden_barrel", 'f', "immersiveengineering:hemp_fiber",
                    'g', "minecraft:gunpowder")));
    private static final List<String> ROCKS = List.of(
            "granite", "diorite", "gabbro", "shale", "claystone", "limestone", "conglomerate",
            "dolomite", "chert", "chalk", "tuff", "rhyolite", "basalt", "andesite", "dacite",
            "quartzite", "slate", "phyllite", "schist", "gneiss", "marble");

    private CraftingGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void general_crafting_rows_patterns_and_counts(GameTestHelper helper)
    {
        helper.assertTrue(GENERAL_RECIPES.size() == 34, "The chapter 15 expectation table must contain 34 rows");
        for(ExpectedRecipe expected : GENERAL_RECIPES)
        {
            ShapedRecipe recipe = shaped(helper, "immersiveengineering:crafting/" + expected.path());
            ItemStack result = recipe.getResultItem(helper.getLevel().registryAccess());
            helper.assertTrue(result.is(item("immersiveengineering:" + expected.path()))
                            && result.getCount() == expected.count(),
                    expected.path() + " output differs from the chapter 15 table");
            assertExpectedIngredients(helper, expected, recipe);
            CraftingInput input = input(expected.pattern(), expected.items());
            helper.assertTrue(recipe.matches(input, helper.getLevel()),
                    expected.path() + " must match its exact material layout");
            helper.assertTrue(recipe.matches(input(mirror(expected.pattern()), expected.items()), helper.getLevel()),
                    expected.path() + " must accept vanilla horizontal mirroring");
            int offsetX = 3 - expected.pattern()[0].length();
            int offsetY = 3 - expected.pattern().length;
            if(offsetX > 0 || offsetY > 0)
                helper.assertTrue(recipe.matches(input(expected.pattern(), expected.items(), offsetX, offsetY),
                                helper.getLevel()),
                        expected.path() + " must accept vanilla movement inside the crafting grid");

            List<ItemStack> missing = new ArrayList<>(input.items());
            missing.set(firstOccupied(missing), ItemStack.EMPTY);
            helper.assertTrue(!recipe.matches(CraftingInput.of(input.width(), input.height(), missing), helper.getLevel()),
                    expected.path() + " must reject a missing material");
            List<ItemStack> wrong = new ArrayList<>(input.items());
            wrong.set(firstOccupied(wrong), new ItemStack(Items.COBBLESTONE));
            helper.assertTrue(!recipe.matches(CraftingInput.of(input.width(), input.height(), wrong), helper.getLevel()),
                    expected.path() + " must reject a swapped material");
            helper.assertTrue(rejectsDisturbedLayout(recipe,
                            input(expected.pattern(), expected.items(), 0, 0), helper),
                    expected.path() + " must reject a broken material layout");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void wrought_iron_replacement_is_strict(GameTestHelper helper)
    {
        ItemStack wroughtIngot = stack(WROUGHT_INGOT);
        ItemStack ironIngot = new ItemStack(Items.IRON_INGOT);
        ItemStack castIngot = stack("tfc:metal/ingot/cast_iron");
        ItemStack wroughtRod = stack(WROUGHT_ROD);
        ItemStack steelRod = stack("tfc:metal/rod/steel");
        ItemStack castRod = stack("tfc:metal/rod/cast_iron");
        ItemStack wroughtPlate = stack(WROUGHT_PLATE);
        ItemStack ironPlate = stack("immersiveengineering:plate_iron");
        ItemStack castPlate = stack("tfcm:metal/plate/cast_iron");

        for(ExpectedRecipe expected : GENERAL_RECIPES)
            for(var ingredient : shaped(helper, "immersiveengineering:crafting/" + expected.path()).getIngredients())
            {
                if(ingredient.test(wroughtIngot))
                    helper.assertTrue(!ingredient.test(ironIngot) && !ingredient.test(castIngot),
                            expected.path() + " must not substitute iron or cast iron for wrought iron ingots");
                if(ingredient.test(wroughtRod))
                    helper.assertTrue(!ingredient.test(steelRod) && !ingredient.test(castRod),
                            expected.path() + " must not substitute another rod for wrought iron");
                if(ingredient.test(wroughtPlate))
                    helper.assertTrue(!ingredient.test(ironPlate) && !ingredient.test(castPlate),
                            expected.path() + " must not substitute another plate for wrought iron");
            }

        ShapedRecipe hatch = shaped(helper, "immersiveengineering:crafting/hatch");
        helper.assertTrue(hatch.matches(CraftingInput.of(2, 2, List.of(
                        wroughtRod, wroughtRod, wroughtPlate, wroughtPlate)), helper.getLevel()),
                "Hatch must accept wrought iron rods and plates together");
        helper.assertTrue(!hatch.matches(CraftingInput.of(2, 2, List.of(
                        steelRod, steelRod, wroughtPlate, wroughtPlate)), helper.getLevel())
                        && !hatch.matches(CraftingInput.of(2, 2, List.of(
                        wroughtRod, wroughtRod, ironPlate, ironPlate)), helper.getLevel()),
                "Hatch must reject replacement of either wrought iron shape");
        helper.assertTrue(!wroughtIngot.is(tag("c:ingots/iron")),
                "Wrought iron must not be added to the normal iron ingot tag");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void treated_wood_workstation_materials(GameTestHelper helper)
    {
        ExpectedRecipe table = expected("craftingtable");
        ShapedRecipe craftingTable = shaped(helper, "immersiveengineering:crafting/craftingtable");
        helper.assertTrue(craftingTable.matches(input(table.pattern(), table.items()), helper.getLevel()),
                "IE Crafting Table must use three treated lumber items");
        Map<Character, String> slabTable = new HashMap<>(table.items());
        slabTable.put('s', "immersiveengineering:slab_treated_wood_horizontal");
        helper.assertTrue(!craftingTable.matches(input(table.pattern(), slabTable), helper.getLevel()),
                "IE Crafting Table must reject treated slabs in the lumber row");

        ExpectedRecipe bench = expected("workbench");
        ShapedRecipe workbench = shaped(helper, "immersiveengineering:crafting/workbench");
        helper.assertTrue(workbench.matches(input(bench.pattern(), bench.items()), helper.getLevel()),
                "Engineer's Workbench must use two treated slabs");
        Map<Character, String> lumberBench = new HashMap<>(bench.items());
        lumberBench.put('s', "tfie:treated_wood_lumber");
        helper.assertTrue(!workbench.matches(input(bench.pattern(), lumberBench), helper.getLevel()),
                "Engineer's Workbench must reject treated lumber in its slab slots");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void stone_bricks_post_foundations(GameTestHelper helper)
    {
        TagKey<Item> tag = tag("tfie:stone_bricks_no_variants");
        Set<ResourceLocation> expected = new HashSet<>();
        ROCKS.forEach(rock -> expected.add(id("tfc:rock/bricks/" + rock)));
        expected.add(id("minecraft:stone_bricks"));
        Set<ResourceLocation> actual = new HashSet<>();
        BuiltInRegistries.ITEM.getTag(tag).orElseThrow().forEach(holder ->
                actual.add(BuiltInRegistries.ITEM.getKey(holder.value())));
        helper.assertTrue(actual.equals(expected),
                "#tfie:stone_bricks_no_variants must contain exactly the 22 base brick blocks");

        for(String post : List.of("treated_post", "steel_post", "alu_post"))
        {
            ShapedRecipe recipe = shaped(helper, "immersiveengineering:crafting/" + post);
            String fence = post.equals("treated_post") ? "immersiveengineering:treated_fence"
                    : post.equals("steel_post") ? "immersiveengineering:steel_fence"
                    : "immersiveengineering:alu_fence";
            for(ResourceLocation brick : expected)
                helper.assertTrue(recipe.matches(CraftingInput.of(1, 3, List.of(
                                stack(fence), stack(fence), new ItemStack(BuiltInRegistries.ITEM.get(brick)))),
                                helper.getLevel()),
                        post + " must accept " + brick);
            for(String variant : List.of(
                    "minecraft:stone_brick_slab", "minecraft:stone_brick_stairs",
                    "minecraft:stone_brick_wall", "minecraft:mossy_stone_bricks",
                    "minecraft:cracked_stone_bricks", "tfc:rock/bricks/granite_slab"))
                helper.assertTrue(!recipe.matches(CraftingInput.of(1, 3, List.of(
                                stack(fence), stack(fence), stack(variant))), helper.getLevel()),
                        post + " must reject derived brick variant " + variant);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void hemp_tags_and_firmalife_ersatz_leather(GameTestHelper helper)
    {
        helper.assertTrue(stack("immersiveengineering:hemp_fabric").is(tag("c:fabric_hemp"))
                        && stack("tfc:burlap_cloth").is(tag("c:fabric_hemp")),
                "The hemp fabric tag must preserve IE fabric and add TFC burlap cloth");
        helper.assertTrue(stack("immersiveengineering:hemp_fiber").is(tag("c:fiber_hemp"))
                        && stack("tfc:jute_fiber").is(tag("c:fiber_hemp")),
                "The hemp fiber tag must preserve IE fiber and add TFC jute fiber");

        Map<String, Character> fabricRecipes = Map.of(
                "balloon", 'f', "cushion", 'f', "strip_curtain", 'f');
        for(Map.Entry<String, Character> entry : fabricRecipes.entrySet())
            assertHempSubstitution(helper, entry.getKey(), entry.getValue(),
                    "immersiveengineering:hemp_fabric", "tfc:burlap_cloth");
        Map<String, Character> fiberRecipes = Map.of(
                "toolupgrade_buzzsaw_spareblades", 'h', "wirecoil_structure_rope", 'w',
                "gunpowder_barrel", 'f');
        for(Map.Entry<String, Character> entry : fiberRecipes.entrySet())
            assertHempSubstitution(helper, entry.getKey(), entry.getValue(),
                    "immersiveengineering:hemp_fiber", "tfc:jute_fiber");

        Set<Item> hempMaterials = Set.of(
                item("immersiveengineering:hemp_fabric"), item("tfc:burlap_cloth"),
                item("immersiveengineering:hemp_fiber"), item("tfc:jute_fiber"));
        for(var holder : helper.getLevel().getRecipeManager().getRecipes())
            if(holder.id().getNamespace().equals(TFIE.MOD_ID))
            {
                Item result = holder.value().getResultItem(helper.getLevel().registryAccess()).getItem();
                if(hempMaterials.contains(result))
                    for(Ingredient ingredient : holder.value().getIngredients())
                        for(Item material : hempMaterials)
                            helper.assertTrue(material == result || !ingredient.test(new ItemStack(material)),
                                    holder.id() + " must not convert between IE and TFC hemp materials");
            }

        ResourceLocation ersatzId = id("tfie:crafting/ersatz_leather_firmalife");
        if(ModList.get().isLoaded("firmalife"))
        {
            ShapedRecipe ersatz = shaped(helper, ersatzId.toString());
            Map<Character, String> ieFabric = keys(
                    'c', "firmalife:food/raw_honey", 'f', "immersiveengineering:hemp_fabric");
            Map<Character, String> burlap = keys(
                    'c', "firmalife:food/raw_honey", 'f', "tfc:burlap_cloth");
            String[] pattern = rows("fff", "fcf", "fff");
            helper.assertTrue(ersatz.matches(input(pattern, ieFabric), helper.getLevel())
                            && ersatz.matches(input(pattern, burlap), helper.getLevel()),
                    "FirmaLife ersatz leather must accept IE fabric and TFC burlap");
            ItemStack result = ersatz.getResultItem(helper.getLevel().registryAccess());
            helper.assertTrue(result.is(item("immersiveengineering:ersatz_leather")) && result.getCount() == 8,
                    "One raw honey and eight fabric must produce eight ersatz leather");
        }
        else
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(ersatzId).isEmpty(),
                    "FirmaLife ersatz leather must not load without FirmaLife");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void standard_components_blueprint_and_fluorescent_tube(GameTestHelper helper)
    {
        ShapedRecipe blueprint = shaped(helper, "immersiveengineering:crafting/blueprint_components");
        Map<Character, String> materials = keys(
                'j', "tfc:metal/ingot/copper", 'k', "tfcm:metal/ingot/lead", 'l', "minecraft:iron_ingot",
                'd', "minecraft:blue_dye", 'p', "minecraft:paper");
        CraftingInput input = input(rows("jkl", "ddd", "ppp"), materials);
        helper.assertTrue(blueprint.matches(input, helper.getLevel()),
                "Components Blueprint must retain copper, lead and normal iron");
        ItemStack result = blueprint.assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue("components".equals(result.get(IEApiDataComponents.BLUEPRINT_TYPE.get())),
                "Components Blueprint must carry the components data component");
        Map<Character, String> wrongLead = new HashMap<>(materials);
        wrongLead.put('k', "tfcm:metal/ingot/aluminum");
        Map<Character, String> wrongIron = new HashMap<>(materials);
        wrongIron.put('l', WROUGHT_INGOT);
        helper.assertTrue(!blueprint.matches(input(rows("jkl", "ddd", "ppp"), wrongLead), helper.getLevel())
                        && !blueprint.matches(input(rows("jkl", "ddd", "ppp"), wrongIron), helper.getLevel()),
                "Components Blueprint must reject aluminum and wrought iron substitutions");

        ShapedRecipe tube = shaped(helper, "immersiveengineering:crafting/fluorescent_tube");
        Map<Character, String> tubeMaterials = keys(
                'G', "minecraft:glass", 'e', "immersiveengineering:graphite_electrode",
                'g', "minecraft:glowstone_dust");
        helper.assertTrue(tube.matches(input(rows("GeG", "GgG", "GgG"), tubeMaterials), helper.getLevel()),
                "Fluorescent Tube must retain glass, graphite electrode and glowstone dust");
        Map<Character, String> sphalerite = new HashMap<>(tubeMaterials);
        sphalerite.put('g', "tfc:ore/small_sphalerite");
        helper.assertTrue(!tube.matches(input(rows("GeG", "GgG", "GgG"), sphalerite), helper.getLevel()),
                "Fluorescent Tube must reject small sphalerite in glowstone slots");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void fixed_bucket_recipes_and_non_additions(GameTestHelper helper)
    {
        ShapedRecipe chemthrower = shaped(helper, "immersiveengineering:crafting/chemthrower");
        Map<Character, String> chemthrowerMaterials = keys(
                'b', "minecraft:bucket", 'g', "immersiveengineering:wooden_grip",
                'h', "immersiveengineering:heavy_engineering", 'p', "immersiveengineering:fluid_pipe",
                't', "immersiveengineering:toolupgrade_drill_waterproof");
        helper.assertTrue(chemthrower.matches(input(rows(" tg", " hg", "pb "), chemthrowerMaterials),
                        helper.getLevel()), "Chemthrower must accept one fixed Minecraft bucket");
        Map<Character, String> blueBucket = new HashMap<>(chemthrowerMaterials);
        blueBucket.put('b', "tfc:metal/bucket/blue_steel");
        helper.assertTrue(!chemthrower.matches(input(rows(" tg", " hg", "pb "), blueBucket), helper.getLevel()),
                "Chemthrower must reject a Blue Steel Bucket");

        ShapedRecipe jerrycan = shaped(helper, "immersiveengineering:crafting/jerrycan");
        Map<Character, String> jerrycanMaterials = keys(
                'b', "minecraft:bucket", 'i', "immersiveengineering:plate_iron");
        helper.assertTrue(jerrycan.matches(input(rows(" ii", "ibb", "ibb"), jerrycanMaterials),
                        helper.getLevel()), "Jerrycan must retain four buckets and four normal iron plates");
        assertIngredientTag(helper, jerrycan, stack("immersiveengineering:plate_iron"),
                "c:plates/iron", "Jerrycan iron plates");
        Map<Character, String> redBucket = new HashMap<>(jerrycanMaterials);
        redBucket.put('b', "tfc:metal/bucket/red_steel");
        helper.assertTrue(!jerrycan.matches(input(rows(" ii", "ibb", "ibb"), redBucket), helper.getLevel()),
                "Jerrycan must reject a Red Steel Bucket");

        for(String path : List.of(
                "tfie:crafting/chemthrower_tfc", "tfie:crafting/jerrycan_tfc",
                "tfie:crafting/blueprint_special_bullet", "minecraft:bucket"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(path)).isEmpty(),
                    path + " must not be shipped as a TFIE acquisition bypass");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void furnace_materials_and_manual_recipe_removals(GameTestHelper helper)
    {
        assertRecipe(helper, "alloybrick", "immersiveengineering:alloybrick", 2, rows("sb", "bs"),
                keys('b', "minecraft:brick", 's', "minecraft:sandstone"));
        assertRecipe(helper, "blastbrick", "immersiveengineering:blastbrick", 3,
                rows("csc", "sms", "csc"), keys(
                'c', "minecraft:nether_brick", 'm', "minecraft:magma_block", 's', "minecraft:brick"));
        var reinforced = helper.getLevel().getRecipeManager().byKey(
                id("immersiveengineering:crafting/blastbrick_reinforced")).orElseThrow().value();
        ItemStack reinforcedResult = reinforced.getResultItem(helper.getLevel().registryAccess());
        helper.assertTrue(reinforcedResult.is(item("immersiveengineering:blastbrick_reinforced"))
                        && reinforcedResult.getCount() == 1,
                "One Blast Brick and one Steel Plate must produce one Reinforced Blast Brick");
        helper.assertTrue(reinforced.getIngredients().size() == 2,
                "Reinforced Blast Brick must have exactly two ingredients");
        assertIngredientItem(helper, reinforced.getIngredients(),
                "immersiveengineering:blastbrick", "Reinforced Blast Brick base");
        assertIngredientTag(helper, reinforced.getIngredients(), stack("tfcm:metal/plate/steel"),
                "c:plates/steel", "Reinforced Blast Brick steel plate");

        List<String> removed = new ArrayList<>(List.of(
                "armor_faraday_boots", "armor_faraday_chestplate", "armor_faraday_helmet",
                "armor_faraday_leggings", "empty_casing", "gunpart_barrel",
                "toolupgrade_revolver_bayonet", "drillhead_iron", "drillhead_steel",
                "armor_steel_boots", "armor_steel_chestplate", "armor_steel_helmet",
                "armor_steel_leggings", "axe_steel", "hoe_steel", "pickaxe_steel", "shovel_steel",
                "sword_steel", "gunpowder_from_dusts", "copper_ingot_to_nugget_copper",
                "nugget_copper_to_copper_ingot", "ingot_nickel_to_nugget_nickel",
                "nugget_nickel_to_ingot_nickel", "ingot_silver_to_nugget_silver",
                "nugget_silver_to_ingot_silver", "ingot_steel_to_nugget_steel",
                "nugget_steel_to_ingot_steel", "plate_copper_hammering", "plate_gold_hammering",
                "plate_iron_hammering", "plate_nickel_hammering", "plate_silver_hammering",
                "plate_steel_hammering", "stick_iron", "stick_steel"));
        for(String metal : List.of(
                "aluminum", "constantan", "electrum", "lead", "nickel", "silver", "steel", "uranium"))
        {
            removed.add("ingot_" + metal + "_to_storage_" + metal);
            removed.add("storage_" + metal + "_to_ingot_" + metal);
        }
        for(String path : removed)
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(
                            id("immersiveengineering:crafting/" + path)).isEmpty(),
                    path + " must be disabled by the chapter 33 ledger");
        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(
                        id("immersiveengineering:crafting/alloybrick_to_slab")).isPresent(),
                "An unrelated IE crafting recipe must remain available");
        helper.succeed();
    }

    private static void assertRecipe(GameTestHelper helper, String path, String resultId, int count,
                                     String[] pattern, Map<Character, String> items)
    {
        ShapedRecipe recipe = shaped(helper, "immersiveengineering:crafting/" + path);
        ItemStack result = recipe.getResultItem(helper.getLevel().registryAccess());
        helper.assertTrue(recipe.matches(input(pattern, items), helper.getLevel())
                        && result.is(item(resultId)) && result.getCount() == count,
                path + " does not match its furnace building-material contract");
    }

    private static void assertHempSubstitution(GameTestHelper helper, String path, char symbol,
                                               String ieMaterial, String tfcMaterial)
    {
        ExpectedRecipe expected = expected(path);
        ShapedRecipe recipe = shaped(helper, "immersiveengineering:crafting/" + path);
        Map<Character, String> ieItems = new HashMap<>(expected.items());
        ieItems.put(symbol, ieMaterial);
        Map<Character, String> tfcItems = new HashMap<>(expected.items());
        tfcItems.put(symbol, tfcMaterial);
        helper.assertTrue(recipe.matches(input(expected.pattern(), ieItems), helper.getLevel())
                        && recipe.matches(input(expected.pattern(), tfcItems), helper.getLevel()),
                path + " must accept both IE and TFC hemp material");
    }

    private static void assertExpectedIngredients(GameTestHelper helper, ExpectedRecipe expected,
                                                  ShapedRecipe recipe)
    {
        Map<Character, String> tags = expectedTags(expected.path());
        int width = expected.pattern()[0].length();
        helper.assertTrue(recipe.getIngredients().size() == width * expected.pattern().length,
                expected.path() + " ingredient dimensions differ from the expectation table");
        for(int y = 0; y < expected.pattern().length; y++)
            for(int x = 0; x < width; x++)
            {
                char symbol = expected.pattern()[y].charAt(x);
                if(symbol == ' ')
                    continue;
                Ingredient ingredient = recipe.getIngredients().get(y * width + x);
                String expectedTag = tags.get(symbol);
                Item expectedItem = item(expected.items().get(symbol));
                Item mismatch = null;
                for(Item candidate : BuiltInRegistries.ITEM)
                {
                    ItemStack candidateStack = new ItemStack(candidate);
                    boolean expectedMatch = expectedTag == null ? candidate == expectedItem
                            : candidateStack.is(tag(expectedTag));
                    if(ingredient.test(candidateStack) != expectedMatch)
                    {
                        mismatch = candidate;
                        break;
                    }
                }
                helper.assertTrue(mismatch == null, expected.path() + " symbol " + symbol
                        + " must use " + (expectedTag == null ? expected.items().get(symbol) : "#" + expectedTag)
                        + (mismatch == null ? "" : ", mismatch at " + BuiltInRegistries.ITEM.getKey(mismatch)));
            }
    }

    private static void assertIngredientTag(GameTestHelper helper, ShapedRecipe recipe, ItemStack sample,
                                            String expectedTag, String description)
    {
        assertIngredientTag(helper, recipe.getIngredients(), sample, expectedTag, description);
    }

    private static void assertIngredientTag(GameTestHelper helper, List<Ingredient> ingredients, ItemStack sample,
                                            String expectedTag, String description)
    {
        Ingredient ingredient = ingredients.stream().filter(value -> value.test(sample))
                .findFirst().orElseThrow();
        Item mismatch = null;
        for(Item candidate : BuiltInRegistries.ITEM)
        {
            ItemStack candidateStack = new ItemStack(candidate);
            if(ingredient.test(candidateStack) != candidateStack.is(tag(expectedTag)))
            {
                mismatch = candidate;
                break;
            }
        }
        helper.assertTrue(mismatch == null, description + " must use #" + expectedTag
                + (mismatch == null ? "" : ", mismatch at " + BuiltInRegistries.ITEM.getKey(mismatch)));
    }

    private static void assertIngredientItem(GameTestHelper helper, List<Ingredient> ingredients,
                                             String expectedId, String description)
    {
        Item expected = item(expectedId);
        Ingredient ingredient = ingredients.stream().filter(value -> value.test(new ItemStack(expected)))
                .findFirst().orElseThrow();
        Item mismatch = null;
        for(Item candidate : BuiltInRegistries.ITEM)
            if(ingredient.test(new ItemStack(candidate)) != (candidate == expected))
            {
                mismatch = candidate;
                break;
            }
        helper.assertTrue(mismatch == null, description + " must use fixed item " + expectedId
                + (mismatch == null ? "" : ", mismatch at " + BuiltInRegistries.ITEM.getKey(mismatch)));
    }

    private static ExpectedRecipe expected(String path)
    {
        return GENERAL_RECIPES.stream().filter(recipe -> recipe.path().equals(path)).findFirst().orElseThrow();
    }

    private static Map<Character, String> expectedTags(String path)
    {
        return switch(path)
        {
            case "basic_engineering" -> tags('i', "c:ingots/wrought_iron",
                    'w', "immersiveengineering:treated_wood");
            case "coil_lv", "coil_mv", "coil_hv", "windmill", "conveyor_splitter", "transformer",
                 "transformer_hv" -> tags('i', "c:ingots/wrought_iron");
            case "dynamo" -> tags('i', "c:ingots/wrought_iron", 'r', "c:dusts/redstone");
            case "current_transformer" -> tags('b', "immersiveengineering:connector_insulator",
                    'i', "c:ingots/wrought_iron");
            case "electromagnet" -> tags('i', "c:ingots/wrought_iron", 'p', "c:rods/steel");
            case "floodlight" -> tags('i', "c:ingots/wrought_iron", 'p', "c:glass_panes",
                    's', "c:plates/silver");
            case "conveyor_basic" -> tags('i', "c:ingots/wrought_iron", 'l', "c:leathers",
                    'r', "c:dusts/redstone");
            case "pipe_valve" -> tags('r', "c:dusts/redstone", 's', "c:rods/wrought_iron");
            case "hatch" -> tags('p', "c:plates/wrought_iron", 'r', "c:rods/wrought_iron");
            case "treated_post", "steel_post", "alu_post" ->
                    tags('s', "tfie:stone_bricks_no_variants");
            case "craftingtable" -> tags('c', "c:player_workstations/crafting_tables",
                    'r', "c:rods/treated_wood");
            case "workbench" -> tags('i', "c:ingots/wrought_iron",
                    's', "immersiveengineering:treated_wood_slab");
            case "hammer", "wirecutter" -> tags('s', "c:rods/wooden");
            case "screwdriver" -> tags('i', "c:rods/wrought_iron", 's', "c:rods/wooden");
            case "earmuffs" -> tags('S', "c:rods/wrought_iron", 'W', "minecraft:wool");
            case "speedloader" -> tags('d', "c:plates/plastic", 's', "c:rods/wrought_iron");
            case "toolupgrade_buzzsaw_spareblades" -> tags('h', "c:fiber_hemp",
                    'r', "c:rods/wrought_iron", 't', "immersiveengineering:treated_wood");
            case "toolupgrade_powerpack_magnet" -> tags('i', "c:ingots/wrought_iron",
                    'r', "c:rods/steel");
            case "toolupgrade_shield_magnet" -> tags('i', "c:ingots/wrought_iron",
                    'l', "c:leathers");
            case "toolupgrade_shield_shock" -> tags('r', "c:rods/wrought_iron");
            case "balloon" -> tags('f', "c:fabric_hemp", 's', "immersiveengineering:treated_wood_slab");
            case "cushion" -> tags('f', "c:fabric_hemp");
            case "strip_curtain" -> tags('f', "c:fabric_hemp", 's', "c:rods/all_metal");
            case "wirecoil_structure_rope" -> tags('s', "c:rods/wooden", 'w', "c:fiber_hemp");
            case "gunpowder_barrel" -> tags('f', "c:fiber_hemp", 'g', "c:gunpowders");
            default -> Map.of();
        };
    }

    private static ShapedRecipe shaped(GameTestHelper helper, String id)
    {
        Object recipe = helper.getLevel().getRecipeManager().byKey(ResourceLocation.parse(id)).orElseThrow().value();
        helper.assertTrue(recipe instanceof ShapedRecipe, id + " must be a shaped crafting recipe");
        return (ShapedRecipe)recipe;
    }

    private static CraftingInput input(String[] pattern, Map<Character, String> items)
    {
        int width = pattern[0].length();
        List<ItemStack> stacks = new ArrayList<>(width * pattern.length);
        for(String row : pattern)
            for(int x = 0; x < width; x++)
                stacks.add(row.charAt(x) == ' ' ? ItemStack.EMPTY : stack(items.get(row.charAt(x))));
        return CraftingInput.of(width, pattern.length, stacks);
    }

    private static CraftingInput input(String[] pattern, Map<Character, String> items, int offsetX, int offsetY)
    {
        List<ItemStack> stacks = new ArrayList<>(9);
        for(int i = 0; i < 9; i++)
            stacks.add(ItemStack.EMPTY);
        for(int y = 0; y < pattern.length; y++)
            for(int x = 0; x < pattern[y].length(); x++)
                if(pattern[y].charAt(x) != ' ')
                    stacks.set((y + offsetY) * 3 + x + offsetX, stack(items.get(pattern[y].charAt(x))));
        return CraftingInput.of(3, 3, stacks);
    }

    private static boolean rejectsDisturbedLayout(ShapedRecipe recipe, CraftingInput input, GameTestHelper helper)
    {
        for(int first = 0; first < input.size(); first++)
            for(int second = first + 1; second < input.size(); second++)
                if(!ItemStack.isSameItemSameComponents(input.getItem(first), input.getItem(second)))
                {
                    List<ItemStack> disturbed = new ArrayList<>(input.items());
                    ItemStack moved = disturbed.get(first);
                    disturbed.set(first, disturbed.get(second));
                    disturbed.set(second, moved);
                    if(!recipe.matches(CraftingInput.of(input.width(), input.height(), disturbed), helper.getLevel()))
                        return true;
                }
        return false;
    }

    private static String[] mirror(String[] pattern)
    {
        String[] mirrored = new String[pattern.length];
        for(int i = 0; i < pattern.length; i++)
            mirrored[i] = new StringBuilder(pattern[i]).reverse().toString();
        return mirrored;
    }

    private static int firstOccupied(List<ItemStack> stacks)
    {
        for(int i = 0; i < stacks.size(); i++)
            if(!stacks.get(i).isEmpty())
                return i;
        throw new IllegalStateException("Expected a non-empty crafting input");
    }

    private static ExpectedRecipe recipe(String path, int count, String[] pattern, Map<Character, String> items)
    {
        return new ExpectedRecipe(path, count, pattern, items);
    }

    private static String[] rows(String... rows)
    {
        return rows;
    }

    private static Map<Character, String> keys(Object... values)
    {
        Map<Character, String> keys = new HashMap<>();
        for(int i = 0; i < values.length; i += 2)
            keys.put((Character)values[i], (String)values[i + 1]);
        return keys;
    }

    private static Map<Character, String> tags(Object... values)
    {
        return keys(values);
    }

    private static ItemStack stack(String id)
    {
        return new ItemStack(item(id));
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }

    private static TagKey<Item> tag(String id)
    {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(id));
    }

    private record ExpectedRecipe(String path, int count, String[] pattern, Map<Character, String> items)
    {
    }
}
