package net.claustra01.tfie.gametest.furnace;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder("tfie_restart")
@PrefixGameTestTemplate(false)
public final class ArcRestartGameTests
{
    @GameTest(template = "gametest/empty_platform")
    public static void arc_server_restart_persistence(GameTestHelper helper)
    {
        FurnaceGameTests.runArcServerRestartPersistence(helper);
    }
}
