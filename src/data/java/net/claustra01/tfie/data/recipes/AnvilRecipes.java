package net.claustra01.tfie.data.recipes;

import net.claustra01.tfcm.common.metal.Metal;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AnvilRecipes extends RecipeProvider
{
    private static final List<ForgeRule> DRILL_RULES = List.of(
            ForgeRule.BEND_THIRD_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.HIT_LAST);

    public AnvilRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        drillhead(output, "iron", "wrought_iron", "immersiveengineering:drillhead_iron", 3, null);
        drillhead(output, "steel", "steel", "immersiveengineering:drillhead_steel", 4, null);
        drillhead(output, "black_steel", "black_steel", "tfie:drillhead_black_steel", 5, null);
        drillhead(output, "blue_steel", "blue_steel", "tfie:drillhead_blue_steel", 6, null);
        drillhead(output, "red_steel", "red_steel", "tfie:drillhead_red_steel", 6, null);
        drillhead(output, "invar", "invar", "tfie:drillhead_invar", 3, Metal.INVAR);
        drillhead(output, "titanium", "titanium", "tfie:drillhead_titanium", 5, Metal.TITANIUM);
        drillhead(output, "netherite", "netherite", "tfie:drillhead_netherite", 6, Metal.NETHERITE);
        drillhead(output, "tungsten_steel", "tungsten_steel", "tfie:drillhead_tungsten_steel", 7,
                Metal.TUNGSTEN_STEEL);

        for(String part : List.of("boots", "chestplate", "helmet", "leggings"))
            anvil(output, "armor_faraday_" + part, "double_sheets/steel",
                    "immersiveengineering:armor_faraday_" + part, 1, 4,
                    List.of(ForgeRule.PUNCH_NOT_LAST, ForgeRule.HIT_ANY, ForgeRule.DRAW_ANY), true);
        anvil(output, "empty_casing", "ingots/copper", "immersiveengineering:empty_casing", 2, 1,
                List.of(ForgeRule.PUNCH_ANY, ForgeRule.UPSET_ANY, ForgeRule.SHRINK_LAST), false);
        anvil(output, "gunpart_barrel", "ingots/steel", "immersiveengineering:gunpart_barrel", 1, 4,
                List.of(ForgeRule.SHRINK_ANY, ForgeRule.HIT_ANY, ForgeRule.DRAW_LAST), false);
        anvil(output, "tool_head/ie_hammer", "ingots/wrought_iron", "tfie:tool_head/ie_hammer", 1, 3,
                List.of(ForgeRule.HIT_THIRD_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.UPSET_LAST), false);
        anvil(output, "tool_head/wirecutter", "ingots/wrought_iron", "tfie:tool_head/wirecutter", 1, 3,
                List.of(ForgeRule.BEND_NOT_LAST, ForgeRule.SHRINK_NOT_LAST, ForgeRule.HIT_LAST), false);
        anvil(output, "toolupgrade_revolver_bayonet", "ingots/steel",
                "immersiveengineering:toolupgrade_revolver_bayonet", 1, 4,
                List.of(ForgeRule.DRAW_ANY, ForgeRule.BEND_ANY, ForgeRule.HIT_LAST), false);
    }

    private static void drillhead(RecipeOutput output, String recipeName, String metal, String result,
                                  int tier, Metal condition)
    {
        ResourceLocation id = id("anvil/drillhead_" + recipeName);
        AnvilRecipe recipe = recipe("double_sheets/" + metal, result, 1, tier, DRILL_RULES, false);
        if(condition == null)
            output.accept(id, recipe, null);
        else
            output.accept(id, recipe, null, TFIEConditions.recipe(id,
                    metal + " drill head material is unavailable", null,
                    List.of(ResourceLocation.fromNamespaceAndPath("tfcm", "metal/double_sheet/" + metal),
                            ResourceLocation.parse(result)), List.of(), condition, null));
    }

    private static void anvil(RecipeOutput output, String path, String inputTag, String result, int count,
                              int tier, List<ForgeRule> rules, boolean bonus)
    {
        output.accept(id("anvil/" + path), recipe(inputTag, result, count, tier, rules, bonus), null);
    }

    private static AnvilRecipe recipe(String inputTag, String result, int count, int tier,
                                      List<ForgeRule> rules, boolean bonus)
    {
        TagKey<Item> tag = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", inputTag));
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.parse(result));
        return new AnvilRecipe(Ingredient.of(tag), tier, rules, bonus, ItemStackProvider.of(item, count));
    }

    private static ResourceLocation id(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path);
    }
}
