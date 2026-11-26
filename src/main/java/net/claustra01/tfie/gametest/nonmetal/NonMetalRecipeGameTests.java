package net.claustra01.tfie.gametest.nonmetal;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.FermenterRecipe;
import blusunrize.immersiveengineering.api.crafting.MixerRecipe;
import blusunrize.immersiveengineering.api.crafting.RefineryRecipe;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockOrientation;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CokeOvenLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.NonMirrorableWithActiveBlock;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.dries007.tfc.common.recipes.InstantBarrelRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class NonMetalRecipeGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String[] SQUEEZER_CROPS = {
            "alfalfa", "barley", "beet", "cabbage", "canola", "carrot", "cassava", "jute", "maize",
            "melon", "oat", "onion", "peanut", "pumpkin", "radish", "red_bell_pepper", "rice", "rye",
            "soybean", "squash", "tomato", "wheat", "yellow_bell_pepper"
    };
    private static final String[] FERMENTER_FOODS = {
            "banana", "beet", "blackberry", "blueberry", "bunchberry", "cassava", "cherry", "cloudberry",
            "cranberry", "elderberry", "gooseberry", "green_apple", "lemon", "melon_slice", "olive", "orange",
            "peach", "plum", "potato", "pumpkin_chunks", "raspberry", "red_apple", "snowberry", "strawberry",
            "sugarcane", "wintergreen_berry"
    };
    private static final String[] SAND_COLORS = {
            "black", "brown", "green", "pink", "red", "white", "yellow"
    };

    private NonMetalRecipeGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void squeezer_seed_table(GameTestHelper helper)
    {
        long expectedCount = 23 + (ModList.get().isLoaded("firmalife") ? 1 : 0);
        helper.assertTrue(tfieRecipeCount(helper, "squeezer/") == expectedCount,
                "The loaded configuration has the wrong number of Squeezer recipes");
        for(String crop : SQUEEZER_CROPS)
        {
            SqueezerRecipe recipe = recipe(helper, "tfie:squeezer/" + crop, SqueezerRecipe.class);
            assertMachineValues(helper, recipe.getBaseEnergy(), recipe.getBaseTime(), 6400, 80,
                    "Squeezer " + crop);
            helper.assertTrue(recipe.input.getCount() == 1 && recipe.input.test(item("tfc:seeds/" + crop)),
                    crop + " must consume one matching seed");
            helper.assertTrue(recipe.fluidOutput.is(fluid("immersiveengineering:plantoil"))
                            && recipe.fluidOutput.getAmount() == 80 && recipe.itemOutput.get().isEmpty()
                            && recipe.getFluidInputs() == null && recipe.getMultipleProcessTicks() == 0,
                    crop + " must output only 80 mB Plant Oil");
            Recipe<?> fermenter = optionalRecipe(helper, "tfie:fermenter/" + crop);
            helper.assertTrue(!(fermenter instanceof FermenterRecipe old)
                            || !old.fluidOutput.is(fluid("immersiveengineering:plantoil")),
                    crop + " must not retain the old Plant Oil Fermenter route");
        }
        for(String excluded : new String[]{"green_bean", "lentil", "garlic", "potato", "sugarcane", "papyrus"})
            helper.assertTrue(optionalRecipe(helper, "tfie:squeezer/" + excluded) == null,
                    excluded + " seed must not receive a TFIE Plant Oil route");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void fermenter_food_and_grain_table(GameTestHelper helper)
    {
        for(String food : FERMENTER_FOODS)
        {
            FermenterRecipe recipe = recipe(helper, "tfie:fermenter/" + food, FermenterRecipe.class);
            assertFermenter(helper, recipe, "tfc:food/" + food, 80, food);
        }
        FermenterRecipe grains = recipe(helper, "tfie:fermenter/grains", FermenterRecipe.class);
        assertMachineValues(helper, grains.getBaseEnergy(), grains.getBaseTime(), 6400, 80, "Grain Fermenter");
        helper.assertTrue(grains.input.serialize().getAsJsonObject().get("tag").getAsString()
                        .equals("c:foods/grain"),
                "The grain recipe must retain the extensible #c:foods/grain tag ingredient");
        for(String grain : new String[]{"barley", "maize", "oat", "rice", "rye", "wheat"})
        {
            helper.assertTrue(grains.input.test(item("tfc:food/" + grain + "_grain")),
                    grain + " grain must match #c:foods/grain");
            helper.assertTrue(!grains.input.test(item("tfc:food/" + grain)),
                    "Unthreshed " + grain + " must not match the grain recipe");
        }
        helper.assertTrue(grains.fluidOutput.is(fluid("immersiveengineering:ethanol"))
                        && grains.fluidOutput.getAmount() == 80 && grains.itemOutput.get().isEmpty(),
                "Grains must output only 80 mB Ethanol");
        for(String excluded : new String[]{"melon", "pumpkin", "peanut", "strawberry_jam"})
            helper.assertTrue(optionalRecipe(helper, "tfie:fermenter/" + excluded) == null,
                    excluded + " must not receive an individual TFIE Fermenter route");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void firmalife_fermentation_chain(GameTestHelper helper)
    {
        boolean loaded = ModList.get().isLoaded("firmalife");
        helper.assertTrue((optionalRecipe(helper, "tfie:fermenter/raw_honey") != null) == loaded,
                "Raw Honey fermentation must follow the FirmaLife load condition");
        helper.assertTrue((optionalRecipe(helper, "tfie:squeezer/firmalife/wild_honeycomb") != null) == loaded,
                "Wild Honeycomb squeezing must follow the FirmaLife load condition");
        if(loaded)
        {
            FermenterRecipe fermenter = recipe(helper, "tfie:fermenter/raw_honey", FermenterRecipe.class);
            assertFermenter(helper, fermenter, "firmalife:food/raw_honey", 250, "Raw Honey");
            SqueezerRecipe squeezer = recipe(helper, "tfie:squeezer/firmalife/wild_honeycomb",
                    SqueezerRecipe.class);
            assertMachineValues(helper, squeezer.getBaseEnergy(), squeezer.getBaseTime(), 6400, 80,
                    "Wild Honeycomb Squeezer");
            ItemStack result = squeezer.itemOutput.get();
            helper.assertTrue(squeezer.input.test(item("firmalife:wild_honeycomb"))
                            && result.is(item("firmalife:food/raw_honey").getItem()) && result.getCount() == 1
                            && squeezer.fluidOutput.isEmpty(),
                    "Wild Honeycomb must yield one solid Raw Honey and no fluid");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void coke_oven_batch_processing(GameTestHelper helper)
    {
        for(String coal : new String[]{"bituminous_coal", "lignite"})
        {
            CokeOvenRecipe recipe = recipe(helper, "tfie:cokeoven/" + coal, CokeOvenRecipe.class);
            ItemStack output = recipe.output.get();
            ItemStack input = item("tfc:ore/" + coal);
            input.setCount(16);
            helper.assertTrue(recipe.input.getCount() == 16 && recipe.input.test(input),
                    coal + " must use a batch limit of 16");
            helper.assertTrue(output.is(item("immersiveengineering:coal_coke").getItem())
                            && output.getCount() == 1 && recipe.creosoteOutput == 500 && recipe.time == 6000,
                    coal + " must yield one Coke and 500 mB Creosote per input over 6000 ticks");
        }
        CokeOvenFixture single = cokeOven(helper, 1, ItemStack.EMPTY, 0);
        single.start();
        helper.assertTrue(single.state().get(CokeOvenLogic.State.MAX_BURN_TIME) == 6000,
                "A one-item Coke Oven batch must still run for 6000 ticks");
        for(int tick = 0; tick < 6000; tick++)
            single.logic().tickServer(single.context());
        helper.assertTrue(single.output().isEmpty() && single.state().get(CokeOvenLogic.State.BURN_TIME) == 0,
                "The one-item batch must not complete before all 6000 processing ticks elapse");
        single.logic().tickServer(single.context());
        assertCokeResult(helper, single, 0, 1, 500, "one Bituminous Coal");

        CokeOvenFixture sixteen = cokeOven(helper, 16, ItemStack.EMPTY, 0);
        sixteen.complete();
        assertCokeResult(helper, sixteen, 0, 16, 8000, "sixteen Bituminous Coal");

        CokeOvenFixture seventeen = cokeOven(helper, 17, ItemStack.EMPTY, 0);
        seventeen.complete();
        assertCokeResult(helper, seventeen, 1, 16, 8000, "seventeen Bituminous Coal");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void coke_oven_capacity_during_processing(GameTestHelper helper)
    {
        for(int[] example : new int[][]{{0, 16}, {1, 16}, {60, 4}})
        {
            ItemStack output = item("immersiveengineering:coal_coke");
            output.setCount(example[0]);
            CokeOvenFixture oven = cokeOven(helper, 16, output, 0);
            oven.complete();
            assertCokeResult(helper, oven, 16 - example[1], example[0] + example[1],
                    example[1] * 500, "output containing " + example[0] + " Coke");
        }

        CokeOvenFixture full = cokeOven(helper, 16,
                itemWithCount("immersiveengineering:coal_coke", 64), 0);
        full.start();
        helper.assertTrue(full.state().get(CokeOvenLogic.State.BURN_TIME) == 0,
                "A full Coke output slot must prevent a new process");

        ItemStack namedCoke = item("immersiveengineering:coal_coke");
        namedCoke.set(DataComponents.CUSTOM_NAME, Component.literal("Different components"));
        for(ItemStack incompatible : new ItemStack[]{namedCoke, item("minecraft:charcoal")})
        {
            CokeOvenFixture oven = cokeOven(helper, 16, incompatible, 0);
            oven.start();
            helper.assertTrue(oven.state().get(CokeOvenLogic.State.BURN_TIME) == 0
                            && oven.input().getCount() == 16,
                    "A different output item or component must prevent a new process");
        }

        CokeOvenFixture lowFluid = cokeOven(helper, 16, ItemStack.EMPTY,
                CokeOvenLogic.TANK_CAPACITY - 499);
        lowFluid.start();
        helper.assertTrue(lowFluid.state().get(CokeOvenLogic.State.BURN_TIME) == 0
                        && lowFluid.input().getCount() == 16,
                "Less than 500 mB fluid capacity must prevent a new process");

        CokeOvenFixture inserted = cokeOven(helper, 16, ItemStack.EMPTY, 0);
        inserted.start();
        inserted.state().getInventory().setStackInSlot(CokeOvenLogic.OUTPUT_SLOT,
                itemWithCount("immersiveengineering:coal_coke", 63));
        inserted.finishStartedProcess();
        assertCokeResult(helper, inserted, 15, 64, 500,
                "sixty-three Coke inserted during processing");

        CokeOvenFixture extracted = cokeOven(helper, 16,
                itemWithCount("immersiveengineering:coal_coke", 60), 0);
        extracted.start();
        extracted.state().getInventory().setStackInSlot(CokeOvenLogic.OUTPUT_SLOT, ItemStack.EMPTY);
        extracted.finishStartedProcess();
        assertCokeResult(helper, extracted, 0, 16, 8000,
                "Coke extracted during processing");

        CokeOvenFixture blockedDuringProcess = cokeOven(helper, 16, ItemStack.EMPTY, 0);
        blockedDuringProcess.start();
        blockedDuringProcess.state().getInventory().setStackInSlot(CokeOvenLogic.OUTPUT_SLOT,
                itemWithCount("immersiveengineering:coal_coke", 64));
        blockedDuringProcess.logic().tickServer(blockedDuringProcess.context());
        helper.assertTrue(blockedDuringProcess.input().getCount() == 16
                        && blockedDuringProcess.output().getCount() == 64
                        && blockedDuringProcess.state().getTank().getFluidAmount() == 0,
                "A process blocked by an in-progress insertion must not consume or overflow");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void crusher_nonmetal_table(GameTestHelper helper)
    {
        Map<String, CrusherExpectation> fixed = Map.of(
                "charcoal", new CrusherExpectation("minecraft:charcoal", "tfc:powder/charcoal", 4, 1600),
                "fluxstone", new CrusherExpectation("tfc:rock/loose/limestone", "tfc:powder/flux", 2, 1600),
                "olive", new CrusherExpectation("tfc:food/olive", "tfc:olive_paste", 2, 800));
        fixed.forEach((path, expected) -> assertCrusher(helper, path, expected, false));
        for(String shape : new String[]{"cut", "raw", "smooth"})
            for(String color : SAND_COLORS)
                assertCrusher(helper, "sandstone/" + shape + color,
                        new CrusherExpectation("tfc:" + shape + "_sandstone/" + color,
                                "tfc:sand/" + color, 2, 3200), true);
        CrusherRecipe seeded = recipe(helper, "tfie:crusher/sandstone/rawblack", CrusherRecipe.class);
        ApiUtils.RANDOM.setSeed(4096);
        helper.assertTrue(seeded.getActualItemOutputs().size() == 2,
                "A seeded roll below 0.5 must produce Saltpeter");
        ApiUtils.RANDOM.setSeed(0);
        helper.assertTrue(seeded.getActualItemOutputs().size() == 1,
                "A seeded roll above 0.5 must omit Saltpeter while preserving the main output");
        helper.assertTrue(tfieRecipeCount(helper, "crusher/") == 24,
                "TFIE must load exactly 24 non-metal Crusher recipes");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void crusher_management_boundary(GameTestHelper helper)
    {
        Set<String> ores = Set.of("aluminum", "ardite", "coal", "cobalt", "copper", "diamond", "emerald",
                "fluorite", "gold", "iron", "lapis", "lead", "nickel", "osmium", "platinum", "quartz",
                "redstone", "silver", "tin", "tungsten", "uranium", "zinc");
        Set<String> metals = Set.of("aluminum", "ardite", "cobalt", "copper", "gold", "iron", "lead",
                "nickel", "osmium", "platinum", "silver", "tin", "tungsten", "uranium", "zinc");
        Set<String> removed = new HashSet<>();
        ores.forEach(metal -> removed.add("ore_" + metal));
        metals.forEach(metal -> {
            removed.add("raw_ore_" + metal);
            removed.add("raw_block_" + metal);
        });
        removed.add("nether_gold");
        removed.add("wool");
        helper.assertTrue(removed.size() == 54, "The independent Crusher removal table must contain 54 IDs");
        removed.forEach(path -> helper.assertTrue(
                optionalRecipe(helper, "immersiveengineering:crusher/" + path) == null,
                "IE Crusher route " + path + " must be disabled"));
        helper.assertTrue(optionalRecipe(helper, "immersiveengineering:crafting/gunpowder_from_dusts") == null,
                "IE dust Gunpowder crafting must be disabled");
        helper.assertTrue(optionalRecipe(helper, "tfie:crusher/wrought_iron") == null,
                "TFIE must not add a Wrought Iron Crusher route");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void chisel_and_heating_table(GameTestHelper helper)
    {
        for(String material : new String[]{"concrete", "concrete_brick", "concrete_leaded", "concrete_tile"})
        {
            ChiselRecipe slab = recipe(helper, "tfie:chisel/slab_" + material, ChiselRecipe.class);
            ItemStack slabOutput = slab.getItemOutput(ItemStack.EMPTY);
            helper.assertTrue(slab.matches(block("immersiveengineering:" + material).defaultBlockState(),
                            ChiselMode.SLAB.get()) && slab.getMode() == ChiselMode.SLAB.get()
                            && slab.getResultItem(helper.getLevel().registryAccess())
                            .is(item("immersiveengineering:slab_" + material).getItem())
                            && slabOutput.is(item("immersiveengineering:slab_" + material).getItem())
                            && slabOutput.getCount() == 1,
                    material + " slab mode must place one slab and return one additional slab");
            ChiselRecipe stair = recipe(helper, "tfie:chisel/stairs_" + material, ChiselRecipe.class);
            helper.assertTrue(stair.matches(block("immersiveengineering:" + material).defaultBlockState(),
                            ChiselMode.STAIR.get()) && stair.getMode() == ChiselMode.STAIR.get()
                            && stair.getResultItem(helper.getLevel().registryAccess())
                            .is(item("immersiveengineering:stairs_" + material).getItem())
                            && stair.getItemOutput(ItemStack.EMPTY).isEmpty(),
                    material + " stair mode must return no additional item");
        }
        assertHeating(helper, "clinker_brick", "minecraft:bricks", "immersiveengineering:clinker_brick", 1499);
        assertHeating(helper, "slag_glass", "immersiveengineering:slag", "immersiveengineering:slag_glass", 380);
        helper.assertTrue(HeatCapability.getDefinition(item("minecraft:bricks")).heatCapacity() == 1,
                "Bricks must have heat capacity 1.0");
        helper.assertTrue(HeatCapability.getDefinition(item("immersiveengineering:slag")).heatCapacity() == 0.6F,
                "Slag must have heat capacity 0.6");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void base_chemical_table(GameTestHelper helper)
    {
        InstantBarrelRecipe acid = recipe(helper, "tfie:barrel/redstone_acid", InstantBarrelRecipe.class);
        helper.assertTrue(acid.getInputItem().count() == 1 && acid.getInputItem().test(item("minecraft:redstone"))
                        && acid.getInputFluid().test(new FluidStack(fluid("minecraft:water"), 250))
                        && acid.getOutputFluid().is(fluid("immersiveengineering:redstone_acid"))
                        && acid.getOutputFluid().getAmount() == 250,
                "Redstone Acid must be an instant 1 dust + 250 mB Water barrel reaction");
        assertMixer(helper, "herbicide", 2, "immersiveengineering:ethanol", 500,
                "immersiveengineering:herbicide", 500, 3200,
                new String[]{"tfc:powder/sulfur"},
                new String[]{"tfc:powder/native_copper", "tfc:powder/malachite",
                        "tfc:powder/tetrahedrite"});
        MixerRecipe herbicide = recipe(helper, "tfie:mixer/herbicide", MixerRecipe.class);
        helper.assertTrue(herbicide.itemInputs.stream()
                        .noneMatch(input -> input.test(item("tfcm:metal/dust/copper"))),
                "Herbicide must not replace a copper ore powder with Copper Dust");
        assertMixer(helper, "limewater", 1, "minecraft:water", 500, "tfc:limewater", 500, 800,
                new String[]{"tfc:powder/flux", "tfc:powder/lime"});
        assertMixer(helper, "salt_water", 1, "minecraft:water", 125, "tfc:salt_water", 125, 800,
                new String[]{"tfc:powder/salt"});
        assertRefinery(helper, "tfie:refinery/brine", "tfc:vinegar", 1, "tfc:salt_water", 9,
                "tfc:brine", 10);
        boolean firmalife = ModList.get().isLoaded("firmalife");
        helper.assertTrue((optionalRecipe(helper, "tfie:refinery/milk_vinegar") == null) == firmalife,
                "Base Milk Vinegar must load only without FirmaLife");
        if(!firmalife)
            assertRefinery(helper, "tfie:refinery/milk_vinegar", "tfc:vinegar", 1, "minecraft:milk", 9,
                    "tfc:milk_vinegar", 10);
        helper.assertTrue(fluid("immersiveengineering:redstone_acid").is(TagKey.create(
                        net.minecraft.core.registries.Registries.FLUID, id("tfc:usable_in_barrel"))),
                "Redstone Acid must remain in a barrel after the instant reaction");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void firmalife_chemical_table(GameTestHelper helper)
    {
        boolean loaded = ModList.get().isLoaded("firmalife");
        for(String path : new String[]{"olive_oil_water", "tallow", "lye", "caramel"})
            helper.assertTrue((optionalRecipe(helper, "tfie:mixer/firmalife/" + path) != null) == loaded,
                    path + " must follow the FirmaLife load condition");
        helper.assertTrue((optionalRecipe(helper, "tfie:refinery/firmalife/milk_vinegar") != null) == loaded,
                "FirmaLife Milk Vinegar must follow the FirmaLife load condition");
        if(loaded)
        {
            assertMixer(helper, "firmalife/olive_oil_water", 1, "minecraft:water", 200,
                    "tfc:olive_oil_water", 200, 800, new String[]{"tfc:olive_paste"});
            assertMixer(helper, "firmalife/tallow", 1, "minecraft:water", 200, "tfc:tallow", 200, 800,
                    new String[]{"tfc:blubber"});
            assertMixer(helper, "firmalife/lye", 1, "minecraft:water", 200, "tfc:lye", 200, 800,
                    new String[]{"tfc:powder/wood_ash"});
            assertMixer(helper, "firmalife/caramel", 1, "firmalife:sugar_water", 1000,
                    "firmalife:caramel", 1000, 3200, new String[]{"firmalife:food/butter"});
            MixerRecipe caramel = recipe(helper, "tfie:mixer/firmalife/caramel", MixerRecipe.class);
            helper.assertTrue(caramel.itemInputs.getFirst().test(item("firmalife:food/butter")),
                    "Fresh Butter must match the Caramel recipe");
            helper.assertTrue(!caramel.itemInputs.getFirst().test(
                            FoodCapability.setRotten(item("firmalife:food/butter"))),
                    "Rotten Butter must not match the Caramel recipe");
            assertRefinery(helper, "tfie:refinery/firmalife/milk_vinegar", "tfc:vinegar", 1,
                    "firmalife:goat_milk", 9, "tfc:milk_vinegar", 10);
            helper.assertTrue(optionalRecipe(helper, "tfie:refinery/milk_vinegar") == null,
                    "Base and FirmaLife Milk Vinegar recipes must never load together");
        }
        helper.succeed();
    }

    private static void assertFermenter(GameTestHelper helper, FermenterRecipe recipe, String input,
                                         int amount, String label)
    {
        assertMachineValues(helper, recipe.getBaseEnergy(), recipe.getBaseTime(), 6400, 80,
                label + " Fermenter");
        ItemStack ingredient = item(input);
        helper.assertTrue(recipe.input.getCount() == 1 && recipe.input.test(ingredient),
                label + " must consume one ordinary item ingredient");
        helper.assertTrue(recipe.input.test(FoodCapability.setRotten(ingredient.copy())),
                label + " must continue to match after the food rots");
        helper.assertTrue(recipe.fluidOutput.is(fluid("immersiveengineering:ethanol"))
                        && recipe.fluidOutput.getAmount() == amount && recipe.itemOutput.get().isEmpty()
                        && recipe.getFluidInputs() == null && recipe.getMultipleProcessTicks() == 0,
                label + " must output only " + amount + " mB Ethanol");
    }

    private static void assertCrusher(GameTestHelper helper, String path, CrusherExpectation expected,
                                       boolean saltpeter)
    {
        CrusherRecipe recipe = recipe(helper, "tfie:crusher/" + path, CrusherRecipe.class);
        ItemStack output = recipe.output.get();
        helper.assertTrue(recipe.input.test(item(expected.input())) && output.is(item(expected.output()).getItem())
                        && output.getCount() == expected.count(), path + " has the wrong item conversion");
        if(path.equals("olive"))
            helper.assertTrue(recipe.input.test(FoodCapability.setRotten(item(expected.input()))),
                    "The Olive Crusher recipe must accept rotten Olives");
        assertMachineValues(helper, recipe.getBaseEnergy(), recipe.getBaseTime(), expected.energy(), 50,
                "Crusher " + path);
        helper.assertTrue(recipe.getMultipleProcessTicks() == 4,
                path + " must retain the Crusher additional progress limit of four");
        helper.assertTrue(recipe.secondaryOutputs.size() == (saltpeter ? 1 : 0),
                path + " has the wrong secondary output count");
        if(saltpeter)
        {
            var secondary = recipe.secondaryOutputs.getFirst();
            helper.assertTrue(secondary.stack().get().is(item("tfc:powder/saltpeter").getItem())
                            && secondary.stack().get().getCount() == 1 && secondary.chance() == 0.5F,
                    path + " must produce one Saltpeter at 50 percent");
        }
    }

    private static void assertHeating(GameTestHelper helper, String path, String input, String output,
                                      float temperature)
    {
        HeatingRecipe recipe = recipe(helper, "tfie:heating/" + path, HeatingRecipe.class);
        helper.assertTrue(recipe.matches(item(input)) && recipe.getResultItem(helper.getLevel().registryAccess())
                        .is(item(output).getItem()) && recipe.getTemperature() == temperature,
                path + " must use " + input + " at " + temperature + " degrees");
    }

    private static void assertMixer(GameTestHelper helper, String path, int itemCount, String inputFluid,
                                    int inputAmount, String outputFluid, int outputAmount, int energy,
                                    String[]... expectedItems)
    {
        MixerRecipe recipe = recipe(helper, "tfie:mixer/" + path, MixerRecipe.class);
        helper.assertTrue(recipe.itemInputs.size() == itemCount && itemCount == expectedItems.length
                        && recipe.fluidInput.test(new FluidStack(fluid(inputFluid), inputAmount)),
                path + " has the wrong Mixer inputs");
        for(int inputIndex = 0; inputIndex < expectedItems.length; inputIndex++)
            for(String expectedItem : expectedItems[inputIndex])
                helper.assertTrue(recipe.itemInputs.get(inputIndex).test(item(expectedItem)),
                        path + " input " + inputIndex + " must accept " + expectedItem);
        helper.assertTrue(recipe.fluidOutput.is(fluid(outputFluid)) && recipe.fluidOutput.getAmount() == outputAmount,
                path + " has the wrong Mixer output");
        assertMachineValues(helper, recipe.getBaseEnergy(), recipe.getBaseTime(), energy, outputAmount,
                "Mixer " + path);
        helper.assertTrue(recipe.getMultipleProcessTicks() == 7,
                path + " must retain the Mixer additional progress limit of seven");
    }

    private static void assertRefinery(GameTestHelper helper, String recipeId, String input0, int amount0,
                                       String input1, int amount1, String output, int outputAmount)
    {
        RefineryRecipe recipe = recipe(helper, recipeId, RefineryRecipe.class);
        FluidStack first = new FluidStack(fluid(input0), amount0);
        FluidStack second = new FluidStack(fluid(input1), amount1);
        helper.assertTrue((recipe.input0.test(first) && recipe.input1.test(second))
                        || (recipe.input0.test(second) && recipe.input1.test(first)),
                recipeId + " has the wrong Refinery inputs");
        helper.assertTrue(recipe.output.is(fluid(output)) && recipe.output.getAmount() == outputAmount,
                recipeId + " has the wrong Refinery output");
        assertMachineValues(helper, recipe.getBaseEnergy(), recipe.getBaseTime(), 50, 1, recipeId);
        helper.assertTrue(recipe.catalyst.isEmpty(), recipeId + " must not require a catalyst");
    }

    private static void assertMachineValues(GameTestHelper helper, int energy, int time, int expectedEnergy,
                                            int expectedTime, String label)
    {
        helper.assertTrue(energy == expectedEnergy && time == expectedTime,
                label + " used " + energy + " FE and " + time + " base time, expected " + expectedEnergy
                        + " FE and " + expectedTime);
    }

    private static long tfieRecipeCount(GameTestHelper helper, String prefix)
    {
        return helper.getLevel().getRecipeManager().getRecipeIds()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID) && id.getPath().startsWith(prefix))
                .count();
    }

    private static <T> T recipe(GameTestHelper helper, String recipeId, Class<T> type)
    {
        Object value = optionalRecipe(helper, recipeId);
        helper.assertTrue(type.isInstance(value), recipeId + " must be a " + type.getSimpleName());
        return type.cast(value);
    }

    private static Recipe<?> optionalRecipe(GameTestHelper helper, String recipeId)
    {
        return helper.getLevel().getRecipeManager().byKey(id(recipeId)).map(RecipeHolder::value).orElse(null);
    }

    private static ItemStack item(String value)
    {
        return BuiltInRegistries.ITEM.get(id(value)).getDefaultInstance();
    }

    private static ItemStack itemWithCount(String value, int count)
    {
        ItemStack stack = item(value);
        stack.setCount(count);
        return stack;
    }

    private static CokeOvenFixture cokeOven(GameTestHelper helper, int inputCount, ItemStack output,
                                             int creosoteAmount)
    {
        CokeOvenLogic logic = new CokeOvenLogic();
        TestInitialContext initial = new TestInitialContext(helper.getLevel());
        CokeOvenLogic.State state = logic.createInitialState(initial);
        state.getInventory().setStackInSlot(CokeOvenLogic.INPUT_SLOT,
                itemWithCount("tfc:ore/bituminous_coal", inputCount));
        state.getInventory().setStackInSlot(CokeOvenLogic.OUTPUT_SLOT, output);
        if(creosoteAmount > 0)
            state.getTank().fill(new FluidStack(fluid("immersiveengineering:creosote"), creosoteAmount),
                    IFluidHandler.FluidAction.EXECUTE);
        return new CokeOvenFixture(logic, state,
                new TestMultiblockContext(state, new TestMultiblockLevel(helper.getLevel())));
    }

    private static void assertCokeResult(GameTestHelper helper, CokeOvenFixture oven, int expectedInput,
                                         int expectedOutput, int expectedCreosote, String label)
    {
        helper.assertTrue(oven.input().getCount() == expectedInput,
                label + " left " + oven.input().getCount() + " input, expected " + expectedInput);
        helper.assertTrue(oven.output().is(item("immersiveengineering:coal_coke").getItem())
                        && oven.output().getCount() == expectedOutput,
                label + " produced " + oven.output().getCount() + " Coke, expected " + expectedOutput);
        helper.assertTrue(oven.state().getTank().getFluidAmount() == expectedCreosote,
                label + " produced " + oven.state().getTank().getFluidAmount()
                        + " mB Creosote, expected " + expectedCreosote);
    }

    private static net.minecraft.world.level.block.Block block(String value)
    {
        return BuiltInRegistries.BLOCK.get(id(value));
    }

    private static net.minecraft.world.level.material.Fluid fluid(String value)
    {
        return BuiltInRegistries.FLUID.get(id(value));
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }

    private record CrusherExpectation(String input, String output, int count, int energy)
    {
    }

    private record CokeOvenFixture(CokeOvenLogic logic, CokeOvenLogic.State state,
                                   TestMultiblockContext context)
    {
        private void start()
        {
            logic.tickServer(context);
        }

        private void complete()
        {
            start();
            finishStartedProcess();
        }

        private void finishStartedProcess()
        {
            state.set(CokeOvenLogic.State.BURN_TIME, 1);
            logic.tickServer(context);
            logic.tickServer(context);
        }

        private ItemStack input()
        {
            return state.getInventory().getStackInSlot(CokeOvenLogic.INPUT_SLOT);
        }

        private ItemStack output()
        {
            return state.getInventory().getStackInSlot(CokeOvenLogic.OUTPUT_SLOT);
        }
    }

    private record TestInitialContext(Level level) implements IInitialMultiblockContext<CokeOvenLogic.State>
    {
        @Override
        public <T> Supplier<T> getCapabilityAt(BlockCapability<T, Direction> capability,
                                                BlockPos position, RelativeBlockFace face)
        {
            return () -> null;
        }

        @Override
        public <T, C> Supplier<T> getCapabilityAt(BlockCapability<T, C> capability,
                                                   BlockPos position, C context)
        {
            return () -> null;
        }

        @Override
        public Supplier<Level> levelSupplier()
        {
            return () -> level;
        }

        @Override
        public Runnable getMarkDirtyRunnable()
        {
            return () -> {
            };
        }

        @Override
        public Runnable getSyncRunnable()
        {
            return () -> {
            };
        }

        @Override
        public Runnable getBlockUpdateRunnable()
        {
            return () -> {
            };
        }
    }

    private record TestMultiblockContext(CokeOvenLogic.State state,
                                         TestMultiblockLevel level)
            implements IMultiblockContext<CokeOvenLogic.State>
    {
        @Override
        public void markMasterDirty()
        {
        }

        @Override
        public CokeOvenLogic.State getState()
        {
            return state;
        }

        @Override
        public IMultiblockLevel getLevel()
        {
            return level;
        }

        @Override
        public BooleanSupplier isValid()
        {
            return () -> true;
        }

        @Override
        public void requestMasterBESync()
        {
        }

        @Override
        public void setComparatorOutputFor(BlockPos position, int value)
        {
        }

        @Override
        public int getRedstoneInputValue(BlockPos position, RelativeBlockFace side, int fallback)
        {
            return fallback;
        }

        @Override
        public int getRedstoneInputValue(BlockPos position, int fallback)
        {
            return fallback;
        }
    }

    private static final class TestMultiblockLevel implements IMultiblockLevel
    {
        private final Level level;
        private BlockState state;

        private TestMultiblockLevel(Level level)
        {
            this.level = level;
            state = block("immersiveengineering:coke_oven").defaultBlockState()
                    .setValue(NonMirrorableWithActiveBlock.ACTIVE, false);
        }

        @Override
        public BlockState getBlockState(BlockPos position)
        {
            return state;
        }

        @Override
        public void setBlock(BlockPos position, BlockState newState)
        {
            if(newState.hasProperty(NonMirrorableWithActiveBlock.ACTIVE))
                state = newState;
        }

        @Override
        public BlockEntity getBlockEntity(BlockPos position)
        {
            return null;
        }

        @Override
        public BlockEntity forciblyGetBlockEntity(BlockPos position)
        {
            return null;
        }

        @Override
        public boolean shouldTickModulo(int interval)
        {
            return true;
        }

        @Override
        public BlockPos getAbsoluteOrigin()
        {
            return BlockPos.ZERO;
        }

        @Override
        public MultiblockOrientation getOrientation()
        {
            return null;
        }

        @Override
        public BlockPos toAbsolute(BlockPos relative)
        {
            return relative;
        }

        @Override
        public Direction toAbsolute(RelativeBlockFace relative)
        {
            return null;
        }

        @Override
        public AABB toAbsolute(AABB relative)
        {
            return relative;
        }

        @Override
        public Vec3 toAbsolute(Vec3 relative)
        {
            return relative;
        }

        @Override
        public BlockPos toRelative(BlockPos absolute)
        {
            return absolute;
        }

        @Override
        public RelativeBlockFace toRelative(Direction absolute)
        {
            return null;
        }

        @Override
        public boolean isThundering()
        {
            return level.isThundering();
        }

        @Override
        public boolean isRaining()
        {
            return level.isRaining();
        }

        @Override
        public int getMaxBuildHeight()
        {
            return level.getMaxBuildHeight();
        }

        @Override
        public Level getRawLevel()
        {
            return level;
        }

        @Override
        public void updateNeighbourForOutputSignal(BlockPos position)
        {
        }
    }
}
