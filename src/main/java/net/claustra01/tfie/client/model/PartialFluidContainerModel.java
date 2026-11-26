package net.claustra01.tfie.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PartialFluidContainerModel extends BakedModelWrapper<BakedModel>
{
    private final float verticalOffset;

    public PartialFluidContainerModel(BakedModel originalModel, int level)
    {
        super(originalModel);
        verticalOffset = (16 - level) * 0.25f / 16;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random)
    {
        return moveFluidLayer(super.getQuads(state, side, random));
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random, ModelData data,
                                    RenderType renderType)
    {
        return moveFluidLayer(super.getQuads(state, side, random, data, renderType));
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack stack, boolean fabulous)
    {
        return List.of(this);
    }

    private List<BakedQuad> moveFluidLayer(List<BakedQuad> quads)
    {
        List<BakedQuad> moved = new ArrayList<>(quads.size());
        for(BakedQuad quad : quads)
        {
            if(quad.getTintIndex() != 1)
            {
                moved.add(quad);
                continue;
            }

            int[] vertices = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
            for(int vertex = 0; vertex < 4; vertex++)
            {
                int yIndex = vertex * IQuadTransformer.STRIDE + IQuadTransformer.POSITION + 1;
                float y = Float.intBitsToFloat(vertices[yIndex]);
                vertices[yIndex] = Float.floatToRawIntBits(y - verticalOffset);
            }
            moved.add(new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), quad.getSprite(),
                    quad.isShade(), quad.hasAmbientOcclusion()));
        }
        return moved;
    }
}
