package net.claustra01.tfie.gametest.press;

import blusunrize.immersiveengineering.api.IEApiDataComponents;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.gametest.metal.ExpectedMetals;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MetalPressGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final List<String> REPLACED_METALS = List.of(
            "aluminum", "brass", "bronze", "cobalt", "constantan", "copper", "electrum", "gold", "invar",
            "iron", "lead", "nickel", "osmium", "platinum", "rose_gold", "silver", "steel", "tin",
            "tungsten", "uranium", "zinc");
    private static final TagKey<Item> MOLDS =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tfie", "molds"));

    private MetalPressGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void metal_press_recipe_table(GameTestHelper helper)
    {
        List<ExpectedMetals.Entry> metals = ExpectedMetals.available();
        int checked = 0;
        for(ExpectedMetals.Entry entry : metals)
        {
            ItemStack ingot = new ItemStack(item(ExpectedMetals.ingot(entry)));
            for(String shape : ExpectedMetals.SHAPES)
            {
                MetalPressRecipe recipe = press(helper, ExpectedMetals.recipeId(entry, shape));
                ItemStack mold = new ItemStack(item(ExpectedMetals.mold(shape)));
                ItemStack selected = new ItemStack(ingot.getItem(), ExpectedMetals.ingots(shape));

                helper.assertTrue(recipe.getTotalProcessEnergy() == 2400,
                        entry.id() + " " + shape + " must cost 2400 FE");
                helper.assertTrue(recipe.getTotalProcessTime() == 60,
                        entry.id() + " " + shape + " must take 60 ticks at multiplier one");
                helper.assertTrue(recipe.getItemInputs().size() == 1,
                        entry.id() + " " + shape + " must not consume the mold as an input");
                helper.assertTrue(recipe.getItemInputs().getFirst().getCount() == ExpectedMetals.ingots(shape)
                                && recipe.getItemInputs().getFirst().test(selected),
                        entry.id() + " " + shape + " must consume " + ExpectedMetals.ingots(shape) + " ingots");
                helper.assertTrue(recipe.mold == mold.getItem(),
                        entry.id() + " " + shape + " must use " + ExpectedMetals.mold(shape));
                ItemStack output = recipe.output.get();
                helper.assertTrue(output.is(item(ExpectedMetals.output(entry, shape)))
                                && output.getCount() == ExpectedMetals.results(shape),
                        entry.id() + " " + shape + " must output "
                                + ExpectedMetals.results(shape) + " standard items");

                RecipeHolder<MetalPressRecipe> found =
                        MetalPressRecipe.findRecipe(mold, selected, helper.getLevel());
                helper.assertTrue(found != null && found.id().toString().equals(ExpectedMetals.recipeId(entry, shape)),
                        "Selecting " + ExpectedMetals.mold(shape) + " with " + entry.id()
                                + " must resolve the canonical TFIE recipe, found "
                                + (found == null ? "none" : found.id()));
                checked++;
            }
        }
        helper.assertTrue(checked == metals.size() * 4,
                "Every available metal must provide all four shapes");
        if(!"disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod")))
            helper.assertTrue(checked == (net.neoforged.fml.ModList.get().isLoaded("firmalife") ? 252 : 244),
                    "The full metal set must yield 252 recipes, found " + checked);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void metal_press_replacement(GameTestHelper helper)
    {
        for(String metal : REPLACED_METALS)
            for(String shape : List.of("plate", "rod", "wire"))
                helper.assertTrue(helper.getLevel().getRecipeManager()
                                .byKey(ExpectedMetals.id("immersiveengineering:metalpress/"
                                        + shape + "_" + metal)).isEmpty(),
                        "The IE standard " + shape + " recipe for " + metal + " must be removed");
        helper.assertTrue(helper.getLevel().getRecipeManager()
                        .byKey(ExpectedMetals.id("immersiveengineering:metalpress/rod_netherite")).isEmpty(),
                "The IE netherite rod recipe must be removed");

        for(String kept : List.of("gear_copper", "gear_steel", "blaze_rod", "bullet_casing", "melon"))
            helper.assertTrue(helper.getLevel().getRecipeManager()
                            .byKey(ExpectedMetals.id("immersiveengineering:metalpress/" + kept)).isPresent(),
                    "Unrelated IE metal press recipe " + kept + " must stay");

        List<ExpectedMetals.Entry> available = ExpectedMetals.available();
        for(ExpectedMetals.Entry entry : available)
            for(String shape : ExpectedMetals.SHAPES)
            {
                RecipeHolder<MetalPressRecipe> found = MetalPressRecipe.findRecipe(
                        new ItemStack(item(ExpectedMetals.mold(shape))),
                        new ItemStack(item(ExpectedMetals.ingot(entry)), ExpectedMetals.ingots(shape)),
                        helper.getLevel());
                helper.assertTrue(found != null && found.id().toString().equals(
                                ExpectedMetals.recipeId(entry, shape)),
                        "The standard recipe for " + entry.id() + " " + shape
                                + " must be replaced by its TFIE recipe");
            }

        ItemStack vanillaIron = new ItemStack(Items.IRON_INGOT);
        for(String shape : List.of("plate", "rod", "wire"))
            helper.assertTrue(MetalPressRecipe.findRecipe(
                            new ItemStack(item(ExpectedMetals.mold(shape))), vanillaIron,
                            helper.getLevel()) == null,
                    "Plain iron must not be substituted for wrought iron in the " + shape + " recipe");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void molds_blueprint_and_sheet_mold(GameTestHelper helper)
    {
        ShapedRecipe blueprint = (ShapedRecipe)helper.getLevel().getRecipeManager()
                .byKey(ExpectedMetals.id("immersiveengineering:crafting/blueprint_molds"))
                .orElseThrow().value();
        helper.assertTrue(blueprint.category() == CraftingBookCategory.MISC
                        && blueprint.getWidth() == 3 && blueprint.getHeight() == 3,
                "The Molds Blueprint must be a three by three shaped recipe");

        ItemStack wroughtPlate = new ItemStack(item("tfcm:metal/plate/wrought_iron"));
        ItemStack blue = new ItemStack(Items.BLUE_DYE);
        ItemStack paper = new ItemStack(Items.PAPER);
        List<ItemStack> grid = new ArrayList<>(List.of(
                ItemStack.EMPTY, wroughtPlate, ItemStack.EMPTY,
                blue, blue, blue,
                paper, paper, paper));
        CraftingInput input = CraftingInput.of(3, 3, grid);
        helper.assertTrue(blueprint.matches(input, helper.getLevel()),
                "The Molds Blueprint must accept a wrought iron plate");
        ItemStack result = blueprint.assemble(input, helper.getLevel().registryAccess());
        helper.assertTrue(result.is(item("immersiveengineering:blueprint"))
                        && "molds".equals(result.get(IEApiDataComponents.BLUEPRINT_TYPE.get())),
                "The Molds Blueprint must hold the molds component");

        List<ItemStack> ironGrid = new ArrayList<>(grid);
        ironGrid.set(1, new ItemStack(item("immersiveengineering:plate_iron")));
        helper.assertTrue(!blueprint.matches(CraftingInput.of(3, 3, ironGrid), helper.getLevel()),
                "A normal iron plate must not replace the wrought iron plate");

        BlueprintCraftingRecipe sheetMold = (BlueprintCraftingRecipe)helper.getLevel().getRecipeManager()
                .byKey(ExpectedMetals.id("tfie:blueprint/mold_sheet")).orElseThrow().value();
        helper.assertTrue("molds".equals(sheetMold.blueprintCategory) && sheetMold.inputs.size() == 2,
                "The sheet mold must be an Engineer's Workbench molds recipe with two inputs");
        helper.assertTrue(sheetMold.inputs.getFirst().getCount() == 3
                        && sheetMold.inputs.getFirst().testIgnoringSize(new ItemStack(item("tfcm:metal/plate/steel"))),
                "The sheet mold must consume three steel plates");
        helper.assertTrue(sheetMold.inputs.getLast().testIgnoringSize(
                                new ItemStack(item("immersiveengineering:wirecutter"))),
                "The sheet mold must require a wirecutter");
        helper.assertTrue(sheetMold.output.get().is(ModItems.MOLD_SHEET.get())
                        && sheetMold.output.get().getCount() == 1,
                "The sheet mold recipe must output one tfie:mold_sheet");
        BlueprintCraftingRecipe plateMold = (BlueprintCraftingRecipe)helper.getLevel().getRecipeManager()
                .byKey(ExpectedMetals.id("immersiveengineering:blueprint/mold_plate")).orElseThrow().value();
        helper.assertTrue(plateMold.inputs.size() == 2
                        && plateMold.inputs.getLast().testIgnoringSize(
                        new ItemStack(item("immersiveengineering:wirecutter"))),
                "The IE mold recipes must keep the same wirecutter input, so the tool is damaged and returned");

        for(String retired : List.of("tfie:blueprint/mold_block", "tfie:metalpress/block_steel",
                "tfie:metalpress/block_uranium"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(ExpectedMetals.id(retired)).isEmpty(),
                    "Retired block mold recipe " + retired + " must not ship");
        helper.assertTrue(!BuiltInRegistries.ITEM.containsKey(ExpectedMetals.id("tfie:mold_block")),
                "The old block mold item must not be registered");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void mold_tag_size_and_selection(GameTestHelper helper)
    {
        Set<String> expected = Set.of(
                "tfie:mold_sheet", "immersiveengineering:mold_plate", "immersiveengineering:mold_gear",
                "immersiveengineering:mold_rod", "immersiveengineering:mold_bullet_casing",
                "immersiveengineering:mold_wire", "immersiveengineering:mold_packing_4",
                "immersiveengineering:mold_packing_9", "immersiveengineering:mold_unpacking");
        Set<String> actual = BuiltInRegistries.ITEM.keySet().stream()
                .filter(id -> new ItemStack(BuiltInRegistries.ITEM.get(id)).is(MOLDS))
                .map(ResourceLocation::toString)
                .collect(Collectors.toCollection(HashSet::new));
        helper.assertTrue(actual.equals(expected),
                "The mold tag must contain exactly the four reuse molds and IE's molds: " + actual);

        ItemStack sheet = new ItemStack(ModItems.MOLD_SHEET.get());
        ItemSizeDefinition size = ItemSizeManager.getDefinition(sheet);
        helper.assertTrue(size != null && size.getSize(sheet) == Size.NORMAL
                        && size.getWeight(sheet) == Weight.MEDIUM,
                "Molds must be normal sized and medium weight");
        helper.assertTrue(sheet.getMaxStackSize() == 1, "The sheet mold must not stack");

        for(String shape : ExpectedMetals.SHAPES)
        {
            ItemStack mold = new ItemStack(item(ExpectedMetals.mold(shape)));
            helper.assertTrue(MetalPressRecipe.isValidMold(helper.getLevel(), mold),
                    ExpectedMetals.mold(shape) + " must be accepted as a metal press mold");
            ItemStack ingot = new ItemStack(item("tfc:metal/ingot/copper"), ExpectedMetals.ingots(shape));
            RecipeHolder<MetalPressRecipe> found = MetalPressRecipe.findRecipe(mold, ingot, helper.getLevel());
            helper.assertTrue(found != null && found.id().getPath().equals("metalpress/" + shape + "/copper"),
                    "The " + shape + " mold with " + ExpectedMetals.ingots(shape)
                            + " copper ingots must select the copper " + shape + " recipe");
        }
        helper.succeed();
    }

    private static MetalPressRecipe press(GameTestHelper helper, String id)
    {
        Object value = helper.getLevel().getRecipeManager().byKey(ExpectedMetals.id(id)).orElseThrow().value();
        helper.assertTrue(value instanceof MetalPressRecipe, id + " must be an Immersive Engineering metal press recipe");
        return (MetalPressRecipe)value;
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ExpectedMetals.id(id));
    }
}
