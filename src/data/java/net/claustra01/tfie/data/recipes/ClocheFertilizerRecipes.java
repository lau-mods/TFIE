package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.ClocheFertilizerBuilder;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ClocheFertilizerRecipes extends RecipeProvider
{
    private static final List<FertilizerEntry> FERTILIZERS = List.of(
            new FertilizerEntry("compost", "tfc:compost", 1.2F),
            new FertilizerEntry("guano", "tfc:groundcover/guano", 1.3F),
            new FertilizerEntry("pure_nitrogen", "tfc:pure_nitrogen", 1.2F),
            new FertilizerEntry("pure_phosphorus", "tfc:pure_phosphorus", 1.2F),
            new FertilizerEntry("pure_potassium", "tfc:pure_potassium", 1.2F),
            new FertilizerEntry("shellfish", "tfc:food/shellfish", 1.1F),
            new FertilizerEntry("sylvite", "tfc:powder/sylvite", 1.2F),
            new FertilizerEntry("wood_ash", "tfc:powder/wood_ash", 1.1F));

    public ClocheFertilizerRecipes(PackOutput output,
                                    CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        for(FertilizerEntry entry : FERTILIZERS)
            ClocheFertilizerBuilder.builder(entry.modifier)
                    .input(Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.parse(entry.item))))
                    .build(output, ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                            "fertilizer/" + entry.name));
    }

    private record FertilizerEntry(String name, String item, float modifier)
    {
    }
}
