package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.multiblocks.BlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Set;

@Mixin(targets = "blusunrize.immersiveengineering.api.multiblocks.TemplateMultiblock", remap = false)
public abstract class TemplateMultiblockMixin
{
    private static final Set<ResourceLocation> TFIE_STRICT_BLOCKS = Set.of(
            ResourceLocation.fromNamespaceAndPath("tfc", "metal/block/steel"),
            ResourceLocation.fromNamespaceAndPath("tfc", "metal/block/black_steel"),
            ResourceLocation.fromNamespaceAndPath("tfc", "fire_bricks"),
            ResourceLocation.fromNamespaceAndPath("tfc", "crucible"));
    private static final Set<ResourceLocation> TFIE_STRICT_STATES = Set.of(
            ResourceLocation.fromNamespaceAndPath("immersiveengineering", "slab_sheetmetal_steel"),
            ResourceLocation.fromNamespaceAndPath("immersiveengineering", "steel_fence"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "hopper"));

    @Redirect(method = {"isBlockTrigger", "createStructure"}, at = @At(value = "INVOKE", target =
            "Lblusunrize/immersiveengineering/api/multiblocks/BlockMatcher;matches(" +
                    "Lnet/minecraft/world/level/block/state/BlockState;" +
                    "Lnet/minecraft/world/level/block/state/BlockState;" +
                    "Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;" +
                    "Ljava/util/List;)Lblusunrize/immersiveengineering/api/multiblocks/BlockMatcher$Result;"))
    private BlockMatcher.Result requireSpecifiedTfcBlock(BlockState expected, BlockState found, Level level,
                                                         BlockPos pos,
                                                         List<BlockMatcher.MatcherPredicate> additional)
    {
        ResourceLocation expectedId = BuiltInRegistries.BLOCK.getKey(expected.getBlock());
        if(TFIE_STRICT_BLOCKS.contains(expectedId))
            return expected.getBlock() == found.getBlock()
                    ? BlockMatcher.Result.allow(100)
                    : BlockMatcher.Result.deny(100);
        if(TFIE_STRICT_STATES.contains(expectedId))
            return expected.equals(found)
                    ? BlockMatcher.Result.allow(100)
                    : BlockMatcher.Result.deny(100);
        return BlockMatcher.matches(expected, found, level, pos, additional);
    }
}
