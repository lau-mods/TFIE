package net.claustra01.tfie.gametest.heater;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import blusunrize.immersiveengineering.common.blocks.metal.FurnaceHeaterBlockEntity;
import blusunrize.immersiveengineering.common.config.IEServerConfig;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.config.TFIEConfig;
import net.claustra01.tfie.mixin.accessor.CrucibleBlockEntityAccessor;
import net.claustra01.tfie.mixin.accessor.TickableInventoryBlockEntityAccessor;
import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class HeaterGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private HeaterGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void crucible_heats_from_all_sides(GameTestHelper helper)
    {
        BlockPos pos = new BlockPos(1, 2, 1);
        helper.setBlock(pos, block("tfc:crucible"));
        CrucibleBlockEntity crucible = helper.getBlockEntity(pos);
        CrucibleBlockEntityAccessor heat = (CrucibleBlockEntityAccessor)crucible;
        TickableInventoryBlockEntityAccessor sync = (TickableInventoryBlockEntityAccessor)crucible;

        for(Direction side : Direction.values())
        {
            ExternalHeaterHandler.IExternalHeatable capability = helper.getLevel().getCapability(
                    ExternalHeaterHandler.CAPABILITY, helper.absolutePos(pos), side);
            helper.assertTrue(capability != null, "The crucible must expose external heating on " + side);
            heat.tfie$setTargetTemperature(0);
            heat.tfie$setTargetTemperatureStabilityTicks(0);
            sync.tfie$setNeedsClientUpdate(false);
            int consumed = capability.doHeatTick(32_000, false);
            helper.assertTrue(consumed == 20, side + " must consume 20 FE per call");
            helper.assertTrue(heat.tfie$getTargetTemperature() == 2_000,
                    side + " must set the 2000 degree target");
            helper.assertTrue(heat.tfie$getTargetTemperatureStabilityTicks() == 5,
                    side + " must refresh the target for five ticks");
            helper.assertTrue(sync.tfie$needsClientUpdate(), side + " must synchronize the crucible");
            helper.assertTrue(crucible.getTemperature() == 0,
                    side + " must not replace the actual temperature instantly");
            helper.assertTrue(capability.doHeatTick(32_000, false) == 20,
                    side + " must keep consuming while the empty crucible is already targeted");
        }
        ExternalHeaterHandler.IExternalHeatable capability = helper.getLevel().getCapability(
                ExternalHeaterHandler.CAPABILITY, helper.absolutePos(pos), Direction.NORTH);
        heat.tfie$setTemperature(2_000);
        helper.assertTrue(crucible.getTemperature() == 2_000,
                "The continued-consumption precondition must place actual temperature at the target");
        helper.assertTrue(capability.doHeatTick(32_000, false) == 20
                        && heat.tfie$getTargetTemperature() == 2_000,
                "Heating must keep consuming 20 FE after actual temperature reaches the target");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void heater_start_threshold_and_storage(GameTestHelper helper)
    {
        BlockPos heaterPos = new BlockPos(1, 2, 1);
        BlockPos cruciblePos = heaterPos.east();
        helper.setBlock(heaterPos, heaterState(Direction.EAST));
        helper.setBlock(cruciblePos, block("tfc:crucible"));
        FurnaceHeaterBlockEntity heater = helper.getBlockEntity(heaterPos);
        CrucibleBlockEntityAccessor crucible = (CrucibleBlockEntityAccessor)helper.getBlockEntity(cruciblePos);

        heater.energyStorage.setStoredEnergy(20);
        heater.tickServer();
        helper.assertTrue(heater.energyStorage.getEnergyStored() == 20
                        && crucible.tfie$getTargetTemperature() == 0,
                "Twenty FE must not start an inactive heater");
        heater.energyStorage.setStoredEnergy(3_200);
        heater.tickServer();
        helper.assertTrue(heater.energyStorage.getEnergyStored() == 3_200
                        && crucible.tfie$getTargetTemperature() == 0,
                "The inactive heater threshold must be strictly greater than 3200 FE");
        heater.energyStorage.setStoredEnergy(3_201);
        heater.tickServer();
        helper.assertTrue(heater.energyStorage.getEnergyStored() == 3_181
                        && crucible.tfie$getTargetTemperature() == 2_000,
                "A heater above the IE threshold must call the crucible and spend 20 FE");
        helper.assertTrue(heater.energyStorage.getMaxEnergyStored() == 32_000,
                "The IE heater must retain its 32000 FE capacity");
        int expectedInput = Math.max(256, Math.max(
                IEServerConfig.MACHINES.heater_consumption.getAsInt(),
                IEServerConfig.MACHINES.heater_speedupConsumption.getAsInt()));
        helper.assertTrue(heater.energyStorage.receiveEnergy(Integer.MAX_VALUE, true) == expectedInput,
                "The heater input must retain IE's 256 FE/t floor and configured upper limit");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void heater_stops_and_crucible_cools(GameTestHelper helper)
    {
        BlockPos pos = new BlockPos(1, 2, 1);
        helper.setBlock(pos, block("tfc:crucible"));
        CrucibleBlockEntity crucible = helper.getBlockEntity(pos);
        CrucibleBlockEntityAccessor heat = (CrucibleBlockEntityAccessor)crucible;
        ExternalHeaterHandler.IExternalHeatable capability = helper.getLevel().getCapability(
                ExternalHeaterHandler.CAPABILITY, helper.absolutePos(pos), Direction.NORTH);
        helper.assertTrue(capability != null, "The crucible heat capability must exist");

        capability.doHeatTick(32_000, false);
        CrucibleBlockEntity.serverTick(helper.getLevel(), helper.absolutePos(pos),
                crucible.getBlockState(), crucible);
        float warmed = crucible.getTemperature();
        helper.assertTrue(warmed > 0 && warmed < 2_000,
                "TFC must move actual temperature gradually toward the target");

        helper.assertTrue(capability.doHeatTick(19, false) == 0
                        && heat.tfie$getTargetTemperature() == 0,
                "Insufficient energy must clear the target without consuming FE");
        helper.assertTrue(crucible.getTemperature() == warmed,
                "An insufficient-energy call must not zero actual temperature");
        capability.doHeatTick(32_000, false);
        helper.assertTrue(capability.doHeatTick(32_000, true) == 0
                        && heat.tfie$getTargetTemperature() == 0,
                "A redstone-powered heater call must clear the target without consuming FE");

        capability.doHeatTick(32_000, false);
        float targetBeforeCallsStop = heat.tfie$getTargetTemperature();
        helper.runAfterDelay(10, () ->
        {
            helper.assertTrue(heat.tfie$getTargetTemperatureStabilityTicks() == 0,
                    "TFC must expire the five-tick target stability after heater calls stop");
            helper.assertTrue(heat.tfie$getTargetTemperature() < targetBeforeCallsStop,
                    "TFC must decay the configured target after heater calls stop");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_heater_config")
    public static void multiple_crucibles_and_changed_heater_config(GameTestHelper helper)
    {
        BlockPos heaterPos = new BlockPos(2, 2, 2);
        BlockPos east = heaterPos.east();
        BlockPos west = heaterPos.west();
        helper.setBlock(heaterPos, heaterState(Direction.NORTH));
        helper.setBlock(east, block("tfc:crucible"));
        helper.setBlock(west, block("tfc:crucible"));
        FurnaceHeaterBlockEntity heater = helper.getBlockEntity(heaterPos);
        CrucibleBlockEntityAccessor eastCrucible = (CrucibleBlockEntityAccessor)helper.getBlockEntity(east);
        CrucibleBlockEntityAccessor westCrucible = (CrucibleBlockEntityAccessor)helper.getBlockEntity(west);

        int originalCost = TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.getAsInt();
        int originalTemperature = TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.getAsInt();
        try
        {
            heater.energyStorage.setStoredEnergy(10_000);
            heater.tickServer();
            helper.assertTrue(heater.energyStorage.getEnergyStored() == 9_960,
                    "Two crucibles must consume two independent 20 FE calls");
            helper.assertTrue(eastCrucible.tfie$getTargetTemperature() == 2_000
                            && westCrucible.tfie$getTargetTemperature() == 2_000,
                    "Multiple targets must each receive 2000 degrees without adding temperatures");

            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(40);
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.set(1_500);
            float eastTemperature = ((CrucibleBlockEntity)helper.getBlockEntity(east)).getTemperature();
            float westTemperature = ((CrucibleBlockEntity)helper.getBlockEntity(west)).getTemperature();
            heater.tickServer();
            helper.assertTrue(heater.energyStorage.getEnergyStored() == 9_880,
                    "Changed cost must apply once to each crucible");
            helper.assertTrue(eastCrucible.tfie$getTargetTemperature() == 1_500
                            && westCrucible.tfie$getTargetTemperature() == 1_500,
                    "Changed temperature must apply independently to every crucible");
            helper.assertTrue(((CrucibleBlockEntity)helper.getBlockEntity(east)).getTemperature() == eastTemperature
                            && ((CrucibleBlockEntity)helper.getBlockEntity(west)).getTemperature() == westTemperature,
                    "A changed target must not replace either crucible's actual temperature instantly");

            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(0);
            helper.setBlock(heaterPos, Blocks.AIR);
            helper.setBlock(heaterPos, heaterState(Direction.NORTH));
            heater = helper.getBlockEntity(heaterPos);
            eastCrucible.tfie$setTargetTemperature(0);
            eastCrucible.tfie$setTargetTemperatureStabilityTicks(0);
            westCrucible.tfie$setTargetTemperature(0);
            westCrucible.tfie$setTargetTemperatureStabilityTicks(0);
            helper.assertTrue(eastCrucible.tfie$getTargetTemperature() == 0
                            && eastCrucible.tfie$getTargetTemperatureStabilityTicks() == 0
                            && westCrucible.tfie$getTargetTemperature() == 0
                            && westCrucible.tfie$getTargetTemperatureStabilityTicks() == 0,
                    "The fresh inactive heater must not have made a heating call");
            heater.energyStorage.setStoredEnergy(3_200);
            heater.tickServer();
            helper.assertTrue(eastCrucible.tfie$getTargetTemperature() == 0
                            && westCrucible.tfie$getTargetTemperature() == 0,
                    "Zero configured cost must not bypass IE's inactive start threshold");
            heater.energyStorage.setStoredEnergy(3_201);
            heater.tickServer();
            helper.assertTrue(heater.energyStorage.getEnergyStored() == 3_201
                            && eastCrucible.tfie$getTargetTemperature() == 1_500,
                    "Zero configured cost must heat without FE drain after IE starts the call");
            ExternalHeaterHandler.IExternalHeatable eastCapability = helper.getLevel().getCapability(
                    ExternalHeaterHandler.CAPABILITY, helper.absolutePos(east), Direction.WEST);
            helper.assertTrue(eastCapability != null && eastCapability.doHeatTick(3_201, true) == 0
                            && eastCrucible.tfie$getTargetTemperature() == 0,
                    "Redstone must stop a zero-cost configured call and clear its target");
        }
        finally
        {
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.set(originalCost);
            TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.set(originalTemperature);
        }
        helper.succeed();
    }

    private static BlockState heaterState(Direction facing)
    {
        return block("immersiveengineering:furnace_heater").defaultBlockState()
                .setValue(IEProperties.FACING_ALL, facing);
    }

    private static Block block(String id)
    {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }
}
