package net.claustra01.tfie.gametest.material;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
public final class DisabledMaterialGameTests
{
    private DisabledMaterialGameTests()
    {
    }

    @GameTest(template = "gametest/empty_platform", templateNamespace = "tfie")
    public static void disabled_materials_are_skipped_and_logged(GameTestHelper helper)
    {
        MaterialGameTests.disabled_materials_are_skipped_and_logged(helper);
    }
}
