package net.claustra01.tfie.common.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public record FluidExistsCondition(ResourceLocation fluid) implements ICondition
{
    public static final MapCodec<FluidExistsCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidExistsCondition::fluid))
                    .apply(instance, FluidExistsCondition::new));

    @Override
    public boolean test(IContext context)
    {
        return BuiltInRegistries.FLUID.containsKey(fluid);
    }

    @Override
    public MapCodec<? extends ICondition> codec()
    {
        return CODEC;
    }
}
