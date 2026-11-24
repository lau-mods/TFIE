package net.claustra01.tfie.data.providers;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.data.recipes.AnvilRecipes;
import net.claustra01.tfie.data.recipes.BlueprintRecipes;
import net.claustra01.tfie.data.recipes.HeatRecipes;
import net.claustra01.tfie.data.recipes.BarrelRecipes;
import net.claustra01.tfie.data.recipes.ChiselRecipes;
import net.claustra01.tfie.data.recipes.ClocheFertilizerRecipes;
import net.claustra01.tfie.data.recipes.ClocheRecipes;
import net.claustra01.tfie.data.recipes.CokeOvenRecipes;
import net.claustra01.tfie.data.recipes.CraftingRecipes;
import net.claustra01.tfie.data.recipes.CrusherRecipes;
import net.claustra01.tfie.data.recipes.FermenterRecipes;
import net.claustra01.tfie.data.recipes.IECraftingRecipes;
import net.claustra01.tfie.data.recipes.MixerRecipes;
import net.claustra01.tfie.data.recipes.RefineryRecipes;
import net.claustra01.tfie.data.recipes.SawmillRecipes;
import net.claustra01.tfie.data.recipes.SqueezerRecipes;
import net.claustra01.tfie.util.EmptyRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import net.neoforged.neoforge.common.conditions.ICondition;

@SuppressWarnings("NotNullFieldNotInitialized")
public class BuiltinRecipes extends RecipeProvider implements
    AnvilRecipes,
    BlueprintRecipes,
    HeatRecipes,
    BarrelRecipes,
    ChiselRecipes,
    CraftingRecipes,
    ClocheRecipes,
    ClocheFertilizerRecipes,
    CokeOvenRecipes,
    CrusherRecipes,
    FermenterRecipes,
    MixerRecipes,
    RefineryRecipes,
    SawmillRecipes,
    SqueezerRecipes
{
    PackOutput packOutput;
    CompletableFuture<HolderLookup.Provider> providerFuture;
    RecipeOutput output;
    HolderLookup.Provider lookup;

    public BuiltinRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, lookup);
        this.packOutput = output;
        this.providerFuture = lookup;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output, HolderLookup.Provider lookup)
    {
        this.lookup = lookup;
        return super.run(output, lookup);
    }

    @Override
    public void buildRecipes(RecipeOutput output)
    {
        this.output = output;

        remove(
            "immersiveengineering:crafting/armor_faraday_chestplate",
            "immersiveengineering:crafting/armor_faraday_boots",
            "immersiveengineering:crafting/armor_faraday_helmet",
            "immersiveengineering:crafting/armor_faraday_leggings",
            "immersiveengineering:crafting/armor_steel_chestplate",
            "immersiveengineering:crafting/armor_steel_boots",
            "immersiveengineering:crafting/armor_steel_helmet",
            "immersiveengineering:crafting/armor_steel_leggings",
            "immersiveengineering:crafting/axe_steel",
            "immersiveengineering:crafting/sword_steel",
            "immersiveengineering:crafting/pickaxe_steel",
            "immersiveengineering:crafting/hoe_steel",
            "immersiveengineering:crafting/shovel_steel",
            "immersiveengineering:crafting/drillhead_steel",
            "immersiveengineering:crafting/drillhead_iron",
            "immersiveengineering:crafting/toolupgrade_revolver_bayonet",
            "immersiveengineering:crafting/gunpart_barrel",
            "immersiveengineering:crafting/empty_casing",
            "immersiveengineering:crusher/wool", "immersiveengineering:crafting/treated_wood_horizontal",
            "immersiveengineering:crafting/gunpowder_from_dusts", "immersiveengineering:crafting/alloybrick",
            "immersiveengineering:crafting/blastbrick", "immersiveengineering:crafting/blastbrick_reinforced");

        anvilRecipes();
        blueprintRecipes(output);
        heatRecipes();
        barrelRecipes();
        chiselRecipes();
        craftingRecipes();

        clocheRecipes(output);
        clocheFertilizerRecipes(output);
        cokeOvenRecipes(output);
        crusherRecipes(output);
        fermenterRecipes(output);
        mixerRecipes(output);
        refineryRecipes(output);
        sawmillRecipes(output);
        squeezerRecipes(output);

        new IECraftingRecipes(packOutput, providerFuture).buildRecipes(output);
    }

    @Override
    public HolderLookup.Provider lookup()
    {
        return lookup;
    }

    @Override
    public void add(String prefix, String name, Recipe<?> recipe, ICondition[] conditions)
    {
        output.accept(TFIE.identifier((prefix + "/" + name).toLowerCase(Locale.ROOT)), recipe, null, conditions);
    }

    @Override
    public void remove(String... names)
    {
        for (String name : names)
        {
            final ResourceLocation id = ResourceLocation.parse(name);
            output.accept(id, EmptyRecipe.INSTANCE, null, FalseCondition.INSTANCE);
        }
    }

    @Override
    public void replace(String name, Recipe<?> recipe)
    {
        final ResourceLocation id = ResourceLocation.parse(name);
        output.accept(id, recipe, null);
    }
}
