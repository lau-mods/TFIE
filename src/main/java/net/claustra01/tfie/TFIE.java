package net.claustra01.tfie;

import com.mojang.logging.LogUtils;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.ModCreativeTabs;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfie.config.TFIEConfig;
import net.claustra01.tfie.util.CrucibleHeater;
import net.claustra01.tfie.util.HerbicideEffects;
import net.claustra01.tfie.util.ModInteractionManager;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.gametest.framework.JUnitLikeTestReporter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.claustra01.tfie.util.EmptyRecipe;
import org.slf4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

@Mod(TFIE.MOD_ID)
public final class TFIE
{
    public static final String MOD_ID = "tfie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFIE(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.SERVER, TFIEConfig.SPEC);
        ModDataComponents.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        TFIEConditions.register(modEventBus);
        modEventBus.addListener(this::registerRecipes);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerCapabilities);
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

    private void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ModInteractionManager.register();
            HerbicideEffects.register();
        });
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(
                blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler.CAPABILITY,
                TFCBlockEntities.CRUCIBLE.get(), (crucible, side) -> new CrucibleHeater(crucible));
    }

    private void registerRecipes(RegisterEvent event)
    {
        event.register(Registries.RECIPE_TYPE, EmptyRecipe.ID, () -> EmptyRecipe.TYPE);
        event.register(Registries.RECIPE_SERIALIZER, EmptyRecipe.ID, () -> EmptyRecipe.SERIALIZER);
    }

}
