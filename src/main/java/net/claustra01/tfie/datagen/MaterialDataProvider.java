package net.claustra01.tfie.datagen;

import com.google.gson.JsonObject;
import net.claustra01.tfie.TFIE;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class MaterialDataProvider implements DataProvider
{
    private final PackOutput.PathProvider fuels;
    private final PackOutput.PathProvider itemHeat;

    public MaterialDataProvider(PackOutput output)
    {
        fuels = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/fuel");
        itemHeat = output.createPathProvider(PackOutput.Target.DATA_PACK, "tfc/item_heat");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        List<CompletableFuture<?>> files = new ArrayList<>();
        fuel(files, output, "coal_coke", "immersiveengineering:coal_coke", 3200, 1550.0F);
        fuel(files, output, "coke", "immersiveengineering:coke", 32000, 1550.0F);
        itemHeat(files, output, "bricks", "minecraft:bricks", 1.0F);
        itemHeat(files, output, "slag", "immersiveengineering:slag", 0.6F);
        return CompletableFuture.allOf(files.toArray(CompletableFuture[]::new));
    }

    private void fuel(List<CompletableFuture<?>> files, CachedOutput output, String path, String item, int duration,
            float temperature)
    {
        JsonObject json = new JsonObject();
        json.add("ingredient", item(item));
        json.addProperty("duration", duration);
        json.addProperty("temperature", temperature);
        files.add(DataProvider.saveStable(output, json,
                fuels.json(ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path))));
    }

    private void itemHeat(List<CompletableFuture<?>> files, CachedOutput output, String path, String item,
            float heatCapacity)
    {
        JsonObject json = new JsonObject();
        json.add("ingredient", item(item));
        json.addProperty("heat_capacity", heatCapacity);
        files.add(DataProvider.saveStable(output, json,
                itemHeat.json(ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path))));
    }

    private JsonObject item(String id)
    {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", id);
        return ingredient;
    }

    @Override
    public String getName()
    {
        return "TFIE common material data";
    }
}
