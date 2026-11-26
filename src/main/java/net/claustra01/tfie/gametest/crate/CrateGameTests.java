package net.claustra01.tfie.gametest.crate;

import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.blocks.wooden.WoodenCrateBlockEntity;
import blusunrize.immersiveengineering.common.gui.CrateMenu;
import blusunrize.immersiveengineering.common.register.IEMenuTypes;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.config.TFIEConfig;
import net.dries007.tfc.common.component.size.ItemSizeManager;
import net.dries007.tfc.common.component.size.Size;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CrateGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String[] CRATES = {
            "immersiveengineering:crate", "immersiveengineering:reinforced_crate"
    };

    private CrateGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_crate_config")
    public static void crate_size_boundaries(GameTestHelper helper)
    {
        Map<Size, ItemStack> samples = sizeSamples(helper);
        helper.assertTrue(TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.get() == Size.VERY_LARGE,
                "The default crate size must be VERY_LARGE");
        for(int crateIndex = 0; crateIndex < CRATES.length; crateIndex++)
        {
            BlockPos pos = new BlockPos(1+crateIndex, 2, 1);
            helper.setBlock(pos, block(CRATES[crateIndex]));
            WoodenCrateBlockEntity crate = helper.getBlockEntity(pos);
            for(Size size : Size.values())
                helper.assertTrue(crate.isStackValid(0, samples.get(size)) == size.isEqualOrSmallerThan(Size.VERY_LARGE),
                        CRATES[crateIndex] + " must use TFC's size order at " + size);
            assertInsertionPaths(helper, crate, samples.get(Size.VERY_LARGE), true);
            assertInsertionPaths(helper, crate, samples.get(Size.HUGE), false);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_crate_config")
    public static void crate_keeps_ie_rejections(GameTestHelper helper)
    {
        ItemStack nestedContainer = new ItemStack(Items.SHULKER_BOX);
        helper.assertTrue(!nestedContainer.getItem().canFitInsideContainerItems(),
                "The test item must exercise IE's container-in-container rejection");
        helper.assertTrue(!IEApi.isAllowedInCrate(nestedContainer),
                "TFIE must not override IE's container-in-container rejection");
        List<ItemStack> forbiddenSamples = forbiddenSamples(helper);
        for(int crateIndex = 0; crateIndex < CRATES.length; crateIndex++)
        {
            BlockPos pos = new BlockPos(1+crateIndex, 2, 1);
            helper.setBlock(pos, block(CRATES[crateIndex]));
            WoodenCrateBlockEntity crate = helper.getBlockEntity(pos);
            assertInsertionPaths(helper, crate, nestedContainer, false);
            assertInsertionPaths(helper, crate, forbiddenSamples.getFirst(), false);
        }
        helper.assertTrue(forbiddenSamples.stream().noneMatch(IEApi::isAllowedInCrate),
                "Every data-pack item in IE's forbidden crate tag must remain rejected");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_crate_config")
    public static void crate_gui_shift_and_capability_follow_config(GameTestHelper helper)
    {
        Map<Size, ItemStack> samples = sizeSamples(helper);
        ItemStack nestedContainer = new ItemStack(Items.SHULKER_BOX);
        ItemStack forbiddenSample = forbiddenSamples(helper).getFirst();
        Size originalMaximum = TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.get();
        try
        {
            TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.set(Size.NORMAL);
            for(int crateIndex = 0; crateIndex < CRATES.length; crateIndex++)
            {
                BlockPos pos = new BlockPos(1+crateIndex, 2, 1);
                helper.setBlock(pos, block(CRATES[crateIndex]));
                WoodenCrateBlockEntity crate = helper.getBlockEntity(pos);
                for(Size size : Size.values())
                    assertInsertionPaths(helper, crate, samples.get(size),
                            size.isEqualOrSmallerThan(Size.NORMAL));
                assertInsertionPaths(helper, crate, nestedContainer, false);
                assertInsertionPaths(helper, crate, forbiddenSample, false);
            }

            TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.set(Size.HUGE);
            for(int crateIndex = 0; crateIndex < CRATES.length; crateIndex++)
            {
                BlockPos pos = new BlockPos(1+crateIndex, 2, 3);
                helper.setBlock(pos, block(CRATES[crateIndex]));
                WoodenCrateBlockEntity crate = helper.getBlockEntity(pos);
                for(Size size : Size.values())
                    assertInsertionPaths(helper, crate, samples.get(size), true);
                assertInsertionPaths(helper, crate, nestedContainer, false);
                assertInsertionPaths(helper, crate, forbiddenSample, false);
            }
        }
        finally
        {
            TFIEConfig.CRATE_MAXIMUM_ITEM_SIZE.set(originalMaximum);
        }
        helper.succeed();
    }

    private static void assertInsertionPaths(GameTestHelper helper, WoodenCrateBlockEntity crate,
                                             ItemStack sample, boolean accepted)
    {
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        CrateMenu menu = new CrateMenu(IEMenuTypes.WOODEN_CRATE.get(), 0, player.getInventory(), crate);
        helper.assertTrue(menu.getSlot(0).mayPlace(sample) == accepted,
                "Normal GUI insertion must follow the configured TFC size for "
                        + BuiltInRegistries.ITEM.getKey(sample.getItem()) + " ("
                        + ItemSizeManager.get(sample).getSize(sample) + ")");

        crate.clearContent();
        player.getInventory().setItem(9, sample.copy());
        int originalCount = sample.getCount();
        menu.quickMoveStack(player, WoodenCrateBlockEntity.CONTAINER_SIZE);
        helper.assertTrue((crate.isEmpty() != accepted)
                        && player.getInventory().getItem(9).getCount() == (accepted ? 0 : originalCount),
                "Shift-click must neither lose nor duplicate an accepted or rejected stack");

        crate.clearContent();
        ItemStack remainder = crate.getInventoryCap().insertItem(0, sample.copy(), false);
        helper.assertTrue(remainder.isEmpty() == accepted,
                "Capability insertion must use the same crate validity check");
        helper.assertTrue(crate.getItem(0).getCount() == (accepted ? originalCount : 0)
                        && remainder.getCount() == (accepted ? 0 : originalCount),
                "Capability rejection must preserve the full input stack");
        crate.clearContent();
        player.discard();
    }

    private static Map<Size, ItemStack> sizeSamples(GameTestHelper helper)
    {
        Map<Size, ItemStack> samples = new EnumMap<>(Size.class);
        for(Item item : BuiltInRegistries.ITEM)
        {
            ItemStack stack = new ItemStack(item);
            if(!stack.isEmpty() && item.canFitInsideContainerItems() && !stack.is(IETags.forbiddenInCrates))
                samples.putIfAbsent(ItemSizeManager.get(stack).getSize(stack), stack);
        }
        for(Size size : Size.values())
            helper.assertTrue(samples.containsKey(size), "A normal crate-safe sample must exist for TFC size " + size);
        return samples;
    }

    private static List<ItemStack> forbiddenSamples(GameTestHelper helper)
    {
        List<ItemStack> samples = BuiltInRegistries.ITEM.stream()
                .filter(item -> new ItemStack(item).is(IETags.forbiddenInCrates))
                .map(ItemStack::new)
                .toList();
        helper.assertTrue(!samples.isEmpty(),
                "The IE forbidden-in-crates tag must contain a sample item");
        return samples;
    }

    private static Block block(String id)
    {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }
}
