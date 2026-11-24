package net.claustra01.tfie.gametest.multiblock;

import blusunrize.immersiveengineering.api.multiblocks.TemplateMultiblock;
import blusunrize.immersiveengineering.common.blocks.multiblocks.ArcFurnaceMultiblock;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MultiblockGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";

    private MultiblockGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void arc_furnace_template(GameTestHelper helper)
    {
        Machine arc = arc();
        assertTemplate(helper, arc);
        assertEveryMissingCoordinateFails(helper, arc);
        assertFormsAtMaster(helper, arc, Direction.SOUTH, false, true);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void advanced_templates(GameTestHelper helper)
    {
        if(ModList.get().isLoaded("advancedtfctech"))
            for(Machine machine : advanced())
            {
                assertTemplate(helper, machine);
                assertEveryMissingCoordinateFails(helper, machine);
                assertFormsAtMaster(helper, machine, Direction.SOUTH, false, false);
            }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void strict_tfc_block_ids(GameTestHelper helper)
    {
        Machine arc = arc();
        assertSubstitutionFails(helper, arc, positionOf(arc, "tfc:metal/block/black_steel"),
                block("immersiveengineering:storage_steel").defaultBlockState());
        assertSubstitutionFails(helper, arc, positionOf(arc, "tfc:fire_bricks"),
                Blocks.BRICKS.defaultBlockState());
        assertSubstitutionFails(helper, arc, positionOf(arc, "tfc:crucible"),
                Blocks.CAULDRON.defaultBlockState());
        if(ModList.get().isLoaded("advancedtfctech"))
            for(Machine machine : advanced())
                assertSubstitutionFails(helper, machine, positionOf(machine, "tfc:metal/block/steel"),
                        block("immersiveengineering:storage_steel").defaultBlockState());
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, timeoutTicks = 200)
    public static void rotations_mirroring_and_air(GameTestHelper helper)
    {
        for(Machine machine : machines())
        {
            for(Direction side : Direction.Plane.HORIZONTAL)
                for(boolean mirrored : new boolean[]{false, true})
                    assertFormsAtMaster(helper, machine, side, mirrored, true);

            place(helper, machine, Direction.SOUTH, false, null);
            BlockPos trigger = absoluteTrigger(helper, machine, Direction.SOUTH, false);
            helper.assertTrue(!machine.multiblock().createStructure(helper.getLevel(), trigger,
                            Direction.UP, helper.makeMockPlayer(GameType.CREATIVE))
                            && !machine.multiblock().createStructure(helper.getLevel(), trigger,
                            Direction.DOWN, helper.makeMockPlayer(GameType.CREATIVE)),
                    machine.name() + " must reject clicks on vertical faces");
            clear(helper, trigger);

            BlockPos directional = directionalPosition(machine);
            if(directional != null)
            {
                place(helper, machine, Direction.SOUTH, false, null);
                BlockState wrong = wrongDirectionalState(machine.expected().get(directional));
                helper.getLevel().setBlockAndUpdate(absolute(helper, machine, directional,
                        Direction.SOUTH, false), wrong);
                helper.assertTrue(!machine.multiblock().createStructure(helper.getLevel(),
                                absoluteTrigger(helper, machine, Direction.SOUTH, false), Direction.SOUTH,
                                helper.makeMockPlayer(GameType.CREATIVE)),
                        machine.name() + " must reject an incorrectly oriented template state");
                clear(helper, absoluteTrigger(helper, machine, Direction.SOUTH, false));
            }
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void advanced_mod_gate_and_persistence(GameTestHelper helper)
    {
        for(Machine machine : machines())
        {
            BlockPos trigger = absoluteTrigger(helper, machine, Direction.WEST, true);
            place(helper, machine, Direction.WEST, true, null);
            helper.assertTrue(machine.multiblock().createStructure(helper.getLevel(), trigger, Direction.WEST,
                            helper.makeMockPlayer(GameType.CREATIVE)),
                    machine.name() + " must form before its master is serialized");
            BlockPos master = absolute(helper, machine, machine.master(), Direction.WEST, true);
            BlockEntity formed = helper.getLevel().getBlockEntity(master);
            helper.assertTrue(formed != null, machine.name() + " must create its master block entity");
            CompoundTag saved = formed.saveWithFullMetadata(helper.getLevel().registryAccess());
            BlockEntity loaded = BlockEntity.loadStatic(master, formed.getBlockState(), saved,
                    helper.getLevel().registryAccess());
            helper.assertTrue(loaded != null && loaded.getType() == formed.getType(),
                    machine.name() + " master must reload with the same block entity type");
            loaded.setLevel(helper.getLevel());
            helper.assertTrue(loaded.saveWithFullMetadata(helper.getLevel().registryAccess()).equals(saved),
                    machine.name() + " master orientation, mirror and link data must survive serialization");
            clear(helper, trigger);
        }
        helper.succeed();
    }

    private static void assertTemplate(GameTestHelper helper, Machine machine)
    {
        helper.assertTrue(machine.multiblock().getSize(helper.getLevel()).equals(machine.size()),
                machine.name() + " has the wrong dimensions");
        Map<BlockPos, BlockState> actual = new LinkedHashMap<>();
        for(StructureTemplate.StructureBlockInfo info : machine.multiblock().getStructure(helper.getLevel()))
            actual.put(info.pos(), info.state());
        helper.assertTrue(actual.keySet().equals(machine.expected().keySet()),
                machine.name() + " coordinates differ from chapter 16");
        machine.expected().forEach((position, expected) -> helper.assertTrue(
                expected.equals(actual.get(position)),
                machine.name() + " has the wrong block or state at " + position
                        + ": expected " + expected + ", got " + actual.get(position)));
    }

    private static void assertEveryMissingCoordinateFails(GameTestHelper helper, Machine machine)
    {
        for(BlockPos missing : machine.expected().keySet())
        {
            place(helper, machine, Direction.SOUTH, false, missing);
            boolean formed = machine.multiblock().createStructure(helper.getLevel(),
                    absoluteTrigger(helper, machine, Direction.SOUTH, false), Direction.SOUTH,
                    helper.makeMockPlayer(GameType.CREATIVE));
            helper.assertTrue(!formed, machine.name() + " formed with required coordinate missing: " + missing);
            clear(helper, absoluteTrigger(helper, machine, Direction.SOUTH, false));
        }
    }

    private static void assertSubstitutionFails(GameTestHelper helper, Machine machine, BlockPos position,
                                                BlockState substitute)
    {
        place(helper, machine, Direction.SOUTH, false, null);
        helper.getLevel().setBlockAndUpdate(absolute(helper, machine, position, Direction.SOUTH, false), substitute);
        helper.assertTrue(!machine.multiblock().createStructure(helper.getLevel(),
                        absoluteTrigger(helper, machine, Direction.SOUTH, false), Direction.SOUTH,
                        helper.makeMockPlayer(GameType.CREATIVE)),
                machine.name() + " accepted " + substitute + " at strict position " + position);
        clear(helper, absoluteTrigger(helper, machine, Direction.SOUTH, false));
    }

    private static void assertFormsAtMaster(GameTestHelper helper, Machine machine, Direction side,
                                            boolean mirrored, boolean addBlockInEmptyCell)
    {
        place(helper, machine, side, mirrored, null);
        BlockPos trigger = absoluteTrigger(helper, machine, side, mirrored);
        if(addBlockInEmptyCell)
        {
            BlockPos empty = new BlockPos(machine.size().getX() - 1, machine.size().getY() - 1,
                    machine.size().getZ() - 1);
            if(!machine.expected().containsKey(empty))
                helper.getLevel().setBlockAndUpdate(absolute(helper, machine, empty, side, mirrored),
                        Blocks.GOLD_BLOCK.defaultBlockState());
        }
        helper.assertTrue(machine.multiblock().createStructure(helper.getLevel(), trigger, side,
                        helper.makeMockPlayer(GameType.CREATIVE)),
                machine.name() + " did not form for " + side + ", mirrored=" + mirrored);
        BlockPos master = absolute(helper, machine, machine.master(), side, mirrored);
        helper.assertTrue(helper.getLevel().getBlockEntity(master) != null,
                machine.name() + " did not create the master at " + machine.master());
        clear(helper, trigger);
    }

    private static void place(GameTestHelper helper, Machine machine, Direction side,
                              boolean mirrored, BlockPos missing)
    {
        StructureTemplate template = helper.getLevel().getStructureManager()
                .get(machine.multiblock().getTemplateLocation()).orElseThrow();
        BlockPos origin = origin(helper);
        template.placeInWorld(helper.getLevel(), origin, origin, settings(side, mirrored),
                helper.getLevel().getRandom(), Block.UPDATE_ALL);
        if(missing != null)
            helper.getLevel().setBlockAndUpdate(absolute(helper, machine, missing, side, mirrored),
                    Blocks.AIR.defaultBlockState());
    }

    private static BlockPos absolute(GameTestHelper helper, Machine machine, BlockPos position,
                                     Direction side, boolean mirrored)
    {
        return origin(helper).offset(StructureTemplate.calculateRelativePosition(
                settings(side, mirrored), position));
    }

    private static BlockPos absoluteTrigger(GameTestHelper helper, Machine machine, Direction side, boolean mirrored)
    {
        return absolute(helper, machine, machine.trigger(), side, mirrored);
    }

    private static BlockPos origin(GameTestHelper helper)
    {
        return helper.absolutePos(new BlockPos(8, 3, 8));
    }

    private static StructurePlaceSettings settings(Direction side, boolean mirrored)
    {
        return new StructurePlaceSettings().setRotation(rotation(side))
                .setMirror(mirrored ? Mirror.FRONT_BACK : Mirror.NONE);
    }

    private static void clear(GameTestHelper helper, BlockPos center)
    {
        for(BlockPos position : BlockPos.betweenClosed(center.offset(-8, -2, -8), center.offset(8, 7, 8)))
            helper.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
    }

    private static Rotation rotation(Direction side)
    {
        return switch(side)
        {
            case SOUTH -> Rotation.NONE;
            case WEST -> Rotation.CLOCKWISE_90;
            case NORTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.COUNTERCLOCKWISE_90;
            default -> throw new IllegalArgumentException("Horizontal side required");
        };
    }

    private static BlockPos positionOf(Machine machine, String blockId)
    {
        Block block = block(blockId);
        return machine.expected().entrySet().stream()
                .filter(entry -> entry.getValue().is(block))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    private static BlockPos directionalPosition(Machine machine)
    {
        return switch(machine.name())
        {
            case "arc furnace" -> positionOf(machine, "immersiveengineering:slab_sheetmetal_steel");
            case "grist mill" -> positionOf(machine, "minecraft:hopper");
            case "power loom" -> positionOf(machine, "immersiveengineering:steel_fence");
            default -> null;
        };
    }

    private static BlockState wrongDirectionalState(BlockState state)
    {
        if(state.hasProperty(net.minecraft.world.level.block.SlabBlock.TYPE))
            return state.setValue(net.minecraft.world.level.block.SlabBlock.TYPE,
                    net.minecraft.world.level.block.state.properties.SlabType.TOP);
        if(state.hasProperty(net.minecraft.world.level.block.HopperBlock.FACING))
            return state.setValue(net.minecraft.world.level.block.HopperBlock.FACING, Direction.NORTH);
        if(state.hasProperty(net.minecraft.world.level.block.CrossCollisionBlock.EAST))
            return state.setValue(net.minecraft.world.level.block.CrossCollisionBlock.EAST, true)
                    .setValue(net.minecraft.world.level.block.CrossCollisionBlock.SOUTH, false);
        return state;
    }

    private static List<Machine> machines()
    {
        if(ModList.get().isLoaded("advancedtfctech"))
            return List.of(arc(), grist(), loom());
        return List.of(arc());
    }

    private static List<Machine> advanced()
    {
        return List.of(grist(), loom());
    }

    private static Machine arc()
    {
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
        add(blocks, "immersiveengineering:sheetmetal_steel",
                "0,0,0 0,0,1 1,0,0 3,0,0 4,0,0 4,0,1 0,2,0 4,2,0");
        add(blocks, "tfc:metal/block/black_steel",
                "0,0,2 4,0,2 0,1,2 4,1,2 0,2,2 4,2,2");
        add(blocks, "immersiveengineering:slab_sheetmetal_steel",
                "0,0,3 1,0,1 1,0,2 1,0,3 1,0,4 2,0,1 2,0,2 2,0,3 3,0,1 3,0,2 3,0,3 3,0,4 4,0,3 4,0,4",
                "type=bottom,waterlogged=false");
        add(blocks, "immersiveengineering:steel_scaffolding_standard",
                "0,0,4 1,3,0 3,3,0 1,4,0 3,4,0", "waterlogged=false");
        add(blocks, "immersiveengineering:heavy_engineering",
                "2,0,0 0,1,0 0,1,1 4,1,0 4,1,1");
        add(blocks, "tfc:crucible", "2,0,4");
        add(blocks, "immersiveengineering:rs_engineering", "0,1,4");
        add(blocks, "immersiveengineering:light_engineering",
                "1,1,0 2,1,0 3,1,0 1,2,0 2,2,0 3,2,0 2,3,0 2,4,0 2,4,1 2,4,2");
        add(blocks, "tfc:fire_bricks",
                "1,1,1 1,1,2 2,1,1 2,1,2 3,1,1 3,1,2 1,2,1 1,2,2 1,2,3 1,2,4 "
                        + "2,2,1 2,2,2 2,2,3 2,2,4 3,2,1 3,2,2 3,2,3 3,2,4 1,3,1 1,3,2 "
                        + "1,3,3 2,3,1 2,3,2 2,3,3 3,3,1 3,3,2 3,3,3");
        return new Machine("arc furnace", new ArcFurnaceMultiblock(), new Vec3i(5, 5, 5),
                new BlockPos(2, 0, 4), new BlockPos(2, 1, 2), blocks);
    }

    private static Machine grist()
    {
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
        add(blocks, "immersiveengineering:steel_scaffolding_standard",
                "0,0,0 0,0,2 1,0,0 1,0,1 2,0,0 2,0,1 2,0,2 3,0,0 3,0,2", "waterlogged=false");
        add(blocks, "immersiveengineering:sheetmetal_steel", "0,0,1 3,0,1 1,1,2");
        add(blocks, "immersiveengineering:light_engineering", "1,0,2 0,1,1 1,1,1 3,1,1");
        add(blocks, "immersiveengineering:rs_engineering", "1,1,0");
        add(blocks, "immersiveengineering:slab_sheetmetal_steel",
                "2,1,0 2,1,2 3,1,0 3,1,2", "type=bottom,waterlogged=false");
        add(blocks, "tfc:metal/block/steel", "2,1,1");
        add(blocks, "minecraft:hopper", "1,2,1", "facing=down,enabled=true");
        return new Machine("grist mill", advancedMultiblock(
                "com.hermitowo.advancedtfctech.common.multiblocks.GristMillMultiblock"),
                new Vec3i(4, 3, 3), new BlockPos(2, 1, 1), new BlockPos(1, 0, 1), blocks);
    }

    private static Machine loom()
    {
        Map<BlockPos, BlockState> blocks = new LinkedHashMap<>();
        add(blocks, "immersiveengineering:steel_scaffolding_standard",
                "0,0,1 0,0,2 0,0,3 2,0,1 2,0,2 2,0,3 0,1,1 0,1,3 2,1,1 2,1,3 2,2,1 2,2,2 2,2,3",
                "waterlogged=false");
        add(blocks, "immersiveengineering:light_engineering", "1,0,0 1,1,0 1,1,4");
        add(blocks, "immersiveengineering:heavy_engineering", "1,0,1 1,0,2 1,0,3");
        add(blocks, "tfc:metal/block/steel", "1,0,4");
        add(blocks, "immersiveengineering:rs_engineering", "2,0,4");
        add(blocks, "immersiveengineering:sheetmetal_steel", "1,1,1 1,1,3 1,2,1 1,2,2 1,2,3");
        add(blocks, "immersiveengineering:steel_fence", "1,1,2",
                "east=false,west=false,south=true,north=true,waterlogged=false");
        return new Machine("power loom", advancedMultiblock(
                "com.hermitowo.advancedtfctech.common.multiblocks.PowerLoomMultiblock"),
                new Vec3i(3, 3, 5), new BlockPos(1, 1, 4), new BlockPos(1, 0, 2), blocks);
    }

    private static TemplateMultiblock advancedMultiblock(String className)
    {
        try
        {
            return (TemplateMultiblock)Class.forName(className).getConstructor().newInstance();
        }
        catch(ClassNotFoundException | NoSuchMethodException | InstantiationException
              | IllegalAccessException | InvocationTargetException exception)
        {
            throw new IllegalStateException("Unable to instantiate optional multiblock " + className, exception);
        }
    }

    private static void add(Map<BlockPos, BlockState> target, String blockId, String coordinates)
    {
        add(target, blockId, coordinates, "");
    }

    private static void add(Map<BlockPos, BlockState> target, String blockId, String coordinates, String properties)
    {
        BlockState state = block(blockId).defaultBlockState();
        if(!properties.isEmpty())
            for(String assignment : properties.split(","))
            {
                String[] parts = assignment.split("=");
                state = setProperty(state, parts[0], parts[1]);
            }
        for(String coordinate : coordinates.split(" "))
        {
            String[] values = coordinate.split(",");
            target.put(new BlockPos(Integer.parseInt(values[0]), Integer.parseInt(values[1]),
                    Integer.parseInt(values[2])), state);
        }
    }

    private static Block block(String id)
    {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }

    private static BlockState setProperty(BlockState state, String name, String value)
    {
        Property<?> property = state.getProperties().stream()
                .filter(candidate -> candidate.getName().equals(name))
                .findFirst()
                .orElseThrow();
        return setProperty(state, property, value);
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState state,
                                                                     Property<T> property, String value)
    {
        return state.setValue(property, property.getValue(value).orElseThrow());
    }

    private record Machine(String name, TemplateMultiblock multiblock, Vec3i size,
                           BlockPos trigger, BlockPos master, Map<BlockPos, BlockState> expected)
    {
    }
}
