package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.data.recipes.builder.MetalPressRecipeBuilder;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfie.common.metal.MetalTable;
import net.claustra01.tfie.common.metal.MetalTable.MetalEntry;
import net.claustra01.tfie.common.metal.MetalTable.Shape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Writes the four standard metal press shapes for every chapter 6 metal.
 */
public final class MetalPressRecipes extends RecipeProvider
{
    public MetalPressRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        for(MetalEntry metal : MetalTable.entries())
            for(Shape shape : Shape.values())
            {
                ResourceLocation id = MetalTable.recipeId(metal, shape);
                ResourceLocation ingot = ResourceLocation.fromNamespaceAndPath(switch(metal.origin())
                {
                    case TFC -> "tfc";
                    case TFCM -> "tfcm";
                    case FIRMALIFE -> "firmalife";
                }, "metal/ingot/" + metal.id());
                ResourceLocation result = MetalTable.itemId(metal, shape);
                MetalPressRecipeBuilder.builder()
                        .input(new IngredientWithSize(TagKey.create(Registries.ITEM,
                                ResourceLocation.fromNamespaceAndPath("c", "ingots/" + metal.id())), shape.ingots))
                        .mold(BuiltInRegistries.ITEM.get(shape.mold))
                        .output(new TagOutput(BuiltInRegistries.ITEM.get(result), shape.results))
                        .setEnergy(2400)
                        .addCondition(TFIEConditions.recipe(id,
                                shape.id + " shape for " + metal.id() + " is unavailable",
                                metal.origin() == MetalTable.Origin.FIRMALIFE ? "firmalife" : null,
                                List.of(ingot, result), List.of(), metal.tfcmMetal(), null))
                        .build(output, id);
            }
    }
}
