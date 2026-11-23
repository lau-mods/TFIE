package net.claustra01.tfie;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import com.mojang.logging.LogUtils;
import net.claustra01.tfie.common.CrucibleHeater;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.ModCreativeTabs;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfie.config.TFIEConfig;
import blusunrize.immersiveengineering.common.register.IEItems;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;
import net.claustra01.tfie.datagen.TFIEDataGen;
import net.claustra01.tfie.gametest.material.DisabledMaterialGameTests;
import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.gametest.framework.JUnitLikeTestReporter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

@Mod(TFIE.MOD_ID)
public final class TFIE
{
    public static final String MOD_ID = "tfie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFIE(IEventBus modEventBus, ModContainer container)
    {
        container.registerConfig(ModConfig.Type.SERVER, TFIEConfig.SPEC);
        ModDataComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        TFIEConditions.register(modEventBus);
        modEventBus.addListener(TFIEDataGen::gatherData);
        modEventBus.addListener(TFIE::commonSetup);
        modEventBus.addListener(TFIE::registerDisabledMaterialTest);
        modEventBus.addListener(TFIE::registerCapabilities);
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

    private static void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(ExternalHeaterHandler.CAPABILITY, TFCBlockEntities.CRUCIBLE.get(),
                (crucible, side) -> new CrucibleHeater(crucible));
    }

    private static void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> InteractionManager.registerBlock(
                new BlockItemPlacement(IEItems.Misc.HEMP_SEEDS, ModBlocks.HEMP_CROP)));
    }

    private static void registerDisabledMaterialTest(RegisterGameTestsEvent event)
    {
        if(Boolean.getBoolean("tfie.gametest.disabledContent"))
            event.register(DisabledMaterialGameTests.class);
    }

}
