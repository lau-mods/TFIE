package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.dries007.tfc.common.recipes.BarrelRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public final class BarrelRecipes extends RecipeProvider
{
    public BarrelRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ResourceLocation treatedLumberId = ResourceLocation.fromNamespaceAndPath(
                TFIE.MOD_ID, "barrel/treated_wood_lumber");
        BarrelRecipe.Builder builder = new BarrelRecipe.Builder(recipe ->
                output.accept(treatedLumberId, (Recipe<?>)recipe, null));
        builder.input(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "lumber")))
                .input(BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath(
                        "immersiveengineering", "creosote")), 50)
                .output(ModItems.TREATED_WOOD_LUMBER.get())
                .sealed(8000);

        ResourceLocation redstoneAcidId = ResourceLocation.fromNamespaceAndPath(
                TFIE.MOD_ID, "barrel/redstone_acid");
        builder = new BarrelRecipe.Builder(recipe -> output.accept(redstoneAcidId, (Recipe<?>)recipe, null));
        builder.input(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dusts/redstone")))
                .input(BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("minecraft", "water")), 250)
                .output(BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath(
                        "immersiveengineering", "redstone_acid")), 250)
                .instant();
    }
}
