package net.claustra01.tfie.gametest.dynamo;

import blusunrize.immersiveengineering.api.IEEnums.IOSideConfig;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalBE;
import blusunrize.immersiveengineering.common.blocks.metal.CapacitorBlockEntity;
import blusunrize.immersiveengineering.common.blocks.metal.DynamoBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.config.TFIEConfig;
import net.dries007.tfc.common.blockentities.rotation.RotationSinkBlockEntity;
import net.dries007.tfc.util.rotation.Node;
import net.dries007.tfc.util.rotation.Rotation;
import net.dries007.tfc.util.rotation.RotationNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.Set;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DynamoGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private DynamoGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void dynamo_lifecycle_and_facing(GameTestHelper helper)
    {
        BlockPos pos = new BlockPos(2, 3, 2);
        RotationNetworkManager manager = RotationNetworkManager.get(helper.getLevel());
        for(Direction facing : Direction.Plane.HORIZONTAL)
        {
            helper.setBlock(pos, dynamoState(facing));
            DynamoBlockEntity dynamo = helper.getBlockEntity(pos);
            RotationSinkBlockEntity sink = (RotationSinkBlockEntity)dynamo;
            if(sink.getRotationNode()==null)
                dynamo.onLoad();
            Node node = sink.getRotationNode();
            helper.assertTrue(dynamo.getFacing()==facing && node != null
                            && node.connections().equals(Set.of(facing)),
                    "The dynamo must accept TFC rotation only on its facing side " + facing);
            helper.assertTrue(manager.getNode(helper.absolutePos(pos)) == node,
                    "The dynamo sink must be registered when loaded");

            dynamo.onLoad();
            helper.assertTrue(sink.getRotationNode() == node
                            && manager.getNode(helper.absolutePos(pos)) == node,
                    "Repeated load notification must not double-register the sink");
            Direction rotatedFacing = facing.getClockWise();
            IDirectionalBE directional = (IDirectionalBE)dynamo;
            directional.setFacing(rotatedFacing);
            directional.afterRotation(facing, rotatedFacing);
            Node rotated = sink.getRotationNode();
            helper.assertTrue(rotated != null && rotated != node
                            && rotated.connections().equals(Set.of(rotatedFacing))
                            && manager.getNode(helper.absolutePos(pos)) == rotated,
                    "Hammer rotation must replace the sink with one on the new facing side");
            dynamo.onChunkUnloaded();
            helper.assertTrue(manager.getNode(helper.absolutePos(pos)) == null
                            && sink.getRotationNode() == null,
                    "Chunk unload must remove the dynamo sink");
            dynamo.onLoad();
            Node reloaded = sink.getRotationNode();
            helper.assertTrue(reloaded != null && reloaded != node
                            && manager.getNode(helper.absolutePos(pos)) == reloaded,
                    "Chunk reload must register exactly one fresh sink");
            helper.setBlock(pos, Blocks.AIR);
            helper.assertTrue(manager.getNode(helper.absolutePos(pos)) == null,
                    "Removing the dynamo block must remove its rotation sink");
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void dynamo_positive_negative_zero_and_disconnected(GameTestHelper helper)
    {
        BlockPos dynamoPos = new BlockPos(2, 3, 2);
        BlockPos capacitorPos = dynamoPos.below();
        helper.setBlock(capacitorPos, block("immersiveengineering:capacitor_lv"));
        helper.setBlock(dynamoPos, dynamoState(Direction.NORTH));
        DynamoBlockEntity dynamo = helper.getBlockEntity(dynamoPos);
        dynamo.onLoad();
        Node node = ((RotationSinkBlockEntity)dynamo).getRotationNode();
        IEnergyStorage receiver = energy(helper, capacitorPos, Direction.UP);
        helper.assertTrue(IEServerConfig.MACHINES.dynamo_output.get() == 3D,
                "This test requires IE's standard dynamo multiplier of three");

        node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
        ((IEServerTickableBE)dynamo).tickServer();
        helper.assertTrue(receiver.getEnergyStored() == 20,
                "Positive 0.04 rad/tick must provide 20 FE at the standard settings");
        node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, -0.04F));
        ((IEServerTickableBE)dynamo).tickServer();
        helper.assertTrue(receiver.getEnergyStored() == 40,
                "Negative 0.04 rad/tick must provide the same 20 FE");
        node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0F));
        ((IEServerTickableBE)dynamo).tickServer();
        helper.assertTrue(receiver.getEnergyStored() == 40,
                "A connected zero-speed rotation must input zero");
        node.update(Node.NO_NETWORK, null, null);
        ((IEServerTickableBE)dynamo).tickServer();
        helper.assertTrue(receiver.getEnergyStored() == 40,
                "A tick without a Rotation must not invoke IE rotation input");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_dynamo_config")
    public static void dynamo_modifier_and_receiver_limits(GameTestHelper helper)
    {
        BlockPos dynamoPos = new BlockPos(2, 3, 2);
        BlockPos firstPos = dynamoPos.below();
        BlockPos secondPos = dynamoPos.above();
        helper.setBlock(firstPos, block("immersiveengineering:capacitor_lv"));
        helper.setBlock(secondPos, block("immersiveengineering:capacitor_lv"));
        CapacitorBlockEntity secondCapacitor = helper.getBlockEntity(secondPos);
        secondCapacitor.sideConfig.put(Direction.DOWN, IOSideConfig.INPUT);
        helper.setBlock(dynamoPos, dynamoState(Direction.NORTH));
        DynamoBlockEntity dynamo = helper.getBlockEntity(dynamoPos);
        dynamo.onLoad();
        Node node = ((RotationSinkBlockEntity)dynamo).getRotationNode();
        IEnergyStorage first = energy(helper, firstPos, Direction.UP);
        IEnergyStorage second = energy(helper, secondPos, Direction.DOWN);

        int firstTarget = first.getMaxEnergyStored()-10;
        while(first.getEnergyStored() < firstTarget)
            first.receiveEnergy(firstTarget-first.getEnergyStored(), false);
        int firstBefore = first.getEnergyStored();
        int secondBefore = second.getEnergyStored();
        double originalModifier = TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getAsDouble();
        double originalDynamoOutput = IEServerConfig.MACHINES.dynamo_output.get();
        try
        {
            TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(2D);
            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
            ((IEServerTickableBE)dynamo).tickServer();
            helper.assertTrue(first.getEnergyStored() == firstBefore+10,
                    "The first receiver must accept only its remaining ten FE");
            helper.assertTrue(second.getEnergyStored() == secondBefore+30,
                    "IE must distribute the remaining candidate FE to another receiver");
            helper.assertTrue(first.getEnergyStored()+second.getEnergyStored()
                            == firstBefore+secondBefore+40,
                    "Modifier two must produce a 40 FE candidate without internal accumulation");

            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, -0.04F));
            ((IEServerTickableBE)dynamo).tickServer();
            helper.assertTrue(second.getEnergyStored() == secondBefore+70,
                    "Modifier two must also produce 40 FE for negative rotation");

            TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(1D);
            IEServerConfig.MACHINES.dynamo_output.set(6D);
            node.update(1, Direction.NORTH, Rotation.of(Direction.NORTH, 0.04F));
            ((IEServerTickableBE)dynamo).tickServer();
            helper.assertTrue(second.getEnergyStored() == secondBefore+110,
                    "IE's changed dynamo multiplier must remain separate from TFIE's modifier");
        }
        finally
        {
            TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.set(originalModifier);
            IEServerConfig.MACHINES.dynamo_output.set(originalDynamoOutput);
        }
        helper.succeed();
    }

    private static IEnergyStorage energy(GameTestHelper helper, BlockPos pos, Direction side)
    {
        IEnergyStorage energy = helper.getLevel().getCapability(
                Capabilities.EnergyStorage.BLOCK, helper.absolutePos(pos), side);
        helper.assertTrue(energy != null, "Expected an energy receiver at " + pos + " on " + side);
        return energy;
    }

    private static BlockState dynamoState(Direction facing)
    {
        return block("immersiveengineering:dynamo").defaultBlockState()
                .setValue(IEProperties.FACING_HORIZONTAL, facing);
    }

    private static Block block(String id)
    {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }
}
