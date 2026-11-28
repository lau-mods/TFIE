package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.crafting.AlloyRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import net.claustra01.tfcm.common.metal.Metal;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AlloySmelterRecipes extends RecipeProvider
{
    public AlloySmelterRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        alloy(output, "brass", "copper", 9, "zinc", 1, "tfc", 10);
        alloy(output, "bronze", "copper", 9, "tin", 1, "tfc", 10);
        alloy(output, "constantan", "copper", 1, "nickel", 1, "tfcm", 2);
        alloy(output, "electrum", "gold", 1, "silver", 1, "tfcm", 2);
        alloy(output, "invar", "wrought_iron", 2, "nickel", 1, "tfcm", 3);
        andesite(output, "zinc");
        andesite(output, "tin");
        add(output, "insulating_glass", tag("glass_blocks", 2),
                direct("tfcm:metal/dust/wrought_iron", 1), "immersiveengineering:insulating_glass", 2,
                List.of(), List.of(id("tfcm:metal/dust/wrought_iron"),
                        id("immersiveengineering:insulating_glass")));
    }

    private static void alloy(RecipeOutput output, String metal, String first, int firstCount,
                              String second, int secondCount, String namespace, int count)
    {
        String result = namespace + ":metal/ingot/" + metal;
        add(output, metal, tag("ingots/" + first, firstCount), tag("ingots/" + second, secondCount),
                result, count, metals(first, second, metal), List.of(id(result)));
    }

    private static void andesite(RecipeOutput output, String metal)
    {
        String result = "tfcm:metal/ingot/andesite_alloy";
        add(output, "andesite_alloy_from_" + metal, direct("tfc:rock/loose/andesite", 1),
                tag("nuggets/" + metal, 1), result, 1, metals(metal, "andesite_alloy"),
                List.of(id("tfc:rock/loose/andesite"), id(result)));
    }

    private static void add(RecipeOutput output, String path, IngredientWithSize input0,
                            IngredientWithSize input1, String result, int count, List<Metal> metals,
                            List<ResourceLocation> items)
    {
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                "alloysmelter/" + path);
        output.accept(recipeId,
                new AlloyRecipe(new TagOutput(new ItemStack(item(result), count)), input0, input1, 200),
                null, TFIEConditions.recipeForMetals(recipeId,
                        "Alloy Kiln materials are unavailable", null, items, List.of(), metals, null));
    }

    private static IngredientWithSize tag(String path, int count)
    {
        return new IngredientWithSize(TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", path)), count);
    }

    private static IngredientWithSize direct(String id, int count)
    {
        return new IngredientWithSize(Ingredient.of(item(id)), count);
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }

    private static List<Metal> metals(String... ids)
    {
        return Arrays.stream(ids).map(AlloySmelterRecipes::metal).filter(value -> value != null)
                .distinct().toList();
    }

    private static Metal metal(String id)
    {
        for(Metal metal : Metal.values())
            if(metal.id().equals(id))
                return metal;
        return null;
    }
}
