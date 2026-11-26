package net.claustra01.tfie.common.condition;

import com.mojang.serialization.MapCodec;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.condition.OreEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class TFIEConditions
{
    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, TFIE.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<FluidExistsCondition>> FLUID_EXISTS =
            CONDITIONS.register("fluid_exists", () -> FluidExistsCondition.CODEC);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<LoggedRecipeCondition>> LOGGED_RECIPE =
            CONDITIONS.register("logged_recipe", () -> LoggedRecipeCondition.CODEC);

    private TFIEConditions()
    {
    }

    public static LoggedRecipeCondition recipe(ResourceLocation recipe, String reason, String modId,
            List<ResourceLocation> items, List<ResourceLocation> fluids, Metal metal, String ore)
    {
        return recipeForMetals(recipe, reason, modId, items, fluids,
                metal == null ? List.of() : List.of(metal), ore);
    }

    public static LoggedRecipeCondition recipeForMetals(ResourceLocation recipe, String reason, String modId,
            List<ResourceLocation> items, List<ResourceLocation> fluids, List<Metal> metals, String ore)
    {
        List<ICondition> conditions = new ArrayList<>();
        if(modId != null)
            conditions.add(new ModLoadedCondition(modId));
        items.forEach(item -> conditions.add(new ItemExistsCondition(item)));
        fluids.forEach(fluid -> conditions.add(new FluidExistsCondition(fluid)));
        metals.forEach(metal -> conditions.add(new MetalEnabledCondition(metal)));
        if(ore != null)
            conditions.add(new OreEnabledCondition(ore));
        return new LoggedRecipeCondition(recipe, reason, List.copyOf(conditions));
    }

    public static void register(IEventBus eventBus)
    {
        CONDITIONS.register(eventBus);
    }
}
