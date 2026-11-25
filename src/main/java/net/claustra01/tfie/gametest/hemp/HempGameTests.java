package net.claustra01.tfie.gametest.hemp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.blocks.HempCropBlock;
import net.claustra01.tfie.util.IECrop;
import net.dries007.tfc.client.overworld.SolarCalculator;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity.NutrientType;
import net.dries007.tfc.common.blockentities.IFarmland;
import net.dries007.tfc.common.blockentities.PileBlockEntity;
import net.dries007.tfc.common.blocks.SnowPileBlock;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.CropHelpers;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock.Part;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.calendar.ServerCalendar;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.Fertilizer;
import net.dries007.tfc.world.feature.BlockConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@GameTestHolder("tfie")
@PrefixGameTestTemplate(false)
public final class HempGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final long GROWTH_STEP = 24000L;

    private HempGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_registration_and_planting(GameTestHelper helper)
    {
        Block crop = ModBlocks.HEMP_CROP.get();
        Block dead = ModBlocks.DEAD_HEMP_CROP.get();
        Block wild = ModBlocks.WILD_HEMP_CROP.get();
        helper.assertTrue(BuiltInRegistries.BLOCK.getKey(crop).equals(id("crop/hemp")),
                "Cultivated hemp ID");
        helper.assertTrue(BuiltInRegistries.BLOCK.getKey(dead).equals(id("dead_crop/hemp")),
                "Dead hemp ID");
        helper.assertTrue(BuiltInRegistries.BLOCK.getKey(wild).equals(id("wild_crop/hemp")),
                "Wild hemp ID");
        helper.assertTrue(BuiltInRegistries.ITEM.containsKey(id("wild_crop/hemp")),
                "Wild hemp must have a BlockItem");
        helper.assertFalse(BuiltInRegistries.ITEM.containsKey(id("crop/hemp")),
                "Cultivated hemp must not have an item");
        helper.assertFalse(BuiltInRegistries.ITEM.containsKey(id("dead_crop/hemp")),
                "Dead hemp must not have an item");
        helper.assertTrue(BuiltInRegistries.ITEM.containsKey(id("immersiveengineering:seed")),
                "Hemp seeds must be the Immersive Engineering item");
        helper.assertTrue(BuiltInRegistries.ITEM.containsKey(id("immersiveengineering:hemp_fiber")),
                "Hemp fiber must be the Immersive Engineering item");
        helper.assertTrue(BuiltInRegistries.ITEM.keySet().stream().noneMatch(key ->
                        key.getNamespace().equals("tfie")
                                && (key.getPath().contains("seed") || key.getPath().contains("fiber"))),
                "TFIE must not register its own seed or fiber");

        BlockState cropState = crop.defaultBlockState();
        BlockState deadState = dead.defaultBlockState();
        BlockState wildState = wild.defaultBlockState();
        for (BlockState state : List.of(cropState, deadState, wildState))
        {
            helper.assertTrue(state.is(BlockTags.CROPS),
                    "Every hemp block must be in #minecraft:crops");
            helper.assertTrue(state.is(BlockTags.MINEABLE_WITH_HOE),
                    "Every hemp block must be in #minecraft:mineable/hoe");
            helper.assertTrue(state.getBlock().getFlammability(state, helper.getLevel(),
                    BlockPos.ZERO, Direction.UP) == 60, "Every hemp block must have flammability 60");
            helper.assertTrue(state.getBlock().getFireSpreadSpeed(state, helper.getLevel(),
                    BlockPos.ZERO, Direction.UP) == 30, "Every hemp block must have fire spread 30");
        }

        helper.assertTrue(wildState.is(TFCTags.Blocks.CAN_BE_SNOW_PILED),
                "Only wild hemp is snow pileable");
        helper.assertFalse(cropState.is(TFCTags.Blocks.CAN_BE_SNOW_PILED),
                "Cultivated hemp is not snow pileable");
        helper.assertFalse(deadState.is(TFCTags.Blocks.CAN_BE_SNOW_PILED),
                "Dead hemp is not snow pileable");
        helper.assertTrue(BuiltInRegistries.BLOCK.get(id("tfc:crop/tomato")).defaultBlockState()
                        .is(BlockTags.CROPS), "Existing crop tags must be preserved");
        helper.assertTrue(Blocks.OAK_LEAVES.defaultBlockState().is(BlockTags.MINEABLE_WITH_HOE),
                "Existing hoe tags must be preserved");
        helper.assertTrue(BuiltInRegistries.BLOCK.get(id("tfc:wild_crop/tomato")).defaultBlockState()
                        .is(TFCTags.Blocks.CAN_BE_SNOW_PILED), "Existing snow tags must be preserved");
        helper.assertTrue(cropState.getCollisionShape(helper.getLevel(), BlockPos.ZERO).isEmpty(),
                "Hemp must have no collision");
        helper.assertTrue(cropState.getDestroySpeed(helper.getLevel(), BlockPos.ZERO) == 0.4F,
                "Hemp hardness must be 0.4");
        helper.assertTrue(cropState.getSoundType() == SoundType.CROP,
                "Hemp must use the crop sound");
        helper.assertTrue(cropState.getPistonPushReaction() == PushReaction.DESTROY,
                "Pistons must destroy hemp");
        helper.assertTrue(ModBlockEntities.CROP.get().isValid(cropState),
                "The crop block entity must accept cultivated hemp");

        BlockPos farmlandPos = helper.absolutePos(BlockPos.ZERO);
        helper.getLevel().setBlockAndUpdate(farmlandPos, farmland());
        BlockPos cropPos = farmlandPos.above();
        helper.getLevel().setBlockAndUpdate(cropPos, Blocks.AIR.defaultBlockState());
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getAbilities().mayBuild = true;
        ItemStack seeds = new ItemStack(seedItem(), 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, seeds);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(farmlandPos), Direction.UP,
                farmlandPos, false);
        Optional<InteractionResult> result = InteractionManager.onItemUse(seeds,
                new UseOnContext(player, InteractionHand.MAIN_HAND, hit), false);
        helper.assertTrue(result.isPresent() && result.get().consumesAction(),
                "Using IE hemp seeds on TFC farmland must plant the crop");
        BlockState planted = helper.getLevel().getBlockState(cropPos);
        helper.assertTrue(planted.is(crop), "The cultivated crop must be planted");
        helper.assertTrue(planted.getValue(ModBlocks.HEMP_CROP.get().getAgeProperty()) == 0,
                "Planting must start at age zero");
        helper.assertTrue(planted.getValue(DoubleCropBlock.PART) == Part.BOTTOM,
                "Planting must create the bottom half");
        helper.assertTrue(helper.getLevel().getBlockEntity(cropPos) instanceof CropBlockEntity,
                "The cultivated crop must create the crop block entity");
        helper.assertTrue(seeds.getCount() == 1, "Planting must consume one IE seed");
        helper.assertTrue(helper.getLevel().getBlockState(cropPos.above()).isAir(),
                "Planting must not create a top half");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_climate_and_nutrients(GameTestHelper helper)
    {
        CompoundTag calendarState = Calendars.SERVER.write();
        try
        {
            ClimateRange range = IECrop.climateRange();
            helper.assertTrue(range.minTemperature() == 10.0F && range.maxTemperature() == 31.0F,
                    "Hemp temperature range must be 10 to 31 degrees");
            helper.assertTrue(range.minHydration() == 30 && range.maxHydration() == 80,
                    "Hemp hydration range must be 30 to 80 percent");
            helper.assertTrue(range.hydrationWiggleRange() == 0
                            && range.temperatureWiggleRange() == 0.0F,
                    "Hemp must not extend its climate range");
            helper.assertTrue(range.checkBoth(30, 10.0F, false),
                    "Lower temperature boundary must be inclusive");
            helper.assertTrue(range.checkBoth(80, 31.0F, false),
                    "Upper temperature boundary must be inclusive");
            helper.assertFalse(range.checkBoth(29, 10.0F, false), "Hydration below 30 must fail");
            helper.assertFalse(range.checkBoth(81, 31.0F, false), "Hydration above 80 must fail");
            helper.assertFalse(range.checkBoth(30, 9.9F, false), "Temperature below 10 must fail");
            helper.assertFalse(range.checkBoth(80, 31.1F, false), "Temperature above 31 must fail");

            HempCropBlock crop = ModBlocks.HEMP_CROP.get();
            helper.assertTrue(crop.getNForGrowth() == 0.6F, "Hemp nitrogen use must be 0.6");
            helper.assertTrue(crop.getPForGrowth() == 0.2F, "Hemp phosphorus use must be 0.2");
            helper.assertTrue(crop.getKForGrowth() == 0.6F, "Hemp potassium use must be 0.6");
            helper.assertTrue(crop.getMaxAge() == 4, "Hemp must have five growth stages");
            BlockPos farmlandPos = placeFarmlandAtValidClimate(helper);
            BlockPos cropPos = farmlandPos.above();
            helper.assertTrue(CropHelpers.lightValid(helper.getLevel(), cropPos),
                    "Hemp must grow in light level 12 or higher");
            CropBlockEntity blockEntity = placeBottom(helper, farmlandPos, 0, 0.0F);
            IFarmland farmland = farmlandEntity(helper, farmlandPos);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            clearAbsorbed(blockEntity);
            growOnce(helper, crop, cropPos, blockEntity);
            helper.assertTrue(blockEntity.getGrowth() > 0.0F,
                    "One growth update must increase growth");
            float nitrogen = 1.0F - farmland.getNutrient(NutrientType.NITROGEN);
            float phosphorus = 1.0F - farmland.getNutrient(NutrientType.PHOSPHOROUS);
            float potassium = 1.0F - farmland.getNutrient(NutrientType.POTASSIUM);
            helper.assertTrue(phosphorus > 0.0F, "Growth must consume phosphorus");
            helper.assertTrue(Math.abs(nitrogen / phosphorus - 3.0F) < 0.05F,
                    "Nitrogen must be consumed in the 0.6:0.2 proportion");
            helper.assertTrue(Math.abs(potassium / phosphorus - 3.0F) < 0.05F,
                    "Potassium must be consumed in the 0.6:0.2 proportion");
            helper.assertTrue(Math.abs(blockEntity.getNAbsorbed() - nitrogen) < 1.0E-4F
                            && Math.abs(blockEntity.getPAbsorbed() - phosphorus) < 1.0E-4F
                            && Math.abs(blockEntity.getKAbsorbed() - potassium) < 1.0E-4F,
                    "The crop must record exactly the nutrients the growth step consumed");
            helper.succeed();
        }
        finally
        {
            Calendars.SERVER.read(calendarState);
        }
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_growth_top_and_limit(GameTestHelper helper)
    {
        CompoundTag calendarState = Calendars.SERVER.write();
        try
        {
            HempCropBlock crop = ModBlocks.HEMP_CROP.get();
            BlockPos farmlandPos = placeFarmlandAtValidClimate(helper);
            BlockPos cropPos = farmlandPos.above();
            IFarmland farmland = farmlandEntity(helper, farmlandPos);
            CropBlockEntity blockEntity = placeBottom(helper, farmlandPos, 3, 0.99F);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            clearAbsorbed(blockEntity);
            growOnce(helper, crop, cropPos, blockEntity);
            helper.assertTrue(helper.getLevel().getBlockState(cropPos)
                            .getValue(crop.getAgeProperty()) == 4,
                    "Full growth must produce age 4");
            BlockPos topPos = cropPos.above();
            BlockState top = helper.getLevel().getBlockState(topPos);
            helper.assertTrue(top.is(crop) && top.getValue(crop.getAgeProperty()) == 4
                            && top.getValue(DoubleCropBlock.PART) == Part.TOP,
                    "Full growth must create the matching top half");

            CropBlockEntity topEntity = cropEntity(helper, topPos);
            topEntity.setGrowth(0.42F);
            long lastGrowth = topEntity.getLastGrowthTick();
            CropBlockEntity.serverTickBottomPartOnly(helper.getLevel(), topPos, top, topEntity);
            helper.assertTrue(topEntity.getGrowth() == 0.42F
                            && topEntity.getLastGrowthTick() == lastGrowth,
                    "The registered ticker must not grow the top half");

            helper.getLevel().setBlockAndUpdate(topPos, Blocks.STONE.defaultBlockState());
            helper.getLevel().setBlockAndUpdate(cropPos, cropState(2, Part.BOTTOM));
            blockEntity = cropEntity(helper, cropPos);
            blockEntity.setGrowth(0.74F);
            clearAbsorbed(blockEntity);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            growOnce(helper, crop, cropPos, blockEntity);
            helper.assertTrue(blockEntity.getGrowth() > 0.74F && blockEntity.getGrowth() <= 0.75F,
                    "An obstructed top position must cap growth at 0.75");
            helper.assertTrue(helper.getLevel().getBlockState(cropPos)
                            .getValue(crop.getAgeProperty()) == 3,
                    "The obstructed crop must stop at age 3");
            helper.assertTrue(helper.getLevel().getBlockState(topPos).is(Blocks.STONE),
                    "The obstructed top position must not be replaced");

            helper.getLevel().setBlockAndUpdate(topPos, Blocks.AIR.defaultBlockState());
            helper.getLevel().setBlockAndUpdate(cropPos, cropState(2, Part.BOTTOM));
            blockEntity = cropEntity(helper, cropPos);
            blockEntity.setGrowth(0.5F);
            blockEntity.setExpiry(1000.0F);
            clearAbsorbed(blockEntity);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            growOnce(helper, crop, cropPos, blockEntity);
            BlockState dead = helper.getLevel().getBlockState(cropPos);
            helper.assertTrue(dead.is(ModBlocks.DEAD_HEMP_CROP.get()),
                    "An expired crop must become dead hemp");
            helper.assertFalse(dead.getValue(DeadCropBlock.MATURE),
                    "An expired immature crop must be dead young");
            helper.assertTrue(dead.getValue(DeadDoubleCropBlock.PART) == Part.BOTTOM,
                    "The dead crop must keep its bottom part");
            helper.succeed();
        }
        finally
        {
            Calendars.SERVER.read(calendarState);
        }
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_loot_tables(GameTestHelper helper)
    {
        Item seeds = seedItem();
        Item fiber = BuiltInRegistries.ITEM.get(id("immersiveengineering:hemp_fiber"));
        LootTable cropTable = lootTable(helper, "blocks/crop/hemp");
        LootTable deadTable = lootTable(helper, "blocks/dead_crop/hemp");
        LootTable wildTable = lootTable(helper, "blocks/wild_crop/hemp");
        BlockPos anchor = helper.absolutePos(new BlockPos(6, 1, 6));
        helper.getLevel().setBlockAndUpdate(anchor.below(), farmland());
        helper.getLevel().setBlockAndUpdate(anchor, cropState(0, Part.BOTTOM));
        CropBlockEntity blockEntity = cropEntity(helper, anchor);

        for (int age = 0; age <= 3; age++)
        {
            List<ItemStack> drops = drops(helper, cropTable, cropState(age, Part.BOTTOM),
                    blockEntity, age);
            helper.assertTrue(count(drops, seeds) == 1,
                    "Immature cultivated hemp must drop one seed at age " + age);
            helper.assertTrue(count(drops, fiber) == 0,
                    "Immature cultivated hemp must not drop fiber at age " + age);
        }

        blockEntity.setYield(1.0F);
        List<ItemStack> mature = drops(helper, cropTable, cropState(4, Part.BOTTOM),
                blockEntity, 7L);
        helper.assertTrue(count(mature, seeds) == 1, "Mature cultivated hemp must drop one seed");
        helper.assertTrue(count(mature, fiber) >= 6 && count(mature, fiber) <= 10,
                "A full yield factor must produce 6 to 10 fiber");
        blockEntity.setYield(0.0F);
        helper.assertTrue(count(drops(helper, cropTable, cropState(4, Part.BOTTOM),
                blockEntity, 11L), fiber) == 0, "A zero yield factor must produce no fiber");
        blockEntity.setYield(0.5F);
        int halfYield = count(drops(helper, cropTable, cropState(4, Part.BOTTOM),
                blockEntity, 13L), fiber);
        helper.assertTrue(halfYield >= 3 && halfYield <= 5,
                "A half yield factor must interpolate the uniform maximum");
        blockEntity.setYield(1.0F);
        helper.assertTrue(drops(helper, cropTable, cropState(4, Part.TOP),
                blockEntity, 17L).isEmpty(), "The cultivated top half must not duplicate drops");

        BlockState deadMature = ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState()
                .setValue(DeadCropBlock.MATURE, true)
                .setValue(DeadDoubleCropBlock.PART, Part.BOTTOM);
        BlockState deadYoung = ModBlocks.DEAD_HEMP_CROP.get().defaultBlockState()
                .setValue(DeadCropBlock.MATURE, false)
                .setValue(DeadDoubleCropBlock.PART, Part.BOTTOM);
        int minDeadSeeds = Integer.MAX_VALUE;
        int maxDeadSeeds = 0;
        for (int sample = 0; sample < 64; sample++)
        {
            int count = count(drops(helper, deadTable, deadMature, blockEntity, 100L + sample),
                    seeds);
            minDeadSeeds = Math.min(minDeadSeeds, count);
            maxDeadSeeds = Math.max(maxDeadSeeds, count);
        }
        helper.assertTrue(minDeadSeeds == 1 && maxDeadSeeds == 3,
                "Mature dead hemp must drop one to three seeds");
        helper.assertTrue(count(drops(helper, deadTable, deadYoung, blockEntity, 29L), seeds) == 1,
                "Immature dead hemp must drop one seed");
        BlockState deadTop = deadMature.setValue(DeadDoubleCropBlock.PART, Part.TOP);
        helper.assertTrue(drops(helper, deadTable, deadTop, blockEntity, 31L).isEmpty(),
                "The dead top half must not duplicate drops");

        BlockState wildMature = wildCropState(true, Part.BOTTOM);
        BlockState wildYoung = wildCropState(false, Part.BOTTOM);
        List<ItemStack> wildMatureDrops = drops(helper, wildTable, wildMature, blockEntity, 37L);
        helper.assertTrue(count(wildMatureDrops, seeds) == 1,
                "Mature wild hemp must drop one seed");
        helper.assertTrue(count(wildMatureDrops, fiber) >= 1
                        && count(wildMatureDrops, fiber) <= 3,
                "Mature wild hemp must drop one to three fiber");
        List<ItemStack> wildYoungDrops = drops(helper, wildTable, wildYoung, blockEntity, 41L);
        helper.assertTrue(count(wildYoungDrops, seeds) == 1 && count(wildYoungDrops, fiber) == 0,
                "Immature wild hemp must drop one seed and no fiber");
        helper.assertTrue(drops(helper, wildTable, wildCropState(true, Part.TOP),
                blockEntity, 43L).isEmpty(), "The wild top half must not duplicate drops");
        helper.assertTrue(drops(helper, cropTable, cropState(4, Part.BOTTOM), blockEntity,
                47L, Float.MAX_VALUE).isEmpty(), "Cultivated loot must survive only outside explosions");
        helper.assertTrue(drops(helper, deadTable, deadMature, blockEntity,
                53L, Float.MAX_VALUE).isEmpty(), "Dead loot must survive only outside explosions");
        helper.assertTrue(drops(helper, wildTable, wildMature, blockEntity,
                59L, Float.MAX_VALUE).isEmpty(), "Wild loot must survive only outside explosions");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_worldgen_and_seasons(GameTestHelper helper)
    {
        RegistryAccess access = helper.getLevel().registryAccess();
        Registry<PlacedFeature> placed = access.registryOrThrow(Registries.PLACED_FEATURE);
        Registry<ConfiguredFeature<?, ?>> configured = access.registryOrThrow(Registries.CONFIGURED_FEATURE);
        ResourceKey<PlacedFeature> individualKey = ResourceKey.create(Registries.PLACED_FEATURE,
                id("plant/wild_crop/hemp"));
        ResourceKey<PlacedFeature> patchKey = ResourceKey.create(Registries.PLACED_FEATURE,
                id("plant/wild_crop/hemp_patch"));
        PlacedFeature individual = placed.getOrThrow(individualKey);
        PlacedFeature patch = placed.getOrThrow(patchKey);
        ConfiguredFeature<?, ?> patchConfigured = configured.getOrThrow(ResourceKey.create(
                Registries.CONFIGURED_FEATURE, id("plant/wild_crop/hemp_patch")));
        ConfiguredFeature<?, ?> individualConfigured = configured.getOrThrow(ResourceKey.create(
                Registries.CONFIGURED_FEATURE, id("plant/wild_crop/hemp")));
        helper.assertTrue(patchConfigured.feature() instanceof RandomPatchFeature,
                "The patch must use random_patch");
        helper.assertTrue(patchConfigured.config() instanceof RandomPatchConfiguration configuration
                        && configuration.tries() == 6
                        && configuration.xzSpread() == 5
                        && configuration.ySpread() == 1
                        && configuration.feature().unwrapKey().orElseThrow().equals(individualKey),
                "The patch must try six times with spreads 5 and 1 and reference the individual feature");
        helper.assertTrue(individualConfigured.config() instanceof BlockConfig<?> blockConfig
                        && blockConfig.block() == ModBlocks.WILD_HEMP_CROP.get(),
                "The individual feature must place wild hemp");

        JsonElement patchJson = PlacedFeature.DIRECT_CODEC
                .encodeStart(RegistryOps.create(JsonOps.INSTANCE, access), patch).getOrThrow();
        JsonArray patchPlacement = patchJson.getAsJsonObject().getAsJsonArray("placement");
        helper.assertTrue(patchPlacement.size() == 3,
                "The patch must have three placement modifiers");
        helper.assertTrue(patchPlacement.get(0).getAsJsonObject().get("type").getAsString()
                        .equals("minecraft:rarity_filter")
                        && patchPlacement.get(0).getAsJsonObject().get("chance").getAsInt() == 90,
                "The patch rarity must be 90");
        helper.assertTrue(patchPlacement.get(1).getAsJsonObject().get("type").getAsString()
                        .equals("minecraft:in_square"), "The patch must use in_square");
        JsonObject climate = patchPlacement.get(2).getAsJsonObject();
        helper.assertTrue(climate.get("type").getAsString().equals("tfc:climate")
                        && climate.get("min_temperature").getAsFloat() == 10.0F
                        && climate.get("max_temperature").getAsFloat() == 40.0F
                        && climate.get("min_groundwater").getAsFloat() == 140.0F
                        && climate.get("max_groundwater").getAsFloat() == 360.0F,
                "The patch climate must use temperature and groundwater, not rainfall");

        JsonElement individualJson = PlacedFeature.DIRECT_CODEC
                .encodeStart(RegistryOps.create(JsonOps.INSTANCE, access), individual).getOrThrow();
        JsonArray placement = individualJson.getAsJsonObject().getAsJsonArray("placement");
        helper.assertTrue(placement.size() == 3,
                "The individual feature must have three placement modifiers");
        helper.assertTrue(placement.get(0).getAsJsonObject().get("type").getAsString()
                        .equals("minecraft:heightmap")
                        && placement.get(0).getAsJsonObject().get("heightmap").getAsString()
                        .equals("WORLD_SURFACE_WG"),
                "The individual feature must use WORLD_SURFACE_WG");
        helper.assertTrue(placement.get(1).getAsJsonObject().getAsJsonObject("predicate")
                        .get("type").getAsString().equals("tfc:replaceable"),
                "The individual feature must require replaceable ground");
        JsonObject survive = placement.get(2).getAsJsonObject().getAsJsonObject("predicate");
        helper.assertTrue(survive.get("type").getAsString().equals("minecraft:would_survive")
                        && survive.getAsJsonObject("state").get("Name").getAsString()
                        .equals("tfie:wild_crop/hemp"),
                "The individual feature must require would_survive for the bottom half");

        TagKey<PlacedFeature> cropsTag = TagKey.create(Registries.PLACED_FEATURE,
                id("tfc:feature/crops"));
        List<Holder<PlacedFeature>> tagged = placed.getTag(cropsTag).orElseThrow().stream().toList();
        helper.assertTrue(tagged.stream().anyMatch(holder ->
                        holder.unwrapKey().orElseThrow().equals(patchKey)),
                "The patch must be in #tfc:feature/crops");
        helper.assertTrue(tagged.stream().anyMatch(holder -> holder.unwrapKey().orElseThrow()
                        .location().equals(id("tfc:plant/wild_crop/jute_patch"))),
                "The existing crop feature tag contents must be preserved");

        CompoundTag calendarState = Calendars.SERVER.write();
        ConfigValue<Month> startValue = maturityConfig("wildCropMaturityStartMonth");
        ConfigValue<Month> endValue = maturityConfig("wildCropMaturityEndMonth");
        Month oldStart = startValue.get();
        Month oldEnd = endValue.get();
        try
        {
            helper.assertTrue(oldStart == Month.JUNE && oldEnd == Month.OCTOBER,
                    "The baseline maturity window must be June through October");
            BlockPos base = helper.absolutePos(BlockPos.ZERO);
            helper.getLevel().setBlockAndUpdate(base,
                    BuiltInRegistries.BLOCK.get(id("tfc:dirt/entisol")).defaultBlockState());
            BlockPos bottomPos = base.above();
            BlockPos topPos = bottomPos.above();
            WildDoubleCropBlock wild = ModBlocks.WILD_HEMP_CROP.get();
            ServerLevel level = helper.getLevel();
            ServerCalendar calendar = Calendars.SERVER;
            boolean north = SolarCalculator.getInNorthernHemisphere(bottomPos, level);
            for (Month target : List.of(Month.MAY, Month.JUNE, Month.OCTOBER, Month.NOVEMBER))
            {
                moveToMonth(calendar, north, target);
                Month month = calendar.getHemispheralCalendarMonthOfYear(north);
                assertWildMaturity(helper, level, wild, bottomPos, topPos, !inWindow(month,
                        oldStart, oldEnd), inWindow(month, oldStart, oldEnd));
            }

            startValue.set(Month.NOVEMBER);
            endValue.set(Month.FEBRUARY);
            for (Month target : List.of(Month.DECEMBER, Month.JANUARY, Month.JUNE))
            {
                moveToMonth(calendar, north, target);
                Month month = calendar.getHemispheralCalendarMonthOfYear(north);
                boolean mature = inWindow(month, Month.NOVEMBER, Month.FEBRUARY);
                assertWildMaturity(helper, level, wild, bottomPos, topPos, !mature, mature);
            }

            moveToMonth(calendar, north, Month.JANUARY);
            level.setBlockAndUpdate(bottomPos, wildCropState(true, Part.BOTTOM));
            level.setBlockAndUpdate(topPos, wildCropState(true, Part.TOP));
            SnowPileBlock.placeSnowPile(level, bottomPos, level.getBlockState(bottomPos), true);
            helper.assertTrue(level.getBlockState(bottomPos).is(TFCBlocks.SNOW_PILE.get()),
                    "The TFC snow pile must cover the wild hemp");
            PileBlockEntity pile = (PileBlockEntity) level.getBlockEntity(bottomPos);
            helper.assertFalse(pile.getInternalState().getValue(WildCropBlock.MATURE),
                    "Snow cover must clear the maturity of the bottom half");
            helper.assertTrue(pile.getAboveState() != null
                            && !pile.getAboveState().getValue(WildCropBlock.MATURE),
                    "Snow cover must clear the maturity of the top half");
            helper.succeed();
        }
        finally
        {
            startValue.set(oldStart);
            endValue.set(oldEnd);
            Calendars.SERVER.read(calendarState);
        }
    }

    @GameTest(template = TEMPLATE, templateNamespace = "tfie", skyAccess = true)
    public static void hemp_persistence_and_top_proxy(GameTestHelper helper)
    {
        CompoundTag calendarState = Calendars.SERVER.write();
        try
        {
            HempCropBlock crop = ModBlocks.HEMP_CROP.get();
            BlockPos farmlandPos = placeFarmlandAtValidClimate(helper);
            BlockPos bottomPos = farmlandPos.above();
            BlockPos topPos = bottomPos.above();
            IFarmland farmland = farmlandEntity(helper, farmlandPos);
            CropBlockEntity blockEntity = placeBottom(helper, farmlandPos, 3, 0.99F);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            clearAbsorbed(blockEntity);
            growOnce(helper, crop, bottomPos, blockEntity);
            BlockState bottom = helper.getLevel().getBlockState(bottomPos);
            BlockState top = helper.getLevel().getBlockState(topPos);
            helper.assertTrue(bottom.getValue(crop.getAgeProperty()) == 4 && top.is(crop)
                            && top.getValue(crop.getAgeProperty()) == 4
                            && top.getValue(DoubleCropBlock.PART) == Part.TOP,
                    "The persistence test must start from a real age 4 pair");

            blockEntity.setYield(0.63F);
            blockEntity.setNAbsorbed(0.11F);
            blockEntity.setPAbsorbed(0.22F);
            blockEntity.setKAbsorbed(0.33F);
            blockEntity.setExpiry(0.44F);
            blockEntity.setLastGrowthTick(12345L);
            Provider registries = helper.getLevel().registryAccess();
            CompoundTag saved = blockEntity.saveWithoutMetadata(registries);
            CropBlockEntity reloaded = new CropBlockEntity(ModBlockEntities.CROP.get(), bottomPos, bottom);
            reloaded.loadWithComponents(saved, registries);
            helper.assertTrue(Math.abs(reloaded.getGrowth() - 1.0F) < 1.0E-5F
                            && Math.abs(reloaded.getYield() - 0.63F) < 1.0E-5F
                            && Math.abs(reloaded.getNAbsorbed() - 0.11F) < 1.0E-5F
                            && Math.abs(reloaded.getPAbsorbed() - 0.22F) < 1.0E-5F
                            && Math.abs(reloaded.getKAbsorbed() - 0.33F) < 1.0E-5F
                            && Math.abs(reloaded.getExpiry() - 0.44F) < 1.0E-5F
                            && reloaded.getLastGrowthTick() == 12345L,
                    "Every crop block entity field must survive saving and loading");
            helper.assertTrue(helper.getLevel().getBlockState(bottomPos).equals(bottom)
                            && helper.getLevel().getBlockState(topPos).equals(top),
                    "Both block states of the age 4 pair must be unchanged after loading");
            CompoundTag update = blockEntity.getUpdateTag(registries);
            helper.assertTrue(update.contains("growth") && update.contains("yield")
                            && update.contains("n") && update.contains("p") && update.contains("k")
                            && update.contains("expiry") && update.contains("lastGrowthTick"),
                    "The server crop state must be present in the synchronization tag");

            helper.getLevel().removeBlockEntity(bottomPos);
            helper.getLevel().setBlockEntity(reloaded);
            clearAbsorbed(reloaded);
            setNutrients(farmland, 1.0F, 1.0F, 1.0F);
            reloaded.setLastGrowthTick(Calendars.SERVER.getTicks());
            growOnce(helper, crop, bottomPos, reloaded);
            float nitrogen = 1.0F - farmland.getNutrient(NutrientType.NITROGEN);
            float phosphorus = 1.0F - farmland.getNutrient(NutrientType.PHOSPHOROUS);
            float potassium = 1.0F - farmland.getNutrient(NutrientType.POTASSIUM);
            helper.assertTrue(reloaded.getGrowth() == 1.0F,
                    "Growth must stay at its maximum, not double");
            helper.assertTrue(nitrogen > 0.0F
                            && Math.abs(reloaded.getNAbsorbed() - nitrogen) < 1.0E-4F
                            && Math.abs(reloaded.getPAbsorbed() - phosphorus) < 1.0E-4F
                            && Math.abs(reloaded.getKAbsorbed() - potassium) < 1.0E-4F,
                    "One update after loading must consume nutrients exactly once");
            helper.assertTrue(reloaded.getNAbsorbed() < 0.1F
                            && reloaded.getPAbsorbed() < 0.1F
                            && reloaded.getKAbsorbed() < 0.1F,
                    "A double update would consume two growth steps of nutrients");

            setNutrients(farmland, 0.2F, 0.4F, 0.6F);
            List<Component> bottomInfo = new ArrayList<>();
            List<Component> topInfo = new ArrayList<>();
            crop.addHoeOverlayInfo(helper.getLevel(), bottomPos, bottom, bottomInfo::add, true);
            crop.addHoeOverlayInfo(helper.getLevel(), topPos, top, topInfo::add, true);
            helper.assertTrue(bottomInfo.size() >= 5,
                    "The hoe overlay must report hydration and all three farmland nutrients");
            helper.assertTrue(topInfo.equals(bottomInfo),
                    "The top hoe overlay must read the same hydration and nutrients as the bottom farmland");

            setNutrients(farmland, 0.0F, 0.0F, 0.0F);
            Player player = helper.makeMockPlayer(GameType.SURVIVAL);
            ItemStack compost = new ItemStack(BuiltInRegistries.ITEM.get(id("tfc:compost")));
            Fertilizer fertilizer = Fertilizer.get(compost);
            helper.assertTrue(fertilizer != null, "TFC compost must be a fertilizer");
            player.setItemInHand(InteractionHand.MAIN_HAND, compost);
            ItemInteractionResult result = top.useItemOn(compost, helper.getLevel(), player,
                    InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(topPos),
                            Direction.UP, topPos, false));
            float soilModifier = CropHelpers.getSoilModifier(helper.getLevel().getBlockState(farmlandPos));
            helper.assertTrue(result.consumesAction(), "Fertilizing the top half must succeed");
            helper.assertTrue(compost.getCount() == 0,
                    "Fertilizing the top half must consume exactly one item");
            helper.assertTrue(Math.abs(farmland.getNutrient(NutrientType.NITROGEN)
                            - fertilizer.nitrogen() * soilModifier) < 1.0E-5F
                            && Math.abs(farmland.getNutrient(NutrientType.PHOSPHOROUS)
                            - fertilizer.phosphorus() * soilModifier) < 1.0E-5F
                            && Math.abs(farmland.getNutrient(NutrientType.POTASSIUM)
                            - fertilizer.potassium() * soilModifier) < 1.0E-5F,
                    "Fertilizing from the top half must add one fertilizer to the bottom farmland");
            helper.succeed();
        }
        finally
        {
            Calendars.SERVER.read(calendarState);
        }
    }

    private static BlockPos placeFarmlandAtValidClimate(GameTestHelper helper)
    {
        ServerLevel level = helper.getLevel();
        ClimateRange range = IECrop.climateRange();
        BlockPos origin = helper.absolutePos(new BlockPos(0, 1, 0));
        for (int month = 0; month < 12; month++)
        {
            long monthTicks = Calendars.SERVER.getCalendarTicksInMonth();
            Calendars.SERVER.skipForwardBy(monthTicks
                    - Calendars.SERVER.getCalendarTicks() % monthTicks);
            for (int dx = 0; dx < 8; dx++)
            {
                for (int dz = 0; dz < 8; dz++)
                {
                    BlockPos cropPos = origin.offset(dx, 0, dz);
                    BlockPos farmlandPos = cropPos.below();
                    int rainHydration = FarmlandBlock.getInstantRainHydration(level, farmlandPos);
                    boolean water = rainHydration < 30;
                    int hydration = water ? rainHydration + 40 : rainHydration;
                    float temperature = Climate.getInstantTemperature(level, cropPos);
                    if (hydration >= 30 && hydration <= 80
                            && range.checkBoth(hydration, temperature, false))
                    {
                        level.setBlockAndUpdate(farmlandPos.below(), Blocks.STONE.defaultBlockState());
                        level.setBlockAndUpdate(farmlandPos, farmland());
                        level.setBlockAndUpdate(cropPos, Blocks.AIR.defaultBlockState());
                        if (water)
                        {
                            level.setBlockAndUpdate(farmlandPos.east(), Blocks.WATER.defaultBlockState());
                        }
                        helper.assertTrue(range.checkBoth(
                                        FarmlandBlock.getInstantHydration(level, farmlandPos),
                                        temperature, false),
                                "The selected farmland must keep hemp's climate range");
                        return farmlandPos;
                    }
                }
            }
        }
        helper.fail("No farmland position within hemp's climate range was found");
        return BlockPos.ZERO;
    }

    private static CropBlockEntity placeBottom(GameTestHelper helper, BlockPos farmlandPos,
                                               int age, float growth)
    {
        BlockPos cropPos = farmlandPos.above();
        helper.getLevel().setBlockAndUpdate(cropPos, cropState(age, Part.BOTTOM));
        CropBlockEntity blockEntity = cropEntity(helper, cropPos);
        blockEntity.setGrowth(growth);
        return blockEntity;
    }

    private static CropBlockEntity cropEntity(GameTestHelper helper, BlockPos pos)
    {
        helper.assertTrue(helper.getLevel().getBlockEntity(pos) instanceof CropBlockEntity,
                "Expected a crop block entity at " + pos);
        return (CropBlockEntity) helper.getLevel().getBlockEntity(pos);
    }

    private static IFarmland farmlandEntity(GameTestHelper helper, BlockPos pos)
    {
        helper.assertTrue(helper.getLevel().getBlockEntity(pos) instanceof IFarmland,
                "Expected farmland at " + pos);
        return (IFarmland) helper.getLevel().getBlockEntity(pos);
    }

    private static void growOnce(GameTestHelper helper, HempCropBlock crop, BlockPos pos,
                                 CropBlockEntity blockEntity)
    {
        Calendars.SERVER.skipForwardBy(GROWTH_STEP);
        crop.growthTick(helper.getLevel(), pos, helper.getLevel().getBlockState(pos), blockEntity);
    }

    private static void setNutrients(IFarmland farmland, float nitrogen, float phosphorus,
                                     float potassium)
    {
        farmland.setNutrient(NutrientType.NITROGEN, nitrogen);
        farmland.setNutrient(NutrientType.PHOSPHOROUS, phosphorus);
        farmland.setNutrient(NutrientType.POTASSIUM, potassium);
    }

    private static void clearAbsorbed(CropBlockEntity blockEntity)
    {
        blockEntity.setNAbsorbed(0.0F);
        blockEntity.setPAbsorbed(0.0F);
        blockEntity.setKAbsorbed(0.0F);
    }

    @SuppressWarnings("unchecked")
    private static ConfigValue<Month> maturityConfig(String key)
    {
        return (ConfigValue<Month>) TFCConfig.SERVER.spec().getValues()
                .get(List.of("blocks", "crops", key));
    }

    private static void moveToMonth(ServerCalendar calendar, boolean north, Month target)
    {
        for (int attempt = 0;
             attempt < 12 && calendar.getHemispheralCalendarMonthOfYear(north) != target;
             attempt++)
        {
            long monthTicks = calendar.getCalendarTicksInMonth();
            calendar.skipForwardBy(monthTicks - calendar.getCalendarTicks() % monthTicks);
        }
    }

    private static boolean inWindow(Month month, Month start, Month end)
    {
        return start.ordinal() <= end.ordinal()
                ? month.ordinal() >= start.ordinal() && month.ordinal() <= end.ordinal()
                : month.ordinal() >= start.ordinal() || month.ordinal() <= end.ordinal();
    }

    private static void assertWildMaturity(GameTestHelper helper, ServerLevel level,
                                           WildDoubleCropBlock wild, BlockPos bottomPos,
                                           BlockPos topPos, boolean initial, boolean expected)
    {
        BlockState bottom = wildCropState(initial, Part.BOTTOM);
        level.setBlockAndUpdate(bottomPos, bottom);
        level.setBlockAndUpdate(topPos, wildCropState(initial, Part.TOP));
        wild.randomTick(bottom, level, bottomPos, RandomSource.create(1L));
        helper.assertTrue(level.getBlockState(bottomPos).getValue(WildCropBlock.MATURE) == expected,
                "The wild bottom half must follow the maturity window");
        helper.assertTrue(level.getBlockState(topPos).getValue(WildCropBlock.MATURE) == expected,
                "The wild top half must follow the bottom half");
    }

    private static BlockState cropState(int age, Part part)
    {
        HempCropBlock crop = ModBlocks.HEMP_CROP.get();
        return crop.defaultBlockState().setValue(crop.getAgeProperty(), age)
                .setValue(DoubleCropBlock.PART, part);
    }

    private static BlockState wildCropState(boolean mature, Part part)
    {
        return ModBlocks.WILD_HEMP_CROP.get().defaultBlockState()
                .setValue(WildCropBlock.MATURE, mature)
                .setValue(WildDoubleCropBlock.PART, part);
    }

    private static BlockState farmland()
    {
        return BuiltInRegistries.BLOCK.get(id("tfc:farmland/entisol")).defaultBlockState();
    }

    private static Item seedItem()
    {
        return BuiltInRegistries.ITEM.get(id("immersiveengineering:seed"));
    }

    private static LootTable lootTable(GameTestHelper helper, String path)
    {
        return helper.getLevel().getServer().reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, id(path)));
    }

    private static List<ItemStack> drops(GameTestHelper helper, LootTable table,
                                         BlockState state, CropBlockEntity entity, long seed)
    {
        return drops(helper, table, state, entity, seed, null);
    }

    private static List<ItemStack> drops(GameTestHelper helper, LootTable table,
                                         BlockState state, CropBlockEntity entity, long seed,
                                         Float explosionRadius)
    {
        Builder builder = new Builder(helper.getLevel())
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.BLOCK_ENTITY, entity)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(BlockPos.ZERO));
        if (explosionRadius != null)
        {
            builder.withOptionalParameter(LootContextParams.EXPLOSION_RADIUS, explosionRadius);
        }
        return new ArrayList<>(table.getRandomItems(builder.create(LootContextParamSets.BLOCK),
                RandomSource.create(seed)));
    }

    private static int count(List<ItemStack> drops, Item item)
    {
        return drops.stream().filter(stack -> stack.is(item)).mapToInt(ItemStack::getCount).sum();
    }

    private static ResourceLocation id(String path)
    {
        return ResourceLocation.parse(path.contains(":") ? path : "tfie:" + path);
    }
}
