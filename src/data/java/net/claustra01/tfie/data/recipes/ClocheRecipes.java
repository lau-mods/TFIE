package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.client.utils.ClocheRenderFunctions;
import blusunrize.immersiveengineering.data.recipes.builder.ClocheRecipeBuilder;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.util.ModClocheRenderFunctions;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ClocheRecipes extends RecipeProvider
{
    private static final List<CropOutput> CROPS = List.of(
            crop("alfalfa", "tfc:alfalfa", 6),
            crop("barley", "tfc:food/barley", 6),
            crop("beet", "tfc:food/beet", 6),
            crop("cabbage", "tfc:food/cabbage", 6),
            crop("canola", "tfc:canola", 6),
            crop("carrot", "tfc:food/carrot", 6),
            crop("cassava", "tfc:food/cassava", 6),
            crop("garlic", "tfc:food/garlic", 6),
            doubleCrop("green_bean", "tfc:food/green_bean", 4),
            doubleCrop("jute", "tfc:jute", 3),
            crop("lentil", "tfc:food/lentil", 6),
            doubleCrop("maize", "tfc:food/maize", 3),
            crop("melon", "tfc:melon", 1),
            crop("oat", "tfc:food/oat", 6),
            crop("onion", "tfc:food/onion", 6),
            doubleCrop("papyrus", "tfc:papyrus", 3),
            crop("peanut", "tfc:food/peanut", 6),
            crop("potato", "tfc:food/potato", 6),
            crop("pumpkin", "tfc:pumpkin", 1),
            crop("radish", "tfc:food/radish", 6),
            crop("red_bell_pepper", "tfc:food/red_bell_pepper", 4),
            crop("rice", "tfc:food/rice", 6),
            crop("rye", "tfc:food/rye", 6),
            crop("soybean", "tfc:food/soybean", 6),
            crop("squash", "tfc:food/squash", 6),
            doubleCrop("sugarcane", "tfc:food/sugarcane", 4),
            doubleCrop("tomato", "tfc:food/tomato", 4),
            crop("wheat", "tfc:food/wheat", 6),
            crop("yellow_bell_pepper", "tfc:food/yellow_bell_pepper", 4));

    public ClocheRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ItemLike[] soils = TFCBlocks.SOIL.get(SoilBlockType.DIRT).values().toArray(ItemLike[]::new);
        Ingredient soil = Ingredient.of(soils);
        for(CropOutput entry : CROPS)
        {
            Crop crop = Crop.valueOf(entry.name.toUpperCase());
            Block cropBlock = TFCBlocks.CROPS.get(crop).holder().get();
            Item seed = TFCItems.CROP_SEEDS.get(crop).holder().get();
            Item result = BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.output));
            ClocheRecipeBuilder builder = ClocheRecipeBuilder.builder()
                    .seed(seed)
                    .soil(soil)
                    .output(new TagOutput(result, entry.count))
                    .output(new TagOutput(seed), 0.25F)
                    .setTime(128000);
            if(entry.doublingAge > 0)
                builder.setRender(new ModClocheRenderFunctions.DoubleCrop(cropBlock, entry.doublingAge));
            else
                builder.setRender(new ClocheRenderFunctions.RenderFunctionCrop(cropBlock));
            builder.build(output, ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                    "cloche/" + entry.name));
        }
    }

    private static CropOutput crop(String name, String output, int count)
    {
        return new CropOutput(name, output, count, 0);
    }

    private static CropOutput doubleCrop(String name, String output, int doublingAge)
    {
        return new CropOutput(name, output, 6, doublingAge);
    }

    private record CropOutput(String name, String output, int count, int doublingAge)
    {
    }
}
