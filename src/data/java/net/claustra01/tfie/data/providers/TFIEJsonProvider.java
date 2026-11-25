package net.claustra01.tfie.data.providers;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

/**
 * Writes TFIE data that has no registry-backed builder in the target mods.
 */
public abstract class TFIEJsonProvider implements DataProvider
{
    private final PackOutput.PathProvider paths;
    private final String name;

    protected TFIEJsonProvider(PackOutput output, String name, PackOutput.Target target, String directory)
    {
        paths = output.createPathProvider(target, directory);
        this.name = name;
    }

    protected final CompletableFuture<?> save(CachedOutput cached, ResourceLocation id, JsonObject json)
    {
        return DataProvider.saveStable(cached, json, paths.json(id));
    }

    @Override
    public final String getName()
    {
        return name;
    }
}
