package net.claustra01.tfie.data.recipes;

import java.util.Arrays;
import blusunrize.immersiveengineering.common.register.IEFluids;
import blusunrize.immersiveengineering.data.recipes.builder.FermenterRecipeBuilder;
import com.eerussianguy.firmalife.common.items.FLFood;
import com.eerussianguy.firmalife.common.items.FLItems;
import net.claustra01.tfie.TFIE;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;

public interface FermenterRecipes extends Recipes
{
    default void fermenterRecipes(RecipeOutput output)
    {
        final Fluid ethanol = IEFluids.ETHANOL.getStill();
        Food[] VEGETABLES = {Food.SUGARCANE, Food.BEET, Food.POTATO, Food.CASSAVA};

        for (Food food : Food.values())
        {
            if (food.hasJam() && food != Food.PEANUT)
            {
                FermenterRecipeBuilder.builder()
//                    .output(ethanol, (int) (80 * getNutrients(food)[Nutrient.FRUIT.ordinal()]))
                    .output(ethanol, 80)
                    .input(TFCItems.FOOD.get(food))
                    .setEnergy(6400)
                    .build(output, toRL(food.getSerializedName()));
            }
            else if (Arrays.asList(VEGETABLES).contains(food))
            {
                FermenterRecipeBuilder.builder()
                    .output(ethanol, 80)
                    .input(TFCItems.FOOD.get(food))
                    .setEnergy(6400)
                    .build(output, toRL(food.getSerializedName()));
            }
        }

        FermenterRecipeBuilder.builder()
            .output(ethanol, 80)
            .input(TFCTags.Items.GRAINS)
            .setEnergy(6400)
            .build(output, toRL("grains"));

        // Firmalife

        FermenterRecipeBuilder.builder()
            .output(ethanol, 250)
            .input(FLItems.FOODS.get(FLFood.RAW_HONEY))
            .setEnergy(6400)
            .addCondition(new ModLoadedCondition("firmalife"))
            .build(output, toRL("raw_honey"));
    }

    private float[] getNutrients(Food food)
    {
        return FoodCapability.MANAGER.getOrThrow(TFCItems.FOOD.get(food).getId()).food().nutrients();
    }

    private ResourceLocation toRL(String name)
    {
        return TFIE.identifier("fermenter/" + name);
    }
}
