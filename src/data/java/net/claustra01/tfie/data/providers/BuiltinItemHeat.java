package net.claustra01.tfie.data.providers;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class BuiltinItemHeat extends TFIEJsonProvider
{
    public BuiltinItemHeat(PackOutput output)
    {
        super(output, "TFIE TFC item heat", PackOutput.Target.DATA_PACK, "tfc/item_heat");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cached)
    {
        return CompletableFuture.allOf(
                heat(cached, "bricks", "minecraft:bricks", 1.0F),
                heat(cached, "slag", "immersiveengineering:slag", 0.6F));
    }

    private CompletableFuture<?> heat(CachedOutput cached, String path, String item, float capacity)
    {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", item);
        JsonObject json = new JsonObject();
        json.addProperty("heat_capacity", capacity);
        json.add("ingredient", ingredient);
        return save(cached, ResourceLocation.fromNamespaceAndPath("tfie", path), json);
    }
}
