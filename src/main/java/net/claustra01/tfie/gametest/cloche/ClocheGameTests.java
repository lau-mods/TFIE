package net.claustra01.tfie.gametest.cloche;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.util.ModClocheRenderFunctions;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ClocheGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final List<String> SOILS = List.of(
            "entisol", "aridisol", "oxisol", "fluvisol",
            "andisol", "podzol", "alfisol", "mollisol");
    private static final Map<String, ExpectedCrop> CROPS = expectedCrops();

    private ClocheGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void cloche_recipe_set_and_harvests(GameTestHelper helper)
    {
        int found = 0;
        for(Map.Entry<String, ExpectedCrop> expected : CROPS.entrySet())
        {
            ClocheRecipe recipe = recipe(helper, expected.getKey());
            ItemStack seed = item("tfc:seeds/" + expected.getKey()).getDefaultInstance();
            helper.assertTrue(recipe.seed.test(seed), expected.getKey() + " must use its TFC seed");
            helper.assertTrue(recipe.time == 128000, expected.getKey() + " must target 128000 growth");
            helper.assertTrue(recipe.outputs.size() == 2,
                    expected.getKey() + " must have a main output and one seed chance");
            assertOutput(helper, recipe.outputs.getFirst(), expected.getValue().item(),
                    expected.getValue().count(), 1.0F, expected.getKey() + " main output");
            assertOutput(helper, recipe.outputs.get(1), "tfc:seeds/" + expected.getKey(),
                    1, 0.25F, expected.getKey() + " seed output");
            for(String soil : SOILS)
            {
                ItemStack dirt = item("tfc:dirt/" + soil).getDefaultInstance();
                helper.assertTrue(recipe.matches(seed, dirt, new FluidStack(Fluids.WATER, 1)),
                        expected.getKey() + " must accept " + soil + " dirt with water");
            }

            String harvestedSoil = SOILS.get(found % SOILS.size());
            ClocheBlockEntity cloche = preparedCloche(
                    helper, expected.getKey(), harvestedSoil, 4000, 16000);
            cloche.tickServer();
            setGrowth(cloche, 128000);
            int energy = cloche.energyStorage.getEnergyStored();
            ItemStack soil = cloche.getInventory().get(ClocheBlockEntity.SLOT_SOIL).copy();
            ItemStack plantedSeed = cloche.getInventory().get(ClocheBlockEntity.SLOT_SEED).copy();
            cloche.tickServer();
            helper.assertTrue(cloche.getInventory().subList(3, 7).stream()
                            .anyMatch(stack -> stack.is(item(expected.getValue().item()))
                                    && stack.getCount() == expected.getValue().count()),
                    expected.getKey() + " must harvest its specified main output");
            helper.assertTrue(ItemStack.isSameItemSameComponents(
                            cloche.getInventory().get(ClocheBlockEntity.SLOT_SOIL), soil)
                            && ItemStack.isSameItemSameComponents(
                            cloche.getInventory().get(ClocheBlockEntity.SLOT_SEED), plantedSeed),
                    expected.getKey() + " must not consume its dirt or seed");
            helper.assertTrue(cloche.energyStorage.getEnergyStored() == energy - 8,
                    expected.getKey() + " harvest confirmation must consume eight FE");
            found++;
        }
        long actual = helper.getLevel().getRecipeManager().getRecipes().stream()
                .filter(holder -> holder.id().getNamespace().equals(TFIE.MOD_ID))
                .filter(holder -> holder.id().getPath().startsWith("cloche/"))
                .count();
        helper.assertTrue(found == 29 && actual == 29, "Exactly 29 TFIE cloche recipes must load");

        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID,
            batch = "tfie_cloche_config")
    public static void cloche_tick_energy_growth_and_capacity(GameTestHelper helper)
    {
        ClocheBlockEntity cloche = preparedCloche(helper, "wheat", 250, 16000);
        helper.assertTrue(cloche.energyStorage.getMaxEnergyStored() == 16000,
                "Cloche energy capacity must remain 16000 FE");
        helper.assertTrue(cloche.tank.getCapacity() == 4000,
                "Cloche fluid capacity must remain 4000 mB");
        helper.assertTrue(cloche.getInventory().size() == 7,
                "Cloche must retain soil, seed, fertilizer, and four output slots");
        helper.assertTrue(cloche.getSlotLimit(0) == 1 && cloche.getSlotLimit(1) == 1,
                "Soil and seed slots must retain their single-item limits");

        cloche.tickServer();
        helper.assertTrue(cloche.fertilizerAmount == 6000,
                "The first water dose must provide 6000 operating ticks");
        int energy = cloche.energyStorage.getEnergyStored();
        cloche.tickServer();
        float standardGrowth = cloche.getGuiProgress() * 128000;
        helper.assertTrue(Math.abs(standardGrowth - 1.0F) < 0.001F,
                "One standard growth tick must add one growth point, got " + standardGrowth);
        helper.assertTrue(cloche.energyStorage.getEnergyStored() == energy - 8,
                "One standard growth tick must consume eight FE");

        double oldGrowthModifier = IEServerConfig.MACHINES.cloche_growth_mod.get();
        try
        {
            IEServerConfig.MACHINES.cloche_growth_mod.set(2.0);
            setGrowth(cloche, 0);
            cloche.fertilizerMod = 1.3F;
            energy = cloche.energyStorage.getEnergyStored();
            cloche.tickServer();
            helper.assertTrue(Math.abs(cloche.getGuiProgress() * 128000 - 2.6F) < 0.001F,
                    "IE growth and fertilizer multipliers must multiply tick growth");
            helper.assertTrue(cloche.energyStorage.getEnergyStored() == energy - 8,
                    "Growth multipliers must not turn 128000 into a fixed FE amount");
        }
        finally
        {
            IEServerConfig.MACHINES.cloche_growth_mod.set(oldGrowthModifier);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void cloche_water_and_fertilizer_supply(GameTestHelper helper)
    {
        ClocheBlockEntity cloche = preparedCloche(helper, "carrot", 250, 16000);
        cloche.getInventory().set(ClocheBlockEntity.SLOT_FERTILIZER,
                item("tfc:groundcover/guano").getDefaultInstance());
        cloche.tickServer();
        helper.assertTrue(cloche.tank.isEmpty(), "One supply must drain exactly 250 mB water");
        helper.assertTrue(cloche.fertilizerAmount == 6000 && cloche.fertilizerMod == 1.3F,
                "Guano must be consumed when a 6000-tick dose is supplied");
        helper.assertTrue(cloche.getInventory().get(ClocheBlockEntity.SLOT_FERTILIZER).isEmpty(),
                "A valid supply must consume one fertilizer item");
        for(int tick = 0; tick < 6000; tick++)
        {
            if(cloche.energyStorage.getEnergyStored() < 8)
                cloche.energyStorage.setStoredEnergy(16000);
            cloche.tickServer();
        }
        helper.assertTrue(cloche.fertilizerAmount == 0,
                "A dose must last exactly 6000 operating ticks, got " + cloche.fertilizerAmount);
        helper.assertTrue(cloche.tank.isEmpty(),
                "Operating ticks must not drain additional fluid before the next supply");

        ClocheBlockEntity plain = preparedCloche(helper, "carrot", 250, 16000);
        plain.tickServer();
        helper.assertTrue(plain.fertilizerAmount == 6000 && plain.fertilizerMod == 1.0F,
                "A cloche without fertilizer must still receive a 1.0 multiplier dose");

        ClocheRecipe recipe = recipe(helper, "carrot");
        for(String rejected : List.of("tfc:salt_water", "tfc:spring_water", "tfc:alcohol"))
        {
            Fluid fluid = BuiltInRegistries.FLUID.get(id(rejected));
            helper.assertTrue(!recipe.requiredFluid.test(new FluidStack(fluid, 250)),
                    rejected + " must not be accepted as cloche water");
        }
        helper.assertTrue(recipe.requiredFluid.test(new FluidStack(Fluids.WATER, 250)),
                "minecraft:water must be accepted");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void cloche_resets_and_waits_for_output(GameTestHelper helper)
    {
        ClocheBlockEntity cloche = preparedCloche(helper, "barley", 250, 16000);
        cloche.tickServer();
        setGrowth(cloche, 20);
        cloche.energyStorage.setStoredEnergy(0);
        cloche.tickServer();
        helper.assertTrue(cloche.getGuiProgress() == 0,
                "Losing the required FE must reset growth");

        cloche.energyStorage.setStoredEnergy(16000);
        setGrowth(cloche, 20);
        cloche.getInventory().set(ClocheBlockEntity.SLOT_SEED, Blocks.STONE.asItem().getDefaultInstance());
        cloche.tickServer();
        helper.assertTrue(cloche.getGuiProgress() == 0,
                "Losing a valid seed and soil combination must reset growth");

        cloche.getInventory().set(ClocheBlockEntity.SLOT_SEED,
                item("tfc:seeds/barley").getDefaultInstance());
        cloche.cachedRecipe.get();
        cloche.fertilizerAmount = 100;
        setGrowth(cloche, 128000);
        for(int slot = 3; slot < 7; slot++)
            cloche.getInventory().set(slot, new ItemStack(Blocks.STONE, 64));
        int energy = cloche.energyStorage.getEnergyStored();
        int fertilizer = cloche.fertilizerAmount;
        cloche.tickServer();
        helper.assertTrue(cloche.getGuiProgress() == 1.0F,
                "A full output must wait at completed growth, got " + cloche.getGuiProgress());
        helper.assertTrue(cloche.energyStorage.getEnergyStored() == energy
                        && cloche.fertilizerAmount == fertilizer,
                "Output waiting must not consume growth FE or nutrient time");
        for(int slot = 3; slot < 7; slot++)
            cloche.getInventory().set(slot, ItemStack.EMPTY);
        cloche.tickServer();
        helper.assertTrue(cloche.getGuiProgress() == 0
                        && cloche.energyStorage.getEnergyStored() == energy - 8
                        && cloche.fertilizerAmount == fertilizer - 1,
                "A harvest must confirm once all outputs fit");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void double_crop_render_states(GameTestHelper helper)
    {
        Map<String, Integer> doubleCrops = Map.of(
                "green_bean", 4, "jute", 3, "maize", 3,
                "papyrus", 3, "sugarcane", 4, "tomato", 4);
        int normal = 0;
        for(String crop : CROPS.keySet())
        {
            ClocheRenderFunction render = recipe(helper, crop).renderFunction;
            if(!doubleCrops.containsKey(crop))
            {
                helper.assertTrue(!(render instanceof ModClocheRenderFunctions.DoubleCrop),
                        crop + " must use IE's normal crop render function");
                normal++;
                continue;
            }
            helper.assertTrue(render instanceof ModClocheRenderFunctions.DoubleCrop,
                    crop + " must use tfie:doublecroptfc");
            ModClocheRenderFunctions.DoubleCrop function =
                    (ModClocheRenderFunctions.DoubleCrop)render;
            int boundary = doubleCrops.get(crop);
            int maxAge = function.maxAge();
            ItemStack seed = item("tfc:seeds/" + crop).getDefaultInstance();
            helper.assertTrue(function.doublingAge() == boundary
                            && function.getScale(seed, 1) == 0.6875F,
                    crop + " must use its boundary and the 0.6875 scale");

            float beforeGrowth = (boundary - 0.51F) / maxAge;
            Collection<Pair<BlockState, Transformation>> before =
                    function.getBlocks(seed, beforeGrowth);
            helper.assertTrue(before.size() == 1,
                    crop + " must only render the bottom before its boundary");
            helper.assertTrue(age(before.iterator().next().getFirst()) == boundary - 1,
                    crop + " must use round(maxAge * growth) before the boundary");

            Collection<Pair<BlockState, Transformation>> at =
                    function.getBlocks(seed, (float)boundary / maxAge);
            helper.assertTrue(at.size() == 2,
                    crop + " must render two parts at its boundary");
            List<Pair<BlockState, Transformation>> parts = List.copyOf(at);
            helper.assertTrue(age(parts.get(0).getFirst()) == boundary
                            && age(parts.get(1).getFirst()) == boundary,
                    crop + " top and bottom must use the same age");
            helper.assertTrue(parts.get(1).getFirst().getValue(DoubleCropBlock.PART)
                            == DoubleCropBlock.Part.TOP,
                    crop + " upper state must use TOP");
            helper.assertTrue(parts.get(1).getSecond().getTranslation().y() == 1.0F,
                    crop + " upper state must be translated one block upward");

            Collection<Pair<BlockState, Transformation>> mature = function.getBlocks(seed, 2.0F);
            helper.assertTrue(age(List.copyOf(mature).getFirst().getFirst()) == maxAge,
                    crop + " rendered age must clamp to maxAge");
        }
        helper.assertTrue(normal == 23, "The remaining 23 crops must use normal rendering");
        helper.assertTrue(ModClocheRenderFunctions.DoubleCrop.class.getName()
                        .equals("net.claustra01.tfie.util.ModClocheRenderFunctions$DoubleCrop"),
                "The common render-state API must load without a client renderer");
        for(String soil : SOILS)
            helper.assertTrue(ClocheRecipe.getSoilTexture(item("tfc:dirt/" + soil).getDefaultInstance())
                            .equals(id("tfc:block/farmland/" + soil)),
                    soil + " dirt must display its farmland texture");
        helper.succeed();
    }

    private static ClocheBlockEntity preparedCloche(GameTestHelper helper, String crop,
                                                     int water, int energy)
    {
        return preparedCloche(helper, crop, "entisol", water, energy);
    }

    private static ClocheBlockEntity preparedCloche(GameTestHelper helper, String crop,
                                                     String soil, int water, int energy)
    {
        BlockPos pos = new BlockPos(1, 2, 1);
        helper.setBlock(pos, Blocks.AIR);
        helper.setBlock(pos, IEBlocks.MetalDevices.CLOCHE.get());
        ClocheBlockEntity cloche = helper.getBlockEntity(pos);
        NonNullList<ItemStack> inventory = cloche.getInventory();
        inventory.set(ClocheBlockEntity.SLOT_SOIL, item("tfc:dirt/" + soil).getDefaultInstance());
        inventory.set(ClocheBlockEntity.SLOT_SEED, item("tfc:seeds/" + crop).getDefaultInstance());
        cloche.tank.fill(new FluidStack(Fluids.WATER, water), FluidAction.EXECUTE);
        cloche.energyStorage.setStoredEnergy(energy);
        return cloche;
    }

    private static ClocheRecipe recipe(GameTestHelper helper, String crop)
    {
        RecipeHolder<?> holder = helper.getLevel().getRecipeManager()
                .byKey(id("tfie:cloche/" + crop)).orElseThrow();
        helper.assertTrue(holder.value() instanceof ClocheRecipe,
                crop + " must use immersiveengineering:cloche");
        return (ClocheRecipe)holder.value();
    }

    private static void setGrowth(ClocheBlockEntity cloche, float growth)
    {
        CompoundTag tag = new CompoundTag();
        cloche.writeCustomNBT(tag, false, cloche.getLevel().registryAccess());
        tag.putFloat("growth", growth);
        cloche.readCustomNBT(tag, false, cloche.getLevel().registryAccess());
    }

    private static void assertOutput(GameTestHelper helper, StackWithChance output,
                                     String expectedItem, int expectedCount, float expectedChance,
                                     String description)
    {
        ItemStack stack = output.stack().get();
        helper.assertTrue(stack.is(item(expectedItem)) && stack.getCount() == expectedCount
                        && output.chance() == expectedChance,
                description + " must match item, count, and chance");
    }

    private static int age(BlockState state)
    {
        for(IntegerProperty property : state.getProperties().stream()
                .filter(IntegerProperty.class::isInstance).map(IntegerProperty.class::cast).toList())
            if(property.getName().equals("age"))
                return state.getValue(property);
        throw new IllegalStateException("Rendered crop has no age property");
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }

    private static Map<String, ExpectedCrop> expectedCrops()
    {
        Map<String, ExpectedCrop> crops = new LinkedHashMap<>();
        crops.put("alfalfa", new ExpectedCrop("tfc:alfalfa", 6));
        crops.put("barley", new ExpectedCrop("tfc:food/barley", 6));
        crops.put("beet", new ExpectedCrop("tfc:food/beet", 6));
        crops.put("cabbage", new ExpectedCrop("tfc:food/cabbage", 6));
        crops.put("canola", new ExpectedCrop("tfc:canola", 6));
        crops.put("carrot", new ExpectedCrop("tfc:food/carrot", 6));
        crops.put("cassava", new ExpectedCrop("tfc:food/cassava", 6));
        crops.put("garlic", new ExpectedCrop("tfc:food/garlic", 6));
        crops.put("green_bean", new ExpectedCrop("tfc:food/green_bean", 6));
        crops.put("jute", new ExpectedCrop("tfc:jute", 6));
        crops.put("lentil", new ExpectedCrop("tfc:food/lentil", 6));
        crops.put("maize", new ExpectedCrop("tfc:food/maize", 6));
        crops.put("melon", new ExpectedCrop("tfc:melon", 1));
        crops.put("oat", new ExpectedCrop("tfc:food/oat", 6));
        crops.put("onion", new ExpectedCrop("tfc:food/onion", 6));
        crops.put("papyrus", new ExpectedCrop("tfc:papyrus", 6));
        crops.put("peanut", new ExpectedCrop("tfc:food/peanut", 6));
        crops.put("potato", new ExpectedCrop("tfc:food/potato", 6));
        crops.put("pumpkin", new ExpectedCrop("tfc:pumpkin", 1));
        crops.put("radish", new ExpectedCrop("tfc:food/radish", 6));
        crops.put("red_bell_pepper", new ExpectedCrop("tfc:food/red_bell_pepper", 4));
        crops.put("rice", new ExpectedCrop("tfc:food/rice", 6));
        crops.put("rye", new ExpectedCrop("tfc:food/rye", 6));
        crops.put("soybean", new ExpectedCrop("tfc:food/soybean", 6));
        crops.put("squash", new ExpectedCrop("tfc:food/squash", 6));
        crops.put("sugarcane", new ExpectedCrop("tfc:food/sugarcane", 6));
        crops.put("tomato", new ExpectedCrop("tfc:food/tomato", 6));
        crops.put("wheat", new ExpectedCrop("tfc:food/wheat", 6));
        crops.put("yellow_bell_pepper", new ExpectedCrop("tfc:food/yellow_bell_pepper", 4));
        return Map.copyOf(crops);
    }

    private record ExpectedCrop(String item, int count)
    {
    }
}
