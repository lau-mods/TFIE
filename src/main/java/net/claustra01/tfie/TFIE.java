package net.claustra01.tfie;

import blusunrize.immersiveengineering.api.tool.ExternalHeaterHandler;
import com.mojang.logging.LogUtils;
import net.claustra01.tfie.client.ClientEvents;
import net.claustra01.tfie.common.CreativeTabs;
import net.claustra01.tfie.common.blockentities.BlockEntities;
import net.claustra01.tfie.common.blocks.Blocks;
import net.claustra01.tfie.common.items.Items;
import net.claustra01.tfie.config.Config;
import net.claustra01.tfie.util.CrucibleHeater;
import net.claustra01.tfie.util.EmptyRecipe;
import net.claustra01.tfie.util.HerbicideEffects;
import net.claustra01.tfie.util.ModClocheRenderFunctions;
import net.claustra01.tfie.util.ModInteractionManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;

@Mod(TFIE.MOD_ID)
public class TFIE
{
    public static final String MOD_ID = "tfie";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TFIE(ModContainer mod, IEventBus bus)
    {
        mod.registerConfig(ModConfig.Type.SERVER, Config.SERVER.spec());

        bus.addListener(this::setup);
        bus.addListener(this::register);
        bus.addListener(this::registerCapabilities);

        Items.ITEMS.register(bus);
        Blocks.BLOCKS.register(bus);
        BlockEntities.BLOCK_ENTITIES.register(bus);
        CreativeTabs.CREATIVE_TABS.register(bus);

        ModClocheRenderFunctions.register();

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEvents.init(bus);
        }
    }

    private void setup(FMLCommonSetupEvent event)
    {
        ModInteractionManager.registerInteractions();
        ModClocheRenderFunctions.init();
        HerbicideEffects.register();
    }

    private void register(RegisterEvent event)
    {
        event.register(Registries.RECIPE_TYPE, EmptyRecipe.ID, () -> EmptyRecipe.TYPE);
        event.register(Registries.RECIPE_SERIALIZER, EmptyRecipe.ID, () -> EmptyRecipe.SERIALIZER);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.registerBlockEntity(
            ExternalHeaterHandler.CAPABILITY,
            TFCBlockEntities.CRUCIBLE.get(),
            CrucibleHeater::new
        );
    }

    public static ResourceLocation identifier(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
