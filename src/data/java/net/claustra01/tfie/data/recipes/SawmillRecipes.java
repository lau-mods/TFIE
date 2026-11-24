package net.claustra01.tfie.data.recipes;

import blusunrize.immersiveengineering.data.recipes.builder.SawmillRecipeBuilder;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class SawmillRecipes extends RecipeProvider
{
    private static final List<String> TFC_WOODS = List.of(
            "acacia", "ash", "aspen", "birch", "blackwood", "chestnut", "douglas_fir", "hickory",
            "kapok", "mangrove", "maple", "oak", "palm", "pine", "rosewood", "sequoia", "spruce",
            "sycamore", "white_cedar", "willow");
    private static final List<String> AFC_WOODS = List.of(
            "araucaria", "baobab", "beech", "cypress", "eucalyptus", "fig", "ginkgo", "hevea", "ipe",
            "ironwood", "mahoe", "mahogany", "teak", "tualang");
    private static final List<String> OVERDIMS_WOODS = List.of("crimson", "warped");
    private static final TagKey<Item> WOOD_DUST =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dusts/wood"));

    public SawmillRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        TFC_WOODS.forEach(wood -> addWood(output, "tfc", wood, false));
        AFC_WOODS.forEach(wood -> addWood(output, "afc", wood, true));
        OVERDIMS_WOODS.forEach(wood -> addWood(output, "tfcoverdims", wood, true));
    }

    private static void addWood(RecipeOutput output, String namespace, String wood, boolean optional)
    {
        add(output, namespace, wood, "log", List.of(item(namespace, "wood/log/" + wood)),
                item(namespace, "wood/stripped_log/" + wood), 12, true, true, optional);
        add(output, namespace, wood, "wood", List.of(item(namespace, "wood/wood/" + wood)),
                item(namespace, "wood/stripped_wood/" + wood), 12, true, true, optional);
        add(output, namespace, wood, "stripped_log", List.of(
                item(namespace, "wood/stripped_log/" + wood),
                item(namespace, "wood/stripped_wood/" + wood)), null, 12, false, true, optional);
        add(output, namespace, wood, "planks", List.of(item(namespace, "wood/planks/" + wood)),
                null, 4, false, false, optional);
    }

    private static void add(RecipeOutput output, String namespace, String wood, String shape,
                            List<ResourceLocation> inputs, ResourceLocation stripped, int lumberCount,
                            boolean strippingDust, boolean cuttingDust, boolean optional)
    {
        ResourceLocation id = item(TFIE.MOD_ID, "sawmill/" + wood + "/" + shape);
        ResourceLocation lumber = item(namespace, "wood/lumber/" + wood);
        List<ResourceLocation> referenced = new ArrayList<>(inputs);
        if(stripped != null)
            referenced.add(stripped);
        referenced.add(lumber);
        if(referenced.stream().anyMatch(item -> !BuiltInRegistries.ITEM.containsKey(item)))
            return;

        SawmillRecipeBuilder builder = SawmillRecipeBuilder.builder()
                .input(Ingredient.of(inputs.stream()
                        .map(BuiltInRegistries.ITEM::get)
                        .toArray(Item[]::new)))
                .output(BuiltInRegistries.ITEM.get(lumber), lumberCount)
                .setEnergy(1600);
        if(stripped != null)
            builder.addStripped(BuiltInRegistries.ITEM.get(stripped));
        if(strippingDust)
            builder.addStripSecondary(WOOD_DUST);
        if(cuttingDust)
            builder.addSawSecondary(WOOD_DUST);
        if(optional)
            builder.addCondition(TFIEConditions.recipe(id, namespace + " wood item is unavailable", namespace,
                    List.copyOf(referenced), List.of(), null, null));
        builder.build(output, id);
    }

    private static ResourceLocation item(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
