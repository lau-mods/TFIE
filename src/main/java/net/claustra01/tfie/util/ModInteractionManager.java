package net.claustra01.tfie.util;

import net.claustra01.tfie.common.ModBlocks;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;

public final class ModInteractionManager
{
    private ModInteractionManager()
    {
    }

    public static void register()
    {
        InteractionManager.registerBlock(new BlockItemPlacement(IECrop.seed(), ModBlocks.HEMP_CROP));
    }
}
