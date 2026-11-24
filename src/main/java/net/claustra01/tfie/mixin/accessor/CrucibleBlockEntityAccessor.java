package net.claustra01.tfie.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;

@Mixin(CrucibleBlockEntity.class)
public interface CrucibleBlockEntityAccessor
{
    @Accessor("targetTemperature")
    void tfie$setTargetTemperature(float targetTemperature);

    @Accessor("targetTemperatureStabilityTicks")
    void tfie$setTargetTemperatureStabilityTicks(int targetTemperatureStabilityTicks);
}
