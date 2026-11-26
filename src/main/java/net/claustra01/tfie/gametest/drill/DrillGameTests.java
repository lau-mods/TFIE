package net.claustra01.tfie.gametest.drill;

import blusunrize.immersiveengineering.api.tool.IDrillHead;
import com.mojang.authlib.GameProfile;
import net.claustra01.tfcm.common.tool.ToolMetal;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.TFCTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DrillGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final List<Head> HEADS = List.of(
            new Head("immersiveengineering:drillhead_iron", 2, Tiers.IRON, 9, 6, 6000),
            new Head("immersiveengineering:drillhead_steel", 3, Tiers.DIAMOND, 10, 7, 10000),
            new Head("tfie:drillhead_black_steel", 3, TFCTiers.BLACK_STEEL, 12, 8, 16000),
            new Head("tfie:drillhead_blue_steel", 5, TFCTiers.BLUE_STEEL, 15, 10, 20000),
            new Head("tfie:drillhead_red_steel", 5, TFCTiers.RED_STEEL, 15, 10, 20000),
            new Head("tfie:drillhead_invar", 2, ToolMetal.INVAR.toolTier(), 10, 7, 11000),
            new Head("tfie:drillhead_titanium", 3, ToolMetal.TITANIUM.toolTier(), 13, 9, 16500),
            new Head("tfie:drillhead_netherite", 5, ToolMetal.NETHERITE.toolTier(), 16, 11, 32500),
            new Head("tfie:drillhead_tungsten_steel", 5, ToolMetal.TUNGSTEN_STEEL.toolTier(), 18, 12, 40625));

    private DrillGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void drillhead_performance_and_mining_areas(GameTestHelper helper)
    {
        ServerPlayer player = fakePlayer(helper, "tfie_drill_area");
        player.getInventory().clearContent();
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.NETHERITE_PICKAXE));
        BlockPos center = helper.absolutePos(new BlockPos(8, 5, 8));
        for(BlockPos pos : BlockPos.betweenClosed(center.offset(-3, -3, -3), center.offset(3, 3, 3)))
            helper.getLevel().setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());

        for(Head expected : HEADS)
        {
            ItemStack stack = new ItemStack(item(expected.id));
            helper.assertTrue(stack.getMaxStackSize() == 1 && stack.getDamageValue() == 0,
                    expected.id + " must start undamaged and be unstackable");
            helper.assertTrue(stack.getItem() instanceof IDrillHead, expected.id + " must implement IDrillHead");
            IDrillHead head = (IDrillHead) stack.getItem();
            Tier actualTier = head.getMiningLevel(stack);
            helper.assertTrue(actualTier == expected.tier
                            || actualTier instanceof LevelTier actualLevel
                            && expected.tier instanceof LevelTier expectedLevel
                            && actualLevel.level() == expectedLevel.level()
                            && actualTier.getUses() == expected.tier.getUses()
                            && actualTier.getSpeed() == expected.tier.getSpeed()
                            && actualTier.getAttackDamageBonus() == expected.tier.getAttackDamageBonus()
                            && actualTier.getIncorrectBlocksForDrops() == expected.tier.getIncorrectBlocksForDrops()
                            && actualTier.getEnchantmentValue() == expected.tier.getEnchantmentValue(),
                    expected.id + " has the wrong mining tier");
            helper.assertTrue(head.getMiningSpeed(stack) == expected.speed,
                    expected.id + " has the wrong mining speed");
            helper.assertTrue(head.getAttackDamage(stack) == expected.attack,
                    expected.id + " has the wrong attack value");
            helper.assertTrue(head.getMaximumHeadDamage(stack) == expected.durability,
                    expected.id + " has the wrong durability");

            for(Direction face : Direction.values())
            {
                BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(center), face, center, false);
                int extra = head.getExtraBlocksDug(stack, helper.getLevel(), player, hit).size();
                helper.assertTrue(extra == expected.size * expected.size - 1,
                        expected.id + " must mine a " + expected.size + " by " + expected.size
                                + " plane from the " + face + " face, found " + extra + " extra blocks");
            }
        }

        for(Item item : List.of(ModItems.DRILLHEAD_INVAR.get(), ModItems.DRILLHEAD_TITANIUM.get(),
                ModItems.DRILLHEAD_NETHERITE.get(), ModItems.DRILLHEAD_TUNGSTEN_STEEL.get()))
        {
            ItemStack stack = new ItemStack(item);
            String displayedLevel = ((IDrillHead) item).getMiningLevel(stack).toString();
            helper.assertTrue(displayedLevel.matches("[0-9]+"),
                    item + " must expose a readable numeric mining level, found " + displayedLevel);
        }

        ItemStack invar = new ItemStack(ModItems.DRILLHEAD_INVAR.get());
        IDrillHead even = (IDrillHead) invar.getItem();
        BlockHitResult low = new BlockHitResult(new Vec3(center.getX() + 0.25, center.getY() + 0.25,
                center.getZ() + 0.5), Direction.NORTH, center, false);
        BlockHitResult high = new BlockHitResult(new Vec3(center.getX() + 0.75, center.getY() + 0.75,
                center.getZ() + 0.5), Direction.NORTH, center, false);
        List<BlockPos> lowArea = even.getExtraBlocksDug(invar, helper.getLevel(), player, low);
        List<BlockPos> highArea = even.getExtraBlocksDug(invar, helper.getLevel(), player, high);
        helper.assertTrue(lowArea.contains(center.offset(-1, -1, 0))
                        && highArea.contains(center.offset(1, 1, 0)),
                "Even drill widths must shift within the hit face according to the hit position");

        ItemStack tungsten = new ItemStack(ModItems.DRILLHEAD_TUNGSTEN_STEEL.get());
        IDrillHead large = (IDrillHead) tungsten.getItem();
        BlockPos ineffective = center.offset(1, 0, 0);
        BlockPos tooSlow = center.offset(2, 0, 0);
        helper.getLevel().setBlockAndUpdate(ineffective, Blocks.DIRT.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(tooSlow, Blocks.OBSIDIAN.defaultBlockState());
        List<BlockPos> eligible = large.getExtraBlocksDug(tungsten, helper.getLevel(), player,
                new BlockHitResult(Vec3.atCenterOf(center), Direction.NORTH, center, false));
        helper.assertTrue(!eligible.contains(ineffective), "Drill-ineffective blocks must not be added");
        helper.assertTrue(!eligible.contains(tooSlow),
                "Blocks below forty percent of the main break progress must not be added");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WOODEN_PICKAXE));
        helper.getLevel().setBlockAndUpdate(center, Blocks.OBSIDIAN.defaultBlockState());
        BlockPos unharvestable = center.offset(1, 0, 0);
        helper.getLevel().setBlockAndUpdate(unharvestable, Blocks.DIAMOND_ORE.defaultBlockState());
        List<BlockPos> harvestable = large.getExtraBlocksDug(tungsten, helper.getLevel(), player,
                new BlockHitResult(Vec3.atCenterOf(center), Direction.NORTH, center, false));
        helper.assertTrue(!harvestable.contains(unharvestable),
                "Blocks the player cannot harvest must not be added even when progress is sufficient");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void drillhead_repair_amounts(GameTestHelper helper)
    {
        ServerPlayer player = fakePlayer(helper, "tfie_repair_amt");
        player.getInventory().clearContent();
        List<String> ingots = List.of(
                "tfc:metal/ingot/wrought_iron", "tfc:metal/ingot/steel",
                "tfc:metal/ingot/black_steel", "tfc:metal/ingot/blue_steel",
                "tfc:metal/ingot/red_steel", "tfcm:metal/ingot/invar", "tfcm:metal/ingot/titanium",
                "tfcm:metal/ingot/netherite", "tfcm:metal/ingot/tungsten_steel");
        for(int index = 0; index < HEADS.size(); index++)
        {
            Head expected = HEADS.get(index);
            Item ingot = item(ingots.get(index));
            if(ingot == Items.AIR)
                continue;
            ItemStack damaged = new ItemStack(item(expected.id));
            damaged.set(DataComponents.DAMAGE, expected.durability);
            AnvilUpdateEvent event = repair(damaged, new ItemStack(ingot, 2), null, player);
            int perIngot = expected.durability / 4;
            IDrillHead head = (IDrillHead) damaged.getItem();
            helper.assertTrue(!event.getOutput().isEmpty(),
                    expected.id + " did not accept " + ingots.get(index));
            helper.assertTrue(head.getHeadDamage(event.getOutput()) == expected.durability - perIngot * 2,
                    expected.id + " must repair one integer quarter per ingot; remaining damage was "
                            + head.getHeadDamage(event.getOutput()));
            helper.assertTrue(event.getMaterialCost() == 2,
                    expected.id + " must consume exactly the useful repair materials");
        }

        ItemStack iron = new ItemStack(item("immersiveengineering:drillhead_iron"));
        iron.set(DataComponents.DAMAGE, 1500);
        for(String rejected : List.of("tfc:metal/ingot/cast_iron", "minecraft:iron_ingot"))
        {
            AnvilUpdateEvent rejectedRepair = repair(iron, new ItemStack(item(rejected)), null, player);
            helper.assertTrue(rejectedRepair.getOutput().isEmpty() && rejectedRepair.isCanceled(),
                    "Iron drill heads must stop vanilla anvil fallback for " + rejected);
        }
        helper.assertTrue(!repair(iron, new ItemStack(item("tfc:metal/ingot/wrought_iron")), null, player)
                        .getOutput().isEmpty(), "Iron drill heads must accept wrought iron");
        helper.assertTrue(HEADS.get(7).durability / 4 == 8125
                        && HEADS.get(8).durability / 4 == 10156,
                "Netherite and tungsten steel quarter repairs must use integer division");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void drillhead_repair_costs_and_names(GameTestHelper helper)
    {
        ServerPlayer player = fakePlayer(helper, "tfie_repair_cost");
        player.getInventory().clearContent();
        ItemStack head = new ItemStack(ModItems.DRILLHEAD_BLACK_STEEL.get());
        head.set(DataComponents.DAMAGE, 4100);
        ItemStack ingots = new ItemStack(item("tfc:metal/ingot/black_steel"), 2);
        AnvilUpdateEvent unchanged = repair(head, ingots, null, player);
        helper.assertTrue(unchanged.getMaterialCost() == 2 && unchanged.getCost() == 1,
                "Repair cost must use only the final 100 durability repair, not the sum; material cost was "
                        + unchanged.getMaterialCost() + " and level cost was " + unchanged.getCost());

        ItemStack named = head.copy();
        named.set(DataComponents.CUSTOM_NAME, Component.literal("Old Name"));
        AnvilUpdateEvent kept = repair(named, ingots, "Old Name", player);
        helper.assertTrue(kept.getCost() == 1 && kept.getOutput().has(DataComponents.CUSTOM_NAME),
                "Keeping the same name must not add levels");
        AnvilUpdateEvent removed = repair(named, ingots, "", player);
        helper.assertTrue(removed.getCost() == 6 && !removed.getOutput().has(DataComponents.CUSTOM_NAME),
                "Removing an existing name must add five levels");
        AnvilUpdateEvent renamed = repair(named, ingots, "New Name", player);
        helper.assertTrue(renamed.getCost() == 8
                        && renamed.getOutput().getHoverName().getString().equals("New Name"),
                "Replacing an existing name must add seven levels");
        AnvilUpdateEvent firstName = repair(head, ingots, "New Name", player);
        helper.assertTrue(firstName.getCost() == 6,
                "Adding the first custom name must add five levels");

        ItemStack undamaged = new ItemStack(ModItems.DRILLHEAD_BLACK_STEEL.get());
        AnvilUpdateEvent renameOnly = new AnvilUpdateEvent(undamaged, ItemStack.EMPTY, "New Name", 0, player);
        ItemStack vanillaOutput = undamaged.copy();
        vanillaOutput.set(DataComponents.CUSTOM_NAME, Component.literal("New Name"));
        renameOnly.setOutput(vanillaOutput);
        renameOnly.setCost(5);
        NeoForge.EVENT_BUS.post(renameOnly);
        helper.assertTrue(renameOnly.getOutput() == vanillaOutput && renameOnly.getCost() == 5,
                "An undamaged head with an empty right slot must preserve a rename-only anvil result");
        helper.succeed();
    }

    private static AnvilUpdateEvent repair(ItemStack left, ItemStack right, String name, ServerPlayer player)
    {
        AnvilUpdateEvent event = new AnvilUpdateEvent(left, right, name, 0, player);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    private static ServerPlayer fakePlayer(GameTestHelper helper, String name)
    {
        UUID id = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
        return FakePlayerFactory.get(helper.getLevel(), new GameProfile(id, name));
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private record Head(String id, int size, Tier tier, float speed, float attack, int durability)
    {
    }
}
