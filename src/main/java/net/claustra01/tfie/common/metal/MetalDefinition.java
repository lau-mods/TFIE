package net.claustra01.tfie.common.metal;

import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record MetalDefinition(String id, MetalSource source, Set<MetalShape> shapes, Metal tfcmMetal)
{
    public MetalDefinition
    {
        shapes = Set.copyOf(shapes);
    }

    public boolean has(MetalShape shape)
    {
        return shapes.contains(shape);
    }

    public ResourceLocation inputTag(MetalShape shape)
    {
        if(!has(shape))
            throw new IllegalArgumentException(id + " does not have " + shape.id());
        return ResourceLocation.fromNamespaceAndPath("c", shape.tagFolder() + "/" + id);
    }

    public ResourceLocation output(MetalShape shape)
    {
        if(!has(shape))
            throw new IllegalArgumentException(id + " does not have " + shape.id());
        String namespace = switch(source)
        {
            case TFC -> shape == MetalShape.PLATE || shape == MetalShape.WIRE ? "tfcm" : "tfc";
            case FIRMALIFE -> shape == MetalShape.PLATE || shape == MetalShape.WIRE ? "tfcm" : "firmalife";
            case TFCM -> "tfcm";
        };
        return ResourceLocation.fromNamespaceAndPath(namespace, "metal/" + shape.id() + "/" + id);
    }

    public List<ICondition> loadConditions(MetalShape shape)
    {
        List<ICondition> conditions = new ArrayList<>();
        if(source == MetalSource.FIRMALIFE)
            conditions.add(new ModLoadedCondition("firmalife"));
        if(tfcmMetal != null)
            conditions.add(new MetalEnabledCondition(tfcmMetal));
        if(shape != MetalShape.INGOT)
            conditions.add(new ItemExistsCondition(output(MetalShape.INGOT)));
        conditions.add(new ItemExistsCondition(output(shape)));
        return List.copyOf(conditions);
    }
}
