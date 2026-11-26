package net.claustra01.tfie.common.recipes;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.TagOutput;
import blusunrize.immersiveengineering.api.crafting.TagOutputList;
import blusunrize.immersiveengineering.common.register.IEMultiblockLogic;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ModeAwareArcFurnaceRecipeSerializer extends IERecipeSerializer<ArcFurnaceRecipe>
{
    private static final DualMapCodec<RegistryFriendlyByteBuf, ModeAwareArcFurnaceRecipe> CODECS =
            DualCompositeMapCodecs.composite(
                    TagOutputList.CODEC.fieldOf("results"), recipe -> recipe.output,
                    TagOutput.CODECS.optionalFieldOf("slag", TagOutput.EMPTY), recipe -> recipe.slag,
                    CHANCE_LIST_CODECS.optionalFieldOf("secondaries", List.of()), recipe -> recipe.secondaryOutputs,
                    DualCodecs.INT.fieldOf("time"), MultiblockRecipe::getBaseTime,
                    DualCodecs.INT.fieldOf("energy"), MultiblockRecipe::getBaseEnergy,
                    IngredientWithSize.CODECS.fieldOf("input"), recipe -> recipe.input,
                    IngredientWithSize.CODECS.listOf().fieldOf("additives"), recipe -> recipe.additives,
                    DualCodecs.STRING.optionalFieldOf("specialRecipeType", ""), recipe -> recipe.specialRecipeType,
                    DualCodecs.INT.optionalFieldOf("mode", 0), ModeAwareArcFurnaceRecipe::mode,
                    ModeAwareArcFurnaceRecipe::new
            );

    @Override
    protected DualMapCodec<RegistryFriendlyByteBuf, ArcFurnaceRecipe> codecs()
    {
        return CODECS.map(recipe -> recipe, recipe -> (ModeAwareArcFurnaceRecipe)recipe);
    }

    @Override
    public ItemStack getIcon()
    {
        return IEMultiblockLogic.ARC_FURNACE.iconStack();
    }
}
