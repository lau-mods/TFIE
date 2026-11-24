package net.claustra01.tfie.data;

import java.util.Locale;
import java.util.Map;
import blusunrize.immersiveengineering.api.IETags;
import com.google.common.collect.ImmutableMap;
import com.therighthon.afc.common.blocks.AFCBlocks;
import com.therighthon.afc.common.blocks.AFCWood;
import com.therighthon.afc.common.items.AFCItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.calendar.ICalendar;

public interface Accessors
{
    default Ingredient ingredientOf(Metal metal, Metal.ItemType type)
    {
        return type.isCommonTagPart()
            ? Ingredient.of(commonTagOf(metal, type))
            : Ingredient.of(TFCItems.METAL_ITEMS.get(metal).get(type).get());
    }

    default Ingredient ingredientOf(Ingredient... values)
    {
        return CompoundIngredient.of(values);
    }

    default <T> TagKey<T> logsTagOf(ResourceKey<Registry<T>> registry, Wood wood)
    {
        return TagKey.create(registry, Helpers.identifier(wood.getSerializedName() + "_logs"));
    }

    default TagKey<Item> commonTagOf(Metal metal, Metal.ItemType type)
    {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

    default <T> TagKey<T> commonTagOf(ResourceKey<Registry<T>> key, String name)
    {
        return TagKey.create(key, ResourceLocation.fromNamespaceAndPath("c", name.toLowerCase(Locale.ROOT)));
    }

    default Item dyeOf(DyeColor color)
    {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_dye"));
    }

    default Item dyedOf(DyeColor color, String suffix)
    {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_" + suffix));
    }

    default Item itemOf(ResourceLocation name)
    {
        assert BuiltInRegistries.ITEM.containsKey(name) : "No item '" + name + "'";
        return BuiltInRegistries.ITEM.get(name);
    }

    default Fluid fluidOf(DyeColor color)
    {
        return TFCFluids.COLORED_FLUIDS.get(color).getSource();
    }

    default Fluid fluidOf(SimpleFluid fluid)
    {
        return TFCFluids.SIMPLE_FLUIDS.get(fluid).getSource();
    }

    default String nameOf(Ingredient ingredient)
    {
        if (ingredient.getCustomIngredient() instanceof CompoundIngredient(java.util.List<Ingredient> children)) return nameOf(children.get(0));
        final Ingredient.Value value = ingredient.getValues()[0];
        if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) return tag.location().getPath();
        if (value instanceof Ingredient.ItemValue(ItemStack item)) return nameOf(item.getItem());
        throw new AssertionError("Unknown ingredient value");
    }

    default String nameOf(Fluid fluid)
    {
        assert fluid != net.minecraft.world.level.material.Fluids.EMPTY : "Should never get name of Items.AIR";
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }

    default String nameOf(ItemLike item)
    {
        assert item.asItem() != net.minecraft.world.item.Items.AIR : "Should never get name of Items.AIR";
        assert item.asItem() != net.minecraft.world.item.Items.BARRIER : "Should never get name of Items.BARRIER";
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    default int hours(int hours)
    {
        return hours * ICalendar.CALENDAR_TICKS_IN_HOUR;
    }

    /**
     * Given a {@code Map<T1, Map<T2, V1>>}, and a key {@code T2}, constructs a map of all the mappings of {@code T1} to maps which contain
     * an entry for the given key {@code T2}
     *
     * @return An immutable map, with iteration order given by iteration order of the input map
     */
    default <T1, T2, V> Map<T1, V> pivot(Map<T1, Map<T2, V>> map, T2 key)
    {
        // This method must maintain a consistent, deterministic ordering, so we can't collect into a typical
        // hash map - we must use an order-preserving map here - immutable map is the easiest way to do that
        final ImmutableMap.Builder<T1, V> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<T1, Map<T2, V>> entry : map.entrySet())
            if (entry.getValue().containsKey(key))
                builder.put(entry.getKey(), entry.getValue().get(key));
        return builder.build();
    }

    default BlockGetter empty()
    {
        return new BlockGetter()
        {
            @Nullable
            @Override
            public BlockEntity getBlockEntity(BlockPos pos)
            {
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos)
            {
                return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
            }

            @Override
            public FluidState getFluidState(BlockPos pos)
            {
                return net.minecraft.world.level.material.Fluids.EMPTY.defaultFluidState();
            }

            @Override
            public int getHeight()
            {
                return 0;
            }

            @Override
            public int getMinBuildHeight()
            {
                return 0;
            }
        };
    }

    // ===== Added methods ===== //

    default TagKey<Item> ingotTagOf(Metal metal)
    {
        return commonTagOf(Registries.ITEM, "ingots/" + metal.getSerializedName());
    }

    default Ingredient getIronIngotsTag()
    {
        return CompoundIngredient.of(Ingredient.of(Tags.Items.INGOTS_IRON), Ingredient.of(ingotTagOf(Metal.WROUGHT_IRON)));
    }

    default Ingredient getIronRodsTag()
    {
        return CompoundIngredient.of(Ingredient.of(IETags.ironRod), Ingredient.of(commonTagOf(Metal.WROUGHT_IRON, Metal.ItemType.ROD)));
    }

    default Ingredient ingredientOf(ItemLike... values)
    {
        return CompoundIngredient.of(Ingredient.of(values));
    }

    default ItemLike lumberOf(Wood wood)
    {
        return TFCItems.LUMBER.get(wood);
    }

    default ItemLike woodOf(Wood wood, Wood.BlockType type)
    {
        return TFCBlocks.WOODS.get(wood).get(type);
    }

    default ItemLike lumberOf(AFCWood wood)
    {
        return AFCItems.LUMBER.get(wood);
    }

    default ItemLike woodOf(AFCWood wood, Wood.BlockType type)
    {
        return AFCBlocks.WOODS.get(wood).get(type);
    }
}
