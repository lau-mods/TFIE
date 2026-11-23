package net.claustra01.tfie;

import com.mojang.logging.LogUtils;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.ModCreativeTabs;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfie.datagen.TFIEDataGen;
import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.gametest.framework.JUnitLikeTestReporter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

@Mod(TFIE.MOD_ID)
public final class TFIE
{
    public static final String MOD_ID = "tfie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFIE(IEventBus modEventBus)
    {
        ModDataComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        TFIEConditions.register(modEventBus);
        modEventBus.addListener(TFIEDataGen::gatherData);
        String reportPath = System.getProperty("tfie.gametest.report");
        if(reportPath != null)
        {
            File report = new File(reportPath);
            File parent = report.getParentFile();
            if(parent != null)
                parent.mkdirs();
            try
            {
                GlobalTestReporter.replaceWith(new JUnitLikeTestReporter(report));
            }
            catch(ParserConfigurationException exception)
            {
                throw new IllegalStateException("Unable to create the GameTest report", exception);
            }
        }
    }

}
