package net.claustra01.tfie.gametest.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.config.TFIEConfig;
import net.dries007.tfc.common.component.size.Size;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ConfigGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private ConfigGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void server_config_schema(GameTestHelper helper)
    {
        helper.assertTrue(TFIEConfig.SPEC.isLoaded(), "TFIE's SERVER config must be loaded in game");
        Set<String> paths = new HashSet<>();
        Queue<UnmodifiableConfig> configs = new ArrayDeque<>();
        configs.add(TFIEConfig.SPEC.getValues());
        while(!configs.isEmpty())
        {
            for(Object value : configs.remove().valueMap().values())
            {
                if(value instanceof ModConfigSpec.ConfigValue<?> configValue)
                    paths.add(String.join(".", configValue.getPath()));
                else if(value instanceof UnmodifiableConfig nested)
                    configs.add(nested);
            }
        }
        helper.assertTrue(paths.equals(Set.of(
                        "crucibleExternalHeater.fePerTick",
                        "crucibleExternalHeater.temperature",
                        "compatibility.crateMaximumItemSize",
                        "compatibility.tfcRotationalEnergyModifier")),
                "The SERVER config must declare exactly the four chapter 31 keys");

        assertRange(helper, TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_FE_PER_TICK,
                Integer.class, 20, 0, 32_000);
        assertRange(helper, TFIEConfig.CRUCIBLE_EXTERNAL_HEATER_TEMPERATURE,
                Integer.class, 2_000, 0, Integer.MAX_VALUE);
        helper.assertTrue(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.getDefault().getClass() == Size.class
                        && TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.getDefault() == Size.VERY_LARGE,
                "The crate maximum must be a TFC Size enum defaulting to VERY_LARGE");
        List<Size> expectedSizes = List.of(Size.TINY, Size.VERY_SMALL, Size.SMALL, Size.NORMAL,
                Size.LARGE, Size.VERY_LARGE, Size.HUGE);
        helper.assertTrue(Arrays.asList(Size.values()).equals(expectedSizes)
                        && expectedSizes.stream().allMatch(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.getSpec()::test),
                "All and only the seven declared TFC Size values must be accepted");

        ModConfigSpec.ValueSpec rotation = TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getSpec();
        ModConfigSpec.Range<Double> rotationRange = rotation.getRange();
        helper.assertTrue(TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getDefault().getClass() == Double.class
                        && TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getDefault().equals(1D),
                "The rotation modifier must be a double defaulting to one");
        helper.assertTrue(rotationRange.getMin().equals(Double.MIN_NORMAL)
                        && rotationRange.getMax().equals(Double.MAX_VALUE),
                "The rotation modifier must use the declared finite positive range");
        helper.assertTrue(rotation.test(Double.MIN_NORMAL) && rotation.test(Double.MAX_VALUE)
                        && !rotation.test(0D) && !rotation.test(-1D),
                "The rotation modifier range must reject zero and negative values");

        helper.succeed();
    }

    private static void assertRange(GameTestHelper helper, ModConfigSpec.ConfigValue<Integer> value,
                                    Class<?> type, int defaultValue, int minimum, int maximum)
    {
        ModConfigSpec.ValueSpec spec = value.getSpec();
        ModConfigSpec.Range<Integer> range = spec.getRange();
        helper.assertTrue(value.getDefault().getClass() == type && value.getDefault() == defaultValue,
                String.join(".", value.getPath()) + " has the wrong type or default");
        helper.assertTrue(range.getMin() == minimum && range.getMax() == maximum,
                String.join(".", value.getPath()) + " has the wrong declared range");
        helper.assertTrue(spec.test(minimum) && spec.test(maximum)
                        && (minimum == Integer.MIN_VALUE || !spec.test(minimum-1)),
                String.join(".", value.getPath()) + " must enforce its range endpoints");
    }
}
