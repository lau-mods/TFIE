package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.util.EmptyRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.FalseCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class RecipeOverrides extends RecipeProvider
{
    private static final List<String> REPLACED_METALS = List.of(
            "aluminum", "brass", "bronze", "cobalt", "constantan", "copper", "electrum", "gold", "invar",
            "iron", "lead", "nickel", "osmium", "platinum", "rose_gold", "silver", "steel", "tin",
            "tungsten", "uranium", "zinc");

    public RecipeOverrides(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        disable(output, "crafting/treated_wood_horizontal");
        disable(output, "crafting/gunpowder_from_dusts");
        disable(output, "crusher/nether_gold");
        disable(output, "crusher/wool");
        for(String metal : List.of(
                "aluminum", "ardite", "coal", "cobalt", "copper", "diamond", "emerald", "fluorite",
                "gold", "iron", "lapis", "lead", "nickel", "osmium", "platinum", "quartz", "redstone",
                "silver", "tin", "tungsten", "uranium", "zinc"))
            disable(output, "crusher/ore_" + metal);
        for(String metal : List.of(
                "aluminum", "ardite", "cobalt", "copper", "gold", "iron", "lead", "nickel", "osmium",
                "platinum", "silver", "tin", "tungsten", "uranium", "zinc"))
        {
            disable(output, "crusher/raw_ore_" + metal);
            disable(output, "crusher/raw_block_" + metal);
        }
        for(String metal : REPLACED_METALS)
            for(String shape : List.of("plate", "rod", "wire"))
                disable(output, "metalpress/" + shape + "_" + metal);
        disable(output, "metalpress/rod_netherite");
        for(String recipe : List.of(
                "armor_faraday_boots", "armor_faraday_chestplate", "armor_faraday_helmet",
                "armor_faraday_leggings", "drillhead_iron", "drillhead_steel", "empty_casing",
                "gunpart_barrel", "toolupgrade_revolver_bayonet"))
            disable(output, "crafting/" + recipe);
    }

    private static void disable(RecipeOutput output, String path)
    {
        output.accept(ResourceLocation.fromNamespaceAndPath("immersiveengineering", path), EmptyRecipe.INSTANCE,
                null, FalseCondition.INSTANCE);
    }
}
