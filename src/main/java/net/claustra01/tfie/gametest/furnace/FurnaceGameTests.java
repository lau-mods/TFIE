package net.claustra01.tfie.gametest.furnace;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceFuel;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.SawmillRecipe;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IInitialMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockContext;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockLevel;
import blusunrize.immersiveengineering.api.multiblocks.blocks.env.IMultiblockBEHelperMaster;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockState;
import blusunrize.immersiveengineering.api.multiblocks.blocks.logic.IMultiblockBE;
import blusunrize.immersiveengineering.api.multiblocks.TemplateMultiblock;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.MultiblockOrientation;
import blusunrize.immersiveengineering.api.multiblocks.blocks.util.RelativeBlockFace;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AdvBlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.AlloySmelterLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.BlastFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.CrusherLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.FurnaceHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.NonMirrorableWithActiveBlock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.MetalPressLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.sawmill.SawmillLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceInputHandler;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceLogic;
import blusunrize.immersiveengineering.common.blocks.multiblocks.logic.arcfurnace.ArcFurnaceProcess;
import blusunrize.immersiveengineering.common.blocks.metal.BlastFurnacePreheaterBlockEntity;
import blusunrize.immersiveengineering.common.blocks.multiblocks.AlloySmelterMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.ArcFurnaceMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.BlastFurnaceMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.CrusherMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.ImprovedBlastfurnaceMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.MetalPressMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.SawmillMultiblock;
import blusunrize.immersiveengineering.common.gui.ArcFurnaceMenu;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import blusunrize.immersiveengineering.common.register.IEBlockEntities;
import com.mojang.authlib.GameProfile;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeSelection;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeLookup;
import net.claustra01.tfie.common.heat.FurnaceOutputCapacity;
import net.claustra01.tfie.common.heat.FurnaceOutputHeat;
import net.claustra01.tfie.common.recipes.ModeAwareArcFurnaceRecipe;
import net.claustra01.tfie.gametest.ledger.RecipeLedgerGameTests;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.dries007.tfc.common.component.forge.Forging;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.HeatingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FurnaceGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final BlockPos ARC_RESTART_ORIGIN = new BlockPos(1024, 96, 1024);

    private static final List<BriquetExpected> BRIQUETS = List.of(
            briquet("native_copper", "tfc:metal/ingot/copper"),
            briquet("native_gold", "tfc:metal/ingot/gold"),
            briquet("hematite", "tfc:metal/ingot/cast_iron"),
            briquet("native_silver", "tfc:metal/ingot/silver"),
            briquet("cassiterite", "tfc:metal/ingot/tin"),
            briquet("bismuthinite", "tfc:metal/ingot/bismuth"),
            briquet("garnierite", "tfc:metal/ingot/nickel"),
            briquet("malachite", "tfc:metal/ingot/copper"),
            briquet("magnetite", "tfc:metal/ingot/cast_iron"),
            briquet("limonite", "tfc:metal/ingot/cast_iron"),
            briquet("sphalerite", "tfc:metal/ingot/zinc"),
            briquet("tetrahedrite", "tfc:metal/ingot/copper"),
            briquet("galena", "tfcm:metal/ingot/lead"),
            briquet("magnesite", "tfcm:metal/ingot/magnesium"),
            briquet("bauxite", "tfcm:metal/ingot/aluminum"),
            briquet("uraninite", "tfcm:metal/ingot/uranium"),
            briquet("chromite", "firmalife:metal/ingot/chromium"),
            briquet("rutile", "tfcm:metal/ingot/titanium"),
            briquet("zircon", "tfcm:metal/ingot/zirconium"),
            briquet("cobaltite", "tfcm:metal/ingot/cobalt"),
            briquet("stibnite", "tfcm:metal/ingot/antimony"),
            briquet("native_platinum", "tfcm:metal/ingot/platinum"),
            briquet("wolframite", "tfcm:metal/ingot/tungsten"),
            briquet("pyrolusite", "tfcm:metal/ingot/manganese"),
            briquet("monazite", "tfcm:metal/ingot/neodymium"),
            briquet("thorianite", "tfcm:metal/ingot/thorium"),
            briquet("native_iridium", "tfcm:metal/ingot/iridium"),
            briquet("native_osmium", "tfcm:metal/ingot/osmium"),
            briquet("spodumene", "tfcm:metal/ingot/lithium"),
            briquet("carobbiite", "tfcm:metal/ingot/beryllium"),
            briquet("native_naquadah", "tfcm:metal/ingot/naquadah"),
            briquet("mithril_matrix", "tfcm:metal/ingot/mithril"),
            briquet("borax", "tfcm:metal/ingot/boron"));

    private static final List<ArcExpected> ARC_ALLOYS = List.of(
            arc("bismuth_bronze", 3, "copper", 3, 5, 128000, 250),
            arc("black_bronze", 4, "copper", 2, 4, 102400, 200),
            arc("brass", 1, "copper", 9, 10, 256000, 500),
            arc("bronze", 2, "copper", 9, 10, 128000, 250),
            arc("rose_gold", 1, "gold", 3, 4, 51200, 100),
            arc("sterling_silver", 1, "silver", 3, 5, 128000, 250),
            arc("weak_steel", 1, "steel", 6, 10, 256000, 500),
            arc("weak_blue_steel", 1, "black_steel", 10, 20, 512000, 1000),
            arc("weak_red_steel", 2, "black_steel", 10, 20, 512000, 1000),
            arc("constantan", 5, "copper", 1, 2, 51200, 100),
            arc("electrum", 2, "gold", 1, 2, 51200, 100),
            arc("invar", 2, "wrought_iron", 2, 3, 51200, 100),
            arc("osmiridium", 1, "osmium", 2, 3, 76800, 150),
            arc("solder", 1, "antimony", 2, 4, 102400, 200),
            arc("hastelloy", 2, "nickel", 7, 14, 358400, 700),
            arc("high_speed_steel", 2, "steel", 10, 14, 358400, 700),
            arc("andesite_alloy_from_zinc", 1, "andesite", 10, 10, 256000, 500),
            arc("andesite_alloy_from_tin", 2, "andesite", 10, 10, 256000, 500));

    private FurnaceGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void furnace_output_heat_and_capacity(GameTestHelper helper)
    {
        ItemStack bloom = item("tfc:raw_iron_bloom");
        FurnaceOutputHeat.apply(bloom);
        helper.assertTrue(Math.abs(HeatCapability.getTemperature(bloom) - 1381.5f) < 0.01f,
                "Raw Iron Bloom must be created at exactly 90% of 1535 C");
        ItemStack copper = item("tfc:metal/ingot/copper");
        FurnaceOutputHeat.apply(copper);
        helper.assertTrue(Math.abs(HeatCapability.getTemperature(copper) - 972f) < 0.01f,
                "Furnace output heat must use the loaded metal Heat Definition");
        if(ModList.get().isLoaded("firmalife"))
        {
            ItemStack chromium = item("firmalife:metal/ingot/chromium");
            FurnaceOutputHeat.apply(chromium);
            helper.assertTrue(Math.abs(HeatCapability.getTemperature(chromium) - 1125f) < 0.01f,
                    "Chromium must use FirmaLife's registered 1250 C melting point");
        }
        ItemStack slag = item("immersiveengineering:slag");
        FurnaceOutputHeat.apply(slag);
        helper.assertTrue(HeatCapability.getTemperature(slag) == 0,
                "Slag must not receive furnace output heat");
        ItemStack glass = item("immersiveengineering:insulating_glass");
        FurnaceOutputHeat.apply(glass);
        helper.assertTrue(HeatCapability.getTemperature(glass) == 0,
                "Insulating Glass must not receive furnace output heat");

        ItemStack cold = item("tfc:metal/ingot/copper");
        ItemStack hot = cold.copy();
        HeatCapability.setTemperature(cold, 700);
        HeatCapability.setTemperature(hot, 900);
        helper.assertTrue(!ItemStack.isSameItemSameComponents(cold, hot),
                "Different Heat components must not merge");
        ItemStackHandler outputs = new ItemStackHandler(2);
        outputs.setStackInSlot(0, cold);
        helper.assertTrue(FurnaceOutputCapacity.canFitAll(outputs, new int[]{0, 1}, List.of(hot)),
                "A differently heated output must use an empty slot");
        outputs.setStackInSlot(1, itemWithCount("minecraft:cobblestone", 64));
        helper.assertTrue(!FurnaceOutputCapacity.canFitAll(outputs, new int[]{0, 1}, List.of(hot)),
                "A differently heated output must wait when every output slot is occupied");

        ItemStackHandler aggregate = new ItemStackHandler(1);
        ItemStack first = itemWithCount("tfc:metal/ingot/copper", 40);
        ItemStack second = itemWithCount("tfc:metal/ingot/gold", 40);
        FurnaceOutputHeat.apply(first);
        FurnaceOutputHeat.apply(second);
        helper.assertTrue(!FurnaceOutputCapacity.canFitAll(aggregate, new int[]{0}, List.of(first, second)),
                "All outputs must fit together before a furnace process commits");
        ItemStackHandler designated = new ItemStackHandler(26);
        for(int slot = 16; slot <= 21; slot++)
            designated.setStackInSlot(slot, itemWithCount("minecraft:cobblestone", 64));
        helper.assertTrue(!FurnaceOutputCapacity.canFitArcOutputs(designated,
                        new int[]{16, 17, 18, 19, 20, 21, 22},
                        List.of(item("tfc:metal/ingot/steel"), item("immersiveengineering:slag")), 22,
                        item("immersiveengineering:slag")),
                "An empty Slag slot must not count as capacity for a normal Arc output");
        for(int slot = 16; slot <= 21; slot++)
            designated.setStackInSlot(slot, ItemStack.EMPTY);
        designated.setStackInSlot(22, itemWithCount("minecraft:cobblestone", 64));
        helper.assertTrue(!FurnaceOutputCapacity.canFitArcOutputs(designated,
                        new int[]{16, 17, 18, 19, 20, 21, 22},
                        List.of(item("tfc:metal/ingot/steel"), item("immersiveengineering:slag")), 22,
                        item("immersiveengineering:slag")),
                "Normal Arc slots must not count as capacity for Slag");

        ItemStack arcOutput = assertArcCommitAndWaiting(helper);
        for(String ore : new String[]{"hematite", "magnetite", "limonite"})
            if(optionalRecipe(helper, "tfie:blastfurnace/briquet/" + ore) != null)
            {
                assertBlastFurnaceProcessing(helper, false, ore);
                assertBlastFurnaceProcessing(helper, true, ore);
            }
        assertAlloyKilnProcessing(helper);
        MetalPressRecipe press = recipe(helper, "tfie:metalpress/plate/copper", MetalPressRecipe.class);
        ItemStack pressed = press.getActualItemOutputs().getFirst();
        helper.assertTrue(HeatCapability.getTemperature(pressed) == 0,
                "Metal Press outputs must not receive furnace output heat");

        float createdTemperature = HeatCapability.getTemperature(arcOutput);
        helper.runAfterDelay(40, () -> {
            helper.assertTrue(HeatCapability.getTemperature(arcOutput) < createdTemperature,
                    "Furnace metal output must cool through TFC's normal Heat Component");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void formed_furnaces_and_metal_press_process_outputs(GameTestHelper helper)
    {
        FormedMachine<ArcFurnaceLogic.State> arc = form(helper, new ArcFurnaceMultiblock(),
                new BlockPos(1, 2, 1), new BlockPos(2, 0, 4), new BlockPos(2, 1, 2));
        ArcFurnaceLogic.State arcState = arc.state();
        IItemHandler arcInput = itemCapability(helper, arc.origin().offset(1, 3, 2));
        ItemStack briquet = item("tfcm:ore_processing/briquet/magnetite");
        helper.assertTrue(arcInput.insertItem(0, briquet.copy(), false).isEmpty()
                        && arcState.getInventory().getStackInSlot(0).is(briquet.getItem()),
                "A formed Arc must receive its Briquet through the machine input capability");
        helper.assertTrue(arcInput.insertItem(1, ModItems.ARC_FURNACE_BLUEPRINT.toStack(), false)
                        .is(ModItems.ARC_FURNACE_BLUEPRINT.get()),
                "The formed Arc input capability must reject automatic Blueprint insertion");
        ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        arcState.getInventory().setStackInSlot(1, blueprint);
        for(int slot = 23; slot <= 25; slot++)
            arcState.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));
        arcState.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        ArcFurnaceProcess arcProcess = new ArcFurnaceProcess(
                arcRecipeHolder(helper, "tfie:arcfurnace/briquet/magnetite"), 7L, 0, 12, 13, 14, 15);
        arcProcess.setInputAmounts(1, 0, 0, 0, 0);
        arcProcess.processTick = arcProcess.getMaxTicks(helper.getLevel()) - 1;
        CompoundTag arcSave = new CompoundTag();
        arcState.writeSaveNBT(arcSave, helper.getLevel().registryAccess());
        arcSave.put("processor", processList(arcProcess, helper));
        arcState.readSaveNBT(arcSave, helper.getLevel().registryAccess());
        ItemStack existing = item("tfc:metal/ingot/cast_iron");
        HeatCapability.setTemperature(existing, 700);
        arcState.getInventory().setStackInSlot(16, existing);
        for(int slot = 17; slot <= 21; slot++)
            arcState.getInventory().setStackInSlot(slot, itemWithCount("minecraft:cobblestone", 64));
        int energy = arcState.getEnergy().getEnergyStored();
        arc.helper().tickServer();
        helper.assertTrue(arcState.getProcessQueue().getFirst().processTick
                        == arcProcess.getMaxTicks(helper.getLevel()) - 1
                        && arcState.getEnergy().getEnergyStored() == energy
                        && HeatCapability.getTemperature(arcState.getInventory().getStackInSlot(16)) == 700,
                "A formed Arc must wait without FE use or reheating when differently heated output cannot fit");
        arcState.getInventory().setStackInSlot(17, ItemStack.EMPTY);
        arc.helper().tickServer();
        ItemStack arcOutput = arcState.getInventory().getStackInSlot(17);
        helper.assertTrue(arcOutput.is(item("tfc:metal/ingot/cast_iron").getItem())
                        && Math.abs(HeatCapability.getTemperature(arcOutput)
                        - HeatCapability.getDefinition(arcOutput).forgingTemperature() / 0.6f * 0.9f) < 0.01f
                        && HeatCapability.getTemperature(arcState.getInventory().getStackInSlot(16)) == 700,
                "A formed Arc must commit one 90%-temperature output into a separate free slot");
        FormedMachine<BlastFurnaceLogic.State> blast = form(helper, new BlastFurnaceMultiblock(),
                new BlockPos(8, 2, 1), new BlockPos(1, 1, 2), new BlockPos(1, 1, 1));
        runBlastFurnace(helper, blast.state().getInventory(), blast.state().getInventory(),
                blast.state().getStateView(),
                blast.helper()::tickServer, "magnetite", "formed Blast Furnace");
        FormedMachine<AdvBlastFurnaceLogic.State> improved = form(helper,
                new ImprovedBlastfurnaceMultiblock(), new BlockPos(12, 2, 1),
                new BlockPos(1, 1, 2), new BlockPos(1, 1, 1));
        IItemHandler improvedInput = itemCapability(helper, improved.origin().offset(1, 3, 1));
        runBlastFurnace(helper, improved.state().getInventory(), improvedInput,
                improved.state().getStateView(),
                improved.helper()::tickServer, "limonite", "formed Improved Blast Furnace");
        FormedMachine<AlloySmelterLogic.State> kiln = form(helper, new AlloySmelterMultiblock(),
                new BlockPos(17, 2, 1), new BlockPos(1, 1, 1), new BlockPos(0, 0, 1));
        runAlloyKiln(helper, kiln.state(), kiln.state().getInventory(), kiln.helper()::tickServer);

        FormedMachine<MetalPressLogic.State> press = form(helper, new MetalPressMultiblock(),
                new BlockPos(21, 2, 1), new BlockPos(1, 1, 0), new BlockPos(1, 1, 0));
        press.state().mold = item("immersiveengineering:mold_plate");
        press.state().getEnergy().receiveEnergy(16000, false);
        IItemHandler pressInput = itemCapability(helper, press.origin().offset(0, 1, 0));
        helper.assertTrue(pressInput.insertItem(0, item("tfc:metal/ingot/copper"), false).isEmpty(),
                "A formed Metal Press must receive its Ingot through the input capability");
        for(int tick = 0; tick < 65; tick++)
            press.helper().tickServer();
        ItemStack pressed = helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                        new AABB(press.origin()).inflate(8)).stream().map(ItemEntity::getItem)
                .filter(stack -> stack.is(item("tfcm:metal/plate/copper").getItem())).findFirst()
                .orElse(ItemStack.EMPTY);
        helper.assertTrue(!pressed.isEmpty() && HeatCapability.getTemperature(pressed) < 100,
                "A real Metal Press process must leave its metal output unheated: "
                        + HeatCapability.getTemperature(pressed));

        float createdTemperature = HeatCapability.getTemperature(arcOutput);
        helper.runAfterDelay(40, () ->
        {
            helper.assertTrue(HeatCapability.getTemperature(arcState.getInventory().getStackInSlot(17))
                            < createdTemperature,
                    "A heated output must cool while it remains in the formed Arc inventory");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void formed_crusher_and_arc_reject_removed_inputs(GameTestHelper helper)
    {
        FormedMachine<CrusherLogic.State> crusher = form(helper, new CrusherMultiblock(),
                new BlockPos(1, 2, 1), new BlockPos(2, 1, 2), CrusherLogic.MASTER_OFFSET);
        CrusherLogic.State crusherState = crusher.state();
        crusherState.getEnergy().receiveEnergy(Integer.MAX_VALUE, false);
        IItemHandler crusherInput = itemCapability(helper, crusher.origin().offset(2, 2, 1));
        for(RecipeLedgerGameTests.CrusherLedgerInput ledger : RecipeLedgerGameTests.crusherLedgerInputs())
        {
            ItemStack input = ledger.input().copy();
            if(input.isEmpty())
                continue;
            RecipeHolder<CrusherRecipe> selected = CrusherRecipe.findRecipe(helper.getLevel(), input);
            ItemStack remainder = crusherInput.insertItem(0, input.copy(), false);
            if(selected == null)
                helper.assertTrue(remainder.getCount() == input.getCount()
                                && crusherState.getProcessQueue().isEmpty(),
                        "A formed Crusher accepted a removed ledger input: " + ledger.removedId());
            else
                helper.assertTrue(!selected.id().equals(ledger.removedId()) && remainder.isEmpty()
                                && crusherState.getProcessQueue().size() == 1
                                && crusherState.getProcessQueue().getFirst().getRecipeId().equals(selected.id()),
                        "A formed Crusher selected the removed route instead of its surviving control: "
                                + ledger.removedId());
            if(!crusherState.getProcessQueue().isEmpty())
            {
                crusherState.getProcessQueue().getFirst().clearProcess = true;
                crusher.helper().tickServer();
                helper.assertTrue(crusherState.getProcessQueue().isEmpty(),
                        "The formed Crusher did not clear its completed ledger probe");
            }
        }
        for(ItemStack input : RecipeLedgerGameTests.crusherControlInputs())
        {
            ItemStack remainder = crusherInput.insertItem(0, input.copy(), false);
            helper.assertTrue(remainder.isEmpty() && crusherState.getProcessQueue().size() == 1
                            && crusherState.getProcessQueue().getFirst().getRecipeId().getNamespace().equals(TFIE.MOD_ID),
                    "A formed Crusher did not start an adopted non-metal control for " + input);
            crusherState.getProcessQueue().getFirst().clearProcess = true;
            crusher.helper().tickServer();
            helper.assertTrue(crusherState.getProcessQueue().isEmpty(),
                    "The formed Crusher did not clear its completed non-metal probe");
        }

        FormedMachine<ArcFurnaceLogic.State> arc = form(helper, new ArcFurnaceMultiblock(),
                new BlockPos(8, 2, 1), new BlockPos(2, 0, 4), new BlockPos(2, 1, 2));
        ArcFurnaceLogic.State arcState = arc.state();
        for(int slot = 23; slot <= 25; slot++)
            arcState.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));
        arcState.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        for(ItemStack input : RecipeLedgerGameTests.arcRejectedInputs())
        {
            if(input.isEmpty())
                continue;
            arcState.getInventory().setStackInSlot(0, input.copy());
            arc.helper().tickServer();
            helper.assertTrue(arcState.getProcessQueue().isEmpty()
                            && arcState.getInventory().getStackInSlot(0).getCount() == input.getCount(),
                    "A formed Arc started a removed Ore, Raw Ore, Dust, Ancient Debris or recycling route: "
                            + input);
            arcState.getInventory().setStackInSlot(0, ItemStack.EMPTY);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void formed_metal_press_and_sawmill_ledger_controls(GameTestHelper helper)
    {
        FormedMachine<MetalPressLogic.State> press = form(helper, new MetalPressMultiblock(),
                new BlockPos(1, 2, 1), new BlockPos(1, 1, 0), new BlockPos(1, 1, 0));
        MetalPressLogic.State pressState = press.state();
        IItemHandler pressInput = itemCapability(helper, press.origin().offset(0, 1, 0));
        Set<ResourceLocation> removed = RecipeLedgerGameTests.metalPressLedgerInputs().stream()
                .map(RecipeLedgerGameTests.MetalPressLedgerInput::removedId)
                .collect(java.util.stream.Collectors.toSet());
        for(RecipeLedgerGameTests.MetalPressLedgerInput ledger : RecipeLedgerGameTests.metalPressLedgerInputs())
        {
            ItemStack input = ledger.input().copy();
            if(input.isEmpty())
                continue;
            pressState.mold = ledger.mold().copy();
            RecipeHolder<MetalPressRecipe> selected = MetalPressRecipe.findRecipe(
                    pressState.mold, input, helper.getLevel());
            ItemStack remainder = pressInput.insertItem(0, input.copy(), false);
            if(selected == null)
                helper.assertTrue(remainder.getCount() == input.getCount()
                                && pressState.processor.getQueue().isEmpty(),
                        "A formed Metal Press accepted a removed ledger input: " + ledger.removedId());
            else
            {
                helper.assertTrue(remainder.isEmpty() && pressState.processor.getQueue().size() == 1
                                && pressState.processor.getQueue().getFirst().getRecipeId().equals(selected.id())
                                && !removed.contains(selected.id()),
                        "A formed Metal Press selected a removed route: " + ledger.removedId());
                pressState.processor.getQueue().getFirst().clearProcess = true;
                press.helper().tickServer();
                helper.assertTrue(pressState.processor.getQueue().isEmpty(),
                        "The formed Metal Press did not clear its completed ledger probe");
            }
        }

        List<RecipeHolder<MetalPressRecipe>> pressControls = MetalPressRecipe.STANDARD_RECIPES
                .getRecipes(helper.getLevel()).stream()
                .filter(holder -> holder.id().getNamespace().equals("immersiveengineering"))
                .filter(holder -> holder.id().getPath().startsWith("metalpress/gear_")
                        || List.of("metalpress/blaze_rod", "metalpress/bullet_casing", "metalpress/melon")
                        .contains(holder.id().getPath()))
                .sorted(java.util.Comparator.comparing(holder -> holder.id().toString()))
                .toList();
        helper.assertTrue(pressControls.stream().map(RecipeHolder::id)
                        .map(ResourceLocation::getPath).toList().containsAll(List.of(
                                "metalpress/gear_copper", "metalpress/blaze_rod",
                                "metalpress/bullet_casing", "metalpress/melon")),
                "The formed Metal Press controls must include Gear, Blaze Rod, Bullet Casing and Melon");
        for(RecipeHolder<MetalPressRecipe> holder : pressControls)
        {
            MetalPressRecipe recipe = holder.value();
            ItemStack input = recipe.getItemInputs().getFirst().getMatchingStacks()[0].copy();
            pressState.mold = new ItemStack(recipe.mold);
            helper.assertTrue(pressInput.insertItem(0, input, false).isEmpty()
                            && pressState.processor.getQueue().size() == 1
                            && pressState.processor.getQueue().getFirst().getRecipeId().equals(holder.id()),
                    "A formed Metal Press did not start untouched control " + holder.id());
            finishMetalPressProcess(helper, press, recipe.getActualItemOutputs().getFirst(), false);
        }

        runMetalPressControl(helper, press, pressInput, "tfie:metalpress/wire/copper",
                "immersiveengineering:mold_wire", item("tfc:metal/ingot/copper"),
                itemWithCount("tfcm:metal/wire/copper", 4));
        runMetalPressControl(helper, press, pressInput, "tfie:metalpress/sheet/copper",
                "tfie:mold_sheet", itemWithCount("tfc:metal/ingot/copper", 2),
                item("tfc:metal/sheet/copper"));

        FormedMachine<SawmillLogic.State> sawmill = form(helper, new SawmillMultiblock(),
                new BlockPos(8, 2, 1), new BlockPos(2, 0, 2), new BlockPos(2, 1, 1));
        SawmillLogic.State sawmillState = sawmill.state();
        sawmillState.sawblade = item("immersiveengineering:sawblade");
        IItemHandler sawmillInput = itemCapability(helper, sawmill.origin().offset(0, 1, 1));
        List<RecipeHolder<SawmillRecipe>> sawmillControls = SawmillRecipe.RECIPES
                .getRecipes(helper.getLevel()).stream()
                .filter(holder -> holder.id().getNamespace().equals(TFIE.MOD_ID))
                .toList();
        helper.assertTrue(!sawmillControls.isEmpty(), "The formed Sawmill must have TFIE controls to process");
        for(RecipeHolder<SawmillRecipe> holder : sawmillControls)
            for(ItemStack matching : holder.value().input.getItems())
            {
                clearDroppedItems(helper, sawmill.origin(), 12);
                sawmillState.getEnergy().receiveEnergy(32000, false);
                ItemStack input = matching.copyWithCount(1);
                helper.assertTrue(sawmillInput.insertItem(0, input, false).isEmpty()
                                && sawmillState.sawmillProcessQueue.size() == 1
                                && SawmillRecipe.findRecipe(helper.getLevel(), input) == holder.value(),
                        "A formed Sawmill did not start untouched control " + holder.id() + " for " + input);
                for(int tick = 0; tick <= holder.value().getTotalProcessTime(); tick++)
                    sawmill.helper().tickServer();
                ItemStack expected = holder.value().output.get();
                boolean output = helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                                new AABB(sawmill.origin()).inflate(12)).stream()
                        .map(ItemEntity::getItem)
                        .anyMatch(stack -> ItemStack.isSameItemSameComponents(stack, expected)
                                && stack.getCount() >= expected.getCount());
                helper.assertTrue(sawmillState.sawmillProcessQueue.isEmpty() && output,
                        "A formed Sawmill did not finish untouched control " + holder.id() + " for " + input);
            }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void arc_briquet_recipe_table(GameTestHelper helper)
    {
        boolean disabledMaterials = isDisabledMaterialsRun();
        for(BriquetExpected expected : BRIQUETS)
        {
            String ore = expected.ore;
            Recipe<?> value = optionalRecipe(helper, "tfie:arcfurnace/briquet/" + ore);
            if(disabledMaterials && (ore.equals("hematite") || ore.equals("rutile")))
            {
                helper.assertTrue(value == null,
                        "A Briquet with a disabled ore or output metal must not have an Arc recipe: " + ore);
                continue;
            }
            if(ore.equals("chromite") && !ModList.get().isLoaded("firmalife"))
            {
                helper.assertTrue(value == null, "Chromite Arc smelting must require FirmaLife");
                continue;
            }
            ModeAwareArcFurnaceRecipe recipe = cast(helper, value, ModeAwareArcFurnaceRecipe.class, ore);
            ItemStack input = item("tfcm:ore_processing/briquet/" + ore);
            ItemStack output = recipe.getBaseOutputs().getFirst();
            helper.assertTrue(recipe.mode() == 0 && recipe.getTotalProcessEnergy() == 51200
                            && recipe.getTotalProcessTime() == 100 && recipe.input.getCount() == 1
                            && recipe.additives.isEmpty() && recipe.slag.get().isEmpty()
                            && recipe.input.test(input) && output.is(item(expected.output).getItem())
                            && output.getCount() == 1,
                    ore + " must be a one-Briquet basic Arc process with no additives or slag");
            for(int mode = 0; mode <= 8; mode++)
            {
                ItemStackHandler inventory = new ItemStackHandler(26);
                inventory.setStackInSlot(0, input);
                if(mode > 0)
                {
                    ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
                    blueprint.set(ModDataComponents.ARC_FURNACE_MODE, mode);
                    inventory.setStackInSlot(1, blueprint);
                }
                RecipeHolder<ArcFurnaceRecipe> selected = ArcFurnaceRecipeSelection.find(helper.getLevel(), input,
                        NonNullList.withSize(4, ItemStack.EMPTY), inventory);
                helper.assertTrue(selected != null && selected.id().equals(id("tfie:arcfurnace/briquet/" + ore)),
                        ore + " did not start in Blueprint mode " + mode);
                ArcFurnaceLogic.State processed = completeArcProcess(helper, selected, mode, input.copy(), List.of());
                ItemStack processedOutput = processed.getInventory().getStackInSlot(16);
                float expectedTemperature = HeatCapability.getDefinition(processedOutput)
                        .forgingTemperature() / 0.6f * 0.9f;
                helper.assertTrue(processed.getInventory().getStackInSlot(0).isEmpty()
                                && processedOutput.is(item(expected.output).getItem())
                                && processedOutput.getCount() == 1
                                && Math.abs(HeatCapability.getTemperature(processedOutput)
                                - expectedTemperature) < 0.01f,
                        ore + " did not complete at exactly 90% melting temperature in Blueprint mode " + mode);
            }
            ItemStack heated = output.copy();
            FurnaceOutputHeat.apply(heated);
            helper.assertTrue(HeatCapability.getTemperature(heated) > 0,
                    ore + " output must receive its registered 90% metal heat");
        }
        for(String excluded : new String[]{"cinnabar", "cryolite", "graphite", "sulfur"})
            helper.assertTrue(optionalRecipe(helper, "tfie:arcfurnace/briquet/" + excluded) == null,
                    excluded + " must not gain a Briquet Arc recipe");
        for(String metal : new String[]{"aluminum", "ardite", "cobalt", "copper", "gold", "iron", "lead",
                "nickel", "osmium", "platinum", "silver", "tin", "tungsten", "uranium", "zinc"})
            for(String prefix : new String[]{"ore_", "raw_ore_", "raw_block_"})
                assertMissing(helper, "immersiveengineering:arcfurnace/" + prefix + metal);
        for(String metal : new String[]{"aluminum", "ardite", "brass", "bronze", "cobalt", "constantan",
                "copper", "electrum", "gold", "invar", "iron", "lead", "manyullyn", "nickel", "osmium",
                "platinum", "rose_gold", "silver", "steel", "tin", "tungsten", "uranium", "zinc"})
            assertMissing(helper, "immersiveengineering:arcfurnace/dust_" + metal);
        for(String alloy : new String[]{"brass", "bronze", "constantan", "electrum", "invar", "manyullyn",
                "rose_gold"})
            assertMissing(helper, "immersiveengineering:arcfurnace/alloy_" + alloy);
        for(String fixed : new String[]{"netherite_scrap", "steel", "insulating_glass"})
            assertMissing(helper, "immersiveengineering:arcfurnace/" + fixed);
        for(BriquetExpected expected : BRIQUETS)
        {
            for(String stage : new String[]{"powder", "pellet", "chunks", "dirty_dust", "rocky_chunks"})
                assertNoArcRecipeIfRegistered(helper, "tfcm:ore_processing/" + stage + "/" + expected.ore);
            for(String grade : new String[]{"small", "poor", "normal", "rich"})
            {
                assertNoArcRecipeIfRegistered(helper, "tfc:ore/" + grade + "_" + expected.ore);
                assertNoArcRecipeIfRegistered(helper, "tfcm:ore/" + grade + "_" + expected.ore);
            }
            String metal = expected.output.substring(expected.output.lastIndexOf('/') + 1);
            assertNoArcRecipeIfRegistered(helper, "tfcm:metal/dust/" + metal);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void arc_alloy_and_mode_table(GameTestHelper helper)
    {
        for(ArcExpected expected : ARC_ALLOYS)
        {
            Recipe<?> value = optionalRecipe(helper, "tfie:arcfurnace/alloy/" + expected.path);
            if(expected.path.equals("hastelloy") && !ModList.get().isLoaded("firmalife"))
            {
                helper.assertTrue(value == null, "Hastelloy must require FirmaLife");
                continue;
            }
            ModeAwareArcFurnaceRecipe recipe = cast(helper, value, ModeAwareArcFurnaceRecipe.class,
                    expected.path);
            ItemStack output = recipe.getBaseOutputs().getFirst();
            List<AdditiveExpected> additives = expectedAdditives(expected.path);
            helper.assertTrue(recipe.mode() == expected.mode && recipe.input.getCount() == expected.mainCount
                            && output.is(item(expectedArcOutput(expected.path)).getItem())
                            && output.getCount() == expected.outputCount
                            && recipe.additives.size() == additives.size()
                            && recipe.getTotalProcessEnergy() == expected.energy
                            && recipe.getTotalProcessTime() == expected.time && recipe.slag.get().isEmpty(),
                    expected.path + " differs from the independent Arc alloy table");
            for(int index = 0; index < additives.size(); index++)
            {
                AdditiveExpected additive = additives.get(index);
                ItemStack sample = arcMetalInput(additive.metal);
                sample.setCount(additive.count);
                helper.assertTrue(recipe.additives.get(index).getCount() == additive.count
                                && recipe.additives.get(index).test(sample),
                        expected.path + " has the wrong additive at index " + index);
            }
            List<ItemStack> processAdditives = additives.stream().map(additive -> {
                ItemStack stack = arcMetalInput(additive.metal);
                stack.setCount(additive.count);
                return stack;
            }).toList();
            ArcFurnaceLogic.State processed = completeArcProcess(helper,
                    arcRecipeHolder(helper, "tfie:arcfurnace/alloy/" + expected.path), expected.mode,
                    arcMainInput(expected.main, expected.mainCount), processAdditives);
            ItemStack processedOutput = processed.getInventory().getStackInSlot(16);
            helper.assertTrue(processed.getInventory().getStackInSlot(0).isEmpty()
                            && processedOutput.is(item(expectedArcOutput(expected.path)).getItem())
                            && processedOutput.getCount() == expected.outputCount
                            && HeatCapability.getTemperature(processedOutput) > 0,
                    expected.path + " did not consume its exact inputs and commit its heated output");
        }
        ModeAwareArcFurnaceRecipe steel = recipe(helper, "tfie:arcfurnace/steel",
                ModeAwareArcFurnaceRecipe.class);
        helper.assertTrue(steel.mode() == 1 && steel.input.getCount() == 1 && steel.additives.size() == 1
                        && steel.additives.getFirst().getCount() == 1 && steel.getBaseOutputs().getFirst().getCount() == 1
                        && steel.slag.get().getCount() == 1 && steel.getTotalProcessEnergy() == 204800
                        && steel.getTotalProcessTime() == 400,
                "Steel must use mode 1, one Wrought Iron, one Coke Dust and one Slag");
        ArcFurnaceLogic.State processedSteel = completeArcProcess(helper,
                arcRecipeHolder(helper, "tfie:arcfurnace/steel"), 1,
                item("tfc:metal/ingot/wrought_iron"), List.of(item("immersiveengineering:dust_coke")));
        helper.assertTrue(processedSteel.getInventory().getStackInSlot(16)
                        .is(item("tfc:metal/ingot/steel").getItem())
                        && processedSteel.getInventory().getStackInSlot(22)
                        .is(item("immersiveengineering:slag").getItem()),
                "Steel processing must commit its heated Ingot and dedicated Slag output");
        ModeAwareArcFurnaceRecipe glass = recipe(helper, "tfie:arcfurnace/insulating_glass",
                ModeAwareArcFurnaceRecipe.class);
        helper.assertTrue(glass.mode() == 0 && glass.input.getCount() == 2 && glass.additives.size() == 1
                        && glass.getBaseOutputs().getFirst().getCount() == 2
                        && glass.getTotalProcessEnergy() == 51200 && glass.getTotalProcessTime() == 100,
                "Insulating Glass must remain a basic Arc process");

        for(int mode = 1; mode <= 8; mode++)
            for(ArcExpected expected : ARC_ALLOYS)
            {
                Recipe<?> value = optionalRecipe(helper, "tfie:arcfurnace/alloy/" + expected.path);
                if(value instanceof ModeAwareArcFurnaceRecipe recipe)
                    helper.assertTrue((recipe.mode() == mode) == (expected.mode == mode),
                            expected.path + " changed its fixed mode");
            }

        List<ArcExpected> allRoutes = new ArrayList<>(ARC_ALLOYS);
        allRoutes.add(arc("steel", 1, "wrought_iron", 1, 1, 204800, 400));
        int combinations = 0;
        for(int mode = 0; mode <= 8; mode++)
        {
            for(ArcExpected route : allRoutes)
            {
                combinations++;
                ItemStack mainInput = arcMainInput(route.main);
                ItemStackHandler inventory = new ItemStackHandler(26);
                inventory.setStackInSlot(0, mainInput);
                if(mode > 0)
                {
                    ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
                    blueprint.set(ModDataComponents.ARC_FURNACE_MODE, mode);
                    inventory.setStackInSlot(1, blueprint);
                }
                String expectedPath = expectedArcPath(route.main, mode);
                NonNullList<ItemStack> additives = NonNullList.withSize(4, ItemStack.EMPTY);
                if(expectedPath != null)
                {
                    List<AdditiveExpected> expectedAdditives = expectedPath.equals("steel")
                            ? List.of(new AdditiveExpected("coal_coke_dust", 1))
                            : expectedAdditives(expectedPath);
                    for(int index = 0; index < expectedAdditives.size(); index++)
                    {
                        AdditiveExpected additive = expectedAdditives.get(index);
                        ItemStack stack = additive.metal.equals("coal_coke_dust")
                                ? item("immersiveengineering:dust_coke") : arcMetalInput(additive.metal);
                        stack.setCount(additive.count);
                        additives.set(index, stack);
                    }
                }
                RecipeHolder<ArcFurnaceRecipe> selected = ArcFurnaceRecipeSelection.find(helper.getLevel(),
                        mainInput, additives, inventory);
                if(expectedPath == null || expectedPath.equals("hastelloy")
                        && !ModList.get().isLoaded("firmalife"))
                    helper.assertTrue(selected == null,
                            "Mode " + mode + " unexpectedly selects " + route.main);
                else
                    helper.assertTrue(selected != null && selected.id().equals(
                                    id(expectedPath.equals("steel") ? "tfie:arcfurnace/steel"
                                            : "tfie:arcfurnace/alloy/" + expectedPath)),
                            "Mode " + mode + " did not select " + expectedPath + " for " + route.main);
            }
        }
        helper.assertTrue(combinations == 171, "The fixed Arc table must cover all 171 mode/main combinations");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void blueprint_crafting_modes_and_selection(GameTestHelper helper)
    {
        CraftingRecipe crafting = recipe(helper, "tfie:crafting/arc_furnace_blueprint", CraftingRecipe.class);
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.add(new ItemStack(Items.PAPER));
        ingredients.add(new ItemStack(Items.PAPER));
        ingredients.add(new ItemStack(Items.PAPER));
        ingredients.add(new ItemStack(Items.BLUE_DYE));
        ingredients.add(new ItemStack(Items.BLUE_DYE));
        ingredients.add(new ItemStack(Items.BLUE_DYE));
        CraftingInput craftingInput = CraftingInput.of(3, 2, ingredients);
        helper.assertTrue(crafting.matches(craftingInput, helper.getLevel()),
                "The Arc Blueprint shapeless recipe must match three Paper and three Blue Dye");
        ItemStack crafted = crafting.assemble(craftingInput, helper.getLevel().registryAccess());
        helper.assertTrue(crafted.is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && crafted.getOrDefault(ModDataComponents.ARC_FURNACE_MODE, 0) == 1,
                "Three Paper and three Blue Dye must craft one mode 1 Arc Blueprint");

        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, crafted);
        for(int expected = 2; expected <= 8; expected++)
        {
            crafted.getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
            helper.assertTrue(crafted.get(ModDataComponents.ARC_FURNACE_MODE) == expected,
                    "Blueprint did not advance to mode " + expected);
        }
        crafted.getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(crafted.get(ModDataComponents.ARC_FURNACE_MODE) == 1,
                "Mode 8 must cycle directly to mode 1");

        BlockPos blockPosition = helper.absolutePos(new BlockPos(1, 2, 1));
        helper.getLevel().setBlockAndUpdate(blockPosition, Blocks.CHEST.defaultBlockState());
        int beforeBlockUse = crafted.get(ModDataComponents.ARC_FURNACE_MODE);
        InteractionResult blockUse = crafted.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(blockPosition), Direction.UP, blockPosition, false)));
        helper.assertTrue(blockUse == InteractionResult.PASS
                        && crafted.get(ModDataComponents.ARC_FURNACE_MODE) == beforeBlockUse,
                "A Blueprint block interaction must not cycle its mode before the block handles the click");
        crafted.getItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(crafted.get(ModDataComponents.ARC_FURNACE_MODE) == 2,
                "One server-side air use must advance the Blueprint exactly once");
        crafted.set(ModDataComponents.ARC_FURNACE_MODE, 1);

        ItemStackHandler inventory = new ItemStackHandler(26);
        inventory.setStackInSlot(0, itemWithCount("tfc:metal/ingot/copper", 9));
        inventory.setStackInSlot(1, crafted);
        NonNullList<ItemStack> additives = NonNullList.withSize(4, ItemStack.EMPTY);
        additives.set(0, item("tfc:metal/ingot/zinc"));
        RecipeHolder<ArcFurnaceRecipe> selected = ArcFurnaceRecipeSelection.find(helper.getLevel(),
                inventory.getStackInSlot(0), additives, inventory);
        helper.assertTrue(selected != null && selected.id().equals(id("tfie:arcfurnace/alloy/brass")),
                "Mode 1 Copper and Zinc must select Brass regardless of recipe order");
        crafted.set(ModDataComponents.ARC_FURNACE_MODE, 3);
        helper.assertTrue(ArcFurnaceRecipeSelection.find(helper.getLevel(), inventory.getStackInSlot(0),
                        additives, inventory) == null,
                "Mode 3 Copper must wait for Bismuth instead of falling back to Brass");
        inventory.setStackInSlot(2, crafted.copy());
        helper.assertTrue(ArcFurnaceRecipeSelection.find(helper.getLevel(), inventory.getStackInSlot(0),
                        additives, inventory) == null,
                "Multiple Blueprints must stop new Arc processes");
        helper.assertTrue(ArcFurnaceRecipeSelection.stopReason(helper.getLevel(), inventory::getStackInSlot)
                        == ArcFurnaceRecipeSelection.StopReason.MULTIPLE_BLUEPRINTS,
                "The Arc screen must report multiple Blueprints");
        inventory.setStackInSlot(2, ItemStack.EMPTY);
        crafted.set(ModDataComponents.ARC_FURNACE_MODE, 0);
        helper.assertTrue(ArcFurnaceRecipeSelection.stopReason(helper.getLevel(), inventory::getStackInSlot)
                        == ArcFurnaceRecipeSelection.StopReason.INVALID_MODE,
                "The Arc screen must report an out-of-range Blueprint mode");

        ArcFurnaceLogic.State bismuthBronze = completeArcProcess(helper,
                arcRecipeHolder(helper, "tfie:arcfurnace/alloy/bismuth_bronze"), 3,
                itemWithCount("tfc:metal/ingot/copper", 9),
                List.of(item("tfc:metal/ingot/zinc"), item("tfc:metal/ingot/bismuth")));
        helper.assertTrue(bismuthBronze.getInventory().getStackInSlot(0).getCount() == 6
                        && bismuthBronze.getInventory().getStackInSlot(16).getCount() == 5,
                "Adding Bismuth in mode 3 must consume only three Copper and produce five Bismuth Bronze");
        ArcFurnaceLogic.State brass = completeArcProcess(helper,
                arcRecipeHolder(helper, "tfie:arcfurnace/alloy/brass"), 1,
                itemWithCount("tfc:metal/ingot/copper", 9),
                List.of(item("tfc:metal/ingot/zinc"), item("tfc:metal/ingot/bismuth")));
        helper.assertTrue(brass.getInventory().getStackInSlot(16).getCount() == 10
                        && brass.getInventory().getStackInSlot(13).is(item("tfc:metal/ingot/bismuth").getItem()),
                "Mode 1 must produce Brass while leaving excess Bismuth untouched");
        ArcFurnaceLogic.State constantan = completeArcProcess(helper,
                arcRecipeHolder(helper, "tfie:arcfurnace/alloy/constantan"), 5,
                itemWithCount("tfc:metal/ingot/copper", 9),
                List.of(item("tfc:metal/ingot/nickel"), item("tfc:metal/ingot/zinc")));
        helper.assertTrue(constantan.getInventory().getStackInSlot(0).getCount() == 8
                        && constantan.getInventory().getStackInSlot(13).is(item("tfc:metal/ingot/zinc").getItem())
                        && constantan.getInventory().getStackInSlot(16).getCount() == 2,
                "Mode 5 must consume one Copper and Nickel while leaving excess Zinc untouched");

        ItemStackHandler slots = new ItemStackHandler(26);
        IESlot.ArcInput firstSlot = new IESlot.ArcInput(slots, 0, 0, 0, helper.getLevel());
        IESlot.ArcInput secondSlot = new IESlot.ArcInput(slots, 1, 0, 0, helper.getLevel());
        ItemStack validBlueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        helper.assertTrue(firstSlot.mayPlace(validBlueprint),
                "A player must be able to place the first Blueprint in a normal main input slot");
        slots.setStackInSlot(0, validBlueprint);
        helper.assertTrue(firstSlot.mayPlace(validBlueprint.copy()) && !secondSlot.mayPlace(validBlueprint.copy()),
                "Same-slot Blueprint replacement must work while a second main-slot Blueprint is rejected");
        ArcFurnaceInputHandler automation = new ArcFurnaceInputHandler(slots, () -> {
        });
        helper.assertTrue(automation.insertItem(0, validBlueprint.copy(), false).is(validBlueprint.getItem())
                        && slots.getStackInSlot(0).getCount() == 1,
                "Arc automation must reject Blueprint insertion without changing the existing Blueprint");
        for(int slot = 1; slot < 12; slot++)
            slots.setStackInSlot(slot, item("minecraft:cobblestone"));
        helper.assertTrue(slots.getStackInSlot(0).is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && java.util.stream.IntStream.range(1, 12)
                        .allMatch(slot -> !slots.getStackInSlot(slot).isEmpty()),
                "An installed Blueprint must leave exactly eleven main material slots");

        player.getInventory().clearContent();
        ItemStack shifted = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        player.getInventory().setItem(9, shifted);
        ArcFurnaceMenu menu = ArcFurnaceMenu.makeClient(IEMenuTypes.ARC_FURNACE.getType(), 0,
                player.getInventory());
        helper.assertTrue(menu.quickMoveStack(player, 26).is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && menu.getSlot(0).getItem().is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && player.getInventory().getItem(9).isEmpty(),
                "Shift-click must place the first Blueprint through the real Arc menu path");
        ItemStack secondBlueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        secondBlueprint.set(ModDataComponents.ARC_FURNACE_MODE, 5);
        player.getInventory().setItem(10, secondBlueprint);
        helper.assertTrue(menu.quickMoveStack(player, 27).isEmpty()
                        && player.getInventory().getItem(10).is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && menu.getSlot(1).getItem().isEmpty(),
                "Shift-click must reject a second Blueprint through the real Arc menu path");
        menu.setCarried(secondBlueprint.copy());
        menu.clicked(0, 0, net.minecraft.world.inventory.ClickType.PICKUP, player);
        helper.assertTrue(menu.getSlot(0).getItem().get(ModDataComponents.ARC_FURNACE_MODE) == 5
                        && menu.getCarried().get(ModDataComponents.ARC_FURNACE_MODE) == 1,
                "A normal menu click must allow replacing the Blueprint in its existing main slot");
        menu.clicked(1, 0, net.minecraft.world.inventory.ClickType.PICKUP, player);
        helper.assertTrue(menu.getSlot(1).getItem().isEmpty()
                        && menu.getCarried().is(ModItems.ARC_FURNACE_BLUEPRINT.get()),
                "A normal menu click must reject the carried second Blueprint");
        player.getInventory().clearContent();
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void arc_basic_processing_and_persistence_contract(GameTestHelper helper)
    {
        String ore = isDisabledMaterialsRun() ? "magnetite" : "hematite";
        RecipeHolder<ArcFurnaceRecipe> briquetHolder = arcRecipeHolder(helper,
                "tfie:arcfurnace/briquet/" + ore);
        ModeAwareArcFurnaceRecipe briquet = cast(helper, briquetHolder.value(),
                ModeAwareArcFurnaceRecipe.class, briquetHolder.id().toString());
        ItemStackHandler inventory = new ItemStackHandler(26);
        ItemStack input = item("tfcm:ore_processing/briquet/" + ore);
        inventory.setStackInSlot(0, input);
        NonNullList<ItemStack> additives = NonNullList.withSize(4, ItemStack.EMPTY);
        for(int mode = 0; mode <= 8; mode++)
        {
            if(mode == 0)
                inventory.setStackInSlot(1, ItemStack.EMPTY);
            else
            {
                ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
                blueprint.set(ModDataComponents.ARC_FURNACE_MODE, mode);
                inventory.setStackInSlot(1, blueprint);
            }
            RecipeHolder<ArcFurnaceRecipe> selected = ArcFurnaceRecipeSelection.find(helper.getLevel(), input,
                    additives, inventory);
            helper.assertTrue(selected != null && selected.value() == briquet,
                    "Briquet processing must remain available without a Blueprint and in every mode");
        }
        ItemStack saved = inventory.getStackInSlot(1).copy();
        helper.assertTrue(saved.get(ModDataComponents.ARC_FURNACE_MODE) == 8 && saved.getCount() == 1,
                "Blueprint mode and count must survive inventory copying and machine persistence");

        ArcFurnaceProcess process = new ArcFurnaceProcess(briquetHolder, 42L, 0, 12, 13, 14, 15);
        process.processTick = 37;
        process.setInputAmounts(1, 0, 0, 0, 0);
        CompoundTag savedProcess = new CompoundTag();
        savedProcess.putString("recipe", briquetHolder.id().toString());
        savedProcess.putInt("process_processTick", process.processTick);
        process.writeExtraDataToNBT(savedProcess, helper.getLevel().registryAccess());
        helper.assertTrue(!savedProcess.getString("tfie_recipe_snapshot").isEmpty()
                        && savedProcess.getInt("process_processTick") == 37
                        && savedProcess.getIntArray("process_inputAmounts")[0] == 1,
                "Arc process save data must retain the recipe snapshot, progress and reservations");
        ArcFurnaceProcess loaded = new ArcFurnaceProcess(
                (level, id) -> ArcFurnaceRecipeLookup.getById(level, id), savedProcess);
        CompoundTag savedAgain = new CompoundTag();
        loaded.writeExtraDataToNBT(savedAgain, helper.getLevel().registryAccess());
        helper.assertTrue(savedAgain.getString("tfie_recipe_snapshot")
                        .equals(savedProcess.getString("tfie_recipe_snapshot"))
                        && loaded.processTick == 37 && loaded.getInputAmounts()[0] == 1,
                "Arc process reload must preserve the original definition and reservation state");

        ArcFurnaceLogic logic = new ArcFurnaceLogic();
        TestInitialContext<ArcFurnaceLogic.State> initial = new TestInitialContext<>(helper.getLevel());
        ArcFurnaceLogic.State state = logic.createInitialState(initial);
        state.getInventory().setStackInSlot(0, input.copy());
        state.getInventory().setStackInSlot(1, saved.copy());
        state.getEnergy().receiveEnergy(state.getEnergy().getMaxEnergyStored(), false);
        for(int slot = 23; slot <= 25; slot++)
            state.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));
        ItemStack hotOutput = item("tfc:metal/ingot/copper");
        HeatCapability.setTemperature(hotOutput, 900);
        state.getInventory().setStackInSlot(16, hotOutput);
        CompoundTag machineSave = new CompoundTag();
        state.writeSaveNBT(machineSave, helper.getLevel().registryAccess());
        machineSave.put("processor", processList(process, helper));

        ArcFurnaceLogic.State reloaded = logic.createInitialState(initial);
        reloaded.readSaveNBT(machineSave, helper.getLevel().registryAccess());
        ArcFurnaceProcess reloadedProcess = (ArcFurnaceProcess)reloaded.getProcessQueue().getFirst();
        helper.assertTrue(reloadedProcess.processTick == 37 && reloadedProcess.getInputAmounts()[0] == 1
                        && reloaded.getInventory().getStackInSlot(0).getCount() == 1
                        && reloaded.getInventory().getStackInSlot(1).is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && reloaded.getInventory().getStackInSlot(1).get(ModDataComponents.ARC_FURNACE_MODE) == 8
                        && HeatCapability.getTemperature(reloaded.getInventory().getStackInSlot(16)) > 0,
                "Arc State NBT must retain inventory, mode, heat, progress and reservations");

        CompoundTag changedDefinition = machineSave.copy();
        changedDefinition.getList("processor", Tag.TAG_COMPOUND).getCompound(0)
                .putString("tfie_recipe_snapshot", "changed datapack definition");
        ArcFurnaceLogic.State waiting = logic.createInitialState(initial);
        waiting.readSaveNBT(changedDefinition, helper.getLevel().registryAccess());
        ArcFurnaceProcess waitingProcess = (ArcFurnaceProcess)waiting.getProcessQueue().getFirst();
        TestMultiblockContext<ArcFurnaceLogic.State> waitingContext = context(waiting, helper.getLevel(),
                Blocks.AIR.defaultBlockState());
        logic.tickServer(waitingContext);
        helper.assertTrue(waitingProcess.processTick == 37
                        && waiting.getInventory().getStackInSlot(0).getCount() == 1,
                "A changed or removed datapack definition must leave the old Arc process waiting without consumption");

        ArcFurnaceLogic.State restored = logic.createInitialState(initial);
        restored.readSaveNBT(machineSave, helper.getLevel().registryAccess());
        helper.assertTrue(((net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess)
                        restored.getProcessQueue().getFirst()).tfie$matchesCurrentRecipe(helper.getLevel()),
                "Restoring the original recipe definition must make the saved Arc process resumable");
        List<ItemStack> dismantled = new ArrayList<>();
        logic.dropExtraItems(waiting, dismantled::add);
        helper.assertTrue(dismantled.stream().filter(stack -> stack.is(ModItems.ARC_FURNACE_BLUEPRINT.get()))
                        .mapToInt(ItemStack::getCount).sum() == 1
                        && dismantled.stream().filter(stack -> stack.is(input.getItem()))
                        .mapToInt(ItemStack::getCount).sum() == 1,
                "Dismantling a waiting Arc must recover its one Blueprint and reserved physical input");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 400)
    public static void arc_block_entity_and_datapack_reload_lifecycle(GameTestHelper helper)
    {
        FormedMachine<ArcFurnaceLogic.State> formed = form(helper, new ArcFurnaceMultiblock(),
                new BlockPos(513, 2, 513), new BlockPos(2, 0, 4), new BlockPos(2, 1, 2));
        ChunkPos formedChunk = new ChunkPos(formed.master());
        helper.getLevel().setChunkForced(formedChunk.x, formedChunk.z, true);
        ArcFurnaceLogic.State state = formed.state();
        ItemStack input = item("tfcm:ore_processing/briquet/magnetite");
        state.getInventory().setStackInSlot(0, input.copy());
        state.getInventory().setStackInSlot(2, input.copy());
        ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        blueprint.set(ModDataComponents.ARC_FURNACE_MODE, 8);
        state.getInventory().setStackInSlot(1, blueprint);
        for(int slot = 23; slot <= 25; slot++)
            state.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));
        state.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        ItemStack hotOutput = item("tfc:metal/ingot/copper");
        HeatCapability.setTemperature(hotOutput, 900);
        state.getInventory().setStackInSlot(16, hotOutput);
        for(int slot = 17; slot <= 21; slot++)
            state.getInventory().setStackInSlot(slot, itemWithCount("minecraft:cobblestone", 64));
        ArcFurnaceProcess running = new ArcFurnaceProcess(
                arcRecipeHolder(helper, "tfie:arcfurnace/briquet/magnetite"), 17L, 0, 12, 13, 14, 15);
        running.processTick = 37;
        running.setInputAmounts(1, 0, 0, 0, 0);
        ArcFurnaceProcess outputWaiting = new ArcFurnaceProcess(
                arcRecipeHolder(helper, "tfie:arcfurnace/briquet/magnetite"), 18L, 2, 12, 13, 14, 15);
        outputWaiting.processTick = outputWaiting.getMaxTicks(helper.getLevel()) - 1;
        outputWaiting.setInputAmounts(1, 0, 0, 0, 0);
        CompoundTag stateTag = new CompoundTag();
        state.writeSaveNBT(stateTag, helper.getLevel().registryAccess());
        ListTag processTags = processList(running, helper);
        processTags.add(processList(outputWaiting, helper).getCompound(0));
        stateTag.put("processor", processTags);
        state.readSaveNBT(stateTag, helper.getLevel().registryAccess());
        MinecraftServer server = helper.getLevel().getServer();
        ArcFurnaceProcess savedRunning = (ArcFurnaceProcess)state.getProcessQueue().get(0);
        ArcFurnaceProcess savedWaiting = (ArcFurnaceProcess)state.getProcessQueue().get(1);
        int waitingProgress = savedWaiting.processTick;
        int energyBeforeWaitingTick = state.getEnergy().getEnergyStored();
        formed.helper().tickServer();
        int runningProgress = savedRunning.processTick;
        int processEnergy = savedRunning.getRecipe(helper.getLevel()).getTotalProcessEnergy()
                / savedRunning.getMaxTicks(helper.getLevel());
        helper.assertTrue(savedWaiting.processTick == waitingProgress
                        && energyBeforeWaitingTick - state.getEnergy().getEnergyStored() == processEnergy
                        && state.getInventory().getStackInSlot(2).getCount() == 1,
                "The full-output Arc process must wait without progress, FE or reserved-input consumption");
        server.saveEverything(false, true, false);
        helper.getLevel().setChunkForced(formedChunk.x, formedChunk.z, false);
        waitForChunkUnload(helper, formedChunk, 100, () ->
        {
            helper.getLevel().getChunk(formedChunk.x, formedChunk.z);
            helper.getLevel().setChunkForced(formedChunk.x, formedChunk.z, true);
            BlockEntity loadedMaster = helper.getLevel().getBlockEntity(formed.master());
            helper.assertTrue(loadedMaster != null,
                    "The formed Arc master must reload after its chunk is unloaded");
            FormedMachine<ArcFurnaceLogic.State> reloaded = formedFromMaster(helper, loadedMaster,
                    formed.origin(), formed.master());
            ArcFurnaceProcess reloadedRunning = (ArcFurnaceProcess)reloaded.state().getProcessQueue().get(0);
            ArcFurnaceProcess reloadedWaiting = (ArcFurnaceProcess)reloaded.state().getProcessQueue().get(1);
            helper.assertTrue(reloadedRunning.processTick == runningProgress
                            && reloadedWaiting.processTick == waitingProgress
                            && reloadedRunning.getInputAmounts()[0] == 1
                            && reloadedWaiting.getInputAmounts()[0] == 1
                            && reloaded.state().getInventory().getStackInSlot(0).getCount() == 1
                            && reloaded.state().getInventory().getStackInSlot(2).getCount() == 1
                            && reloaded.state().getInventory().getStackInSlot(1)
                            .get(ModDataComponents.ARC_FURNACE_MODE) == 8
                            && HeatCapability.getTemperature(
                            reloaded.state().getInventory().getStackInSlot(16)) > 0,
                    "A real chunk unload and disk reload must retain running and output-waiting processes, "
                            + "reservations, inventory, mode and heat");
            int energyBeforeReloadedTick = reloaded.state().getEnergy().getEnergyStored();
            reloaded.helper().tickServer();
            helper.assertTrue(reloadedWaiting.processTick == waitingProgress
                            && energyBeforeReloadedTick - reloaded.state().getEnergy().getEnergyStored()
                            == processEnergy
                            && reloaded.state().getInventory().getStackInSlot(2).getCount() == 1,
                    "The reloaded output-waiting process must not consume extra FE or input");
            reloaded.state().getInventory().setStackInSlot(21, ItemStack.EMPTY);

        List<String> originalPacks = List.copyOf(server.getPackRepository().getSelectedIds());
        Path pack = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("tfie-gametest-arc-reload");
        writeArcReloadPack(pack, false);
        server.getPackRepository().reload();
        String packId = server.getPackRepository().getAvailableIds().stream()
                .filter(value -> value.contains("tfie-gametest-arc-reload"))
                .findFirst().orElseThrow();
        List<String> changedPacks = new ArrayList<>(originalPacks);
        changedPacks.add(packId);
        whenDone(helper, server.reloadResources(changedPacks), () ->
        {
            RecipeLedgerGameTests.assertReloadedLedger(helper);
            RecipeLedgerGameTests.assertNamespaceControl(helper);
            ModeAwareArcFurnaceRecipe changed = recipe(helper,
                    "tfie:arcfurnace/briquet/magnetite", ModeAwareArcFurnaceRecipe.class);
            helper.assertTrue(changed.getTotalProcessTime() == 101
                            && changed.getTotalProcessEnergy() == 51201
                            && changed.getBaseOutputs().getFirst().getCount() == 2,
                    "The higher-priority datapack must actually replace the Arc recipe definition");
            int runningReloadProgress = reloadedRunning.processTick;
            int waitingReloadProgress = reloadedWaiting.processTick;
            int energy = reloaded.state().getEnergy().getEnergyStored();
            reloaded.helper().tickServer();
            helper.assertTrue(reloadedRunning.processTick == runningReloadProgress
                            && reloadedWaiting.processTick == waitingReloadProgress
                            && reloaded.state().getEnergy().getEnergyStored() == energy
                            && reloaded.state().getInventory().getStackInSlot(0).getCount() == 1
                            && reloaded.state().getInventory().getStackInSlot(2).getCount() == 1,
                    "A changed datapack recipe must leave the old formed-Arc process waiting without consumption");
            ItemStackHandler duplicateInventory = new ItemStackHandler(26);
            duplicateInventory.setStackInSlot(0, itemWithCount("tfc:metal/ingot/copper", 9));
            ItemStack modeOne = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
            duplicateInventory.setStackInSlot(1, modeOne);
            NonNullList<ItemStack> zinc = NonNullList.withSize(4, ItemStack.EMPTY);
            zinc.set(0, item("tfc:metal/ingot/zinc"));
            helper.assertTrue(ArcFurnaceRecipeSelection.find(helper.getLevel(),
                            duplicateInventory.getStackInSlot(0), zinc, duplicateInventory) == null,
                    "A datapack duplicate with the same mode and main input must stop selection");

            disableReloadProbe(pack);
            whenDone(helper, server.reloadResources(changedPacks), () ->
            {
                RecipeLedgerGameTests.assertReloadedLedger(helper);
                RecipeLedgerGameTests.assertNamespaceControl(helper);
                helper.assertTrue(optionalRecipe(helper, "tfie:arcfurnace/briquet/magnetite") == null,
                        "The datapack must actually remove the running process recipe");
                reloaded.helper().tickServer();
                helper.assertTrue(reloadedRunning.processTick == runningReloadProgress
                                && reloadedWaiting.processTick == waitingReloadProgress
                                && reloaded.state().getInventory().getStackInSlot(0).getCount() == 1
                                && reloaded.state().getInventory().getStackInSlot(2).getCount() == 1,
                        "A removed datapack recipe must preserve the old process and reserved input");

                deletePack(pack);
                server.getPackRepository().reload();
                whenDone(helper, server.reloadResources(originalPacks), () ->
                {
                    RecipeLedgerGameTests.assertReloadedLedger(helper);
                    RecipeLedgerGameTests.assertNamespaceControlRemoved(helper);
                    helper.assertTrue(((net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess)
                                    reloadedRunning).tfie$matchesCurrentRecipe(helper.getLevel())
                                    && ((net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess)
                                    reloadedWaiting).tfie$matchesCurrentRecipe(helper.getLevel()),
                            "Restoring the original datapack definition must resume the saved Arc process");
                    reloaded.helper().tickServer();
                    reloaded.helper().tickServer();
                    helper.assertTrue(!reloaded.state().getInventory().getStackInSlot(21).isEmpty()
                                    && reloaded.state().getInventory().getStackInSlot(2).isEmpty(),
                            "Restoring the original definition must resume and commit the waiting process: output="
                                    + reloaded.state().getInventory().getStackInSlot(21) + ", input="
                                    + reloaded.state().getInventory().getStackInSlot(2) + ", progress="
                                    + reloadedWaiting.processTick + ", queue="
                                    + reloaded.state().getProcessQueue().size());
                    new ArcFurnaceLogic().dropExtraItems(reloaded.state(), stack ->
                            Block.popResource(helper.getLevel(), reloaded.master(), stack));
                    reloaded.helper().disassemble();
                    helper.runAfterDelay(5, () ->
                    {
                        List<ItemStack> recovered = helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                                        new AABB(reloaded.origin()).inflate(8)).stream()
                                .map(ItemEntity::getItem).toList();
                        helper.assertTrue(recovered.stream().anyMatch(stack -> stack.is(input.getItem()))
                                        && recovered.stream().anyMatch(stack -> stack.is(
                                        ModItems.ARC_FURNACE_BLUEPRINT.get())),
                                "Dismantling the restored formed Arc must recover its input and Blueprint");
                        helper.getLevel().setChunkForced(formedChunk.x, formedChunk.z, false);
                        helper.succeed();
                    });
                });
            });
        });
        });
    }

    public static void runArcServerRestartPersistence(GameTestHelper helper)
    {
        switch(System.getProperty("tfie.gametest.restartPhase", ""))
        {
            case "prepare" -> prepareArcServerRestart(helper);
            case "verify" -> verifyArcServerRestart(helper);
            default -> helper.fail("The Arc restart GameTest requires a prepare or verify phase");
        }
    }

    private static void prepareArcServerRestart(GameTestHelper helper)
    {
        BlockPos testOrigin = helper.absolutePos(BlockPos.ZERO);
        FormedMachine<ArcFurnaceLogic.State> formed = form(helper, new ArcFurnaceMultiblock(),
                ARC_RESTART_ORIGIN.subtract(testOrigin), new BlockPos(2, 0, 4), new BlockPos(2, 1, 2));
        ArcFurnaceLogic.State state = formed.state();
        ItemStack input = item("tfcm:ore_processing/briquet/magnetite");
        state.getInventory().setStackInSlot(0, input.copy());
        state.getInventory().setStackInSlot(2, input.copy());
        ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        blueprint.set(ModDataComponents.ARC_FURNACE_MODE, 8);
        state.getInventory().setStackInSlot(1, blueprint);
        for(int slot = 23; slot <= 25; slot++)
            state.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));
        state.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        ItemStack hotOutput = item("tfc:metal/ingot/copper");
        HeatCapability.setTemperature(hotOutput, 900);
        state.getInventory().setStackInSlot(16, hotOutput);
        for(int slot = 17; slot <= 21; slot++)
            state.getInventory().setStackInSlot(slot, itemWithCount("minecraft:cobblestone", 64));
        ArcFurnaceProcess running = new ArcFurnaceProcess(
                arcRecipeHolder(helper, "tfie:arcfurnace/briquet/magnetite"), 27L, 0, 12, 13, 14, 15);
        running.processTick = 37;
        running.setInputAmounts(1, 0, 0, 0, 0);
        ArcFurnaceProcess outputWaiting = new ArcFurnaceProcess(
                arcRecipeHolder(helper, "tfie:arcfurnace/briquet/magnetite"), 28L, 2, 12, 13, 14, 15);
        outputWaiting.processTick = outputWaiting.getMaxTicks(helper.getLevel()) - 1;
        outputWaiting.setInputAmounts(1, 0, 0, 0, 0);
        CompoundTag stateTag = new CompoundTag();
        state.writeSaveNBT(stateTag, helper.getLevel().registryAccess());
        ListTag processTags = processList(running, helper);
        processTags.add(processList(outputWaiting, helper).getCompound(0));
        stateTag.put("processor", processTags);
        state.readSaveNBT(stateTag, helper.getLevel().registryAccess());

        BlockPos stopPosition = ARC_RESTART_ORIGIN.offset(-1, 1, 4);
        helper.getLevel().setBlockAndUpdate(stopPosition, Blocks.REDSTONE_BLOCK.defaultBlockState());
        ChunkPos chunk = new ChunkPos(formed.master());
        helper.getLevel().setChunkForced(chunk.x, chunk.z, true);
        helper.getLevel().getChunkAt(formed.master()).setUnsaved(true);
        MinecraftServer server = helper.getLevel().getServer();
        try
        {
            Files.writeString(server.getWorldPath(LevelResource.ROOT).resolve("tfie-gametest-restart.pid"),
                    Long.toString(ProcessHandle.current().pid()), StandardCharsets.UTF_8);
        }
        catch(IOException exception)
        {
            throw new IllegalStateException("Unable to write the Arc restart process marker", exception);
        }
        server.saveEverything(false, true, false);
        helper.succeed();
    }

    private static void verifyArcServerRestart(GameTestHelper helper)
    {
        MinecraftServer server = helper.getLevel().getServer();
        try
        {
            long prepareProcess = Long.parseLong(Files.readString(
                    server.getWorldPath(LevelResource.ROOT).resolve("tfie-gametest-restart.pid"),
                    StandardCharsets.UTF_8));
            helper.assertTrue(prepareProcess != ProcessHandle.current().pid(),
                    "The Arc persistence verification must run after a real server process restart");
        }
        catch(IOException exception)
        {
            throw new IllegalStateException("Unable to read the Arc restart process marker", exception);
        }

        BlockPos masterPosition = ARC_RESTART_ORIGIN.offset(2, 1, 2);
        ChunkPos chunk = new ChunkPos(masterPosition);
        helper.getLevel().setChunkForced(chunk.x, chunk.z, true);
        helper.getLevel().getChunk(chunk.x, chunk.z);
        BlockEntity master = helper.getLevel().getBlockEntity(masterPosition);
        helper.assertTrue(master != null, "The formed Arc master must load from the previous server process");
        FormedMachine<ArcFurnaceLogic.State> formed = formedFromMaster(helper, master,
                ARC_RESTART_ORIGIN, masterPosition);
        ArcFurnaceLogic.State state = formed.state();
        helper.assertTrue(state.getProcessQueue().size() == 2,
                "A running and an output-waiting Arc process must survive the server restart");
        ArcFurnaceProcess running = (ArcFurnaceProcess)state.getProcessQueue().get(0);
        ArcFurnaceProcess outputWaiting = (ArcFurnaceProcess)state.getProcessQueue().get(1);
        int waitingProgress = outputWaiting.getMaxTicks(helper.getLevel()) - 1;
        helper.assertTrue(running.processTick == 37
                        && outputWaiting.processTick == waitingProgress
                        && running.getInputAmounts()[0] == 1
                        && outputWaiting.getInputAmounts()[0] == 1
                        && state.getInventory().getStackInSlot(0).getCount() == 1
                        && state.getInventory().getStackInSlot(2).getCount() == 1
                        && state.getInventory().getStackInSlot(1).is(ModItems.ARC_FURNACE_BLUEPRINT.get())
                        && state.getInventory().getStackInSlot(1)
                        .get(ModDataComponents.ARC_FURNACE_MODE) == 8
                        && HeatCapability.getTemperature(state.getInventory().getStackInSlot(16)) > 0,
                "A server restart must retain Arc recipes, progress, reservations, inventory, Blueprint mode, "
                        + "waiting state and normally cooling Heat");
        helper.assertTrue(((net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess)running)
                        .tfie$matchesCurrentRecipe(helper.getLevel())
                        && ((net.claustra01.tfie.common.arc.ArcFurnaceProcessSnapshotAccess)outputWaiting)
                        .tfie$matchesCurrentRecipe(helper.getLevel()),
                "Restarted Arc processes must retain and validate their saved recipe snapshots");

        helper.getLevel().removeBlock(ARC_RESTART_ORIGIN.offset(-1, 1, 4), false);
        int energy = state.getEnergy().getEnergyStored();
        int processEnergy = running.getRecipe(helper.getLevel()).getTotalProcessEnergy()
                / running.getMaxTicks(helper.getLevel());
        formed.helper().tickServer();
        helper.assertTrue(running.processTick == 38
                        && outputWaiting.processTick == waitingProgress
                        && energy - state.getEnergy().getEnergyStored() == processEnergy
                        && state.getInventory().getStackInSlot(2).getCount() == 1,
                "After restart, the running process must resume while the full-output process waits without "
                        + "extra FE or input consumption");
        helper.getLevel().setChunkForced(chunk.x, chunk.z, false);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void blast_furnace_recipe_fuel_and_refining_table(GameTestHelper helper)
    {
        boolean disabledMaterials = isDisabledMaterialsRun();
        for(String ore : new String[]{"hematite", "magnetite", "limonite"})
        {
            if(ore.equals("hematite") && disabledMaterials)
            {
                helper.assertTrue(optionalRecipe(helper, "tfie:blastfurnace/briquet/hematite") == null,
                        "Disabled Hematite must not have a Blast Furnace recipe");
                continue;
            }
            BlastFurnaceRecipe recipe = recipe(helper, "tfie:blastfurnace/briquet/" + ore,
                    BlastFurnaceRecipe.class);
            ItemStack output = recipe.output.get();
            helper.assertTrue(recipe.input.getCount() == 1
                            && recipe.input.test(item("tfcm:ore_processing/briquet/" + ore))
                            && recipe.time == 1200
                            && output.is(item("tfc:raw_iron_bloom").getItem()) && output.getCount() == 1
                            && recipe.slag.get().isEmpty(),
                    ore + " must produce one Raw Iron Bloom without slag in 1200 ticks");
        }
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        item("immersiveengineering:coal_coke")) == 1200,
                "A single Coke must burn for 1200 ticks");
        helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(),
                        new ItemStack(Items.CHARCOAL)) == 300,
                "A single Charcoal must burn for 300 ticks");
        for(String rejected : new String[]{"immersiveengineering:coke", "minecraft:coal_block",
                "minecraft:coal", "minecraft:charcoal"})
            if(!rejected.equals("minecraft:charcoal"))
                helper.assertTrue(BlastFurnaceFuel.getBlastFuelTime(helper.getLevel(), item(rejected)) == 0,
                        rejected + " must not be a Blast Furnace fuel");
        for(String deleted : new String[]{"steel", "steel_block", "fuel_coke_block", "fuel_charcoal_block"})
            helper.assertTrue(optionalRecipe(helper, "immersiveengineering:blastfurnace/" + deleted) == null,
                    "Deleted Blast Furnace recipe remains: " + deleted);
        for(String rejected : new String[]{"tfc:metal/ingot/wrought_iron",
                "tfcm:ore_processing/briquet/native_copper"})
            helper.assertTrue(BlastFurnaceRecipe.findRecipe(helper.getLevel(), item(rejected), null) == null,
                    rejected + " must not start a Blast Furnace recipe");
        AnvilRecipe refined = recipe(helper, "tfc:anvil/refined_iron_bloom", AnvilRecipe.class);
        AnvilRecipe wrought = recipe(helper, "tfc:anvil/metal/ingot/wrought_iron", AnvilRecipe.class);
        helper.assertTrue(refined.getInput()
                        .test(item("tfc:raw_iron_bloom"))
                        && refined.getResultItem(helper.getLevel().registryAccess())
                        .is(item("tfc:refined_iron_bloom").getItem())
                        && wrought.getInput()
                        .test(item("tfc:refined_iron_bloom"))
                        && wrought.getResultItem(helper.getLevel().registryAccess())
                        .is(item("tfc:metal/ingot/wrought_iron").getItem()),
                "TFC Raw Bloom must retain both normal anvil refining stages to Wrought Iron");
        HeatingRecipe bloomHeating = HeatingRecipe.getRecipe(item("tfc:raw_iron_bloom"));
        helper.assertTrue(bloomHeating != null
                        && bloomHeating.assembleFluid(item("tfc:raw_iron_bloom")).getAmount() == 100,
                "Raw Iron Bloom must retain its 100 mB TFC metal content");
        assertBloomRefiningInAnvils(helper);
        helper.succeed();
    }

    private static boolean isDisabledMaterialsRun()
    {
        return "disabled-materials".equals(System.getProperty("tfie.gametest.expectedMod"));
    }

    private static ItemStack assertArcCommitAndWaiting(GameTestHelper helper)
    {
        ArcFurnaceLogic logic = new ArcFurnaceLogic();
        TestInitialContext<ArcFurnaceLogic.State> initial = new TestInitialContext<>(helper.getLevel());
        ArcFurnaceLogic.State state = logic.createInitialState(initial);
        TestMultiblockContext<ArcFurnaceLogic.State> context = context(state, helper.getLevel(),
                Blocks.AIR.defaultBlockState());
        state.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        state.getInventory().setStackInSlot(0, item("tfc:metal/ingot/wrought_iron"));
        ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
        blueprint.set(ModDataComponents.ARC_FURNACE_MODE, 1);
        state.getInventory().setStackInSlot(1, blueprint);
        state.getInventory().setStackInSlot(12, item("immersiveengineering:dust_coke"));
        for(int slot = 23; slot <= 25; slot++)
            state.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));

        RecipeHolder<ArcFurnaceRecipe> holder = arcRecipeHolder(helper, "tfie:arcfurnace/steel");
        ArcFurnaceProcess process = new ArcFurnaceProcess(holder, 42L, 0, 12, 13, 14, 15);
        process.setInputAmounts(1, 1, 0, 0, 0);
        process.processTick = process.getMaxTicks(helper.getLevel()) - 1;
        CompoundTag savedState = new CompoundTag();
        state.writeSaveNBT(savedState, helper.getLevel().registryAccess());
        savedState.put("processor", processList(process, helper));
        state.readSaveNBT(savedState, helper.getLevel().registryAccess());
        process = (ArcFurnaceProcess)state.getProcessQueue().getFirst();

        ItemStack existing = itemWithCount("tfc:metal/ingot/steel", 64);
        HeatCapability.setTemperature(existing, 700);
        state.getInventory().setStackInSlot(16, existing);
        for(int slot = 17; slot <= 21; slot++)
            state.getInventory().setStackInSlot(slot, itemWithCount("minecraft:cobblestone", 64));
        int energy = state.getEnergy().getEnergyStored();
        logic.tickServer(context);
        helper.assertTrue(process.processTick == process.getMaxTicks(helper.getLevel()) - 1
                        && state.getEnergy().getEnergyStored() == energy
                        && state.getInventory().getStackInSlot(0).getCount() == 1
                        && state.getInventory().getStackInSlot(12).getCount() == 1
                        && HeatCapability.getTemperature(existing) == 700,
                "A full normal Arc output bank must preserve progress, FE, materials and existing heat");

        for(int slot = 16; slot <= 21; slot++)
            state.getInventory().setStackInSlot(slot, ItemStack.EMPTY);
        state.getInventory().setStackInSlot(22, itemWithCount("minecraft:cobblestone", 64));
        logic.tickServer(context);
        helper.assertTrue(process.processTick == process.getMaxTicks(helper.getLevel()) - 1
                        && state.getEnergy().getEnergyStored() == energy,
                "A full Slag slot must stop Steel without consuming FE or using a normal output slot");

        state.getInventory().setStackInSlot(22, ItemStack.EMPTY);
        logic.tickServer(context);
        ItemStack output = state.getInventory().getStackInSlot(16);
        helper.assertTrue(output.is(item("tfc:metal/ingot/steel").getItem()) && output.getCount() == 1
                        && HeatCapability.getTemperature(output) > 0
                        && state.getInventory().getStackInSlot(22).is(item("immersiveengineering:slag").getItem())
                        && state.getInventory().getStackInSlot(22).getCount() == 1
                        && state.getInventory().getStackInSlot(0).isEmpty()
                        && state.getInventory().getStackInSlot(12).isEmpty()
                        && state.getInventory().getStackInSlot(1).is(ModItems.ARC_FURNACE_BLUEPRINT.get()),
                "Arc Steel must commit its heated output and Slag exactly once without consuming the Blueprint");
        return output;
    }

    private static void assertBlastFurnaceProcessing(GameTestHelper helper, boolean improved, String ore)
    {
        if(improved)
        {
            AdvBlastFurnaceLogic logic = new AdvBlastFurnaceLogic();
            TestInitialContext<AdvBlastFurnaceLogic.State> initial = new TestInitialContext<>(helper.getLevel());
            AdvBlastFurnaceLogic.State state = logic.createInitialState(initial);
            TestMultiblockContext<AdvBlastFurnaceLogic.State> context = context(state, helper.getLevel(),
                    blockState("immersiveengineering:blast_furnace"));
            runBlastFurnace(helper, state.getInventory(), state.getInventory(), state.getStateView(),
                    () -> logic.tickServer(context), ore, "Improved Blast Furnace");
            BlockPos relative = new BlockPos(25, 2, 25);
            BlockPos absolute = helper.absolutePos(relative);
            BlockState preheaterState = BuiltInRegistries.BLOCK.get(
                    id("immersiveengineering:blastfurnace_preheater")).defaultBlockState();
            BlastFurnacePreheaterBlockEntity preheater = new BlastFurnacePreheaterBlockEntity(
                    IEBlockEntities.BLASTFURNACE_PREHEATER.master(), absolute, preheaterState);
            helper.getLevel().setBlock(absolute, preheaterState, Block.UPDATE_ALL);
            helper.getLevel().setBlockEntity(preheater);
            preheater.energyStorage.receiveEnergy(preheater.energyStorage.getMaxEnergyStored(), false);
            ((TestMultiblockLevel)context.getLevel()).setBlockEntity(new BlockPos(-1, 0, 1), preheater);
            int stored = preheater.energyStorage.getEnergyStored();
            helper.assertTrue(state.getProcessSpeed(context.getLevel()) == 2
                            && preheater.active && preheater.energyStorage.getEnergyStored() < stored,
                    "An energized Improved Blast Furnace preheater must consume FE and double process speed");
        }
        else
        {
            BlastFurnaceLogic logic = new BlastFurnaceLogic();
            TestInitialContext<BlastFurnaceLogic.State> initial = new TestInitialContext<>(helper.getLevel());
            BlastFurnaceLogic.State state = logic.createInitialState(initial);
            TestMultiblockContext<BlastFurnaceLogic.State> context = context(state, helper.getLevel(),
                    blockState("immersiveengineering:blast_furnace"));
            runBlastFurnace(helper, state.getInventory(), state.getInventory(), state.getStateView(),
                    () -> logic.tickServer(context), ore, "Blast Furnace");
        }
    }

    private static void runBlastFurnace(GameTestHelper helper,
                                        net.neoforged.neoforge.items.IItemHandlerModifiable inventory,
                                        IItemHandler input,
                                        net.minecraft.world.inventory.ContainerData view, Runnable tick,
                                        String ore, String label)
    {
        helper.assertTrue(input.insertItem(0, item("tfcm:ore_processing/briquet/" + ore), false).isEmpty()
                        && input.insertItem(1, item("immersiveengineering:coal_coke"), false).isEmpty(),
                label + " must accept its Briquet and fuel through its exposed input handler");
        tick.run();
        tick.run();
        view.set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        inventory.setStackInSlot(2, itemWithCount("minecraft:cobblestone", 64));
        int burn = view.get(FurnaceHandler.StateView.BURN_TIME);
        tick.run();
        helper.assertTrue(view.get(FurnaceHandler.StateView.CURRENT_PROCESS) == 1
                        && view.get(FurnaceHandler.StateView.BURN_TIME) == burn
                        && inventory.getStackInSlot(0).getCount() == 1,
                label + " must wait without fuel or material use when its output is full");
        inventory.setStackInSlot(2, ItemStack.EMPTY);
        tick.run();
        ItemStack output = inventory.getStackInSlot(2);
        helper.assertTrue(inventory.getStackInSlot(0).isEmpty()
                        && output.is(item("tfc:raw_iron_bloom").getItem()) && output.getCount() == 1
                        && Math.abs(HeatCapability.getTemperature(output) - 1381.5f) < 0.01f
                        && inventory.getStackInSlot(3).isEmpty(),
                label + " must commit one 1381.5 C Raw Iron Bloom without Slag: input="
                        + inventory.getStackInSlot(0) + ", output=" + output + ", temperature="
                        + HeatCapability.getTemperature(output) + ", slag=" + inventory.getStackInSlot(3));
        helper.assertTrue(input.insertItem(0, item("tfcm:ore_processing/briquet/" + ore), false).isEmpty(),
                label + " must accept another Briquet through its input handler");
        tick.run();
        view.set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        ItemStack matchingOutput = item("tfc:raw_iron_bloom");
        FurnaceOutputHeat.apply(matchingOutput);
        inventory.setStackInSlot(2, matchingOutput);
        tick.run();
        helper.assertTrue(inventory.getStackInSlot(0).isEmpty()
                        && inventory.getStackInSlot(2).getCount() == 2,
                label + " must merge output with the same Heat Component within the stack limit");
    }

    private static void assertBloomRefiningInAnvils(GameTestHelper helper)
    {
        ServerPlayer player = FakePlayerFactory.get(helper.getLevel(), new GameProfile(
                UUID.nameUUIDFromBytes("tfie_bloom_refining".getBytes(StandardCharsets.UTF_8)),
                "tfie_bloom_refining"));
        ItemStack refined = completeBloomAnvilStage(helper, player, new BlockPos(18, 2, 18),
                "tfc:raw_iron_bloom", id("tfc:anvil/refined_iron_bloom"));
        helper.assertTrue(refined.is(item("tfc:refined_iron_bloom").getItem()),
                "The first real TFC anvil stage must produce Refined Iron Bloom");
        ItemStack wrought = completeBloomAnvilStage(helper, player, new BlockPos(21, 2, 18),
                "tfc:refined_iron_bloom", id("tfc:anvil/metal/ingot/wrought_iron"));
        helper.assertTrue(wrought.is(item("tfc:metal/ingot/wrought_iron").getItem()),
                "The second real TFC anvil stage must produce Wrought Iron");
        player.getInventory().clearContent();
    }

    private static ItemStack completeBloomAnvilStage(GameTestHelper helper, ServerPlayer player,
                                                      BlockPos position, String inputId,
                                                      ResourceLocation recipeId)
    {
        helper.setBlock(position, BuiltInRegistries.BLOCK.get(id("tfc:metal/anvil/bronze")));
        AnvilBlockEntity anvil = helper.getBlockEntity(position);
        ItemStack input = item(inputId);
        IHeat heat = HeatCapability.get(input);
        heat.setTemperature(heat.getWorkingTemperature());
        anvil.setInventoryFromOutsideWorld(input, item("tfc:metal/hammer/wrought_iron"), ItemStack.EMPTY);
        anvil.chooseRecipe(recipeId);
        Forging forging = anvil.getMainInputForging();
        forging.addStep(ForgeStep.HIT_LIGHT, forging.target() + 6);
        forging.addStep(ForgeStep.HIT_LIGHT);
        anvil.work(player, ForgeStep.HIT_LIGHT);
        return anvil.getInventory().getStackInSlot(0);
    }

    private static void assertAlloyKilnProcessing(GameTestHelper helper)
    {
        AlloySmelterLogic logic = new AlloySmelterLogic();
        TestInitialContext<AlloySmelterLogic.State> initial = new TestInitialContext<>(helper.getLevel());
        AlloySmelterLogic.State state = logic.createInitialState(initial);
        TestMultiblockContext<AlloySmelterLogic.State> context = context(state, helper.getLevel(),
                blockState("immersiveengineering:alloy_smelter"));
        state.getInventory().setStackInSlot(0, itemWithCount("tfc:metal/ingot/copper", 9));
        state.getInventory().setStackInSlot(1, item("tfc:metal/ingot/zinc"));
        state.getInventory().setStackInSlot(2, new ItemStack(Items.COAL));
        logic.tickServer(context);
        logic.tickServer(context);
        state.getStateView().set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        state.getInventory().setStackInSlot(3, itemWithCount("minecraft:cobblestone", 64));
        int burn = state.getStateView().get(FurnaceHandler.StateView.BURN_TIME);
        logic.tickServer(context);
        helper.assertTrue(state.getStateView().get(FurnaceHandler.StateView.CURRENT_PROCESS) == 1
                        && state.getStateView().get(FurnaceHandler.StateView.BURN_TIME) == burn
                        && state.getInventory().getStackInSlot(0).getCount() == 9,
                "Alloy Kiln must wait without fuel or input use when its output is full");
        state.getInventory().setStackInSlot(3, ItemStack.EMPTY);
        logic.tickServer(context);
        ItemStack output = state.getInventory().getStackInSlot(3);
        helper.assertTrue(output.is(item("tfc:metal/ingot/brass").getItem()) && output.getCount() == 10
                        && HeatCapability.getTemperature(output) > 0
                        && state.getInventory().getStackInSlot(0).isEmpty()
                        && state.getInventory().getStackInSlot(1).isEmpty(),
                "Alloy Kiln must use ordinary furnace fuel and commit heated Brass once");

        state.getInventory().setStackInSlot(0, itemWithCount("minecraft:glass", 2));
        state.getInventory().setStackInSlot(1, item("tfcm:metal/dust/wrought_iron"));
        state.getInventory().setStackInSlot(3, ItemStack.EMPTY);
        logic.tickServer(context);
        state.getStateView().set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        logic.tickServer(context);
        ItemStack glass = state.getInventory().getStackInSlot(3);
        helper.assertTrue(glass.is(item("immersiveengineering:insulating_glass").getItem())
                        && glass.getCount() == 2 && HeatCapability.getTemperature(glass) == 0,
                "The Alloy Kiln cache must switch from Brass to unheated Insulating Glass");
    }

    private static void runAlloyKiln(GameTestHelper helper, AlloySmelterLogic.State state,
                                     IItemHandler input, Runnable tick)
    {
        helper.assertTrue(input.insertItem(0, itemWithCount("tfc:metal/ingot/copper", 9), false).isEmpty()
                        && input.insertItem(1, item("tfc:metal/ingot/zinc"), false).isEmpty()
                        && input.insertItem(2, new ItemStack(Items.COAL), false).isEmpty(),
                "A formed Alloy Kiln must accept both materials and ordinary fuel through its input handler");
        tick.run();
        tick.run();
        state.getStateView().set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        state.getInventory().setStackInSlot(3, itemWithCount("minecraft:cobblestone", 64));
        int burn = state.getStateView().get(FurnaceHandler.StateView.BURN_TIME);
        tick.run();
        helper.assertTrue(state.getStateView().get(FurnaceHandler.StateView.CURRENT_PROCESS) == 1
                        && state.getStateView().get(FurnaceHandler.StateView.BURN_TIME) == burn
                        && state.getInventory().getStackInSlot(0).getCount() == 9,
                "A formed Alloy Kiln must preserve fuel and inputs while its output is full");
        state.getInventory().setStackInSlot(3, ItemStack.EMPTY);
        tick.run();
        ItemStack output = state.getInventory().getStackInSlot(3);
        helper.assertTrue(output.is(item("tfc:metal/ingot/brass").getItem()) && output.getCount() == 10
                        && Math.abs(HeatCapability.getTemperature(output)
                        - HeatCapability.getDefinition(output).forgingTemperature() / 0.6f * 0.9f) < 0.01f,
                "A formed Alloy Kiln must commit one heated Brass batch exactly once");
    }

    private static ItemStack completeAlloyProcess(GameTestHelper helper, AlloyExpected row)
    {
        AlloySmelterLogic logic = new AlloySmelterLogic();
        TestInitialContext<AlloySmelterLogic.State> initial = new TestInitialContext<>(helper.getLevel());
        AlloySmelterLogic.State state = logic.createInitialState(initial);
        TestMultiblockContext<AlloySmelterLogic.State> context = context(state, helper.getLevel(),
                blockState("immersiveengineering:alloy_smelter"));
        state.getInventory().setStackInSlot(0, itemWithCount(row.first, row.firstCount));
        state.getInventory().setStackInSlot(1, itemWithCount(row.second, row.secondCount));
        state.getInventory().setStackInSlot(2, new ItemStack(Items.COAL));
        logic.tickServer(context);
        logic.tickServer(context);
        state.getStateView().set(FurnaceHandler.StateView.CURRENT_PROCESS, 1);
        logic.tickServer(context);
        helper.assertTrue(state.getInventory().getStackInSlot(0).isEmpty()
                        && state.getInventory().getStackInSlot(1).isEmpty(),
                row.path + " must consume exactly its two Alloy Kiln inputs");
        return state.getInventory().getStackInSlot(3);
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void alloy_kiln_recipe_table_and_cache_boundary(GameTestHelper helper)
    {
        AlloyExpected[] rows = {
                new AlloyExpected("brass", "tfc:metal/ingot/copper", 9, "tfc:metal/ingot/zinc", 1,
                        "tfc:metal/ingot/brass", 10),
                new AlloyExpected("bronze", "tfc:metal/ingot/copper", 9, "tfc:metal/ingot/tin", 1,
                        "tfc:metal/ingot/bronze", 10),
                new AlloyExpected("constantan", "tfc:metal/ingot/copper", 1,
                        "tfc:metal/ingot/nickel", 1, "tfcm:metal/ingot/constantan", 2),
                new AlloyExpected("electrum", "tfc:metal/ingot/gold", 1, "tfc:metal/ingot/silver", 1,
                        "tfcm:metal/ingot/electrum", 2),
                new AlloyExpected("invar", "tfc:metal/ingot/wrought_iron", 2,
                        "tfc:metal/ingot/nickel", 1, "tfcm:metal/ingot/invar", 3),
                new AlloyExpected("andesite_alloy_from_zinc", "tfc:rock/loose/andesite", 10,
                        "tfc:metal/ingot/zinc", 1, "tfcm:metal/ingot/andesite_alloy", 10),
                new AlloyExpected("andesite_alloy_from_tin", "tfc:rock/loose/andesite", 10,
                        "tfc:metal/ingot/tin", 1, "tfcm:metal/ingot/andesite_alloy", 10),
                new AlloyExpected("insulating_glass", "minecraft:glass", 2,
                        "tfcm:metal/dust/wrought_iron", 1, "immersiveengineering:insulating_glass", 2)};
        for(AlloyExpected row : rows)
        {
            AlloyRecipe recipe = recipe(helper, "tfie:alloysmelter/" + row.path, AlloyRecipe.class);
            ItemStack output = recipe.output.get();
            ItemStack first = itemWithCount(row.first, row.firstCount);
            ItemStack second = itemWithCount(row.second, row.secondCount);
            helper.assertTrue(recipe.input0.getCount() == row.firstCount
                            && recipe.input0.test(first)
                            && recipe.input1.getCount() == row.secondCount
                            && recipe.input1.test(second)
                            && output.is(item(row.output).getItem()) && output.getCount() == row.outputCount
                            && recipe.time == 200,
                    row.path + " differs from the independent Alloy Kiln table");
            FurnaceOutputHeat.apply(output);
            helper.assertTrue(row.path.equals("insulating_glass")
                            ? HeatCapability.getTemperature(output) == 0
                            : HeatCapability.getTemperature(output) > 0,
                    row.path + " has the wrong Alloy Kiln output heat: has=" + HeatCapability.has(output)
                            + ", melting=" + FurnaceOutputHeat.meltingPoint(output)
                            + ", temperature=" + HeatCapability.getTemperature(output));
            ItemStack processed = completeAlloyProcess(helper, row);
            helper.assertTrue(processed.is(item(row.output).getItem())
                            && processed.getCount() == row.outputCount
                            && (row.path.equals("insulating_glass")
                            ? HeatCapability.getTemperature(processed) == 0
                            : HeatCapability.getTemperature(processed) > 0),
                    row.path + " did not complete with the expected output and heat");
        }
        for(String deleted : new String[]{"brass", "bronze", "constantan", "electrum", "invar",
                "rose_gold", "manyullyn", "insulating_glass"})
            helper.assertTrue(optionalRecipe(helper, "immersiveengineering:alloysmelter/" + deleted) == null,
                    "Old IE Alloy Kiln recipe remains: " + deleted);
        helper.assertTrue(optionalRecipe(helper, "tfie:alloysmelter/rose_gold") == null
                        && optionalRecipe(helper, "tfie:alloysmelter/manyullyn") == null,
                "Rose Gold and Manyullyn must not receive replacement Kiln recipes");
        helper.succeed();
    }

    private static ArcExpected arc(String path, int mode, String main, int mainCount, int outputCount,
                                   int energy, int time)
    {
        return new ArcExpected(path, mode, main, mainCount, outputCount, energy, time);
    }

    private static ListTag processList(ArcFurnaceProcess process, GameTestHelper helper)
    {
        CompoundTag processTag = new CompoundTag();
        processTag.putString("recipe", process.getRecipeId().toString());
        processTag.putInt("process_processTick", process.processTick);
        process.writeExtraDataToNBT(processTag, helper.getLevel().registryAccess());
        ListTag processes = new ListTag();
        processes.add(processTag);
        return processes;
    }

    private static BriquetExpected briquet(String ore, String output)
    {
        return new BriquetExpected(ore, output);
    }

    private static String expectedArcOutput(String path)
    {
        if(path.startsWith("andesite_alloy"))
            return "tfcm:metal/ingot/andesite_alloy";
        if(List.of("bismuth_bronze", "black_bronze", "brass", "bronze", "rose_gold",
                "sterling_silver", "weak_steel", "weak_blue_steel", "weak_red_steel").contains(path))
            return "tfc:metal/ingot/" + path;
        return "tfcm:metal/ingot/" + path;
    }

    private static List<AdditiveExpected> expectedAdditives(String path)
    {
        return switch(path)
        {
            case "bismuth_bronze" -> additives("zinc", 1, "bismuth", 1);
            case "black_bronze" -> additives("silver", 1, "gold", 1);
            case "brass" -> additives("zinc", 1);
            case "bronze" -> additives("tin", 1);
            case "rose_gold" -> additives("copper", 1);
            case "sterling_silver" -> additives("copper", 2);
            case "weak_steel" -> additives("nickel", 2, "black_bronze", 2);
            case "weak_blue_steel" -> additives("steel", 5, "bismuth_bronze", 2,
                    "sterling_silver", 3);
            case "weak_red_steel" -> additives("steel", 5, "brass", 2, "rose_gold", 3);
            case "constantan" -> additives("nickel", 1);
            case "electrum" -> additives("silver", 1);
            case "invar" -> additives("nickel", 1);
            case "osmiridium" -> additives("iridium", 1);
            case "solder" -> additives("bismuth", 1, "tin", 1);
            case "hastelloy" -> additives("chromium", 3, "molybdenum", 2, "wrought_iron", 1,
                    "tungsten", 1);
            case "high_speed_steel" -> additives("tungsten", 1, "vanadium", 1, "molybdenum", 1,
                    "manganese", 1);
            case "andesite_alloy_from_zinc" -> additives("zinc", 1);
            case "andesite_alloy_from_tin" -> additives("tin", 1);
            default -> List.of();
        };
    }

    private static List<AdditiveExpected> additives(Object... values)
    {
        List<AdditiveExpected> result = new ArrayList<>();
        for(int index = 0; index < values.length; index += 2)
            result.add(new AdditiveExpected((String)values[index], (int)values[index + 1]));
        return result;
    }

    private static ItemStack arcMetalInput(String metal)
    {
        if(metal.equals("chromium"))
            return item("firmalife:metal/ingot/chromium");
        if(List.of("copper", "gold", "cast_iron", "silver", "tin", "bismuth", "nickel", "zinc",
                "wrought_iron", "steel", "black_steel", "bismuth_bronze", "black_bronze", "brass",
                "bronze", "rose_gold", "sterling_silver").contains(metal))
            return item("tfc:metal/ingot/" + metal);
        return item("tfcm:metal/ingot/" + metal);
    }

    private static void assertMissing(GameTestHelper helper, String recipeId)
    {
        helper.assertTrue(optionalRecipe(helper, recipeId) == null, "Deleted recipe remains: " + recipeId);
    }

    private static void assertNoArcRecipeIfRegistered(GameTestHelper helper, String itemId)
    {
        BuiltInRegistries.ITEM.getOptional(id(itemId))
                .ifPresent(value -> assertNoArcRecipeFor(helper, value.getDefaultInstance(), itemId));
    }

    private static void assertNoArcRecipeFor(GameTestHelper helper, ItemStack input, String itemId)
    {
        ItemStackHandler inventory = new ItemStackHandler(26);
        inventory.setStackInSlot(0, input);
        helper.assertTrue(ArcFurnaceRecipeSelection.find(helper.getLevel(), input,
                        NonNullList.withSize(4, ItemStack.EMPTY), inventory) == null,
                "Forbidden non-Briquet input can start an Arc process: " + itemId);
    }

    private static String expectedArcPath(String main, int mode)
    {
        if(main.equals("wrought_iron") && mode == 1)
            return "steel";
        return ARC_ALLOYS.stream().filter(row -> row.main.equals(main) && row.mode == mode)
                .map(ArcExpected::path).findFirst().orElse(null);
    }

    private static ItemStack arcMainInput(String metal)
    {
        ItemStack input = item(switch(metal)
        {
            case "andesite" -> "tfc:rock/loose/andesite";
            case "osmium", "antimony" -> "tfcm:metal/ingot/" + metal;
            default -> "tfc:metal/ingot/" + metal;
        });
        input.setCount(input.getMaxStackSize());
        return input;
    }

    private static ItemStack arcMainInput(String metal, int count)
    {
        ItemStack input = arcMainInput(metal);
        input.setCount(count);
        return input;
    }

    private static ArcFurnaceLogic.State completeArcProcess(GameTestHelper helper,
                                                             RecipeHolder<ArcFurnaceRecipe> holder,
                                                             int mode, ItemStack mainInput,
                                                             List<ItemStack> additives)
    {
        ArcFurnaceLogic logic = new ArcFurnaceLogic();
        TestInitialContext<ArcFurnaceLogic.State> initial = new TestInitialContext<>(helper.getLevel());
        ArcFurnaceLogic.State state = logic.createInitialState(initial);
        state.getEnergy().receiveEnergy(ArcFurnaceLogic.ENERGY_CAPACITY, false);
        state.getInventory().setStackInSlot(0, mainInput);
        if(mode > 0)
        {
            ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.toStack();
            blueprint.set(ModDataComponents.ARC_FURNACE_MODE, mode);
            state.getInventory().setStackInSlot(1, blueprint);
        }
        for(int index = 0; index < additives.size(); index++)
            state.getInventory().setStackInSlot(12 + index, additives.get(index));
        for(int slot = 23; slot <= 25; slot++)
            state.getInventory().setStackInSlot(slot, item("immersiveengineering:graphite_electrode"));

        ArcFurnaceRecipe recipe = holder.value();
        ArcFurnaceProcess process = new ArcFurnaceProcess(holder, 42L, 0, 12, 13, 14, 15);
        int[] amounts = new int[5];
        amounts[0] = recipe.input.getCount();
        for(int index = 0; index < recipe.additives.size(); index++)
            amounts[index + 1] = recipe.additives.get(index).getCount();
        process.setInputAmounts(amounts);
        process.processTick = process.getMaxTicks(helper.getLevel()) - 1;
        CompoundTag savedState = new CompoundTag();
        state.writeSaveNBT(savedState, helper.getLevel().registryAccess());
        savedState.put("processor", processList(process, helper));
        state.readSaveNBT(savedState, helper.getLevel().registryAccess());
        logic.tickServer(context(state, helper.getLevel(), Blocks.AIR.defaultBlockState()));
        helper.assertTrue(state.getProcessQueue().isEmpty(),
                holder.id() + " must commit exactly once when all output slots have capacity");
        return state;
    }

    private static <T> T recipe(GameTestHelper helper, String recipeId, Class<T> type)
    {
        return cast(helper, optionalRecipe(helper, recipeId), type, recipeId);
    }

    private static <T> T cast(GameTestHelper helper, Object value, Class<T> type, String label)
    {
        helper.assertTrue(type.isInstance(value), label + " must be a " + type.getSimpleName());
        return type.cast(value);
    }

    private static Recipe<?> optionalRecipe(GameTestHelper helper, String recipeId)
    {
        return helper.getLevel().getRecipeManager().byKey(id(recipeId)).map(RecipeHolder::value).orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<ArcFurnaceRecipe> arcRecipeHolder(GameTestHelper helper, String recipeId)
    {
        RecipeHolder<?> holder = helper.getLevel().getRecipeManager().byKey(id(recipeId)).orElse(null);
        helper.assertTrue(holder != null && holder.value() instanceof ArcFurnaceRecipe,
                recipeId + " must be an Arc Furnace recipe");
        return (RecipeHolder<ArcFurnaceRecipe>)holder;
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

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }

    private static void runMetalPressControl(GameTestHelper helper, FormedMachine<MetalPressLogic.State> press,
                                             IItemHandler inputHandler, String recipeId, String moldId,
                                             ItemStack input, ItemStack expected)
    {
        press.state().mold = item(moldId);
        helper.assertTrue(inputHandler.insertItem(0, input.copy(), false).isEmpty()
                        && press.state().processor.getQueue().size() == 1
                        && press.state().processor.getQueue().getFirst().getRecipeId().equals(id(recipeId)),
                "A formed Metal Press did not start control " + recipeId);
        finishMetalPressProcess(helper, press, expected, true);
    }

    private static void finishMetalPressProcess(GameTestHelper helper, FormedMachine<MetalPressLogic.State> press,
                                                ItemStack expected, boolean verifyDroppedOutput)
    {
        clearDroppedItems(helper, press.origin(), 8);
        press.state().getEnergy().receiveEnergy(16000, false);
        for(int tick = 0; tick < 65; tick++)
            press.helper().tickServer();
        boolean output = helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                        new AABB(press.origin()).inflate(8)).stream()
                .map(ItemEntity::getItem)
                .anyMatch(stack -> ItemStack.isSameItemSameComponents(stack, expected)
                        && stack.getCount() >= expected.getCount());
        helper.assertTrue(press.state().processor.getQueue().isEmpty()
                        && (!verifyDroppedOutput || output),
                "A formed Metal Press did not finish with " + expected);
    }

    private static void clearDroppedItems(GameTestHelper helper, BlockPos origin, double radius)
    {
        helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(origin).inflate(radius))
                .forEach(ItemEntity::discard);
    }

    private static BlockState blockState(String value)
    {
        return BuiltInRegistries.BLOCK.get(id(value)).defaultBlockState()
                .setValue(NonMirrorableWithActiveBlock.ACTIVE, false);
    }

    @SuppressWarnings("unchecked")
    private static <S extends IMultiblockState> FormedMachine<S> form(
            GameTestHelper helper, TemplateMultiblock multiblock, BlockPos relativeOrigin,
            BlockPos trigger, BlockPos master)
    {
        BlockPos origin = helper.absolutePos(relativeOrigin);
        StructureTemplate template = helper.getLevel().getStructureManager()
                .get(multiblock.getTemplateLocation()).orElseThrow();
        template.placeInWorld(helper.getLevel(), origin, origin, new StructurePlaceSettings(),
                helper.getLevel().getRandom(), Block.UPDATE_ALL);
        helper.assertTrue(multiblock.createStructure(helper.getLevel(), origin.offset(trigger), Direction.SOUTH,
                        helper.makeMockPlayer(GameType.CREATIVE)),
                multiblock.getTemplateLocation() + " did not form for its processing GameTest");
        BlockEntity masterBlockEntity = helper.getLevel().getBlockEntity(origin.offset(master));
        helper.assertTrue(masterBlockEntity instanceof IMultiblockBE<?>,
                multiblock.getTemplateLocation() + " did not create a multiblock master");
        IMultiblockBE<S> formed = (IMultiblockBE<S>)masterBlockEntity;
        helper.assertTrue(formed.getHelper() instanceof IMultiblockBEHelperMaster<?>,
                multiblock.getTemplateLocation() + " master has no server helper");
        IMultiblockBEHelperMaster<S> formedHelper = (IMultiblockBEHelperMaster<S>)formed.getHelper();
        return new FormedMachine<>(formedHelper.getState(), formedHelper, origin, origin.offset(master));
    }

    @SuppressWarnings("unchecked")
    private static <S extends IMultiblockState> FormedMachine<S> formedFromMaster(
            GameTestHelper helper, BlockEntity master, BlockPos origin, BlockPos masterPosition)
    {
        helper.assertTrue(master instanceof IMultiblockBE<?>, "Reloaded block entity is not a multiblock master");
        IMultiblockBE<S> formed = (IMultiblockBE<S>)master;
        helper.assertTrue(formed.getHelper() instanceof IMultiblockBEHelperMaster<?>,
                "Reloaded block entity has no multiblock master helper");
        IMultiblockBEHelperMaster<S> formedHelper = (IMultiblockBEHelperMaster<S>)formed.getHelper();
        return new FormedMachine<>(formedHelper.getState(), formedHelper, origin, masterPosition);
    }

    private static IItemHandler itemCapability(GameTestHelper helper, BlockPos position)
    {
        IItemHandler capability = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, position, null);
        if(capability != null)
            return capability;
        for(Direction side : Direction.values())
        {
            capability = helper.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, position, side);
            if(capability != null)
                return capability;
        }
        throw new IllegalStateException("No item capability at " + position);
    }

    private static void writeArcReloadPack(Path pack, boolean disabled)
    {
        try
        {
            Files.createDirectories(pack.resolve("data/tfie/recipe/arcfurnace/briquet"));
            Files.createDirectories(pack.resolve("data/tfie/recipe/arcfurnace/alloy"));
            Files.createDirectories(pack.resolve("data/ledger_test/recipe/crusher"));
            Files.createDirectories(pack.resolve("data/immersiveengineering/recipe"));
            Files.writeString(pack.resolve("pack.mcmeta"),
                    "{\"pack\":{\"pack_format\":48,\"description\":\"TFIE GameTest Arc reload\"}}",
                    StandardCharsets.UTF_8);
            Files.writeString(pack.resolve("data/tfie/recipe/arcfurnace/briquet/magnetite.json"), disabled
                            ? "{\"neoforge:conditions\":[{\"type\":\"neoforge:false\"}],\"type\":\"tfie:empty\"}"
                            : "{\"type\":\"tfie:arc_furnace\",\"additives\":[],\"energy\":51201,"
                            + "\"input\":{\"item\":\"tfcm:ore_processing/briquet/magnetite\"},"
                            + "\"results\":[{\"count\":2,\"id\":\"tfc:metal/ingot/cast_iron\"}],\"time\":101}",
                    StandardCharsets.UTF_8);
            Files.writeString(pack.resolve("data/tfie/recipe/arcfurnace/alloy/duplicate_brass.json"),
                    "{\"type\":\"tfie:arc_furnace\",\"additives\":[{\"tag\":\"c:ingots/zinc\"}],"
                            + "\"energy\":256000,\"input\":{\"basePredicate\":{\"tag\":\"c:ingots/copper\"},"
                            + "\"count\":9},\"mode\":1,\"results\":[{\"count\":10,"
                            + "\"id\":\"tfc:metal/ingot/brass\"}],\"time\":500}",
                    StandardCharsets.UTF_8);
            Files.writeString(pack.resolve("data/ledger_test/recipe/crusher/wool.json"),
                    "{\"type\":\"minecraft:crafting_shapeless\",\"category\":\"misc\","
                            + "\"ingredients\":[{\"item\":\"minecraft:stone\"}],"
                            + "\"result\":{\"id\":\"minecraft:cobblestone\",\"count\":1}}",
                    StandardCharsets.UTF_8);
            Files.writeString(pack.resolve("data/immersiveengineering/recipe/arc_recycling_list.json"),
                    "{\"type\":\"immersiveengineering:generated_list\","
                            + "\"generatorID\":\"immersiveengineering:arc_recycling_list\"}",
                    StandardCharsets.UTF_8);
        }
        catch(IOException exception)
        {
            throw new IllegalStateException("Could not write the Arc reload GameTest pack", exception);
        }
    }

    private static void disableReloadProbe(Path pack)
    {
        writeArcReloadPack(pack, true);
    }

    private static void deletePack(Path pack)
    {
        if(!Files.exists(pack))
            return;
        try(var paths = Files.walk(pack))
        {
            for(Path path : paths.sorted(Comparator.reverseOrder()).toList())
                Files.deleteIfExists(path);
        }
        catch(IOException exception)
        {
            throw new IllegalStateException("Could not remove the Arc reload GameTest pack", exception);
        }
    }

    private static void whenDone(GameTestHelper helper, CompletableFuture<?> future, Runnable then)
    {
        helper.runAfterDelay(1, () ->
        {
            if(future.isDone())
            {
                future.join();
                then.run();
            }
            else
                whenDone(helper, future, then);
        });
    }

    private static void waitForChunkUnload(GameTestHelper helper, ChunkPos chunk, int ticksLeft,
                                           Runnable then)
    {
        helper.runAfterDelay(1, () ->
        {
            if(helper.getLevel().getChunkSource().getChunkNow(chunk.x, chunk.z) == null)
                then.run();
            else
            {
                helper.assertTrue(ticksLeft > 0, "The saved Arc chunk did not unload");
                waitForChunkUnload(helper, chunk, ticksLeft - 1, then);
            }
        });
    }

    private static <S> TestMultiblockContext<S> context(S state, Level level, BlockState blockState)
    {
        return new TestMultiblockContext<>(state, new TestMultiblockLevel(level, blockState));
    }

    private record TestInitialContext<S extends IMultiblockState>(Level level)
            implements IInitialMultiblockContext<S>
    {
        @Override
        public <T> Supplier<T> getCapabilityAt(BlockCapability<T, Direction> capability,
                                                BlockPos position, RelativeBlockFace face)
        {
            return () -> null;
        }

        @Override
        public <T, C> Supplier<T> getCapabilityAt(BlockCapability<T, C> capability,
                                                   BlockPos position, C capabilityContext)
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

    private record FormedMachine<S extends IMultiblockState>(S state,
                                                              IMultiblockBEHelperMaster<S> helper,
                                                              BlockPos origin, BlockPos master)
    {
    }

    private record TestMultiblockContext<S>(S state, TestMultiblockLevel level)
            implements IMultiblockContext<S>
    {
        @Override
        public void markMasterDirty()
        {
        }

        @Override
        public S getState()
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
        private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
        private BlockState state;

        private TestMultiblockLevel(Level level, BlockState state)
        {
            this.level = level;
            this.state = state;
        }

        @Override
        public BlockState getBlockState(BlockPos position)
        {
            return state;
        }

        @Override
        public void setBlock(BlockPos position, BlockState newState)
        {
            state = newState;
        }

        @Override
        public BlockEntity getBlockEntity(BlockPos position)
        {
            return blockEntities.get(position);
        }

        @Override
        public BlockEntity forciblyGetBlockEntity(BlockPos position)
        {
            return blockEntities.get(position);
        }

        private void setBlockEntity(BlockPos position, BlockEntity blockEntity)
        {
            blockEntities.put(position, blockEntity);
        }

        @Override
        public boolean shouldTickModulo(int interval)
        {
            return false;
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

    private record ArcExpected(String path, int mode, String main, int mainCount, int outputCount,
                               int energy, int time)
    {
    }

    private record BriquetExpected(String ore, String output)
    {
    }

    private record AdditiveExpected(String metal, int count)
    {
    }

    private record AlloyExpected(String path, String first, int firstCount, String second,
                                 int secondCount, String output, int outputCount)
    {
    }
}
