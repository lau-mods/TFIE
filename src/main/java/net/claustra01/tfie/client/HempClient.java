package net.claustra01.tfie.client;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = TFIE.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class HempClient
{
    private HempClient()
    {
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void setup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            RenderType cutout = RenderType.cutout();
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HEMP_CROP.get(), cutout);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_HEMP_CROP.get(), cutout);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_HEMP_CROP.get(), cutout);
        });
    }
}
