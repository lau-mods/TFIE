package net.claustra01.tfie.gametest.fertilizer;

import blusunrize.immersiveengineering.api.crafting.ClocheFertilizer;
import blusunrize.immersiveengineering.common.blocks.metal.ClocheBlockEntity;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.blocks.HempCropBlock;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity.NutrientType;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.dries007.tfc.common.blocks.crop.CropHelpers;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.util.data.Fertilizer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FertilizerGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private FertilizerGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void farmland_fertilizer_values(GameTestHelper helper)
    {
        assertFertilizer(helper, "tfie:dust_saltpeter", "immersiveengineering:dust_saltpeter",
                0.2F, 0.0F, 0.0F);
        assertFertilizer(helper, "tfie:fertilizer", "immersiveengineering:fertilizer",
                0.1F, 0.2F, 0.5F);
        assertFertilizer(helper, "tfie:slag", "immersiveengineering:slag",
                0.0F, 0.2F, 0.1F);

        int index = 0;
        for(String fertilizerId : new String[]{"immersiveengineering:dust_saltpeter",
                "immersiveengineering:fertilizer", "immersiveengineering:slag"})
        {
            BlockPos relative = new BlockPos(index++, 2, 0);
            IFarmland farmland = placeFarmland(helper, relative, "entisol");
            Player player = helper.makeMockPlayer(GameType.SURVIVAL);
            ItemStack stack = new ItemStack(item(fertilizerId), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            boolean used = CropHelpers.useFertilizer(helper.getLevel(), player,
                    InteractionHand.MAIN_HAND, helper.absolutePos(relative));
            Fertilizer fertilizer = Fertilizer.get(new ItemStack(item(fertilizerId)));
            helper.assertTrue(used && stack.getCount() == 1,
                    fertilizerId + " must apply to farmland and consume one item");
            assertNutrients(helper, farmland, fertilizer.nitrogen(), fertilizer.phosphorus(),
                    fertilizer.potassium(), fertilizerId + " normal-soil nutrients");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void farmland_modifiers_shift_and_consumption(GameTestHelper helper)
    {
        Map<String, Float> modifiers = new LinkedHashMap<>();
        modifiers.put("mollisol", 1.2F);
        modifiers.put("andisol", 1.1F);
        modifiers.put("entisol", 1.0F);
        modifiers.put("podzol", 0.9F);
        modifiers.put("oxisol", 0.8F);
        int index = 0;
        for(Map.Entry<String, Float> entry : modifiers.entrySet())
        {
            BlockPos relative = new BlockPos(index++, 2, 1);
            IFarmland farmland = placeFarmland(helper, relative, entry.getKey());
            Player player = helper.makeMockPlayer(GameType.SURVIVAL);
            ItemStack stack = new ItemStack(item("immersiveengineering:fertilizer"), 2);
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            helper.assertTrue(CropHelpers.useFertilizer(helper.getLevel(), player,
                            InteractionHand.MAIN_HAND, helper.absolutePos(relative)),
                    entry.getKey() + " farmland must accept fertilizer");
            assertNutrients(helper, farmland, 0.1F * entry.getValue(),
                    0.2F * entry.getValue(), 0.5F * entry.getValue(),
                    entry.getKey() + " soil modifier");
            helper.assertTrue(stack.getCount() == 1,
                    "Normal use must consume one item on " + entry.getKey());
        }

        BlockPos shiftPos = new BlockPos(0, 2, 2);
        IFarmland shiftFarmland = placeFarmland(helper, shiftPos, "entisol");
        Player shiftPlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        shiftPlayer.setShiftKeyDown(true);
        ItemStack saltpeter = new ItemStack(item("immersiveengineering:dust_saltpeter"), 10);
        shiftPlayer.setItemInHand(InteractionHand.MAIN_HAND, saltpeter);
        helper.assertTrue(CropHelpers.useFertilizer(helper.getLevel(), shiftPlayer,
                        InteractionHand.MAIN_HAND, helper.absolutePos(shiftPos)),
                "Shift-use must fill a target nutrient");
        helper.assertTrue(saltpeter.getCount() == 5
                        && shiftFarmland.getNutrient(NutrientType.NITROGEN) == 1.0F,
                "Shift-use must consume the five 0.2 doses needed to fill nitrogen");
        helper.assertTrue(!CropHelpers.useFertilizer(helper.getLevel(), shiftPlayer,
                        InteractionHand.MAIN_HAND, helper.absolutePos(shiftPos))
                        && saltpeter.getCount() == 5,
                "A full target nutrient must reject fertilizer without consuming it");

        BlockPos creativePos = new BlockPos(1, 2, 2);
        IFarmland creativeFarmland = placeFarmland(helper, creativePos, "entisol");
        Player creative = helper.makeMockPlayer(GameType.CREATIVE);
        ItemStack creativeStack = new ItemStack(item("immersiveengineering:fertilizer"), 3);
        creative.setItemInHand(InteractionHand.MAIN_HAND, creativeStack);
        helper.assertTrue(CropHelpers.useFertilizer(helper.getLevel(), creative,
                        InteractionHand.MAIN_HAND, helper.absolutePos(creativePos)),
                "Creative use must still apply nutrients");
        helper.assertTrue(creativeStack.getCount() == 3,
                "Creative use must not consume fertilizer");
        helper.assertTrue(creativeFarmland.getNutrient(NutrientType.NITROGEN) <= 1.0F
                        && creativeFarmland.getNutrient(NutrientType.PHOSPHOROUS) <= 1.0F
                        && creativeFarmland.getNutrient(NutrientType.POTASSIUM) <= 1.0F,
                "Fertilizer must keep every nutrient at or below the gauge maximum");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void cloche_fertilizer_modifiers(GameTestHelper helper)
    {
        Map<String, Float> expected = Map.of(
                "compost", 1.2F,
                "guano", 1.3F,
                "pure_nitrogen", 1.2F,
                "pure_phosphorus", 1.2F,
                "pure_potassium", 1.2F,
                "shellfish", 1.1F,
                "sylvite", 1.2F,
                "wood_ash", 1.1F);
        Map<String, String> items = Map.of(
                "compost", "tfc:compost",
                "guano", "tfc:groundcover/guano",
                "pure_nitrogen", "tfc:pure_nitrogen",
                "pure_phosphorus", "tfc:pure_phosphorus",
                "pure_potassium", "tfc:pure_potassium",
                "shellfish", "tfc:food/shellfish",
                "sylvite", "tfc:powder/sylvite",
                "wood_ash", "tfc:powder/wood_ash");
        for(Map.Entry<String, Float> entry : expected.entrySet())
        {
            Object value = helper.getLevel().getRecipeManager()
                    .byKey(id("tfie:fertilizer/" + entry.getKey())).orElseThrow().value();
            helper.assertTrue(value instanceof ClocheFertilizer,
                    entry.getKey() + " must use immersiveengineering:fertilizer");
            ClocheFertilizer recipe = (ClocheFertilizer)value;
            ItemStack stack = item(items.get(entry.getKey())).getDefaultInstance();
            helper.assertTrue(recipe.input.test(stack)
                            && recipe.growthModifier == entry.getValue()
                            && ClocheFertilizer.getFertilizerGrowthModifier(
                            helper.getLevel(), stack) == entry.getValue(),
                    entry.getKey() + " must match by item and use its growth multiplier");

            ClocheBlockEntity cloche = preparedCloche(helper, stack.copy());
            cloche.tickServer();
            helper.assertTrue(cloche.fertilizerAmount == 6000
                            && cloche.fertilizerMod == entry.getValue()
                            && cloche.getInventory().get(ClocheBlockEntity.SLOT_FERTILIZER).isEmpty(),
                    entry.getKey() + " must be consumed when the water dose is supplied");
            int energy = cloche.energyStorage.getEnergyStored();
            cloche.tickServer();
            helper.assertTrue(Math.abs(cloche.getGuiProgress() * 128000 - entry.getValue()) < 0.001F
                            && cloche.energyStorage.getEnergyStored() == energy - 8,
                    entry.getKey() + " must multiply growth without changing per-tick FE");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void shellfish_rot_and_hemp_top_fertilizing(GameTestHelper helper)
    {
        ItemStack fresh = item("tfc:food/shellfish").getDefaultInstance();
        ItemStack rotten = FoodCapability.setRotten(fresh.copy());
        helper.assertTrue(FoodCapability.isRotten(rotten),
                "The shellfish test must create a rotten stack");
        helper.assertTrue(ClocheFertilizer.getFertilizerGrowthModifier(helper.getLevel(), fresh) == 1.1F
                        && ClocheFertilizer.getFertilizerGrowthModifier(helper.getLevel(), rotten) == 1.1F,
                "Shellfish must match by item ID before and after rotting");
        ClocheBlockEntity cloche = preparedCloche(helper, rotten.copy());
        cloche.tickServer();
        cloche.tickServer();
        helper.assertTrue(cloche.fertilizerMod == 1.1F
                        && cloche.getInventory().get(ClocheBlockEntity.SLOT_FERTILIZER).isEmpty()
                        && Math.abs(cloche.getGuiProgress() * 128000 - 1.1F) < 0.001F,
                "Rotten shellfish must be consumed and apply 1.1 growth in a cloche");

        BlockPos farmlandPos = new BlockPos(0, 2, 3);
        IFarmland farmland = placeFarmland(helper, farmlandPos, "entisol");
        BlockPos bottomPos = farmlandPos.above();
        BlockPos topPos = bottomPos.above();
        HempCropBlock hemp = ModBlocks.HEMP_CROP.get();
        BlockState bottom = hemp.defaultBlockState()
                .setValue(hemp.getAgeProperty(), 4)
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
        BlockState top = bottom.setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP);
        helper.setBlock(bottomPos, bottom);
        helper.setBlock(topPos, top);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack ieFertilizer = item("immersiveengineering:fertilizer").getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, ieFertilizer);
        BlockPos absoluteTop = helper.absolutePos(topPos);
        ItemInteractionResult result = top.useItemOn(ieFertilizer, helper.getLevel(), player,
                InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(absoluteTop),
                Direction.UP, absoluteTop, false));
        helper.assertTrue(result.consumesAction() && ieFertilizer.isEmpty(),
                "Using IE fertilizer on hemp's top must fertilize and consume one item");
        assertNutrients(helper, farmland, 0.1F, 0.2F, 0.5F,
                "hemp top-part farmland proxy");
        helper.succeed();
    }

    private static ClocheBlockEntity preparedCloche(GameTestHelper helper, ItemStack fertilizer)
    {
        BlockPos relative = new BlockPos(4, 2, 3);
        helper.setBlock(relative, Blocks.AIR);
        helper.setBlock(relative, IEBlocks.MetalDevices.CLOCHE.get());
        ClocheBlockEntity cloche = helper.getBlockEntity(relative);
        cloche.getInventory().set(ClocheBlockEntity.SLOT_SOIL,
                item("tfc:dirt/entisol").getDefaultInstance());
        cloche.getInventory().set(ClocheBlockEntity.SLOT_SEED,
                item("tfc:seeds/wheat").getDefaultInstance());
        cloche.getInventory().set(ClocheBlockEntity.SLOT_FERTILIZER, fertilizer);
        cloche.tank.fill(new FluidStack(Fluids.WATER, 250), FluidAction.EXECUTE);
        cloche.energyStorage.setStoredEnergy(16000);
        return cloche;
    }

    private static IFarmland placeFarmland(GameTestHelper helper, BlockPos relative, String soil)
    {
        helper.setBlock(relative, BuiltInRegistries.BLOCK.get(id("tfc:farmland/" + soil)));
        Object blockEntity = helper.getLevel().getBlockEntity(helper.absolutePos(relative));
        helper.assertTrue(blockEntity instanceof IFarmland,
                soil + " farmland must provide its block entity");
        IFarmland farmland = (IFarmland)blockEntity;
        farmland.setNutrient(NutrientType.NITROGEN, 0);
        farmland.setNutrient(NutrientType.PHOSPHOROUS, 0);
        farmland.setNutrient(NutrientType.POTASSIUM, 0);
        return farmland;
    }

    private static void assertFertilizer(GameTestHelper helper, String dataId, String itemId,
                                         float nitrogen, float phosphorus, float potassium)
    {
        Fertilizer fertilizer = Fertilizer.MANAGER.get(id(dataId));
        helper.assertTrue(fertilizer != null, dataId + " must be loaded as TFC fertilizer data");
        helper.assertTrue(fertilizer.ingredient().test(new ItemStack(item(itemId)))
                        && fertilizer.nitrogen() == nitrogen
                        && fertilizer.phosphorus() == phosphorus
                        && fertilizer.potassium() == potassium,
                dataId + " must target " + itemId + " with the specified N/P/K values");
    }

    private static void assertNutrients(GameTestHelper helper, IFarmland farmland,
                                        float nitrogen, float phosphorus, float potassium,
                                        String description)
    {
        helper.assertTrue(Math.abs(farmland.getNutrient(NutrientType.NITROGEN) - nitrogen) < 1.0E-5F
                        && Math.abs(farmland.getNutrient(NutrientType.PHOSPHOROUS) - phosphorus) < 1.0E-5F
                        && Math.abs(farmland.getNutrient(NutrientType.POTASSIUM) - potassium) < 1.0E-5F,
                description + " must match all three expected nutrients");
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }
}
