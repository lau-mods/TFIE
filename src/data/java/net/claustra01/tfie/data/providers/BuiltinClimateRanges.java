package net.claustra01.tfie.data.providers;

import com.google.gson.JsonObject;
import net.claustra01.tfie.util.IECrop;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public final class BuiltinClimateRanges implements DataProvider
{
    private final PackOutput.PathProvider path;

    public BuiltinClimateRanges(PackOutput output)
    {
        path = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/climate_range");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        JsonObject json = new JsonObject();
        json.addProperty("min_hydration", 30);
        json.addProperty("max_hydration", 80);
        json.addProperty("min_temperature", 10F);
        json.addProperty("max_temperature", 31F);
        return DataProvider.saveStable(output, json, path.json(IECrop.CLIMATE_RANGE));
    }

    @Override
    public String getName()
    {
        return "TFIE climate ranges";
    }
}
