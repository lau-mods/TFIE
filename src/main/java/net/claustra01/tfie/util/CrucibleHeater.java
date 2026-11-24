package net.claustra01.tfie.util;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import net.claustra01.tfie.config.Config;
import net.claustra01.tfie.mixin.accessor.CrucibleBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;


public record CrucibleHeater(CrucibleBlockEntity crucible, Direction side) implements ExternalHeaterHandler.IExternalHeatable
{
    @Override
    public int doHeatTick(int energyAvailable, boolean redstone)
    {
        Level level = crucible.getLevel();
        BlockPos pos = crucible.getBlockPos();
        assert level != null;
        if (level.getBlockEntity(pos) instanceof CrucibleBlockEntity)
        {
            int FEPerTick = Config.SERVER.crucibleExternalHeaterFEPerTick.get();
            if (energyAvailable >= FEPerTick && !redstone)
            {
                setTargetTemperature(Config.SERVER.crucibleExternalHeaterTemperature.get());
                return FEPerTick;
            }
            setTargetTemperature(0);
        }
        return 0;
    }

    private void setTargetTemperature(float temperature)
    {
        ((CrucibleBlockEntityAccessor) crucible).tfie$setTargetTemperature(temperature);
        ((CrucibleBlockEntityAccessor) crucible).tfie$setTargetTemperatureStabilityTicks(5);
        crucible.markForSync();
    }
}
