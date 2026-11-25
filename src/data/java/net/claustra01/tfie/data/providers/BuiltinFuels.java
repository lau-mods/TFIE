package net.claustra01.tfie.data.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class BuiltinFuels extends TFIEJsonProvider
{
    public BuiltinFuels(PackOutput output)
    {
        super(output, "TFIE TFC fuels", PackOutput.Target.DATA_PACK, "tfc/fuel");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cached)
    {
        return CompletableFuture.allOf(
                fuel(cached, "coal_coke", "immersiveengineering:coal_coke", 3200),
                fuel(cached, "coke", "immersiveengineering:coke", 32000));
    }

    private CompletableFuture<?> fuel(CachedOutput cached, String path, String item, int duration)
    {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", item);
        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient);
        JsonObject json = new JsonObject();
        json.addProperty("duration", duration);
        json.add("ingredient", ingredients);
        json.addProperty("temperature", 1550.0F);
        return save(cached, ResourceLocation.fromNamespaceAndPath("tfie", path), json);
    }
}
