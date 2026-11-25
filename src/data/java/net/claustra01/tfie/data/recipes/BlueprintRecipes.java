package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.api.IEApiDataComponents;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.data.recipes.builder.BlueprintCraftingRecipeBuilder;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public final class BlueprintRecipes extends RecipeProvider
{
    private static final TagKey<Item> WROUGHT_IRON_PLATES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "plates/wrought_iron"));
    private static final TagKey<Item> BLUE_DYES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "dyes/blue"));
    private static final TagKey<Item> PAPER = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "paper"));
    private static final TagKey<Item> STEEL_PLATES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "plates/steel"));

    public BlueprintRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        ItemStack molds = new ItemStack(BuiltInRegistries.ITEM.get(
                ResourceLocation.fromNamespaceAndPath("immersiveengineering", "blueprint")));
        molds.set(IEApiDataComponents.BLUEPRINT_TYPE.get(), "molds");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, molds)
                .define('P', WROUGHT_IRON_PLATES)
                .define('d', BLUE_DYES)
                .define('p', PAPER)
                .pattern(" P ")
                .pattern("ddd")
                .pattern("ppp")
                .unlockedBy("has_wrought_iron_plate", has(WROUGHT_IRON_PLATES))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        "immersiveengineering", "crafting/blueprint_molds"));

        BlueprintCraftingRecipeBuilder.builder()
                .category("molds")
                .input(new IngredientWithSize(STEEL_PLATES, 3))
                .input(new IngredientWithSize(Ingredient.of(BuiltInRegistries.ITEM.get(
                        ResourceLocation.fromNamespaceAndPath("immersiveengineering", "wirecutter")))))
                .output(new TagOutput(ModItems.MOLD_SHEET.get()))
                .build(output, ResourceLocation.fromNamespaceAndPath("tfie", "blueprint/mold_sheet"));
    }
}
