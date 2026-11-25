package net.claustra01.tfie.config;

import net.dries007.tfc.common.component.size.Size;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class TFIEConfig
{
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK;
    public static final ModConfigSpec.IntValue CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE;
    public static final ModConfigSpec.EnumValue<Size> CRATE_MAXIMUM_ITEM_SIZE;
    public static final ModConfigSpec.DoubleValue TFC_ROTATIONAL_ENERGY_MODIFIER;

    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("crucibleExternalHeater");
        CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK = builder.defineInRange("fePerTick", 20, 0, 32_000);
        CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE = builder.defineInRange(
                "temperature", 2_000, 0, Integer.MAX_VALUE);
        builder.pop();
        builder.push("compatibility");
        CRATE_MAXIMUM_ITEM_SIZE = builder.defineEnum("crateMaximumItemSize", Size.VERY_LARGE);
        TFC_ROTATIONAL_ENERGY_MODIFIER = builder.defineInRange(
                "tfcRotationalEnergyModifier", 1D, Double.MIN_NORMAL, Double.MAX_VALUE);
        builder.pop();
        SPEC = builder.build();
    }

    private TFIEConfig()
    {
    }
}
