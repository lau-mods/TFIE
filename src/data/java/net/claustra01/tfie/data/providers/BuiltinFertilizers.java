package net.claustra01.tfie.data.providers;

import com.google.gson.JsonObject;
import net.claustra01.tfie.TFIE;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public final class BuiltinFertilizers extends TFIEJsonProvider
{
    public BuiltinFertilizers(PackOutput output)
    {
        super(output, "TFIE TFC fertilizers", PackOutput.Target.DATA_PACK, "tfc/fertilizer");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cached)
    {
        return CompletableFuture.allOf(
                fertilizer(cached, "dust_saltpeter", "immersiveengineering:dust_saltpeter",
                        0.2F, 0, 0),
                fertilizer(cached, "fertilizer", "immersiveengineering:fertilizer",
                        0.1F, 0.2F, 0.5F),
                fertilizer(cached, "slag", "immersiveengineering:slag",
                        0, 0.2F, 0.1F));
    }

    private CompletableFuture<?> fertilizer(CachedOutput cached, String path, String item,
                                              float nitrogen, float phosphorus, float potassium)
    {
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", item);
        JsonObject json = new JsonObject();
        json.add("ingredient", ingredient);
        if(nitrogen > 0)
            json.addProperty("nitrogen", nitrogen);
        if(phosphorus > 0)
            json.addProperty("phosphorus", phosphorus);
        if(potassium > 0)
            json.addProperty("potassium", potassium);
        return save(cached, ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path), json);
    }
}
