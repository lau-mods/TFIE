package net.claustra01.tfie.mixin.accessor;

import net.dries007.tfc.common.blockentities.CrucibleBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CrucibleBlockEntity.class)
public interface CrucibleBlockEntityAccessor
{
    @Accessor("temperature")
    void tfie$setTemperature(float temperature);

    @Accessor("targetTemperature")
    float tfie$getTargetTemperature();

    @Accessor("targetTemperature")
    void tfie$setTargetTemperature(float temperature);

    @Accessor("targetTemperatureStabilityTicks")
    int tfie$getTargetTemperatureStabilityTicks();

    @Accessor("targetTemperatureStabilityTicks")
    void tfie$setTargetTemperatureStabilityTicks(int ticks);
}
