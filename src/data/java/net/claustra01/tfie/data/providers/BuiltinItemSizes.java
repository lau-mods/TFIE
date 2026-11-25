package net.claustra01.tfie.data.providers;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class BuiltinItemSizes extends TFIEJsonProvider
{
    public BuiltinItemSizes(PackOutput output)
    {
        super(output, "TFIE TFC item sizes", PackOutput.Target.DATA_PACK, "tfc/item_size");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cached)
    {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("tag", "tfie:molds");
        JsonObject json = new JsonObject();
        json.add("ingredient", ingredient);
        json.addProperty("size", "normal");
        json.addProperty("weight", "medium");
        return save(cached, ResourceLocation.fromNamespaceAndPath("tfie", "molds"), json);
    }
}
