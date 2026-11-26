package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.concurrent.CompletableFuture;

public final class CraftingRecipes extends RecipeProvider
{
    public CraftingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("immersiveengineering", "treated_wood_horizontal")))
                .define('X', ModItems.TREATED_WOOD_LUMBER.get())
                .pattern("XX")
                .pattern("XX")
                .unlockedBy("has_treated_lumber", has(ModItems.TREATED_WOOD_LUMBER.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        TFIE.MOD_ID, "crafting/treated_wood_horizontal"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("immersiveengineering", "ersatz_leather")), 8)
                .define('c', BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("firmalife", "food/raw_honey")))
                .define('f', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "fabric_hemp")))
                .pattern("fff")
                .pattern("fcf")
                .pattern("fff")
                .unlockedBy("has_raw_honey", has(BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("firmalife", "food/raw_honey"))))
                .save(output.withConditions(new ModLoadedCondition("firmalife")),
                        ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID,
                                "crafting/ersatz_leather_firmalife"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ARC_FURNACE_BLUEPRINT.get())
                .requires(Ingredient.of(ItemTags.create(
                        ResourceLocation.fromNamespaceAndPath("c", "paper"))), 3)
                .requires(Ingredient.of(ItemTags.create(
                        ResourceLocation.fromNamespaceAndPath("c", "dyes/blue"))), 3)
                .unlockedBy("has_paper", has(ItemTags.create(
                        ResourceLocation.fromNamespaceAndPath("c", "paper"))))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        TFIE.MOD_ID, "crafting/arc_furnace_blueprint"));
    }
}
