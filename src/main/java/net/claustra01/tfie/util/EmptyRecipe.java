package net.claustra01.tfie.util;

import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.recipes.INoopInputRecipe;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class EmptyRecipe implements INoopInputRecipe
{
    public static final EmptyRecipe INSTANCE = new EmptyRecipe();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "empty");
    public static final RecipeType<EmptyRecipe> TYPE = RecipeType.simple(ID);
    public static final RecipeSerializer<EmptyRecipe> SERIALIZER = new RecipeSerializerImpl<>(INSTANCE);

    private EmptyRecipe()
    {
    }

    @Override
    public RecipeType<?> getType()
    {
        return TYPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }
}
