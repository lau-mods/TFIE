package net.claustra01.tfie.common.recipes;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeSerializer;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes
{
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, TFIE.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, IERecipeSerializer<ArcFurnaceRecipe>> ARC_FURNACE =
            SERIALIZERS.register("arc_furnace", ModeAwareArcFurnaceRecipeSerializer::new);

    private ModRecipes()
    {
    }

    public static void register(IEventBus eventBus)
    {
        SERIALIZERS.register(eventBus);
    }
}
