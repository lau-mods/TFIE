package net.claustra01.tfie.gametest.anvil;

import blusunrize.immersiveengineering.common.register.IEItems;
import com.mojang.authlib.GameProfile;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.component.forge.ForgeStep;
import net.dries007.tfc.common.component.forge.Forging;
import net.dries007.tfc.common.component.forge.ForgingBonus;
import net.dries007.tfc.common.component.forge.ForgingCapability;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class AnvilGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final ResourceLocation HIGH_CARBON_STEEL =
            ResourceLocation.fromNamespaceAndPath("tfc", "anvil/metal/ingot/high_carbon_steel");
    private static final List<AnvilCase> DRILLHEADS = List.of(
            new AnvilCase("drillhead_iron", "tfc:metal/double_sheet/wrought_iron",
                    "immersiveengineering:drillhead_iron", 1, 3, drillRules(), false),
            new AnvilCase("drillhead_steel", "tfc:metal/double_sheet/steel",
                    "immersiveengineering:drillhead_steel", 1, 4, drillRules(), false),
            new AnvilCase("drillhead_black_steel", "tfc:metal/double_sheet/black_steel",
                    "tfie:drillhead_black_steel", 1, 5, drillRules(), false),
            new AnvilCase("drillhead_blue_steel", "tfc:metal/double_sheet/blue_steel",
                    "tfie:drillhead_blue_steel", 1, 6, drillRules(), false),
            new AnvilCase("drillhead_red_steel", "tfc:metal/double_sheet/red_steel",
                    "tfie:drillhead_red_steel", 1, 6, drillRules(), false),
            new AnvilCase("drillhead_invar", "tfcm:metal/double_sheet/invar",
                    "tfie:drillhead_invar", 1, 3, drillRules(), false),
            new AnvilCase("drillhead_titanium", "tfcm:metal/double_sheet/titanium",
                    "tfie:drillhead_titanium", 1, 5, drillRules(), false),
            new AnvilCase("drillhead_netherite", "tfcm:metal/double_sheet/netherite",
                    "tfie:drillhead_netherite", 1, 6, drillRules(), false),
            new AnvilCase("drillhead_tungsten_steel", "tfcm:metal/double_sheet/tungsten_steel",
                    "tfie:drillhead_tungsten_steel", 1, 7, drillRules(), false));
    private static final List<AnvilCase> OTHER = List.of(
            faraday("boots"), faraday("chestplate"), faraday("helmet"), faraday("leggings"),
            new AnvilCase("empty_casing", "tfc:metal/ingot/copper", "immersiveengineering:empty_casing",
                    2, 1, List.of(ForgeRule.PUNCH_ANY, ForgeRule.UPSET_ANY, ForgeRule.SHRINK_LAST), false),
            new AnvilCase("gunpart_barrel", "tfc:metal/ingot/steel", "immersiveengineering:gunpart_barrel",
                    1, 4, List.of(ForgeRule.SHRINK_ANY, ForgeRule.HIT_ANY, ForgeRule.DRAW_LAST), false),
            new AnvilCase("tool_head/ie_hammer", "tfc:metal/ingot/wrought_iron", "tfie:tool_head/ie_hammer",
                    1, 3, List.of(ForgeRule.HIT_THIRD_LAST, ForgeRule.DRAW_SECOND_LAST,
                    ForgeRule.UPSET_LAST), false),
            new AnvilCase("tool_head/wirecutter", "tfc:metal/ingot/wrought_iron", "tfie:tool_head/wirecutter",
                    1, 3, List.of(ForgeRule.BEND_NOT_LAST, ForgeRule.SHRINK_NOT_LAST,
                    ForgeRule.HIT_LAST), false),
            new AnvilCase("toolupgrade_revolver_bayonet", "tfc:metal/ingot/steel",
                    "immersiveengineering:toolupgrade_revolver_bayonet", 1, 4,
                    List.of(ForgeRule.DRAW_ANY, ForgeRule.BEND_ANY, ForgeRule.HIT_LAST), false));

    private AnvilGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void drillhead_anvil_recipes_and_failures(GameTestHelper helper)
    {
        for(AnvilCase expected : DRILLHEADS)
            if(item(expected.input) == Items.AIR)
                helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(
                                "tfie:anvil/" + expected.path)).isEmpty(),
                        expected.path + " must be absent when its TFCM metal is disabled");
            else
                verifyRecipe(helper, expected);
        for(String removed : List.of("drillhead_iron", "drillhead_steel"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(
                            "immersiveengineering:crafting/" + removed)).isEmpty(),
                    "The IE crafting recipe " + removed + " must be disabled");
        helper.assertTrue(helper.getLevel().getRecipeManager().getRecipes().stream().noneMatch(holder ->
                        holder.id().getNamespace().equals(TFIE.MOD_ID)
                                && holder.id().getPath().contains("welding")
                                && holder.id().getPath().contains("drillhead")),
                "TFIE must not add welded drill heads");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void other_anvil_recipes(GameTestHelper helper)
    {
        for(AnvilCase expected : OTHER)
            verifyRecipe(helper, expected);
        for(String removed : List.of("armor_faraday_boots", "armor_faraday_chestplate",
                "armor_faraday_helmet", "armor_faraday_leggings", "empty_casing", "gunpart_barrel",
                "toolupgrade_revolver_bayonet"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(
                            "immersiveengineering:crafting/" + removed)).isEmpty(),
                    "The replaced IE crafting recipe " + removed + " must be disabled");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void anvil_rules_temperature_and_hammer_contract(GameTestHelper helper)
    {
        helper.assertTrue(ForgeStep.HIT_LIGHT.step() == -3 && ForgeStep.HIT_MEDIUM.step() == -6
                        && ForgeStep.HIT_HARD.step() == -9 && ForgeStep.DRAW.step() == -15
                        && ForgeStep.PUNCH.step() == 2 && ForgeStep.BEND.step() == 7
                        && ForgeStep.UPSET.step() == 13 && ForgeStep.SHRINK.step() == 16,
                "TFC forge operations must retain their standard work changes");
        ItemStack hammer = new ItemStack(item("tfc:metal/hammer/wrought_iron"));
        helper.assertTrue(hammer.is(net.minecraft.tags.ItemTags.create(id("c:tools/hammer")))
                        && hammer.isDamageableItem(),
                "Anvil work must use a damageable #c:tools/hammer");

        for(AnvilCase expected : concat(DRILLHEADS, OTHER))
        {
            ItemStack input = new ItemStack(item(expected.input));
            if(input.is(Items.AIR))
                continue;
            IHeat heat = HeatCapability.get(input);
            helper.assertTrue(heat != null, expected.input + " must carry TFC heat data");
            float working = heat.getWorkingTemperature();
            heat.setTemperature(Math.nextDown(working));
            helper.assertTrue(!heat.canWork(), expected.input + " must fail immediately below working temperature");
            heat.setTemperature(working);
            helper.assertTrue(heat.canWork(), expected.input + " must work at the temperature boundary");
        }

        AnvilRecipe casing = recipe(helper, "tfie:anvil/empty_casing").value();
        SyntheticInventory inventory = complete(recipe(helper, "tfie:anvil/empty_casing"), 1);
        helper.assertTrue(casing.checkComplete(inventory), "Correct target and last operations must complete");
        ForgingCapability.get(inventory.getItem()).addStep(ForgeStep.DRAW, 0);
        helper.assertTrue(!casing.checkComplete(inventory), "Changing the final operation must prevent completion");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void forging_quality_boundaries(GameTestHelper helper)
    {
        helper.assertTrue(ForgingBonus.byRatio(Math.nextDown(1.5f)) == ForgingBonus.PERFECT
                        && ForgingBonus.byRatio(1.5f) == ForgingBonus.EXPERT
                        && ForgingBonus.byRatio(Math.nextDown(2f)) == ForgingBonus.EXPERT
                        && ForgingBonus.byRatio(2f) == ForgingBonus.WELL
                        && ForgingBonus.byRatio(Math.nextDown(5f)) == ForgingBonus.WELL
                        && ForgingBonus.byRatio(5f) == ForgingBonus.MODEST
                        && ForgingBonus.byRatio(Math.nextDown(10f)) == ForgingBonus.MODEST
                        && ForgingBonus.byRatio(10f) == ForgingBonus.NONE,
                "Forging quality must use the exact 1.5, 2, 5 and 10 ratio boundaries");
        for(AnvilCase expected : concat(DRILLHEADS, OTHER))
            if(item(expected.input) != Items.AIR)
                helper.assertTrue(recipe(helper, "tfie:anvil/" + expected.path).value().shouldApplyForgingBonus()
                                == expected.bonus,
                        expected.path + " has the wrong apply_bonus value");
        helper.assertTrue(OTHER.stream().filter(AnvilCase::bonus).count() == 4,
                "Only the four Faraday recipes may evaluate new forging quality");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_hcs_inventory")
    public static void high_carbon_steel_slag_inventory_and_drop(GameTestHelper helper)
    {
        ServerPlayer open = fakePlayer(helper, "tfie_hcs_open");
        open.getInventory().clearContent();
        completePlayerHighCarbonSteel(helper, new BlockPos(5, 2, 5), open);
        helper.assertTrue(open.getInventory().countItem(IEItems.Ingredients.SLAG.get()) == 1,
                "Successful player HCS work must insert one slag into inventory first");

        ServerPlayer full = fakePlayer(helper, "tfie_hcs_full");
        full.getInventory().clearContent();
        for(int slot = 0; slot < full.getInventory().getContainerSize(); slot++)
            full.getInventory().setItem(slot, new ItemStack(Items.STONE, 64));
        BlockPos fullAnvil = new BlockPos(10, 2, 10);
        completePlayerHighCarbonSteel(helper, fullAnvil, full);
        BlockPos anvilPos = helper.absolutePos(fullAnvil);
        helper.succeedWhen(() ->
        {
            long dropped = helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(anvilPos).inflate(2))
                    .stream().filter(entity -> entity.getItem().is(IEItems.Ingredients.SLAG.get())).count();
            helper.assertTrue(dropped == 1, "A full inventory must drop exactly one slag at the anvil");
            open.getInventory().clearContent();
            full.getInventory().clearContent();
        });
    }

    private static void completePlayerHighCarbonSteel(GameTestHelper helper, BlockPos relative,
                                                       ServerPlayer player)
    {
        completePlayerRecipe(helper, relative, player, HIGH_CARBON_STEEL,
                "tfc:metal/ingot/pig_iron", "tfc:metal/ingot/high_carbon_steel");
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID, batch = "tfie_hcs_exclusions")
    public static void high_carbon_steel_slag_exclusions(GameTestHelper helper)
    {
        ServerPlayer player = fakePlayer(helper, "tfie_hcs_excl");
        player.getInventory().clearContent();

        AnvilBlockEntity inProgress = preparePlayerAnvil(helper, new BlockPos(3, 2, 3),
                "tfc:metal/ingot/pig_iron", true);
        inProgress.chooseRecipe(HIGH_CARBON_STEEL);
        inProgress.work(player, ForgeStep.HIT_LIGHT);
        helper.assertTrue(inProgress.getInventory().getStackInSlot(0)
                        .is(item("tfc:metal/ingot/pig_iron")),
                "In-progress HCS work must keep its input and must not produce slag");

        AnvilBlockEntity failed = preparePlayerAnvil(helper, new BlockPos(6, 2, 3),
                "tfc:metal/ingot/pig_iron", false);
        failed.chooseRecipe(HIGH_CARBON_STEEL);
        failed.work(player, ForgeStep.HIT_LIGHT);
        helper.assertTrue(failed.getInventory().getStackInSlot(0)
                        .is(item("tfc:metal/ingot/pig_iron")),
                "Cold failed HCS work must keep its input and must not produce slag");

        AnvilBlockEntity extraction = preparePlayerAnvil(helper, new BlockPos(9, 2, 3),
                "tfc:metal/ingot/high_carbon_steel", false);
        ItemStack extracted = extraction.getInventory().extractItem(0, 1, false);
        helper.assertTrue(extracted.is(item("tfc:metal/ingot/high_carbon_steel")),
                "Extraction-only coverage must remove the prepared HCS output");

        completePlayerRecipe(helper, new BlockPos(12, 2, 3), player,
                id("tfc:anvil/metal/ingot/steel"), "tfc:metal/ingot/high_carbon_steel",
                "tfc:metal/ingot/steel");

        int offset = 3;
        for(String metal : List.of("high_carbon_black_steel", "high_carbon_blue_steel",
                "high_carbon_red_steel"))
        {
            AnvilBlockEntity otherCarbon = preparePlayerAnvil(helper, new BlockPos(offset, 2, 7),
                    "tfc:metal/ingot/" + metal, true);
            otherCarbon.work(player, ForgeStep.HIT_LIGHT);
            helper.assertTrue(otherCarbon.getInventory().getStackInSlot(0)
                            .is(item("tfc:metal/ingot/" + metal)),
                    metal + " work must not be treated as Pig Iron to HCS completion");
            offset += 3;
        }

        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(id(
                        "tfc:anvil/high_carbon_steel_ingot")).isEmpty(),
                "The removed legacy HCS recipe ID must not be used");

        BlockPos relative = new BlockPos(10, 2, 10);
        helper.setBlock(relative, BuiltInRegistries.BLOCK.get(id("tfc:rock/anvil/granite")));
        AnvilBlockEntity anvil = helper.getBlockEntity(relative);
        ItemStack pigIron = new ItemStack(item("tfc:metal/ingot/pig_iron"));
        IHeat heat = HeatCapability.get(pigIron);
        heat.setTemperature(heat.getWorkingTemperature());
        anvil.setInventoryFromOutsideWorld(pigIron, ItemStack.EMPTY, ItemStack.EMPTY);
        anvil.chooseRecipe(HIGH_CARBON_STEEL);
        Forging forging = anvil.getMainInputForging();
        forging.addStep(ForgeStep.HIT_LIGHT, forging.target() + 6);
        forging.addStep(ForgeStep.HIT_LIGHT);
        boolean remoteComplete = anvil.workRemotely(ForgeStep.HIT_LIGHT, 0, true);
        helper.assertTrue(remoteComplete, "Prepared remote HCS work must complete normally");
        helper.assertTrue(helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                        new AABB(helper.absolutePos(relative)).inflate(2)).stream()
                        .noneMatch(entity -> entity.getItem().is(IEItems.Ingredients.SLAG.get())),
                "Remote anvil completion must not add player-only slag");
        helper.assertTrue(player.getInventory().countItem(IEItems.Ingredients.SLAG.get()) == 0,
                "In-progress, failed, extraction-only, HCS-to-Steel, other-carbon and remote work"
                        + " must not give slag");
        helper.assertTrue(helper.getLevel().getEntitiesOfClass(ItemEntity.class,
                        new AABB(helper.absolutePos(new BlockPos(8, 2, 6))).inflate(10)).stream()
                        .noneMatch(entity -> entity.getItem().is(IEItems.Ingredients.SLAG.get())),
                "Excluded anvil operations must not drop slag");
        player.getInventory().clearContent();
        helper.succeed();
    }

    private static ServerPlayer fakePlayer(GameTestHelper helper, String name)
    {
        UUID id = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
        return FakePlayerFactory.get(helper.getLevel(), new GameProfile(id, name));
    }

    private static AnvilBlockEntity preparePlayerAnvil(GameTestHelper helper, BlockPos relative,
                                                        String inputId, boolean heatInput)
    {
        helper.setBlock(relative, BuiltInRegistries.BLOCK.get(id("tfc:rock/anvil/granite")));
        AnvilBlockEntity anvil = helper.getBlockEntity(relative);
        ItemStack input = new ItemStack(item(inputId));
        if(heatInput)
        {
            IHeat heat = HeatCapability.get(input);
            heat.setTemperature(heat.getWorkingTemperature());
        }
        anvil.setInventoryFromOutsideWorld(input,
                new ItemStack(item("tfc:metal/hammer/wrought_iron")), ItemStack.EMPTY);
        return anvil;
    }

    private static void completePlayerRecipe(GameTestHelper helper, BlockPos relative, ServerPlayer player,
                                             ResourceLocation recipeId, String inputId, String outputId)
    {
        AnvilBlockEntity anvil = preparePlayerAnvil(helper, relative, inputId, true);
        anvil.chooseRecipe(recipeId);
        Forging forging = anvil.getMainInputForging();
        forging.addStep(ForgeStep.HIT_LIGHT, forging.target() + 6);
        forging.addStep(ForgeStep.HIT_LIGHT);
        anvil.work(player, ForgeStep.HIT_LIGHT);
        helper.assertTrue(anvil.getInventory().getStackInSlot(0).is(item(outputId)),
                recipeId + " must complete its normal player anvil output");
    }

    private static void verifyRecipe(GameTestHelper helper, AnvilCase expected)
    {
        RecipeHolder<AnvilRecipe> holder = recipe(helper, "tfie:anvil/" + expected.path);
        AnvilRecipe recipe = holder.value();
        ItemStack input = new ItemStack(item(expected.input));
        ItemStack output = recipe.getResultItem(helper.getLevel().registryAccess());
        helper.assertTrue(recipe.getInput().test(input), expected.path + " has the wrong input");
        helper.assertTrue(output.is(item(expected.output)) && output.getCount() == expected.count,
                expected.path + " has the wrong output");
        helper.assertTrue(recipe.getMinTier() == expected.tier && recipe.isCorrectTier(expected.tier)
                        && !recipe.isCorrectTier(expected.tier - 1),
                expected.path + " has the wrong minimum tier");
        helper.assertTrue(recipe.getRules().equals(expected.rules), expected.path + " has the wrong forge rules");
        helper.assertTrue(recipe.shouldApplyForgingBonus() == expected.bonus,
                expected.path + " has the wrong apply_bonus setting");
        helper.assertTrue(recipe.checkComplete(complete(holder, expected.tier)),
                expected.path + " must complete at its target with its required final operations");
    }

    private static SyntheticInventory complete(RecipeHolder<AnvilRecipe> holder, int tier)
    {
        ItemStack input = new ItemStack(holder.value().getInput().getItems()[0].getItem());
        SyntheticInventory inventory = new SyntheticInventory(input, tier);
        int target = holder.value().computeTarget(inventory);
        Forging forging = ForgingCapability.get(input);
        forging.setRecipe(holder, target);
        List<ForgeRule> rules = holder.value().getRules();
        if(rules.equals(drillRules()))
        {
            forging.addStep(ForgeStep.BEND, target + 18);
            forging.addStep(ForgeStep.DRAW);
            forging.addStep(ForgeStep.HIT_LIGHT);
        }
        else
        {
            ForgeStep[] steps = stepsFor(rules);
            int finalWork = steps[1].step() + steps[2].step();
            forging.addStep(steps[0], target - finalWork);
            forging.addStep(steps[1]);
            forging.addStep(steps[2]);
        }
        return inventory;
    }

    private static ForgeStep[] stepsFor(List<ForgeRule> rules)
    {
        if(rules.equals(List.of(ForgeRule.HIT_THIRD_LAST, ForgeRule.HIT_SECOND_LAST, ForgeRule.HIT_LAST)))
            return new ForgeStep[]{ForgeStep.HIT_LIGHT, ForgeStep.HIT_LIGHT, ForgeStep.HIT_LIGHT};
        if(rules.equals(List.of(ForgeRule.PUNCH_NOT_LAST, ForgeRule.HIT_ANY, ForgeRule.DRAW_ANY)))
            return new ForgeStep[]{ForgeStep.PUNCH, ForgeStep.HIT_LIGHT, ForgeStep.DRAW};
        if(rules.equals(List.of(ForgeRule.PUNCH_ANY, ForgeRule.UPSET_ANY, ForgeRule.SHRINK_LAST)))
            return new ForgeStep[]{ForgeStep.PUNCH, ForgeStep.UPSET, ForgeStep.SHRINK};
        if(rules.equals(List.of(ForgeRule.SHRINK_ANY, ForgeRule.HIT_ANY, ForgeRule.DRAW_LAST)))
            return new ForgeStep[]{ForgeStep.SHRINK, ForgeStep.HIT_LIGHT, ForgeStep.DRAW};
        if(rules.equals(List.of(ForgeRule.HIT_THIRD_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.UPSET_LAST)))
            return new ForgeStep[]{ForgeStep.HIT_LIGHT, ForgeStep.DRAW, ForgeStep.UPSET};
        if(rules.equals(List.of(ForgeRule.BEND_NOT_LAST, ForgeRule.SHRINK_NOT_LAST, ForgeRule.HIT_LAST)))
            return new ForgeStep[]{ForgeStep.BEND, ForgeStep.SHRINK, ForgeStep.HIT_LIGHT};
        return new ForgeStep[]{ForgeStep.DRAW, ForgeStep.BEND, ForgeStep.HIT_LIGHT};
    }

    @SuppressWarnings("unchecked")
    private static RecipeHolder<AnvilRecipe> recipe(GameTestHelper helper, String value)
    {
        Object holder = helper.getLevel().getRecipeManager().byKey(id(value)).orElseThrow();
        return (RecipeHolder<AnvilRecipe>) holder;
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceLocation id(String id)
    {
        return ResourceLocation.parse(id);
    }

    private static List<ForgeRule> drillRules()
    {
        return List.of(ForgeRule.BEND_THIRD_LAST, ForgeRule.DRAW_SECOND_LAST, ForgeRule.HIT_LAST);
    }

    private static AnvilCase faraday(String part)
    {
        return new AnvilCase("armor_faraday_" + part, "tfc:metal/double_sheet/steel",
                "immersiveengineering:armor_faraday_" + part, 1, 4,
                List.of(ForgeRule.PUNCH_NOT_LAST, ForgeRule.HIT_ANY, ForgeRule.DRAW_ANY), true);
    }

    private static List<AnvilCase> concat(List<AnvilCase> first, List<AnvilCase> second)
    {
        return java.util.stream.Stream.concat(first.stream(), second.stream()).toList();
    }

    private record AnvilCase(String path, String input, String output, int count, int tier,
                             List<ForgeRule> rules, boolean bonus)
    {
    }

    private record SyntheticInventory(ItemStack getItem, int getTier) implements AnvilRecipe.Inventory
    {
        @Override
        public long getSeed()
        {
            return 0;
        }

        @Override
        public int size()
        {
            return 1;
        }

        @Override
        public ItemStack getItem(int slot)
        {
            return slot == 0 ? getItem : ItemStack.EMPTY;
        }
    }
}
