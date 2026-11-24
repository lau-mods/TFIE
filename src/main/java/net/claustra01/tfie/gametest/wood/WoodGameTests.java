package net.claustra01.tfie.gametest.wood;

import blusunrize.immersiveengineering.api.crafting.SawmillRecipe;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.recipes.SealedBarrelRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WoodGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private WoodGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void sawmill_recipe_set(GameTestHelper helper)
    {
        long count = helper.getLevel().getRecipeManager().getRecipeIds()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID) && id.getPath().startsWith("sawmill/"))
                .count();
        long expected = 80 + (ModList.get().isLoaded("afc") ? 56 : 0)
                + (ModList.get().isLoaded("tfcoverdims") ? 8 : 0);
        helper.assertTrue(count == expected, "Loaded sawmill count was " + count + ", expected " + expected);
        helper.assertTrue(helper.getLevel().getRecipeManager().getRecipeIds()
                        .noneMatch(id -> id.toString().contains("beneath")),
                "Beneath wood recipes must not ship");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void sawmill_blade_and_no_blade_stages(GameTestHelper helper)
    {
        SawmillRecipe log = sawmill(helper, "oak/log");
        helper.assertTrue(log.getBaseEnergy() == 1600 && log.getBaseTime() == 80,
                "Sawmill recipes must cost 1600 FE over 80 ticks");
        helper.assertTrue(log.output.get().getCount() == 12 && log.stripped.get().is(
                        BuiltInRegistries.ITEM.get(ResourceLocation.parse("tfc:wood/stripped_log/oak"))),
                "A bladed log must yield 12 lumber and expose the stripped no-blade result");
        helper.assertTrue(log.secondaryStripping.get().size() == 1 && log.secondaryOutputs.get().size() == 1,
                "Log dust must be split between stripping and cutting stages");

        SawmillRecipe stripped = sawmill(helper, "oak/stripped_log");
        helper.assertTrue(stripped.input.getItems().length == 2 && stripped.secondaryStripping.get().isEmpty()
                        && stripped.secondaryOutputs.get().size() == 1,
                "Stripped log and wood must share one recipe without stripping dust");
        SawmillRecipe planks = sawmill(helper, "oak/planks");
        helper.assertTrue(planks.output.get().getCount() == 4 && planks.secondaryOutputs.get().isEmpty(),
                "Planks must yield four lumber with no dust");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void sawmill_stage_data_contract(GameTestHelper helper)
    {
        ItemStack woodDust = BuiltInRegistries.ITEM.get(id("immersiveengineering:dust_wood")).getDefaultInstance();
        for(String shape : new String[]{"log", "wood"})
        {
            SawmillRecipe recipe = sawmill(helper, "oak/" + shape);
            helper.assertTrue(!recipe.stripped.get().isEmpty(),
                    shape + " must expose its no-blade stripped output");
            helper.assertTrue(recipe.secondaryStripping.get().size() == 1
                            && recipe.secondaryStripping.get().getFirst().is(woodDust.getItem()),
                    shape + " must emit wood dust at the stripping stage");
            helper.assertTrue(recipe.secondaryOutputs.get().size() == 1
                            && recipe.secondaryOutputs.get().getFirst().is(woodDust.getItem()),
                    shape + " must emit wood dust at the cutting stage");
        }
        for(String shape : new String[]{"stripped_log", "planks"})
            helper.assertTrue(sawmill(helper, "oak/" + shape).secondaryStripping.get().isEmpty(),
                    shape + " must not emit stripping-stage dust");
        helper.assertTrue(sawmill(helper, "oak/stripped_log").secondaryOutputs.get().getFirst()
                        .is(woodDust.getItem()),
                "The stripped recipe must resolve #c:dusts/wood to IE wood dust");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void treated_lumber_barrel(GameTestHelper helper)
    {
        Object value = helper.getLevel().getRecipeManager().byKey(id("tfie:barrel/treated_wood_lumber"))
                .orElseThrow().value();
        helper.assertTrue(value instanceof SealedBarrelRecipe,
                "Treated lumber must use a TFC sealed barrel recipe");
        SealedBarrelRecipe barrel = (SealedBarrelRecipe)value;
        helper.assertTrue(barrel.getDuration() == 8000,
                "Treated lumber must remain sealed for 8000 ticks");
        helper.assertTrue(barrel.getInputItem().count() == 1 && barrel.getInputFluid().amount() == 50,
                "The sealed barrel unit ratio must be one lumber to 50 mB Creosote");
        helper.assertTrue(barrel.getInputItem().test(new ItemStack(BuiltInRegistries.ITEM.get(
                        id("tfc:wood/lumber/oak")))),
                "The barrel item input must accept #tfc:lumber");
        helper.assertTrue(barrel.getInputFluid().test(new FluidStack(BuiltInRegistries.FLUID.get(
                        id("immersiveengineering:creosote")), 50)),
                "The barrel fluid input must accept IE Creosote");
        helper.assertTrue(barrel.getOutputFluid().isEmpty(),
                "Treated lumber must not create an output fluid");
        helper.assertTrue(barrel.getResultItem(helper.getLevel().registryAccess()).is(
                        BuiltInRegistries.ITEM.get(id("tfie:treated_wood_lumber"))),
                "The barrel output must be treated wood lumber");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void treated_wood_crafting_and_override(GameTestHelper helper)
    {
        Object value = helper.getLevel().getRecipeManager().byKey(id("tfie:crafting/treated_wood_horizontal"))
                .orElseThrow().value();
        helper.assertTrue(value instanceof ShapedRecipe shaped && shaped.getWidth() == 2 && shaped.getHeight() == 2,
                "Treated wood must use a two by two shaped recipe");
        ShapedRecipe shaped = (ShapedRecipe)value;
        helper.assertTrue(shaped.getIngredients().stream().filter(ingredient -> !ingredient.isEmpty()).count() == 4,
                "Exactly four treated lumber items are required");
        ItemStack result = shaped.getResultItem(helper.getLevel().registryAccess());
        helper.assertTrue(result.is(BuiltInRegistries.ITEM.get(id("immersiveengineering:treated_wood_horizontal")))
                        && result.getCount() == 1,
                "Four treated lumber must produce one treated wood block");
        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(
                        id("immersiveengineering:crafting/treated_wood_horizontal")).isEmpty(),
                "The upstream oil crafting route must be disabled");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void afc_latex_paths_are_untouched(GameTestHelper helper)
    {
        if(!ModList.get().isLoaded("afc"))
        {
            helper.succeed();
            return;
        }
        var rubber = BuiltInRegistries.ITEM.get(id("afc:rubber_bar"));
        helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().getNamespace().equals(TFIE.MOD_ID))
                .forEach(holder -> {
                    helper.assertTrue(!holder.value().getResultItem(helper.getLevel().registryAccess()).is(rubber),
                            holder.id() + " must not output AFC rubber");
                    helper.assertTrue(holder.value().getIngredients().stream()
                                    .noneMatch(ingredient -> java.util.Arrays.stream(ingredient.getItems())
                                            .anyMatch(stack -> stack.is(rubber))),
                            holder.id() + " must not consume AFC rubber");
                });
        ItemStack rubberStack = rubber.getDefaultInstance();
        for(String tag : new String[]{"leathers", "fabrics", "plastics"})
            helper.assertTrue(!rubberStack.is(ItemTags.create(id("c:" + tag))),
                    "AFC rubber must not be added to #c:" + tag);
        helper.succeed();
    }

    private static SawmillRecipe sawmill(GameTestHelper helper, String path)
    {
        Object value = helper.getLevel().getRecipeManager().byKey(id("tfie:sawmill/" + path))
                .orElseThrow().value();
        helper.assertTrue(value instanceof SawmillRecipe, path + " must be an IE Sawmill recipe");
        return (SawmillRecipe)value;
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
