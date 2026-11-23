package net.claustra01.tfie.datagen;

import com.google.gson.JsonObject;
import net.claustra01.tfie.TFIE;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class HempDataProvider implements DataProvider
{
    private final PackOutput.PathProvider climateRanges;

    public HempDataProvider(PackOutput output)
    {
        climateRanges = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/climate_range");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        JsonObject climate = new JsonObject();
        climate.addProperty("min_temperature", 10.0F);
        climate.addProperty("max_temperature", 31.0F);
        climate.addProperty("min_hydration", 30);
        climate.addProperty("max_hydration", 80);
        return DataProvider.saveStable(output, climate, climateRanges.json(
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "crop/hemp")));
    }

    @Override
    public String getName()
    {
        return "TFIE hemp climate";
    }
}
