package net.claustra01.tfie.util;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.IExternalHeatable;
import net.claustra01.tfie.config.TFIEConfig;
import net.claustra01.tfie.mixin.accessor.CrucibleBlockEntityAccessor;
import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;

public final class CrucibleHeater implements IExternalHeatable
{
    private final CrucibleBlockEntity crucible;

    public CrucibleHeater(CrucibleBlockEntity crucible)
    {
        this.crucible = crucible;
    }

    @Override
    public int doHeatTick(int energyAvailable, boolean redstone)
    {
        CrucibleBlockEntityAccessor accessor = (CrucibleBlockEntityAccessor)crucible;
        int energyCost = TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK.getAsInt();
        if(redstone||energyAvailable < energyCost)
        {
            accessor.tfie$setTargetTemperature(0);
            crucible.markForSync();
            return 0;
        }
        accessor.tfie$setTargetTemperature(TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE.getAsInt());
        accessor.tfie$setTargetTemperatureStabilityTicks(5);
        crucible.markForSync();
        return energyCost;
    }
}
