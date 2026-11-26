package net.claustra01.tfie.gametest.herbicide;

import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.common.entities.ChemthrowerShotEntity;
import blusunrize.immersiveengineering.common.register.IEFluids;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.plant.fruit.SeasonalPlantBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class HerbicideGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private HerbicideGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void herbicide_tfc_soils_and_crops(GameTestHelper helper)
    {
        helper.assertTrue(ChemthrowerHandler.effectList.stream()
                        .filter(entry -> entry.getFirst().equals(IETags.fluidHerbicide)).count() == 1,
                "TFIE must replace the standard #c:herbicide effect with one effect");

        CropBlock crop = (CropBlock) TFCBlocks.CROPS.get(Crop.BEET).get();
        Block dead = TFCBlocks.DEAD_CROPS.get(Crop.BEET).get();
        Block wild = TFCBlocks.WILD_CROPS.get(Crop.BEET).get();

        assertCultivatedCropTreatment(helper, new BlockPos(2, 2, 2), SoilBlockType.GRASS,
                SoilBlockType.DIRT, crop, 0.99F, 1);
        assertCultivatedCropTreatment(helper, new BlockPos(6, 2, 2), SoilBlockType.GRASS,
                SoilBlockType.DIRT, crop, 1.0F, 3);
        assertDroppedCropTreatment(helper, new BlockPos(10, 2, 2), SoilBlockType.GRASS,
                SoilBlockType.DIRT, dead.defaultBlockState().setValue(DeadCropBlock.MATURE, false));
        assertDroppedCropTreatment(helper, new BlockPos(14, 2, 2), SoilBlockType.GRASS,
                SoilBlockType.DIRT, wild.defaultBlockState().setValue(WildCropBlock.MATURE, false));

        assertCultivatedCropTreatment(helper, new BlockPos(2, 2, 7), SoilBlockType.FARMLAND,
                SoilBlockType.DIRT, crop, 0.99F, 1);
        assertCultivatedCropTreatment(helper, new BlockPos(6, 2, 7), SoilBlockType.FARMLAND,
                SoilBlockType.DIRT, crop, 1.0F, 3);
        assertDroppedCropTreatment(helper, new BlockPos(10, 2, 7), SoilBlockType.FARMLAND,
                SoilBlockType.DIRT, dead.defaultBlockState().setValue(DeadCropBlock.MATURE, true));
        assertDroppedCropTreatment(helper, new BlockPos(14, 2, 7), SoilBlockType.FARMLAND,
                SoilBlockType.DIRT, wild.defaultBlockState().setValue(WildCropBlock.MATURE, true));

        assertCultivatedCropTreatment(helper, new BlockPos(2, 2, 12), SoilBlockType.CLAY_GRASS,
                SoilBlockType.CLAY, crop, 0.99F, 0);
        assertCultivatedCropTreatment(helper, new BlockPos(6, 2, 12), SoilBlockType.CLAY_GRASS,
                SoilBlockType.CLAY, crop, 1.0F, 0);
        assertDroppedCropTreatment(helper, new BlockPos(10, 2, 12), SoilBlockType.CLAY_GRASS,
                SoilBlockType.CLAY, dead.defaultBlockState().setValue(DeadCropBlock.MATURE, false));
        assertDroppedCropTreatment(helper, new BlockPos(14, 2, 12), SoilBlockType.CLAY_GRASS,
                SoilBlockType.CLAY, wild.defaultBlockState().setValue(WildCropBlock.MATURE, false));
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void herbicide_leaves_bushes_and_other_surfaces(GameTestHelper helper)
    {
        BlockPos leaves = helper.absolutePos(new BlockPos(2, 2, 2));
        helper.getLevel().setBlockAndUpdate(leaves, Blocks.OAK_LEAVES.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(leaves.east(), Blocks.OAK_LEAVES.defaultBlockState());
        clearDrops(helper, leaves);
        hit(helper, leaves);
        helper.assertTrue(helper.getLevel().getBlockState(leaves).isAir(),
                "The hit leaves block must be removed");
        helper.assertTrue(helper.getLevel().getBlockState(leaves.east()).is(Blocks.OAK_LEAVES),
                "Herbicide must not remove leaves adjacent to the hit block");
        helper.assertTrue(dropCount(helper, leaves) == 0,
                "Leaves must be removed without drops");

        BlockPos grassBush = helper.absolutePos(new BlockPos(6, 2, 2));
        placePlant(helper, grassBush, SoilBlockType.Variant.OXISOL,
                SoilBlockType.GRASS, Blocks.DANDELION.defaultBlockState());
        clearDrops(helper, grassBush);
        hit(helper, grassBush);
        helper.assertTrue(helper.getLevel().getBlockState(grassBush).is(SoilBlockType.Variant.OXISOL
                        .getBlock(SoilBlockType.DIRT).get()),
                "TFC grass below an ordinary bush must retain its soil variant");
        helper.assertTrue(helper.getLevel().getBlockState(grassBush.above()).isAir()
                        && dropCount(helper, grassBush) == 0,
                "Ordinary bushes over TFC soil must be removed without drops");

        Block seasonal = TFCBlocks.CRANBERRY_BUSH.get();
        helper.assertTrue(seasonal instanceof SeasonalPlantBlock,
                "The staged cranberry must exercise the seasonal plant exception");
        BlockPos seasonalSurface = helper.absolutePos(new BlockPos(10, 2, 2));
        placePlant(helper, seasonalSurface, SoilBlockType.Variant.ENTISOL,
                SoilBlockType.GRASS, seasonal.defaultBlockState());
        hit(helper, seasonalSurface);
        helper.assertTrue(helper.getLevel().getBlockState(seasonalSurface).is(SoilBlockType.Variant.ENTISOL
                        .getBlock(SoilBlockType.DIRT).get()),
                "TFC grass below a seasonal plant must retain its soil variant");
        helper.assertTrue(helper.getLevel().getBlockState(seasonalSurface.above()).is(seasonal),
                "Seasonal plants must survive herbicide surface processing");

        BlockPos snowyDirt = helper.absolutePos(new BlockPos(2, 2, 7));
        helper.getLevel().setBlockAndUpdate(snowyDirt, Blocks.GRASS_BLOCK.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(snowyDirt.above(), Blocks.POPPY.defaultBlockState());
        clearDrops(helper, snowyDirt);
        hit(helper, snowyDirt);
        helper.assertTrue(helper.getLevel().getBlockState(snowyDirt).is(Blocks.DIRT),
                "Other snowy dirt must become Minecraft dirt");
        helper.assertTrue(helper.getLevel().getBlockState(snowyDirt.above()).isAir()
                        && dropCount(helper, snowyDirt) == 0,
                "Other snowy dirt must become dirt and lose its bush without drops");

        BlockPos farm = helper.absolutePos(new BlockPos(6, 2, 7));
        helper.getLevel().setBlockAndUpdate(farm, Blocks.FARMLAND.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(farm.above(), Blocks.DANDELION.defaultBlockState());
        clearDrops(helper, farm);
        hit(helper, farm);
        helper.assertTrue(helper.getLevel().getBlockState(farm).is(Blocks.DIRT),
                "Other farmland must become Minecraft dirt");
        helper.assertTrue(helper.getLevel().getBlockState(farm.above()).isAir()
                        && dropCount(helper, farm) == 0,
                "Other farmland must become dirt and lose its bush without drops");

        BlockPos unrelated = helper.absolutePos(new BlockPos(10, 2, 7));
        helper.getLevel().setBlockAndUpdate(unrelated, Blocks.STONE.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(unrelated.above(), Blocks.DANDELION.defaultBlockState());
        hit(helper, unrelated);
        helper.assertTrue(helper.getLevel().getBlockState(unrelated).is(Blocks.STONE),
                "Unrelated surfaces must not change");
        helper.assertTrue(helper.getLevel().getBlockState(unrelated.above()).is(Blocks.DANDELION),
                "Plants above unrelated surfaces must not change");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void herbicide_hit_scope_and_entities(GameTestHelper helper)
    {
        ServerLevel level = helper.getLevel();
        CropBlock crop = (CropBlock) TFCBlocks.CROPS.get(Crop.CARROT).get();
        BlockPos farmland = helper.absolutePos(new BlockPos(5, 2, 5));
        placeCultivatedCrop(helper, farmland, SoilBlockType.Variant.MOLLISOL,
                SoilBlockType.FARMLAND, crop, 0.5F);
        BlockPos cropPos = farmland.above();
        placeCultivatedCrop(helper, farmland.east(), SoilBlockType.Variant.MOLLISOL,
                SoilBlockType.FARMLAND, crop, 0.5F);

        hit(helper, cropPos);
        helper.assertTrue(level.getBlockState(cropPos).is(crop),
                "A direct crop hit must not destroy the crop");
        helper.assertTrue(level.getBlockState(farmland).is(SoilBlockType.Variant.MOLLISOL
                        .getBlock(SoilBlockType.FARMLAND).get()),
                "A direct crop hit must not process the surface below it");
        helper.assertTrue(level.getBlockState(farmland.east().above()).is(crop),
                "A direct crop hit must not destroy adjacent crops");
        helper.assertTrue(level.getBlockState(farmland.east()).is(SoilBlockType.Variant.MOLLISOL
                        .getBlock(SoilBlockType.FARMLAND).get()),
                "A direct crop hit must not process adjacent surfaces");

        Player target = helper.makeMockPlayer(GameType.SURVIVAL);
        float health = target.getHealth();
        int effects = target.getActiveEffects().size();
        effect(helper).applyToEntity(target, null, null, ItemStack.EMPTY,
                IEFluids.HERBICIDE.getStill());
        helper.assertTrue(target.getHealth() == health
                        && target.getActiveEffects().size() == effects
                        && !target.isOnFire(),
                "TFIE herbicide must add no living entity effect");

        BlockPos hitPos = helper.absolutePos(new BlockPos(12, 2, 5));
        level.setBlockAndUpdate(hitPos, Blocks.STONE.defaultBlockState());
        ChemthrowerShotEntity inside = shot(level, hitPos.getX() - 0.2D,
                hitPos.getY() + 0.5D, hitPos.getZ() + 0.5D);
        ChemthrowerShotEntity outside = shot(level, hitPos.getX() - 0.8D,
                hitPos.getY() + 0.5D, hitPos.getZ() + 0.5D);
        helper.assertTrue(level.addFreshEntity(inside) && level.addFreshEntity(outside),
                "The staged chemthrower shots must enter the world");
        hit(helper, hitPos);
        helper.assertTrue(inside.isRemoved(),
                "A shot inside the hit AABB inflated by 0.25 must be removed");
        helper.assertFalse(outside.isRemoved(),
                "A shot outside the hit AABB inflated by 0.25 must remain");
        helper.succeed();
    }

    private static void placeCultivatedCrop(GameTestHelper helper, BlockPos surface,
                                            SoilBlockType.Variant variant, SoilBlockType type,
                                            CropBlock crop, float growth)
    {
        BlockState cropState = crop.defaultBlockState()
                .setValue(crop.getAgeProperty(), crop.getMaxAge());
        placePlant(helper, surface, variant, type, cropState);
        helper.assertTrue(helper.getLevel().getBlockEntity(surface.above()) instanceof CropBlockEntity,
                "The staged cultivated crop must have a crop block entity");
        ((CropBlockEntity) helper.getLevel().getBlockEntity(surface.above())).setGrowth(growth);
    }

    private static void assertCultivatedCropTreatment(GameTestHelper helper, BlockPos relativeSurface,
                                                       SoilBlockType surfaceType,
                                                       SoilBlockType resultType, CropBlock crop,
                                                       float growth, long expectedDrops)
    {
        BlockPos surface = helper.absolutePos(relativeSurface);
        placeCultivatedCrop(helper, surface, SoilBlockType.Variant.ENTISOL,
                surfaceType, crop, growth);
        clearDrops(helper, surface);
        helper.getLevel().random.setSeed(3L);
        hit(helper, surface);
        helper.assertTrue(helper.getLevel().getBlockState(surface).is(SoilBlockType.Variant.ENTISOL
                        .getBlock(resultType).get()),
                "Herbicide must preserve the Entisol variant for " + surfaceType);
        helper.assertTrue(helper.getLevel().getBlockState(surface.above()).isAir(),
                "The cultivated crop must be broken after surface treatment");
        helper.assertTrue(dropCount(helper, surface) == expectedDrops,
                "Cultivated crop drops must reflect MATURE=" + (growth >= 1.0F)
                        + " for growth " + growth + " over " + surfaceType);
    }

    private static void assertDroppedCropTreatment(GameTestHelper helper, BlockPos relativeSurface,
                                                    SoilBlockType surfaceType,
                                                    SoilBlockType resultType, BlockState cropState)
    {
        BlockPos surface = helper.absolutePos(relativeSurface);
        placePlant(helper, surface, SoilBlockType.Variant.ENTISOL, surfaceType, cropState);
        clearDrops(helper, surface);
        hit(helper, surface);
        helper.assertTrue(helper.getLevel().getBlockState(surface).is(SoilBlockType.Variant.ENTISOL
                        .getBlock(resultType).get()),
                "Herbicide must preserve the Entisol variant for " + surfaceType);
        helper.assertTrue(helper.getLevel().getBlockState(surface.above()).isAir()
                        && dropCount(helper, surface) > 0,
                "Dead and wild crops over " + surfaceType + " must be broken with drops");
    }

    private static void placePlant(GameTestHelper helper, BlockPos surface,
                                   SoilBlockType.Variant variant, SoilBlockType type,
                                   BlockState plant)
    {
        helper.getLevel().setBlockAndUpdate(surface.below(), Blocks.STONE.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(surface, variant.getBlock(type).get().defaultBlockState());
        helper.getLevel().setBlockAndUpdate(surface.above(), plant);
    }

    private static void hit(GameTestHelper helper, BlockPos pos)
    {
        effect(helper).applyToBlock(helper.getLevel(), new BlockHitResult(
                        Vec3.atCenterOf(pos), Direction.UP, pos, false), null, null,
                ItemStack.EMPTY, IEFluids.HERBICIDE.getStill());
    }

    private static ChemthrowerEffect effect(GameTestHelper helper)
    {
        Fluid fluid = IEFluids.HERBICIDE.getStill();
        helper.assertTrue(fluid.is(IETags.fluidHerbicide),
                "IE herbicide must belong to #c:herbicide");
        ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluid);
        helper.assertTrue(effect != null, "#c:herbicide must have a chemthrower effect");
        return effect;
    }

    private static ChemthrowerShotEntity shot(ServerLevel level, double x, double y, double z)
    {
        return new ChemthrowerShotEntity(level, x, y, z,
                new FluidStack(IEFluids.HERBICIDE.getStill(), 1));
    }

    private static long dropCount(GameTestHelper helper, BlockPos pos)
    {
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                new AABB(pos).inflate(2.0D)).stream().mapToInt(entity -> entity.getItem().getCount()).sum();
    }

    private static void clearDrops(GameTestHelper helper, BlockPos pos)
    {
        helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2.0D))
                .forEach(ItemEntity::discard);
    }
}
