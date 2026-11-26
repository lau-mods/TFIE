package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ChiselRecipes extends RecipeProvider
{
    private static final List<String> BLOCKS = List.of(
            "concrete", "concrete_brick", "concrete_leaded", "concrete_tile");

    public ChiselRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        for(String block : BLOCKS)
        {
            add(output, "slab_" + block, block, "slab_" + block, ChiselMode.SLAB.get(), true);
            add(output, "stairs_" + block, block, "stairs_" + block, ChiselMode.STAIR.get(), false);
        }
    }

    private static void add(RecipeOutput output, String path, String input, String result,
                            ChiselMode mode, boolean itemOutput)
    {
        Block inputBlock = BuiltInRegistries.BLOCK.get(id("immersiveengineering:" + input));
        Block resultBlock = BuiltInRegistries.BLOCK.get(id("immersiveengineering:" + result));
        ChiselRecipe recipe = new ChiselRecipe(BlockIngredient.of(inputBlock), resultBlock.defaultBlockState(), mode,
                itemOutput ? ItemStackProvider.of(resultBlock) : ItemStackProvider.empty());
        output.accept(id(TFIE.MOD_ID + ":chisel/" + path), (Recipe<?>)recipe, null);
    }

    private static ResourceLocation id(String value)
    {
        return ResourceLocation.parse(value);
    }
}
