package net.claustra01.tfie.gametest.hemp;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.crop.HempCropBlock;
import net.claustra01.tfie.common.crop.HempCropBlockEntity;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blockentities.PileBlockEntity;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.SnowPileBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.tracker.WorldTracker;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.placement.ClimatePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class HempGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String TEMPLATE_NAMESPACE = TFIE.MOD_ID;
    private static final TagKey<Block> CAN_BE_SNOW_PILED = BlockTags.create(
            ResourceLocation.fromNamespaceAndPath("tfc", "can_be_snow_piled"));

    private HempGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void planting_registration_tags_and_properties(GameTestHelper helper)
    {
        ServerLevel level = helper.getLevel();
        BlockPos farmlandPos = helper.absolutePos(new BlockPos(1, 2, 1));
        level.setBlockAndUpdate(farmlandPos, farmland().defaultBlockState());
        var player = FakePlayerFactory.getMinecraft(level);
        ItemStack seeds = new ItemStack(IEItems.Misc.HEMP_SEEDS.get());
        player.setItemInHand(InteractionHand.MAIN_HAND, seeds);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(farmlandPos), net.minecraft.core.Direction.UP,
                farmlandPos, false);
        InteractionResult result = InteractionManager.onItemUse(seeds,
                new UseOnContext(player, InteractionHand.MAIN_HAND, hit), false).orElse(InteractionResult.PASS);

        BlockPos cropPos = farmlandPos.above();
        helper.assertTrue(result.consumesAction() && level.getBlockState(cropPos).is(ModBlocks.HEMP_CROP.get()),
                "IE hemp seeds must plant tfie:crop/hemp on TFC farmland");
        helper.assertTrue(level.getBlockEntity(cropPos) instanceof CropBlockEntity,
                "The planted crop must create tfie:crop block entity state");
        helper.assertTrue(ModBlockEntities.CROP.get().isValid(ModBlocks.HEMP_CROP.get().defaultBlockState()),
                "The crop block entity type must accept cultivated hemp");
        helper.assertTrue(ModBlocks.HEMP_CROP.get().asItem() == Items.AIR
                        && ModBlocks.DEAD_HEMP_CROP.get().asItem() == Items.AIR,
                "Cultivated and dead hemp must not have independent block items");
        helper.assertTrue(ModBlocks.WILD_HEMP_CROP.get().asItem() != Items.AIR,
                "Wild hemp must have a block item");
        helper.assertTrue(!BuiltInRegistries.ITEM.containsKey(id("seed"))
                        && !BuiltInRegistries.ITEM.containsKey(id("hemp_fiber")),
                "TFIE must not duplicate IE hemp seeds or fiber");

        for(Block block : List.of(ModBlocks.HEMP_CROP.get(), ModBlocks.DEAD_HEMP_CROP.get(),
                ModBlocks.WILD_HEMP_CROP.get()))
        {
            BlockState state = block.defaultBlockState();
            helper.assertTrue(state.is(BlockTags.CROPS) && state.is(BlockTags.MINEABLE_WITH_HOE),
                    "Every hemp block must be in crops and mineable/hoe");
            helper.assertTrue(state.getCollisionShape(level, cropPos).isEmpty(),
                    "Hemp must not have collision");
            helper.assertTrue(close(state.getDestroySpeed(level, cropPos), 0.4F),
                    "Hemp hardness must be 0.4");
            helper.assertTrue(state.getSoundType() == SoundType.CROP,
                    "Hemp must use crop sounds");
            helper.assertTrue(state.getPistonPushReaction() == PushReaction.DESTROY,
                    "Pistons must destroy hemp");
            helper.assertTrue(block.getFlammability(state, level, cropPos, net.minecraft.core.Direction.UP) == 60
                            && block.getFireSpreadSpeed(state, level, cropPos,
                            net.minecraft.core.Direction.UP) == 30,
                    "Hemp flammability and spread speed must be 60/30");
        }
        helper.assertTrue(ModBlocks.WILD_HEMP_CROP.get().defaultBlockState().is(CAN_BE_SNOW_PILED),
                "Only wild hemp must allow snow piles");
        helper.assertTrue(!ModBlocks.HEMP_CROP.get().defaultBlockState().is(CAN_BE_SNOW_PILED)
                        && !ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState().is(CAN_BE_SNOW_PILED),
                "Cultivated and dead hemp must not allow snow piles");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void climate_nutrients_and_double_growth(GameTestHelper helper)
    {
        HempCropBlock crop = ModBlocks.HEMP_CROP.get();
        ClimateRange climate = crop.getClimateRange();
        helper.assertTrue(climate.minTemperature() == 10.0F && climate.maxTemperature() == 31.0F
                        && climate.minHydration() == 30 && climate.maxHydration() == 80,
                "Hemp climate must be 10-31 C and hydration 30-80");
        helper.assertTrue(climate.temperatureWiggleRange() == 0.0F && climate.hydrationWiggleRange() == 0,
                "Hemp climate must not have extension ranges");
        helper.assertTrue(climate.checkBoth(30, 10.0F, false)
                        && climate.checkBoth(80, 31.0F, false),
                "Both climate boundaries must be inclusive");
        helper.assertTrue(!climate.checkBoth(29, 10.0F, false)
                        && !climate.checkBoth(81, 31.0F, false)
                        && !climate.checkBoth(30, 9.9F, false)
                        && !climate.checkBoth(80, 31.1F, false),
                "Values immediately outside either range must be rejected");
        helper.assertTrue(close(crop.getNForGrowth(), 0.6F) && close(crop.getPForGrowth(), 0.2F)
                        && close(crop.getKForGrowth(), 0.6F),
                "Shared TFC crop growth must use N/P/K 0.6/0.2/0.6");
        helper.assertTrue(crop.getMaxAge() == 4 && HempCropBlock.AGE.getPossibleValues().size() == 5,
                "Hemp must have ages 0 through 4");

        ServerLevel level = helper.getLevel();
        WorldTracker tracker = WorldTracker.get(level);
        ClimateModel originalClimate = tracker.getClimateModel();
        float[] conditions = {10.0F, 300.0F};
        tracker.setClimateModel(new ClimateModel()
        {
            @Override
            public ClimateModelType<?> type()
            {
                return originalClimate.type();
            }

            @Override
            public float getAverageTemperature(LevelReader world, BlockPos position)
            {
                return conditions[0];
            }

            @Override
            public float getInstantTemperature(LevelReader world, BlockPos position, long tick,
                    int daysInMonth)
            {
                return conditions[0];
            }

            @Override
            public float getAverageRainfall(LevelReader world, BlockPos position)
            {
                return conditions[1];
            }

            @Override
            public float getInstantRainfall(LevelReader world, BlockPos position, long tick,
                    int daysInMonth)
            {
                return conditions[1];
            }
        });
        try
        {
            BlockPos boundaryPos = helper.absolutePos(new BlockPos(8, 2, 8));
            long boundaryTick = Calendars.get(level).getTicks();
            for(float[] condition : new float[][] {
                    {10.0F, 300.0F, 30.0F, 0.0F, 1.0F},
                    {31.0F, 400.0F, 80.0F, 1.0F, 1.0F},
                    {9.9F, 300.0F, 30.0F, 0.0F, 0.0F},
                    {31.1F, 400.0F, 80.0F, 1.0F, 0.0F},
                    {20.0F, 290.0F, 29.0F, 0.0F, 0.0F},
                    {20.0F, 410.0F, 81.0F, 1.0F, 0.0F}})
            {
                conditions[0] = condition[0];
                conditions[1] = condition[1];
                level.setBlockAndUpdate(boundaryPos.below().east(), condition[3] == 1.0F
                        ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(boundaryPos.below(), farmland().defaultBlockState());
                level.setBlockAndUpdate(boundaryPos, crop.defaultBlockState());
                FarmlandBlockEntity boundaryFarmland = (FarmlandBlockEntity) level.getBlockEntity(
                        boundaryPos.below());
                for(FarmlandBlockEntity.NutrientType nutrient : FarmlandBlockEntity.NutrientType.values())
                    boundaryFarmland.setNutrient(nutrient, 1.0F);
                helper.assertTrue(net.dries007.tfc.common.blocks.soil.FarmlandBlock.getInstantHydration(
                                level, boundaryPos.below()) == (int) condition[2],
                        "The planted crop must receive the requested hydration boundary");
                CropBlockEntity boundaryCrop = (CropBlockEntity) level.getBlockEntity(boundaryPos);
                net.dries007.tfc.common.blocks.crop.CropHelpers.growthTickStep(level, boundaryPos,
                        level.getBlockState(boundaryPos), RandomSource.create(37L), boundaryTick - 24000L,
                        boundaryTick, boundaryCrop);
                boolean alive = level.getBlockState(boundaryPos).is(crop);
                helper.assertTrue(alive == (condition[4] == 1.0F),
                        "The shared TFC update must accept inclusive climate boundaries and kill outside values");
            }
        }
        finally
        {
            tracker.setClimateModel(originalClimate);
        }
        BlockPos cropPos = helper.absolutePos(new BlockPos(3, 2, 3));
        level.setBlockAndUpdate(cropPos.below(), farmland().defaultBlockState());
        level.setBlockAndUpdate(cropPos, crop.defaultBlockState());
        for(int age = 0; age < 4; age++)
        {
            level.setBlockAndUpdate(cropPos, crop.defaultBlockState().setValue(HempCropBlock.AGE, age));
            helper.assertTrue(level.getBlockState(cropPos.above()).isAir(),
                    "Ages zero through three must have no upper half");
        }
        FarmlandBlockEntity nutrientFarmland = (FarmlandBlockEntity) level.getBlockEntity(cropPos.below());
        for(FarmlandBlockEntity.NutrientType nutrient : FarmlandBlockEntity.NutrientType.values())
            nutrientFarmland.setNutrient(nutrient, 1.0F);
        CropBlockEntity absorbingCrop = (CropBlockEntity) level.getBlockEntity(cropPos);
        long calendarTick = net.dries007.tfc.util.calendar.Calendars.get(level).getTicks();
        net.dries007.tfc.common.blocks.crop.CropHelpers.growthTickStep(level, cropPos,
                level.getBlockState(cropPos), RandomSource.create(13L), calendarTick - 24000L,
                calendarTick, absorbingCrop);
        helper.assertTrue(nutrientFarmland.getNutrient(FarmlandBlockEntity.NutrientType.NITROGEN) < 1.0F
                        && nutrientFarmland.getNutrient(FarmlandBlockEntity.NutrientType.PHOSPHOROUS) < 1.0F
                        && nutrientFarmland.getNutrient(FarmlandBlockEntity.NutrientType.POTASSIUM) < 1.0F
                        && absorbingCrop.getNAbsorbed() > 0.0F
                        && absorbingCrop.getPAbsorbed() > 0.0F
                        && absorbingCrop.getKAbsorbed() > 0.0F,
                "TFC crop updates must move all three nutrients from farmland into hemp");
        level.setBlockAndUpdate(cropPos, crop.defaultBlockState());
        helper.assertTrue(close(crop.getGrowthLimit(level, cropPos, level.getBlockState(cropPos)), 1.0F),
                "An unobstructed upper position must allow full growth");
        level.setBlockAndUpdate(cropPos.above(), Blocks.STONE.defaultBlockState());
        helper.assertTrue(close(crop.getGrowthLimit(level, cropPos, level.getBlockState(cropPos)), 0.75F),
                "An obstructed upper position must cap growth at 0.75");
        level.removeBlock(cropPos.above(), false);

        BlockState matureBottom = crop.defaultBlockState().setValue(HempCropBlock.AGE, 4)
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
        level.setBlockAndUpdate(cropPos, matureBottom);
        CropBlockEntity cropEntity = (CropBlockEntity) level.getBlockEntity(cropPos);
        cropEntity.setGrowth(1.0F);
        crop.growthTick(level, cropPos, matureBottom, cropEntity);
        helper.assertTrue(level.getBlockState(cropPos.above()).is(crop)
                        && level.getBlockState(cropPos.above()).getValue(DoubleCropBlock.PART)
                        == DoubleCropBlock.Part.TOP,
                "Mature hemp must form a top half");
        CropBlockEntity topEntity = (CropBlockEntity) level.getBlockEntity(cropPos.above());
        float bottomGrowth = cropEntity.getGrowth();
        topEntity.setGrowth(0.25F);
        CropBlockEntity.serverTickBottomPartOnly(level, cropPos.above(), level.getBlockState(cropPos.above()),
                topEntity);
        helper.assertTrue(close(topEntity.getGrowth(), 0.25F) && close(cropEntity.getGrowth(), bottomGrowth),
                "The top half must not run growth or nutrient consumption");

        BlockPos darkPos = helper.absolutePos(new BlockPos(2, 2, 2));
        level.setBlockAndUpdate(darkPos.below(), farmland().defaultBlockState());
        level.setBlockAndUpdate(darkPos, crop.defaultBlockState());
        for(int x = -1; x <= 1; x++)
        {
            for(int z = -1; z <= 1; z++)
            {
                BlockPos roof = darkPos.offset(x, 2, z);
                level.setBlockAndUpdate(roof, Blocks.STONE.defaultBlockState());
                level.setBlockAndUpdate(darkPos.offset(x, -2, z), Blocks.STONE.defaultBlockState());
                if(x != 0 || z != 0)
                {
                    level.setBlockAndUpdate(darkPos.offset(x, -1, z), Blocks.STONE.defaultBlockState());
                    level.setBlockAndUpdate(darkPos.offset(x, 0, z), Blocks.STONE.defaultBlockState());
                    level.setBlockAndUpdate(darkPos.offset(x, 1, z), Blocks.STONE.defaultBlockState());
                }
            }
        }
        BlockPos expiredPos = helper.absolutePos(new BlockPos(6, 2, 2));
        level.setBlockAndUpdate(expiredPos.below(), farmland().defaultBlockState());
        level.setBlockAndUpdate(expiredPos, crop.defaultBlockState());
        CropBlockEntity expired = (CropBlockEntity) level.getBlockEntity(expiredPos);
        expired.setExpiry(1000.0F);
        net.dries007.tfc.common.blocks.crop.CropHelpers.growthTickStep(level, expiredPos,
                level.getBlockState(expiredPos), RandomSource.create(31L), calendarTick - 24000L,
                calendarTick, expired);
        helper.assertTrue(level.getBlockState(expiredPos).is(ModBlocks.DEAD_HEMP_CROP.get()),
                "Expiry in the shared TFC update must turn hemp into its dead crop");
        helper.runAfterDelay(5, () -> {
            helper.assertTrue(!net.dries007.tfc.common.blocks.crop.CropHelpers.lightValid(level, darkPos)
                            && close(crop.getGrowthLimit(level, darkPos,
                            level.getBlockState(darkPos)), 0.0F),
                    "Light below 12 must stop hemp growth through the TFC growth limit");
            helper.succeed();
        });
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void controlled_crop_loot_and_upper_half_deduplication(GameTestHelper helper)
    {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(2, 2, 2));
        HempCropBlock crop = ModBlocks.HEMP_CROP.get();
        for(int age = 0; age < 4; age++)
        {
            BlockState state = crop.defaultBlockState().setValue(HempCropBlock.AGE, age)
                    .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
            List<ItemStack> drops = drops(level, pos, state, null, 23L);
            helper.assertTrue(count(drops, IEItems.Misc.HEMP_SEEDS.get()) == 1
                            && count(drops, IEItems.Ingredients.HEMP_FIBER.get()) == 0,
                    "Ages 0-3 must drop one seed and no fiber");
        }

        BlockState mature = crop.defaultBlockState().setValue(HempCropBlock.AGE, 4)
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
        HempCropBlockEntity noYield = new HempCropBlockEntity(pos, mature);
        noYield.setLevel(level);
        noYield.setYield(0.0F);
        helper.assertTrue(count(drops(level, pos, mature, noYield, 41L),
                IEItems.Ingredients.HEMP_FIBER.get()) == 0, "Yield zero must interpolate to zero fiber");
        HempCropBlockEntity fullYield = new HempCropBlockEntity(pos, mature);
        fullYield.setLevel(level);
        fullYield.setYield(1.0F);
        int fullFiber = count(drops(level, pos, mature, fullYield, 41L),
                IEItems.Ingredients.HEMP_FIBER.get());
        helper.assertTrue(count(drops(level, pos, mature, fullYield, 41L),
                IEItems.Misc.HEMP_SEEDS.get()) == 1,
                "Mature cultivated hemp must still drop exactly one seed");
        helper.assertTrue(fullFiber >= 6 && fullFiber <= 10,
                "Yield one must use a controlled uniform maximum from 6 through 10");
        HempCropBlockEntity halfYield = new HempCropBlockEntity(pos, mature);
        halfYield.setLevel(level);
        halfYield.setYield(0.5F);
        int halfFiber = count(drops(level, pos, mature, halfYield, 41L),
                IEItems.Ingredients.HEMP_FIBER.get());
        helper.assertTrue(halfFiber >= 3 && halfFiber <= 5
                        && Math.abs(halfFiber * 2 - fullFiber) <= 1,
                "Yield one half must interpolate the sampled fiber maximum");
        BlockState cropTop = mature.setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP);
        helper.assertTrue(drops(level, pos, cropTop, fullYield, 41L).isEmpty(),
                "The cultivated upper half must not duplicate drops");

        for(boolean matureState : List.of(false, true))
        {
            BlockState dead = ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState()
                    .setValue(DeadCropBlock.MATURE, matureState)
                    .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
            int seeds = count(drops(level, pos, dead, null, 11L), IEItems.Misc.HEMP_SEEDS.get());
            helper.assertTrue(matureState ? seeds >= 1 && seeds <= 3 : seeds == 1,
                    "Dead hemp seed range must depend on maturity");
            helper.assertTrue(drops(level, pos, dead.setValue(DoubleCropBlock.PART,
                    DoubleCropBlock.Part.TOP), null, 11L).isEmpty(),
                    "The dead upper half must not duplicate drops");

            BlockState wild = ModBlocks.WILD_HEMP_CROP.get().defaultBlockState()
                    .setValue(WildCropBlock.MATURE, matureState)
                    .setValue(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
            List<ItemStack> wildDrops = drops(level, pos, wild, null, 17L);
            int fiber = count(wildDrops, IEItems.Ingredients.HEMP_FIBER.get());
            helper.assertTrue(count(wildDrops, IEItems.Misc.HEMP_SEEDS.get()) == 1
                            && (matureState ? fiber >= 1 && fiber <= 3 : fiber == 0),
                    "Wild hemp must drop one seed and mature fiber in the 1-3 range");
            helper.assertTrue(drops(level, pos, wild.setValue(WildDoubleCropBlock.PART,
                    DoubleCropBlock.Part.TOP), null, 17L).isEmpty(),
                    "The wild upper half must not duplicate drops");
        }
        for(BlockState state : List.of(mature,
                ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState()
                        .setValue(DeadCropBlock.MATURE, false),
                ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState()
                        .setValue(DeadCropBlock.MATURE, true),
                ModBlocks.WILD_HEMP_CROP.get().defaultBlockState()
                        .setValue(WildCropBlock.MATURE, false),
                ModBlocks.WILD_HEMP_CROP.get().defaultBlockState()
                        .setValue(WildCropBlock.MATURE, true)))
            helper.assertTrue(explosionDrops(level, pos, state, fullYield).isEmpty(),
                    "Every hemp loot pool must fail the controlled explosion survival check");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void wild_worldgen_and_seasonal_states(GameTestHelper helper)
    {
        var access = helper.getLevel().registryAccess();
        ResourceKey<ConfiguredFeature<?, ?>> plantConfigured = configured("plant/wild_crop/hemp");
        ResourceKey<ConfiguredFeature<?, ?>> patchConfigured = configured("plant/wild_crop/hemp_patch");
        ResourceKey<PlacedFeature> plantPlaced = placed("plant/wild_crop/hemp");
        ResourceKey<PlacedFeature> patchPlaced = placed("plant/wild_crop/hemp_patch");
        helper.assertTrue(access.registryOrThrow(Registries.CONFIGURED_FEATURE).containsKey(plantConfigured)
                        && access.registryOrThrow(Registries.CONFIGURED_FEATURE).containsKey(patchConfigured)
                        && access.registryOrThrow(Registries.PLACED_FEATURE).containsKey(plantPlaced)
                        && access.registryOrThrow(Registries.PLACED_FEATURE).containsKey(patchPlaced),
                "Both individual and patch configured/placed features must load");
        TagKey<PlacedFeature> cropFeatures = TagKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath("tfc", "feature/crops"));
        helper.assertTrue(access.registryOrThrow(Registries.PLACED_FEATURE).getHolderOrThrow(patchPlaced)
                        .is(cropFeatures), "The patch must be appended to #tfc:feature/crops");
        helper.assertTrue(access.registryOrThrow(Registries.PLACED_FEATURE).getHolderOrThrow(
                        ResourceKey.create(Registries.PLACED_FEATURE,
                                ResourceLocation.parse("tfc:plant/wild_crop/jute_patch"))).is(cropFeatures),
                "The hemp tag entry must retain stock TFC crop patches");
        PlacedFeature loadedPatch = access.registryOrThrow(Registries.PLACED_FEATURE).getOrThrow(patchPlaced);
        ClimatePlacement climatePlacement = loadedPatch.placement().stream()
                .filter(ClimatePlacement.class::isInstance).map(ClimatePlacement.class::cast)
                .findFirst().orElseThrow();
        helper.assertTrue(climatePlacement.getMinTemp() == 10.0F
                        && climatePlacement.getMaxTemp() == 40.0F
                        && climatePlacement.getMinGroundwater() == 140.0F
                        && climatePlacement.getMaxGroundwater() == 360.0F,
                "Loaded wild crop placement must use temperature and groundwater limits");
        for(float temperature : List.of(9.9F, 10.0F, 40.0F, 40.1F))
        {
            for(float groundwater : List.of(139.9F, 140.0F, 360.0F, 360.1F))
            {
                ChunkData climateData = new ChunkData(new ChunkPos(0, 0));
                climateData.generatePartial(new LerpFloatLayer(0, 0, 0, 0),
                        new LerpFloatLayer(0, 0, 0, 0),
                        new LerpFloatLayer(groundwater, groundwater, groundwater, groundwater),
                        new LerpFloatLayer(temperature, temperature, temperature, temperature),
                        ForestType.GRASSLAND);
                boolean expected = temperature >= 10.0F && temperature <= 40.0F
                        && groundwater >= 140.0F && groundwater <= 360.0F;
                helper.assertTrue(climatePlacement.isValidNonHemispheral(climateData,
                                new BlockPos(8, 0, 8), RandomSource.create(3L)) == expected,
                        "The loaded climate placement must accept inclusive temperature and groundwater bounds");
            }
        }

        JsonObject configured = resource(helper,
                "worldgen/configured_feature/plant/wild_crop/hemp_patch.json");
        JsonObject placedPatch = resource(helper,
                "worldgen/placed_feature/plant/wild_crop/hemp_patch.json");
        JsonObject placedPlant = resource(helper,
                "worldgen/placed_feature/plant/wild_crop/hemp.json");
        String configuredText = configured.toString();
        String patchText = placedPatch.toString();
        String plantText = placedPlant.toString();
        helper.assertTrue(configuredText.contains("\"tries\":6")
                        && configuredText.contains("\"xz_spread\":5")
                        && configuredText.contains("\"y_spread\":1"),
                "The wild patch must use 6 tries with spread 5/1");
        helper.assertTrue(patchText.contains("\"chance\":90")
                        && patchText.contains("minecraft:in_square")
                        && patchText.contains("\"min_temperature\":10")
                        && patchText.contains("\"max_temperature\":40")
                        && patchText.contains("\"min_groundwater\":140")
                        && patchText.contains("\"max_groundwater\":360")
                        && !patchText.contains("rainfall"),
                "Patch placement must use rarity, square, temperature, and groundwater boundaries");
        helper.assertTrue(plantText.contains("WORLD_SURFACE_WG")
                        && plantText.contains("tfc:replaceable")
                        && plantText.contains("would_survive"),
                "Individual placement must require surface height, replaceable, and survival");
        helper.assertTrue(TFCConfig.SERVER.wildCropMaturityStartMonth.get() == Month.JUNE
                        && TFCConfig.SERVER.wildCropMaturityEndMonth.get() == Month.OCTOBER,
                "The baseline maturity season must include June through October");
        BlockState matureWild = ModBlocks.WILD_HEMP_CROP.get().defaultBlockState()
                .setValue(WildCropBlock.MATURE, true);
        helper.assertTrue(!ModBlocks.WILD_HEMP_CROP.get().getHiddenState(matureWild, true)
                        .getValue(WildCropBlock.MATURE)
                        && !ModBlocks.WILD_HEMP_CROP.get().getHiddenStateAbove(matureWild, true)
                        .getValue(WildCropBlock.MATURE),
                "Snow cover must clear maturity for both wild crop halves");
        ServerLevel level = helper.getLevel();
        BlockPos wildPos = helper.absolutePos(new BlockPos(4, 2, 4));
        level.setBlockAndUpdate(wildPos.below(), TFCBlocks.SOIL.get(SoilBlockType.GRASS)
                .get(SoilBlockType.Variant.ANDISOL).get().defaultBlockState());
        WildDoubleCropBlock wild = ModBlocks.WILD_HEMP_CROP.get();
        level.setBlockAndUpdate(wildPos, wild.defaultBlockState());
        level.setBlockAndUpdate(wildPos.above(), wild.defaultBlockState()
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP));
        var calendar = Calendars.get(level);
        boolean northern = net.dries007.tfc.client.overworld.SolarCalculator
                .getInNorthernHemisphere(wildPos, level);
        for(Month month : List.of(Month.JUNE, Month.OCTOBER, Month.NOVEMBER))
        {
            for(int i = 0; i < 12 && calendar.getHemispheralCalendarMonthOfYear(northern) != month; i++)
                Calendars.SERVER.skipForwardBy(calendar.getCalendarTicksInMonth());
            helper.assertTrue(calendar.getHemispheralCalendarMonthOfYear(northern) == month,
                    "The calendar must reach the requested hemisphere-adjusted month");
            wild.randomTick(level.getBlockState(wildPos), level, wildPos, RandomSource.create(9L));
            wild.randomTick(level.getBlockState(wildPos.above()), level, wildPos.above(),
                    RandomSource.create(9L));
            boolean expectedMature = month != Month.NOVEMBER;
            helper.assertTrue(level.getBlockState(wildPos).getValue(WildCropBlock.MATURE) == expectedMature
                            && level.getBlockState(wildPos.above()).getValue(WildCropBlock.MATURE)
                            == expectedMature,
                    "Both wild crop halves must follow the hemisphere-adjusted maturity season");
        }
        @SuppressWarnings("unchecked")
        var startMonth = (ModConfigSpec.EnumValue<Month>) TFCConfig.SERVER.spec().getValues()
                .get("blocks.crops.wildCropMaturityStartMonth");
        @SuppressWarnings("unchecked")
        var endMonth = (ModConfigSpec.EnumValue<Month>) TFCConfig.SERVER.spec().getValues()
                .get("blocks.crops.wildCropMaturityEndMonth");
        try
        {
            startMonth.set(Month.NOVEMBER);
            endMonth.set(Month.FEBRUARY);
            for(Month month : List.of(Month.NOVEMBER, Month.JANUARY, Month.FEBRUARY, Month.MARCH))
            {
                for(int i = 0; i < 12 && calendar.getHemispheralCalendarMonthOfYear(northern) != month; i++)
                    Calendars.SERVER.skipForwardBy(calendar.getCalendarTicksInMonth());
                wild.randomTick(level.getBlockState(wildPos), level, wildPos, RandomSource.create(9L));
                wild.randomTick(level.getBlockState(wildPos.above()), level, wildPos.above(),
                        RandomSource.create(9L));
                boolean expectedMature = month != Month.MARCH;
                helper.assertTrue(level.getBlockState(wildPos).getValue(WildCropBlock.MATURE) == expectedMature
                                && level.getBlockState(wildPos.above()).getValue(WildCropBlock.MATURE)
                                == expectedMature,
                        "A maturity range crossing the year must update both wild crop halves");
            }
        }
        finally
        {
            startMonth.set(Month.JUNE);
            endMonth.set(Month.OCTOBER);
        }
        for(int i = 0; i < 12 && calendar.getHemispheralCalendarMonthOfYear(northern)
                != Month.JANUARY; i++)
            Calendars.SERVER.skipForwardBy(calendar.getCalendarTicksInMonth());
        level.setBlockAndUpdate(wildPos, wild.defaultBlockState().setValue(WildCropBlock.MATURE, true));
        level.setBlockAndUpdate(wildPos.above(), wild.defaultBlockState()
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP)
                .setValue(WildCropBlock.MATURE, true));
        SnowPileBlock.placeSnowPile(level, wildPos, level.getBlockState(wildPos), true);
        PileBlockEntity pile = (PileBlockEntity) level.getBlockEntity(wildPos);
        helper.assertTrue(!pile.getInternalState().getValue(WildCropBlock.MATURE)
                        && !pile.getAboveState().getValue(WildCropBlock.MATURE),
                "A winter snow pile must clear maturity in both hidden wild crop halves");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void crop_state_save_sync_and_top_fertilizing(GameTestHelper helper)
    {
        ServerLevel level = helper.getLevel();
        BlockPos pos = helper.absolutePos(new BlockPos(5, 2, 5));
        BlockState bottomState = ModBlocks.HEMP_CROP.get().defaultBlockState()
                .setValue(HempCropBlock.AGE, 4)
                .setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
        level.setBlockAndUpdate(pos.below(), farmland().defaultBlockState());
        level.setBlockAndUpdate(pos, bottomState);
        level.setBlockAndUpdate(pos.above(), bottomState.setValue(DoubleCropBlock.PART,
                DoubleCropBlock.Part.TOP));
        CropBlockEntity original = (CropBlockEntity) level.getBlockEntity(pos);
        original.setGrowth(0.82F);
        original.setYield(0.67F);
        original.setNAbsorbed(0.31F);
        original.setPAbsorbed(0.12F);
        original.setKAbsorbed(0.44F);
        original.setExpiry(1.73F);
        original.setLastGrowthTick(123456L);
        original.setLastCalendarUpdateTick(123999L);
        CompoundTag saved = original.saveWithFullMetadata(level.registryAccess());
        level.removeBlockEntity(pos);
        CropBlockEntity loaded = (CropBlockEntity) net.minecraft.world.level.block.entity.BlockEntity.loadStatic(
                pos, bottomState, saved, level.registryAccess());
        level.setBlockEntity(loaded);
        assertCropFields(helper, loaded);
        helper.assertTrue(level.getBlockState(pos).equals(bottomState)
                        && level.getBlockState(pos.above()).getValue(DoubleCropBlock.PART)
                        == DoubleCropBlock.Part.TOP,
                "Placed crop halves must survive block entity reload");
        CropBlockEntity reloadedTop = (CropBlockEntity) level.getBlockEntity(pos.above());
        reloadedTop.setGrowth(0.25F);
        reloadedTop.setLastGrowthTick(0L);
        CropBlockEntity.serverTickBottomPartOnly(level, pos.above(), level.getBlockState(pos.above()),
                reloadedTop);
        helper.assertTrue(close(reloadedTop.getGrowth(), 0.25F) && close(loaded.getGrowth(), 0.82F),
                "Reloaded upper halves must not apply a second growth update");
        CompoundTag sync = loaded.getUpdateTag(level.registryAccess());
        HempCropBlockEntity clientCopy = new HempCropBlockEntity(pos, bottomState);
        clientCopy.loadWithComponents(sync, level.registryAccess());
        assertCropFields(helper, clientCopy);

        BlockPos farmlandPos = helper.absolutePos(new BlockPos(2, 2, 5));
        level.setBlockAndUpdate(farmlandPos, farmland().defaultBlockState());
        BlockPos cropPos = farmlandPos.above();
        level.setBlockAndUpdate(cropPos, bottomState);
        BlockState topState = bottomState.setValue(DoubleCropBlock.PART, DoubleCropBlock.Part.TOP);
        level.setBlockAndUpdate(cropPos.above(), topState);
        FarmlandBlockEntity farmland = (FarmlandBlockEntity) level.getBlockEntity(farmlandPos);
        for(FarmlandBlockEntity.NutrientType nutrient : FarmlandBlockEntity.NutrientType.values())
            farmland.setNutrient(nutrient, 0.0F);
        var bottomInfo = new ArrayList<net.minecraft.network.chat.Component>();
        var topInfo = new ArrayList<net.minecraft.network.chat.Component>();
        ModBlocks.HEMP_CROP.get().addHoeOverlayInfo(level, cropPos, bottomState, bottomInfo::add, false);
        ModBlocks.HEMP_CROP.get().addHoeOverlayInfo(level, cropPos.above(), topState, topInfo::add, false);
        helper.assertTrue(!bottomInfo.isEmpty() && bottomInfo.equals(topInfo),
                "The upper half must report the same farmland information as the lower half");
        ItemStack compost = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse("tfc:compost")), 2);
        var player = FakePlayerFactory.getMinecraft(level);
        player.setItemInHand(InteractionHand.MAIN_HAND, compost);
        BlockHitResult topHit = new BlockHitResult(Vec3.atCenterOf(cropPos.above()),
                net.minecraft.core.Direction.UP, cropPos.above(), false);
        player.gameMode.useItemOn(player, level, compost, InteractionHand.MAIN_HAND, topHit);
        float expected = 0.2F * net.dries007.tfc.common.blocks.crop.CropHelpers.getSoilModifier(
                level.getBlockState(farmlandPos));
        helper.assertTrue(close(farmland.getNutrient(FarmlandBlockEntity.NutrientType.NITROGEN), expected)
                        && close(farmland.getNutrient(FarmlandBlockEntity.NutrientType.POTASSIUM), expected),
                "Top-half fertilizing must apply compost to the bottom farmland exactly once");
        helper.assertTrue(compost.getCount() == 1,
                "Top-half fertilizing must consume exactly one item through the bottom interaction");
        helper.assertTrue(level.getBlockState(cropPos).getValue(DoubleCropBlock.PART)
                        == DoubleCropBlock.Part.BOTTOM
                        && level.getBlockState(cropPos.above()).getValue(DoubleCropBlock.PART)
                        == DoubleCropBlock.Part.TOP,
                "Fertilizing must preserve both mature crop halves");
        helper.succeed();
    }

    private static Block farmland()
    {
        return TFCBlocks.SOIL.get(SoilBlockType.FARMLAND).get(SoilBlockType.Variant.ANDISOL).get();
    }

    private static List<ItemStack> drops(ServerLevel level, BlockPos pos, BlockState state,
            CropBlockEntity blockEntity, long seed)
    {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                .create(LootContextParamSets.BLOCK);
        ResourceKey<LootTable> key = state.getBlock().getLootTable();
        return level.getServer().reloadableRegistries().getLootTable(key).getRandomItems(params,
                RandomSource.create(seed));
    }

    private static List<ItemStack> explosionDrops(ServerLevel level, BlockPos pos, BlockState state,
            CropBlockEntity blockEntity)
    {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.EXPLOSION_RADIUS, Float.MAX_VALUE)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                .create(LootContextParamSets.BLOCK);
        return level.getServer().reloadableRegistries().getLootTable(state.getBlock().getLootTable())
                .getRandomItems(params, RandomSource.create(41L));
    }

    private static int count(List<ItemStack> drops, net.minecraft.world.item.Item item)
    {
        return drops.stream().filter(stack -> stack.is(item)).mapToInt(ItemStack::getCount).sum();
    }

    private static JsonObject resource(GameTestHelper helper, String path)
    {
        try(var reader = helper.getLevel().getServer().getResourceManager().getResource(
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path)).orElseThrow().openAsReader())
        {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
        catch(IOException exception)
        {
            throw new IllegalStateException("Unable to read generated resource " + path, exception);
        }
    }

    private static void assertCropFields(GameTestHelper helper, CropBlockEntity crop)
    {
        helper.assertTrue(close(crop.getGrowth(), 0.82F) && close(crop.getYield(), 0.67F)
                        && close(crop.getNAbsorbed(), 0.31F) && close(crop.getPAbsorbed(), 0.12F)
                        && close(crop.getKAbsorbed(), 0.44F) && close(crop.getExpiry(), 1.73F)
                        && crop.getLastGrowthTick() == 123456L
                        && crop.getLastCalendarUpdateTick() == 123999L,
                "All crop growth, yield, nutrient, lifetime, and update fields must survive save or sync");
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> configured(String path)
    {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(path));
    }

    private static ResourceKey<PlacedFeature> placed(String path)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(path));
    }

    private static ResourceLocation id(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path);
    }

    private static boolean close(float left, float right)
    {
        return Math.abs(left - right) < 0.0001F;
    }
}
