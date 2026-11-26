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
        disable(output, "arcfurnace/netherite_scrap");
        disable(output, "arcfurnace/steel");
        disable(output, "arcfurnace/insulating_glass");
        disable(output, "blastfurnace/steel");
        disable(output, "blastfurnace/steel_block");
        disable(output, "blastfurnace/fuel_coke_block");
        disable(output, "blastfurnace/fuel_charcoal_block");
        for(String alloy : List.of("brass", "bronze", "constantan", "electrum", "invar", "rose_gold",
                "manyullyn", "insulating_glass"))
            disable(output, "alloysmelter/" + alloy);
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
            disable(output, "arcfurnace/ore_" + metal);
            disable(output, "arcfurnace/raw_ore_" + metal);
            disable(output, "arcfurnace/raw_block_" + metal);
        }
        for(String metal : List.of(
                "aluminum", "ardite", "brass", "bronze", "cobalt", "constantan", "copper", "electrum",
                "gold", "invar", "iron", "lead", "manyullyn", "nickel", "osmium", "platinum",
                "rose_gold", "silver", "steel", "tin", "tungsten", "uranium", "zinc"))
            disable(output, "arcfurnace/dust_" + metal);
        for(String alloy : List.of("brass", "bronze", "constantan", "electrum", "invar", "manyullyn", "rose_gold"))
            disable(output, "arcfurnace/alloy_" + alloy);
        for(String metal : REPLACED_METALS)
            for(String shape : List.of("plate", "rod", "wire"))
                disable(output, "metalpress/" + shape + "_" + metal);
        disable(output, "metalpress/rod_netherite");
        for(String recipe : List.of(
                "armor_faraday_boots", "armor_faraday_chestplate", "armor_faraday_helmet",
                "armor_faraday_leggings", "drillhead_iron", "drillhead_steel", "empty_casing",
                "gunpart_barrel", "toolupgrade_revolver_bayonet"))
            disable(output, "crafting/" + recipe);

        for(String armor : List.of("boots", "chestplate", "helmet", "leggings"))
            disable(output, "crafting/armor_steel_" + armor);
        for(String tool : List.of("axe", "hoe", "pickaxe", "shovel", "sword"))
            disable(output, "crafting/" + tool + "_steel");

        for(String recipe : List.of(
                "copper_ingot_to_nugget_copper", "nugget_copper_to_copper_ingot",
                "ingot_nickel_to_nugget_nickel", "nugget_nickel_to_ingot_nickel",
                "ingot_silver_to_nugget_silver", "nugget_silver_to_ingot_silver",
                "ingot_steel_to_nugget_steel", "nugget_steel_to_ingot_steel"))
            disable(output, "crafting/" + recipe);
        for(String metal : List.of(
                "aluminum", "constantan", "electrum", "lead", "nickel", "silver", "steel", "uranium"))
        {
            disable(output, "crafting/ingot_" + metal + "_to_storage_" + metal);
            disable(output, "crafting/storage_" + metal + "_to_ingot_" + metal);
        }
        for(String metal : List.of("copper", "gold", "iron", "nickel", "silver", "steel"))
            disable(output, "crafting/plate_" + metal + "_hammering");
        for(String metal : List.of("iron", "steel"))
            disable(output, "crafting/stick_" + metal);
    }

    private static void disable(RecipeOutput output, String path)
    {
        output.accept(ResourceLocation.fromNamespaceAndPath("immersiveengineering", path), EmptyRecipe.INSTANCE,
                null, FalseCondition.INSTANCE);
    }
}
