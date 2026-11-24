package net.claustra01.tfie.data.recipes;

import java.util.Locale;
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.items.Items;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.SandstoneBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;

public interface CrusherRecipes extends Recipes
{
    default void crusherRecipes(RecipeOutput output)
    {
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.CHARCOAL), 4)
            .input(net.minecraft.world.item.Items.CHARCOAL)
            .setEnergy(1600)
            .build(output, toRL("charcoal"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.POWDERS.get(Powder.FLUX), 2)
            .input(TFCTags.Items.FLUXSTONE)
            .setEnergy(1600)
            .build(output, toRL("fluxstone"));
        CrusherRecipeBuilder.builder()
            .output(TFCItems.OLIVE_PASTE, 2)
            .input(TFCItems.FOOD.get(Food.OLIVE))
            .setEnergy(800)
            .build(output, toRL("olive"));

        // Sandstone
        for (SandBlockType sand : SandBlockType.values())
        {
            for (SandstoneBlockType sandstone : SandstoneBlockType.values())
            {
                CrusherRecipeBuilder.builder()
                    .output(TFCBlocks.SAND.get(sand), 2)
                    .addSecondary(TFCItems.ORE_POWDERS.get(Ore.SALTPETER).asItem(), 0.5f)
                    .input(TFCBlocks.SANDSTONE.get(sand).get(sandstone))
                    .setEnergy(3200)
                    .build(output, toRL("sandstone/" + (sandstone.name() + sand.name()).toLowerCase(Locale.ROOT)));
            }
        }

    }

    private ResourceLocation toRL(String name)
    {
        return TFIE.identifier("crusher/" + name);
    }
}
