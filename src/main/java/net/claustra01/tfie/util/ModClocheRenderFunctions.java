package net.claustra01.tfie.util;

import blusunrize.immersiveengineering.api.crafting.ClocheRecipe;
import blusunrize.immersiveengineering.api.crafting.ClocheRenderFunction;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import malte0811.dualcodecs.DualCodecs;
import malte0811.dualcodecs.DualCompositeMapCodecs;
import malte0811.dualcodecs.DualMapCodec;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.joml.Vector3f;

import java.util.Collection;

public final class ModClocheRenderFunctions
{
    public static final ResourceLocation DOUBLE_CROP_TFC =
            ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "doublecroptfc");

    private ModClocheRenderFunctions()
    {
    }

    public static void register()
    {
        ClocheRenderFunction.RENDER_FUNCTION_FACTORIES.put(DOUBLE_CROP_TFC, DoubleCrop.CODEC);
    }

    public static void initializeSoils()
    {
        for(SoilBlockType.Variant soil : SoilBlockType.Variant.values())
            ClocheRecipe.registerSoilTexture(
                    Ingredient.of(TFCBlocks.SOIL.get(SoilBlockType.DIRT).get(soil)),
                    ResourceLocation.fromNamespaceAndPath(TerraFirmaCraft.MOD_ID,
                            "block/farmland/" + soil.name().toLowerCase()));
    }

    public static final class DoubleCrop implements ClocheRenderFunction
    {
        public static final DualMapCodec<? super RegistryFriendlyByteBuf, DoubleCrop> CODEC =
                DualCompositeMapCodecs.composite(
                        DualCodecs.registryEntry(BuiltInRegistries.BLOCK).fieldOf("block"),
                        function -> function.block,
                        DualCodecs.INT.fieldOf("doublingAge"), function -> function.doublingAge,
                        DoubleCrop::new);

        private final Block block;
        private final IntegerProperty ageProperty;
        private final int maxAge;
        private final int doublingAge;

        public DoubleCrop(Block block, int doublingAge)
        {
            this.block = block;
            this.ageProperty = findAgeProperty(block);
            this.maxAge = ageProperty.getPossibleValues().stream().max(Integer::compareTo).orElseThrow();
            this.doublingAge = doublingAge;
        }

        @Override
        public float getScale(ItemStack seed, float growth)
        {
            return 0.6875F;
        }

        @Override
        public Collection<Pair<BlockState, Transformation>> getBlocks(ItemStack seed, float growth)
        {
            int age = Math.min(maxAge, Math.round(maxAge * growth));
            BlockState bottom = block.defaultBlockState().setValue(ageProperty, age);
            if(age < doublingAge)
                return ImmutableList.of(Pair.of(bottom, new Transformation(null)));
            BlockState top = bottom.setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP);
            return ImmutableList.of(
                    Pair.of(bottom, new Transformation(null)),
                    Pair.of(top, new Transformation(new Vector3f(0, 1, 0), null, null, null)));
        }

        @Override
        public DualMapCodec<? super RegistryFriendlyByteBuf, ? extends ClocheRenderFunction> codec()
        {
            return CODEC;
        }

        public int maxAge()
        {
            return maxAge;
        }

        public int doublingAge()
        {
            return doublingAge;
        }

        private static IntegerProperty findAgeProperty(Block block)
        {
            for(Property<?> property : block.defaultBlockState().getProperties())
                if(property instanceof IntegerProperty integerProperty && property.getName().equals("age"))
                    return integerProperty;
            throw new IllegalArgumentException(block.getDescriptionId() + " is not a crop block");
        }
    }
}
