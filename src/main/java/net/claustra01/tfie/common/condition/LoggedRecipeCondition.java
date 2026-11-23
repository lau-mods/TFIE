package net.claustra01.tfie.common.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.claustra01.tfie.TFIE;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.List;

public record LoggedRecipeCondition(ResourceLocation recipe, String reason, List<ICondition> conditions)
        implements ICondition
{
    public static final MapCodec<LoggedRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("recipe").forGetter(LoggedRecipeCondition::recipe),
                    Codec.STRING.fieldOf("reason").forGetter(LoggedRecipeCondition::reason),
                    ICondition.LIST_CODEC.fieldOf("conditions").forGetter(LoggedRecipeCondition::conditions))
                    .apply(instance, LoggedRecipeCondition::new));

    @Override
    public boolean test(IContext context)
    {
        for(ICondition condition : conditions)
        {
            if(!condition.test(context))
            {
                TFIE.LOGGER.warn("Skipping TFIE recipe {}: {} ({})", recipe, reason, condition);
                return false;
            }
        }
        return true;
    }

    @Override
    public MapCodec<? extends ICondition> codec()
    {
        return CODEC;
    }
}
