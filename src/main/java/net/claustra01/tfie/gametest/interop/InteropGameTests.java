package net.claustra01.tfie.gametest.interop;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.blocks.metal.CapacitorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.DynamoBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.FurnaceHeaterBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import blusunrize.immersiveengineering.common.blocks.wooden.WoodenCrateBlockEntity;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import blusunrize.immersiveengineering.common.gui.CrateMenu;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.config.TFIEConfig;
import net.claustra01.tfie.mixin.CrucibleBlockEntityAccessor;
import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.RotationSinkBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.util.rotation.Node;
import net.dries007.tfc.util.rotation.Rotation;
import net.dries007.tfc.util.rotation.RotationNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.List;
import java.util.Set;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InteropGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String TEMPLATE_NAMESPACE = TFIE.MOD_ID;

    private InteropGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void external_heater_six_sides_and_cost(GameTestHelper helper)
    {
        BlockPos cruciblePos = new BlockPos(3, 3, 3);
        CrucibleBlockEntity crucible = placeCrucible(helper, cruciblePos);
        CrucibleBlockEntityAccessor access = (CrucibleBlockEntityAccessor) crucible;
        float actualTemperature = crucible.getTemperature();

        for(Direction side : Direction.values())
        {
            BlockPos heaterPos = cruciblePos.relative(side);
            helper.setBlock(heaterPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
            FurnaceHeaterBlockEntity heater = helper.getBlockEntity(heaterPos);
            heater.energyStorage.setStoredEnergy(4_000);
            heater.tickServer();
            helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_980,
                    "Each side must take 20 FE from the heater");
            helper.assertValueEqual(access.tfie$getTargetTemperature(), 2_000F,
                    "Each side must set the configured target temperature");
            helper.assertValueEqual(access.tfie$getTargetTemperatureStabilityTicks(), 5,
                    "Each call must refresh the five-tick target stability");
            helper.assertValueEqual(crucible.getTemperature(), actualTemperature,
                    "The capability must not replace the actual temperature");
            heater.tickServer();
            helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_960,
                    "Heating an empty crucible must continue after the target is set");
            helper.setBlock(heaterPos, Blocks.AIR);
        }
        BlockPos continuingHeaterPos = cruciblePos.north();
        helper.setBlock(continuingHeaterPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
        FurnaceHeaterBlockEntity continuingHeater = helper.getBlockEntity(continuingHeaterPos);
        continuingHeater.energyStorage.setStoredEnergy(4_000);
        continuingHeater.tickServer();
        helper.runAfterDelay(20, () ->
        {
            helper.assertTrue(crucible.getTemperature() > actualTemperature
                            && crucible.getTemperature() < 2_000F,
                    "TFC ticking must warm the crucible gradually");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void external_heater_startup_storage_and_stop(GameTestHelper helper)
    {
        BlockPos heaterPos = new BlockPos(2, 2, 2);
        BlockPos cruciblePos = heaterPos.east();
        helper.setBlock(heaterPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
        CrucibleBlockEntity crucible = placeCrucible(helper, cruciblePos);
        FurnaceHeaterBlockEntity heater = helper.getBlockEntity(heaterPos);
        CrucibleBlockEntityAccessor access = (CrucibleBlockEntityAccessor) crucible;

        helper.assertValueEqual(heater.energyStorage.getMaxEnergyStored(), 32_000,
                "External Heater capacity must remain 32,000 FE");
        helper.assertValueEqual(heater.energyStorage.receiveEnergy(1_000, true), 256,
                "External Heater input must retain its standard 256 FE/t limit");
        try
        {
            IEServerConfig.MACHINES.heater_consumption.set(512);
            BlockPos highConsumptionHeaterPos = new BlockPos(5, 2, 2);
            helper.setBlock(highConsumptionHeaterPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
            FurnaceHeaterBlockEntity highConsumptionHeater = helper.getBlockEntity(highConsumptionHeaterPos);
            helper.assertValueEqual(highConsumptionHeater.energyStorage.receiveEnergy(1_000, true), 512,
                    "External Heater input must rise to an IE consumption setting above 256 FE/t");
        }
        finally
        {
            IEServerConfig.MACHINES.heater_consumption.set(8);
        }
        heater.energyStorage.setStoredEnergy(20);
        heater.tickServer();
        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 20,
                "A stopped heater must not start with only one heating call worth of FE");
        helper.assertValueEqual(access.tfie$getTargetTemperature(), 0F,
                "A stopped heater below the startup threshold must not update the target");

        heater.energyStorage.setStoredEnergy(3_200);
        heater.tickServer();
        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_200,
                "The exact 3,200 FE threshold must not start a stopped heater");

        heater.energyStorage.setStoredEnergy(3_201);
        heater.tickServer();
        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_181,
                "A stopped heater above 3,200 FE must start and consume 20 FE");
        helper.assertValueEqual(access.tfie$getTargetTemperature(), 2_000F,
                "A started heater must set the configured target");

        heater.energyStorage.setStoredEnergy(19);
        heater.tickServer();
        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 19,
                "A call with insufficient power must consume no FE");
        helper.assertValueEqual(access.tfie$getTargetTemperature(), 0F,
                "Insufficient power must clear the target");
        heater.energyStorage.setStoredEnergy(4_000);
        helper.setBlock(heaterPos.above(), Blocks.REDSTONE_BLOCK);
        heater.tickServer();
        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 4_000,
                "A redstone-disabled call must consume no FE");
        helper.assertValueEqual(access.tfie$getTargetTemperature(), 0F,
                "Redstone input must clear the target");

        helper.setBlock(heaterPos.above(), Blocks.AIR);
        heater.tickServer();
        helper.runAfterDelay(40, () ->
        {
            float heatedTemperature = crucible.getTemperature();
            helper.assertTrue(heatedTemperature > 0, "The heater must warm the crucible before removal");
            helper.setBlock(heaterPos, Blocks.AIR);
            helper.runAfterDelay(7, () ->
            {
                helper.assertTrue(access.tfie$getTargetTemperature() < 2_000F,
                        "An unrefreshed target must decay after its stability period");
                helper.assertTrue(crucible.getTemperature() > 0
                                && crucible.getTemperature() < access.tfie$getTargetTemperature(),
                        "The actual temperature must follow TFC's gradual temperature change");
                helper.succeed();
            });
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void external_heater_multiple_crucibles_and_runtime_config(GameTestHelper helper)
    {
        BlockPos heaterPos = new BlockPos(3, 2, 2);
        helper.setBlock(heaterPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
        FurnaceHeaterBlockEntity heater = helper.getBlockEntity(heaterPos);
        CrucibleBlockEntity first = placeCrucible(helper, heaterPos.west());
        CrucibleBlockEntity second = placeCrucible(helper, heaterPos.east());
        heater.energyStorage.setStoredEnergy(4_000);
        heater.tickServer();

        helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_960,
                "Two crucibles must consume 40 FE from one heater in a tick");
        helper.assertValueEqual(((CrucibleBlockEntityAccessor) first).tfie$getTargetTemperature(), 2_000F,
                "The first crucible must receive the target without multiplication");
        helper.assertValueEqual(((CrucibleBlockEntityAccessor) second).tfie$getTargetTemperature(), 2_000F,
                "The second crucible must receive the same target without multiplication");

        try
        {
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(40);
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.set(1_500);
            heater.tickServer();
            helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_880,
                    "A runtime cost of 40 FE must apply to both crucibles");
            helper.assertValueEqual(((CrucibleBlockEntityAccessor) first).tfie$getTargetTemperature(), 1_500F,
                    "A runtime target of 1,500 C must be applied");
            helper.assertValueEqual(((CrucibleBlockEntityAccessor) second).tfie$getTargetTemperature(), 1_500F,
                    "The same runtime target must apply to both crucibles");

            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(0);
            heater.tickServer();
            helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_880,
                    "A configured zero heating cost must consume no FE");
            helper.assertValueEqual(((CrucibleBlockEntityAccessor) second).tfie$getTargetTemperature(), 1_500F,
                    "Zero configured cost must still update the target after IE starts the heater");
            helper.setBlock(heaterPos.above(), Blocks.REDSTONE_BLOCK);
            heater.tickServer();
            helper.assertValueEqual(heater.energyStorage.getEnergyStored(), 3_880,
                    "Redstone must stop a zero-cost call without consuming power");
            helper.assertValueEqual(((CrucibleBlockEntityAccessor) second).tfie$getTargetTemperature(), 0F,
                    "Redstone must clear the target even with zero configured cost");
            helper.setBlock(heaterPos.above(), Blocks.AIR);
            BlockPos stoppedPos = new BlockPos(7, 2, 2);
            helper.setBlock(stoppedPos, IEBlocks.MetalDevices.FURNACE_HEATER.defaultBlockState());
            FurnaceHeaterBlockEntity stopped = helper.getBlockEntity(stoppedPos);
            CrucibleBlockEntity stoppedCrucible = placeCrucible(helper, stoppedPos.east());
            stopped.energyStorage.setStoredEnergy(3_200);
            stopped.tickServer();
            helper.assertValueEqual(((CrucibleBlockEntityAccessor) stoppedCrucible).tfie$getTargetTemperature(), 0F,
                    "Zero cost must not bypass the stopped heater's startup threshold");
        }
        finally
        {
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(20);
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.set(2_000);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void dynamo_connection_and_lifecycle(GameTestHelper helper)
    {
        BlockPos relativePos = new BlockPos(2, 2, 2);
        BlockState state = IEBlocks.MetalDevices.DYNAMO.defaultBlockState()
                .setValue(IEProperties.FACING_HORIZONTAL, Direction.NORTH);
        helper.setBlock(relativePos, state);
        DynamoBlockEntity dynamo = helper.getBlockEntity(relativePos);
        RotationSinkBlockEntity sink = (RotationSinkBlockEntity) (Object) dynamo;
        Node node = sink.getRotationNode();
        RotationNetworkManager networks = RotationNetworkManager.get(helper.getLevel());

        helper.assertTrue(node.connections().equals(Set.of(Direction.NORTH)),
                "The dynamo must connect rotation only on its facing side");
        dynamo.onLoad();
        helper.assertTrue(networks.getNode(dynamo.getBlockPos()) == node,
                "The sink must be registered when its block entity loads");

        for(Direction facing : List.of(Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH))
        {
            dynamo.setFacing(facing);
            node = sink.getRotationNode();
            helper.assertTrue(node.connections().equals(Set.of(facing)),
                    "The sink must follow each horizontal dynamo facing");
            helper.assertTrue(networks.getNode(dynamo.getBlockPos()) == node,
                    "Changing facing must replace the registered sink");
        }

        dynamo.onChunkUnloaded();
        helper.assertTrue(networks.getNode(dynamo.getBlockPos()) == null,
                "Chunk unload must remove the rotation sink");
        dynamo.onLoad();
        helper.assertTrue(networks.getNode(dynamo.getBlockPos()) == node,
                "Reload must restore the same sink without a duplicate");
        BlockPos capacitorPos = relativePos.east();
        helper.setBlock(capacitorPos, IEBlocks.MetalDevices.CAPACITOR_LV.defaultBlockState());
        CapacitorBlockEntity capacitor = helper.getBlockEntity(capacitorPos);
        capacitor.sideConfig.put(Direction.WEST, IEEnums.IOSideConfig.INPUT);
        node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
        helper.getLevel().removeBlock(dynamo.getBlockPos(), false);
        helper.assertTrue(networks.getNode(dynamo.getBlockPos()) == null,
                "Removing the dynamo must remove its rotation sink");
        ((IEServerTickableBE) (Object) dynamo).tickServer();
        helper.assertValueEqual(storedEnergy(helper, capacitorPos), 0,
                "A removed dynamo must not deliver power from a stale rotation");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void dynamo_rotation_formula_and_disconnect(GameTestHelper helper)
    {
        BlockPos dynamoPos = new BlockPos(2, 2, 2);
        BlockPos capacitorPos = dynamoPos.east();
        DynamoBlockEntity dynamo = placeDynamoWithCapacitor(helper, dynamoPos, capacitorPos);
        CapacitorBlockEntity capacitor = helper.getBlockEntity(capacitorPos);
        Node node = ((RotationSinkBlockEntity) (Object) dynamo).getRotationNode();

        helper.runAfterDelay(1, () ->
        {
            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
            ((IEServerTickableBE) (Object) dynamo).tickServer();
            helper.assertValueEqual(storedEnergy(helper, capacitorPos), 20,
                    "Positive 0.04 rad/tick must provide 20 FE at the default multipliers");

            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, -0.04F));
            ((IEServerTickableBE) (Object) dynamo).tickServer();
            helper.assertValueEqual(storedEnergy(helper, capacitorPos), 40,
                    "Negative 0.04 rad/tick must provide the same 20 FE");

            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0F));
            ((IEServerTickableBE) (Object) dynamo).tickServer();
            helper.assertValueEqual(storedEnergy(helper, capacitorPos), 40,
                    "A valid zero-speed rotation must provide zero FE");
            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, -0.04F));
            RotationNetworkManager.get(helper.getLevel()).remove(node);
            ((IEServerTickableBE) (Object) dynamo).tickServer();
            helper.assertValueEqual(storedEnergy(helper, capacitorPos), 40,
                    "Disconnecting after a nonzero rotation must stop TFIE power input");
            helper.assertValueEqual(capacitor.getMaxInput(), 256,
                    "The LV receiver must retain its IE input limit");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void dynamo_receives_rotation_during_level_tick(GameTestHelper helper)
    {
        BlockPos dynamoPos = new BlockPos(2, 2, 2);
        BlockPos capacitorPos = dynamoPos.east();
        DynamoBlockEntity dynamo = placeDynamoWithCapacitor(helper, dynamoPos, capacitorPos);
        helper.runAfterDelay(1, () ->
        {
            Node node = ((RotationSinkBlockEntity) (Object) dynamo).getRotationNode();
            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
            helper.runAfterDelay(1, () ->
            {
                helper.assertValueEqual(storedEnergy(helper, capacitorPos), 20,
                        "The level ticker must pass the sink rotation to IE once per tick");
                helper.succeed();
            });
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void dynamo_modifiers_and_receiver_limits(GameTestHelper helper)
    {
        BlockPos dynamoPos = new BlockPos(2, 2, 2);
        BlockPos firstCapacitorPos = dynamoPos.east();
        BlockPos secondCapacitorPos = dynamoPos.west();
        DynamoBlockEntity dynamo = placeDynamoWithCapacitor(helper, dynamoPos, firstCapacitorPos);
        helper.setBlock(secondCapacitorPos, IEBlocks.MetalDevices.CAPACITOR_LV.defaultBlockState());
        CapacitorBlockEntity second = helper.getBlockEntity(secondCapacitorPos);
        second.sideConfig.put(Direction.EAST, IEEnums.IOSideConfig.INPUT);
        Node node = ((RotationSinkBlockEntity) (Object) dynamo).getRotationNode();

        helper.runAfterDelay(1, () ->
        {
            try
            {
                IEServerConfig.MACHINES.dynamo_output.set(6D);
                node.update(2, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                int total = storedEnergy(helper, firstCapacitorPos) + storedEnergy(helper, secondCapacitorPos);
                helper.assertValueEqual(total, 40,
                        "IE dynamo_output must independently scale the power candidate");

                IEServerConfig.MACHINES.dynamo_output.set(3D);
                TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(2D);
                node.update(2, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                total = storedEnergy(helper, firstCapacitorPos) + storedEnergy(helper, secondCapacitorPos);
                helper.assertValueEqual(total, 80,
                        "m=2 must apply before IE's unchanged dynamo_output multiplier");

                node.update(2, Direction.NORTH, Rotation.of(Direction.NORTH, -0.04F));
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                total = storedEnergy(helper, firstCapacitorPos) + storedEnergy(helper, secondCapacitorPos);
                helper.assertValueEqual(total, 120,
                        "The configured multiplier must apply equally to reverse rotation");

                TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(1D);
                node.update(2, Direction.NORTH, Rotation.of(Direction.NORTH, 2F));
                int firstBefore = storedEnergy(helper, firstCapacitorPos);
                int secondBefore = storedEnergy(helper, secondCapacitorPos);
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                int received = storedEnergy(helper, firstCapacitorPos) + storedEnergy(helper, secondCapacitorPos) - total;
                helper.assertValueEqual(storedEnergy(helper, firstCapacitorPos) - firstBefore, 256,
                        "IE must send the first receiver at most its 256 FE/t input");
                helper.assertValueEqual(storedEnergy(helper, secondCapacitorPos) - secondBefore, 256,
                        "IE must distribute the remainder to the second receiver");
                helper.assertValueEqual(received, 512,
                        "No more than the receivers' combined capacity may be delivered");
                helper.setBlock(secondCapacitorPos, Blocks.AIR);
                helper.setBlock(secondCapacitorPos, IEBlocks.MetalDevices.CAPACITOR_LV.defaultBlockState());
                CapacitorBlockEntity replacement = helper.getBlockEntity(secondCapacitorPos);
                replacement.sideConfig.put(Direction.EAST, IEEnums.IOSideConfig.INPUT);
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                helper.assertValueEqual(storedEnergy(helper, secondCapacitorPos), 256,
                        "A replacement receiver must receive only its new tick's share");
                int afterLimitedTick = storedEnergy(helper, firstCapacitorPos) + storedEnergy(helper, secondCapacitorPos);
                node.update(2, Direction.NORTH, Rotation.of(Direction.NORTH, 0F));
                ((IEServerTickableBE) (Object) dynamo).tickServer();
                helper.assertValueEqual(storedEnergy(helper, firstCapacitorPos)
                                + storedEnergy(helper, secondCapacitorPos), afterLimitedTick,
                        "Unaccepted power must not be stored inside the dynamo for a later tick");
            }
            finally
            {
                IEServerConfig.MACHINES.dynamo_output.set(3D);
                TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(1D);
            }
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void crate_size_boundaries_and_ie_prohibitions(GameTestHelper helper)
    {
        List<ItemStack> stacks = List.of(
                stack("tfc:powder/salt"),
                new ItemStack(Items.OAK_SIGN),
                new ItemStack(Items.STONE_SLAB),
                stack("tfc:vane"),
                new ItemStack(Items.BUCKET),
                new ItemStack(Items.MINECART),
                stack("tfc:metal/anvil/copper"));
        List<Size> sizes = List.of(Size.TINY, Size.VERY_SMALL, Size.SMALL, Size.NORMAL, Size.LARGE,
                Size.VERY_LARGE, Size.HUGE);

        for(int index = 0; index < stacks.size(); index++)
        {
            Size actual = ItemSizeManager.get(stacks.get(index)).getSize(stacks.get(index));
            helper.assertValueEqual(actual, sizes.get(index), "The test item must represent " + sizes.get(index));
            helper.assertValueEqual(IEApi.isAllowedInCrate(stacks.get(index)), index < 6,
                    "The default crate boundary must accept through VERY_LARGE and reject HUGE");
        }
        helper.assertFalse(IEApi.isAllowedInCrate(new ItemStack(Items.SHULKER_BOX)),
                "The size predicate must not bypass IE's nested-container prohibition");

        for(Block crateBlock : List.of(IEBlocks.WoodenDevices.CRATE.get(),
                IEBlocks.WoodenDevices.REINFORCED_CRATE.get()))
        {
            BlockPos cratePos = new BlockPos(crateBlock == IEBlocks.WoodenDevices.CRATE.get() ? 2 : 3, 2, 2);
            helper.setBlock(cratePos, crateBlock);
            WoodenCrateBlockEntity crate = helper.getBlockEntity(cratePos);
            for(int index = 0; index < stacks.size(); index++)
            {
                ItemStack remainder = crate.getInventoryCap().insertItem(0, stacks.get(index).copy(), false);
                helper.assertValueEqual(remainder.isEmpty(), index < 6,
                        "Both crate inventories must apply the size boundary to " + sizes.get(index));
                crate.clearContent();
            }
            helper.assertFalse(crate.getInventoryCap().insertItem(0, new ItemStack(Items.SHULKER_BOX), false).isEmpty(),
                    "Both crate inventories must reject nested containers");
            ItemStack tagged = new ItemStack(Items.BUNDLE);
            helper.assertTrue(tagged.is(IETags.forbiddenInCrates),
                    "The test item must belong to IE's forbidden tag");
            helper.assertFalse(crate.getInventoryCap().insertItem(0, tagged, false).isEmpty(),
                    "Both crate inventories must retain IE's forbidden tag");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void crate_routes_and_runtime_config(GameTestHelper helper)
    {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        List<ItemStack> stacks = List.of(stack("tfc:powder/salt"), new ItemStack(Items.OAK_SIGN),
                new ItemStack(Items.STONE_SLAB), stack("tfc:vane"), new ItemStack(Items.BUCKET),
                new ItemStack(Items.MINECART), stack("tfc:metal/anvil/copper"),
                new ItemStack(Items.SHULKER_BOX), new ItemStack(Items.BUNDLE));
        helper.assertTrue(stacks.get(8).is(IETags.forbiddenInCrates),
                "The forbidden-tag test item must be tagged");

        try
        {
            for(Size maximum : List.of(Size.NORMAL, Size.HUGE))
            {
                TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.set(maximum);
                for(Block crateBlock : List.of(IEBlocks.WoodenDevices.CRATE.get(),
                        IEBlocks.WoodenDevices.REINFORCED_CRATE.get()))
                {
                    BlockPos cratePos = new BlockPos(2, 2, 2);
                    helper.setBlock(cratePos, crateBlock);
                    WoodenCrateBlockEntity crate = helper.getBlockEntity(cratePos);
                    CrateMenu menu = new CrateMenu(IEMenuTypes.WOODEN_CRATE.get(), 0, player.getInventory(), crate);
                    IItemHandler automation = crate.getInventoryCap();
                    for(int index = 0; index < stacks.size(); index++)
                    {
                        ItemStack input = stacks.get(index);
                        boolean allowed = index < 7 && index <= maximum.ordinal();
                        int sourceCount = input.getCount();
                        helper.assertValueEqual(menu.getSlot(0).mayPlace(input), allowed,
                                "GUI must use the configured crate rule");
                        helper.assertValueEqual(input.getCount(), sourceCount,
                                "A rejected GUI placement must leave the source unchanged");
                        ItemStack remainder = automation.insertItem(0, input.copy(), false);
                        helper.assertValueEqual(remainder.isEmpty(), allowed,
                                "Automation must use the configured crate rule");
                        helper.assertValueEqual(remainder.getCount(), allowed ? 0 : sourceCount,
                                "Automation must return every rejected item");
                        helper.assertValueEqual(crate.getItem(0).getCount(), allowed ? sourceCount : 0,
                                "Automation must insert exactly the accepted amount");
                        crate.clearContent();
                        player.getInventory().setItem(9, input.copy());
                        helper.assertValueEqual(menu.quickMoveStack(player, 27).isEmpty(), !allowed,
                                "Shift movement must use the configured crate rule");
                        helper.assertValueEqual(player.getInventory().getItem(9).getCount(), allowed ? 0 : 1,
                                "Shift movement must transfer exactly one item or leave it intact");
                        helper.assertValueEqual(crate.getItem(0).getCount(), allowed ? 1 : 0,
                                "The crate must match the shift movement result");
                        crate.clearContent();
                        player.getInventory().setItem(9, ItemStack.EMPTY);
                    }
                }
            }
        }
        finally
        {
            TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.set(Size.VERY_LARGE);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void server_config_defaults_types_and_ranges(GameTestHelper helper)
    {
        helper.assertValueEqual(TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.getDefault(), 20,
                "Heater FE default must be 20");
        helper.assertValueEqual(TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.getDefault(), 2_000,
                "Heater temperature default must be 2,000");
        helper.assertValueEqual(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.getDefault(), Size.VERY_LARGE,
                "Crate size default must be VERY_LARGE");
        helper.assertValueEqual(TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getDefault(), 1D,
                "Rotation modifier default must be 1.0");
        helper.assertValueEqual(TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.get(), 20,
                "The loaded heater cost must be 20 FE");
        helper.assertValueEqual(TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.get(), 2_000,
                "The loaded target must be 2,000 C");
        helper.assertValueEqual(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.get(), Size.VERY_LARGE,
                "The loaded crate limit must be VERY_LARGE");
        helper.assertValueEqual(TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.get(), 1D,
                "The loaded rotation modifier must be 1.0");

        assertRange(helper, TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.getSpec(), Integer.class,
                0, 32_000);
        assertRange(helper, TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.getSpec(), Integer.class,
                0, Integer.MAX_VALUE);
        assertRange(helper, TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getSpec(), Double.class,
                Double.MIN_NORMAL, Double.MAX_VALUE);
        helper.assertFalse(TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getSpec().test(0D),
                "Rotation modifier must reject zero");
        helper.assertFalse(TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getSpec().test(-1D),
                "Rotation modifier must reject negative values");
        helper.assertTrue(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.getSpec().getClazz() == Size.class,
                "Crate size must be declared with the TFC Size enum type");
        helper.assertTrue(List.of(Size.values()).equals(List.of(Size.TINY, Size.VERY_SMALL, Size.SMALL,
                        Size.NORMAL, Size.LARGE, Size.VERY_LARGE, Size.HUGE)),
                "Exactly the seven ordered TFC Size values must be accepted");
        helper.succeed();
    }

    private static CrucibleBlockEntity placeCrucible(GameTestHelper helper, BlockPos relativePos)
    {
        helper.setBlock(relativePos, TFCBlocks.CRUCIBLE.get());
        return helper.getBlockEntity(relativePos);
    }

    private static DynamoBlockEntity placeDynamoWithCapacitor(GameTestHelper helper, BlockPos dynamoPos,
                                                               BlockPos capacitorPos)
    {
        helper.setBlock(dynamoPos, IEBlocks.MetalDevices.DYNAMO.defaultBlockState()
                .setValue(IEProperties.FACING_HORIZONTAL, Direction.NORTH));
        helper.setBlock(capacitorPos, IEBlocks.MetalDevices.CAPACITOR_LV.defaultBlockState());
        CapacitorBlockEntity capacitor = helper.getBlockEntity(capacitorPos);
        Direction inputSide = Direction.fromDelta(dynamoPos.getX() - capacitorPos.getX(),
                dynamoPos.getY() - capacitorPos.getY(), dynamoPos.getZ() - capacitorPos.getZ());
        capacitor.sideConfig.put(inputSide, IEEnums.IOSideConfig.INPUT);
        return helper.getBlockEntity(dynamoPos);
    }

    private static int storedEnergy(GameTestHelper helper, BlockPos relativePos)
    {
        BlockPos absolutePos = helper.absolutePos(relativePos);
        BlockPos dynamoPos = helper.absolutePos(new BlockPos(2, 2, 2));
        Direction side = Direction.fromDelta(dynamoPos.getX() - absolutePos.getX(),
                dynamoPos.getY() - absolutePos.getY(), dynamoPos.getZ() - absolutePos.getZ());
        return helper.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, absolutePos, side)
                .getEnergyStored();
    }

    private static ItemStack stack(String id)
    {
        ResourceLocation location = ResourceLocation.parse(id);
        Item item = BuiltInRegistries.ITEM.get(location);
        if(item == Items.AIR)
            throw new IllegalStateException("Missing test item " + id);
        return new ItemStack(item);
    }

    private static <T extends Comparable<? super T>> void assertRange(GameTestHelper helper,
                                                                      ModConfigSpec.ValueSpec spec,
                                                                      Class<?> type, T minimum, T maximum)
    {
        ModConfigSpec.Range<T> range = spec.getRange();
        helper.assertTrue(range.getClazz() == type, "Configuration value has the wrong type");
        helper.assertValueEqual(range.getMin(), minimum, "Configuration range has the wrong minimum");
        helper.assertValueEqual(range.getMax(), maximum, "Configuration range has the wrong maximum");
        helper.assertTrue(spec.test(minimum), "Configuration range must include its minimum");
        helper.assertTrue(spec.test(maximum), "Configuration range must include its maximum");
    }
}
