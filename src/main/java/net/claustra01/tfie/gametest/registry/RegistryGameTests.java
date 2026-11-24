package net.claustra01.tfie.gametest.registry;

import net.claustra01.tfie.TFIE;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.claustra01.tfie.common.ModBlockEntities;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.ModCreativeTabs;
import net.claustra01.tfie.common.ModDataComponents;
import net.claustra01.tfie.common.ModItems;
import net.claustra01.tfie.common.condition.FluidExistsCondition;
import net.claustra01.tfie.common.condition.LoggedRecipeCondition;
import net.claustra01.tfie.common.condition.TFIEConditions;
import net.claustra01.tfcm.common.condition.MetalEnabledCondition;
import net.claustra01.tfcm.common.condition.OreEnabledCondition;
import net.claustra01.tfcm.common.metal.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.conditions.ItemExistsCondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RegistryGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final String TEMPLATE_NAMESPACE = TFIE.MOD_ID;

    private RegistryGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void mod_identity_and_server_tick(GameTestHelper helper)
    {
        var container = ModList.get().getModContainerById(TFIE.MOD_ID).orElseThrow();
        helper.assertTrue(container.getModId().equals("tfie"), "Mod ID must be tfie");
        helper.assertTrue(container.getNamespace().equals("tfie"), "Default namespace must be tfie");
        helper.assertTrue(container.getModInfo().getVersion().toString().equals("0.1.0"),
                "Mod version must be 0.1.0");
        helper.runAfterDelay(2, helper::succeed);
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void required_and_optional_dependencies(GameTestHelper helper)
    {
        List<? extends IModInfo.ModVersion> dependencies = ModList.get().getModContainerById(TFIE.MOD_ID)
                .orElseThrow().getModInfo().getDependencies();
        Set<String> required = dependencies.stream()
                .filter(dependency -> dependency.getType() == IModInfo.DependencyType.REQUIRED)
                .map(IModInfo.ModVersion::getModId)
                .collect(Collectors.toSet());
        Set<String> optional = dependencies.stream()
                .filter(dependency -> dependency.getType() == IModInfo.DependencyType.OPTIONAL)
                .map(IModInfo.ModVersion::getModId)
                .collect(Collectors.toSet());

        helper.assertTrue(required.equals(Set.of(
                "neoforge", "minecraft", "tfc", "immersiveengineering", "tfcm", "patchouli")),
                "Exactly the baseline mods must be required");
        helper.assertTrue(optional.equals(Set.of("firmalife", "advancedtfctech")),
                "Only FirmaLife and Advanced TFC Tech may be optional metadata dependencies");
        helper.assertTrue(dependencies.stream().noneMatch(dependency ->
                        Set.of("afc", "tfcoverdims", "jei").contains(dependency.getModId())),
                "AFC, Overdims and JEI must not be metadata dependencies");

        Map<String, String> minimumVersions = Map.of(
                "tfc", "[4.2.16,)",
                "immersiveengineering", "[12.4.2-194,)",
                "tfcm", "[2.2.1,)",
                "patchouli", "[1.21.1-93,)",
                "firmalife", "[3.0.16,)",
                "advancedtfctech", "[3.3,)");
        for(IModInfo.ModVersion dependency : dependencies)
        {
            if(minimumVersions.containsKey(dependency.getModId()))
            {
                helper.assertTrue(dependency.getVersionRange().toString()
                                .equals(minimumVersions.get(dependency.getModId())),
                        dependency.getModId() + " has an incorrect minimum version");
                helper.assertTrue(dependency.getOrdering() == IModInfo.Ordering.AFTER,
                        dependency.getModId() + " must be ordered so TFIE loads after it");
            }
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void registry_contents(GameTestHelper helper)
    {
        Set<String> items = BuiltInRegistries.ITEM.keySet().stream()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::getPath)
                .collect(Collectors.toSet());
        Set<String> blocks = BuiltInRegistries.BLOCK.keySet().stream()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::getPath)
                .collect(Collectors.toSet());
        Set<String> blockEntities = BuiltInRegistries.BLOCK_ENTITY_TYPE.keySet().stream()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::getPath)
                .collect(Collectors.toSet());
        Set<String> components = BuiltInRegistries.DATA_COMPONENT_TYPE.keySet().stream()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::getPath)
                .collect(Collectors.toSet());
        Set<String> tabs = BuiltInRegistries.CREATIVE_MODE_TAB.keySet().stream()
                .filter(id -> id.getNamespace().equals(TFIE.MOD_ID))
                .map(ResourceLocation::getPath)
                .collect(Collectors.toSet());

        helper.assertTrue(items.equals(Set.of(
                "treated_wood_lumber", "tool_head/ie_hammer", "tool_head/wirecutter", "mold_sheet",
                "arc_furnace_blueprint", "drillhead_black_steel", "drillhead_blue_steel",
                "drillhead_red_steel", "drillhead_invar", "drillhead_titanium", "drillhead_netherite",
                "drillhead_tungsten_steel", "wild_crop/hemp")), "TFIE item registry differs from chapter 4");
        helper.assertTrue(blocks.equals(Set.of("crop/hemp", "dead_crop/hemp", "wild_crop/hemp")),
                "TFIE block registry differs from chapter 4");
        helper.assertTrue(blockEntities.equals(Set.of("crop")), "Only the crop block entity is registered");
        helper.assertTrue(components.equals(Set.of("arc_furnace_mode")), "Only the Arc mode component is registered");
        helper.assertTrue(tabs.equals(Set.of("main")), "Only the main creative tab is registered");
        helper.assertTrue(ModBlockEntities.CROP.get().isValid(ModBlocks.HEMP_CROP.get().defaultBlockState()),
                "Crop block entity must accept the cultivated hemp block");
        helper.assertTrue(ModCreativeTabs.MAIN.get().getDisplayName().getContents() instanceof TranslatableContents title
                        && title.getKey().equals("tfie.creative_tab.main"),
                "The main tab must use the chapter 4 translation key");
        helper.assertTrue(ModCreativeTabs.MAIN.get().getIconItem().is(ModItems.TREATED_WOOD_LUMBER.get()),
                "The main tab icon must be treated lumber");
        helper.assertTrue(!items.contains("mold_block") && items.stream().noneMatch(id ->
                        id.contains("seed") || id.contains("fiber") || id.startsWith("metal/") || id.startsWith("ore/")),
                "Removed and upstream-owned content must not be registered");
        for(String item : List.of("drillhead_iron", "drillhead_steel", "seed", "hemp_fiber"))
            helper.assertTrue(BuiltInRegistries.ITEM.containsKey(
                            ResourceLocation.fromNamespaceAndPath("immersiveengineering", item)),
                    "TFIE must use the existing Immersive Engineering item " + item);
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void item_stack_limits(GameTestHelper helper)
    {
        helper.assertTrue(new ItemStack(ModItems.TREATED_WOOD_LUMBER.get()).getMaxStackSize() == 64,
                "Treated lumber must stack to 64");
        helper.assertTrue(new ItemStack(ModItems.IE_HAMMER_HEAD.get()).getMaxStackSize() == 64,
                "Hammer heads must stack to 64");
        helper.assertTrue(new ItemStack(ModItems.WIRECUTTER_HEAD.get()).getMaxStackSize() == 64,
                "Wirecutter heads must stack to 64");
        helper.assertTrue(new ItemStack(ModItems.MOLD_SHEET.get()).getMaxStackSize() == 1,
                "Sheet molds must not stack");
        helper.assertTrue(new ItemStack(ModItems.ARC_FURNACE_BLUEPRINT.get()).getMaxStackSize() == 1,
                "Arc blueprints must not stack");
        for(var drillhead : List.of(
                ModItems.DRILLHEAD_BLACK_STEEL, ModItems.DRILLHEAD_BLUE_STEEL, ModItems.DRILLHEAD_RED_STEEL,
                ModItems.DRILLHEAD_INVAR, ModItems.DRILLHEAD_TITANIUM, ModItems.DRILLHEAD_NETHERITE,
                ModItems.DRILLHEAD_TUNGSTEN_STEEL))
            helper.assertTrue(new ItemStack(drillhead.get()).getMaxStackSize() == 1, "Drill heads must not stack");
        helper.assertTrue(new ItemStack(ModItems.WILD_HEMP_CROP.get()).getMaxStackSize() == 32,
                "The wild hemp block item must retain its TFC size limit");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void blueprint_default_mode(GameTestHelper helper)
    {
        ItemStack blueprint = ModItems.ARC_FURNACE_BLUEPRINT.get().getDefaultInstance();
        helper.assertTrue(blueprint.get(ModDataComponents.ARC_FURNACE_MODE.get()) == 1,
                "A new Arc Furnace Blueprint must start in mode 1");
        var mode = ModDataComponents.ARC_FURNACE_MODE.get();
        helper.assertTrue(mode.codec() != null && mode.streamCodec() != null,
                "Arc mode must be persistent and network synchronized");
        int saved = mode.codec().encodeStart(JsonOps.INSTANCE, 5)
                .flatMap(json -> mode.codec().parse(JsonOps.INSTANCE, json)).getOrThrow();
        helper.assertTrue(saved == 5, "Arc mode must survive codec serialization");
        RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
        mode.streamCodec().encode(buffer, 5);
        helper.assertTrue(mode.streamCodec().decode(buffer) == 5,
                "Arc mode must survive network serialization");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TEMPLATE_NAMESPACE)
    public static void conditional_loading_support(GameTestHelper helper)
    {
        LoggedRecipeCondition condition = TFIEConditions.recipe(
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, "test/conditional"),
                "test referenced content",
                "tfc",
                List.of(ResourceLocation.fromNamespaceAndPath("minecraft", "stick")),
                List.of(ResourceLocation.fromNamespaceAndPath("minecraft", "water")),
                Metal.TITANIUM,
                "hematite");
        Set<Class<?>> types = condition.conditions().stream().map(Object::getClass).collect(Collectors.toSet());

        helper.assertTrue(types.containsAll(Set.of(ModLoadedCondition.class, ItemExistsCondition.class,
                        FluidExistsCondition.class, MetalEnabledCondition.class, OreEnabledCondition.class)),
                "Shared conditions must cover mods, items, fluids, metals and ores");
        helper.assertTrue(new FluidExistsCondition(ResourceLocation.fromNamespaceAndPath("minecraft", "water"))
                        .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY),
                "Registered fluids must pass the fluid existence condition");
        helper.assertTrue(!new ItemExistsCondition(ResourceLocation.fromNamespaceAndPath("tfie", "missing"))
                        .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY),
                "Missing referenced items must fail instead of being substituted");
        for(String modId : List.of("firmalife", "advancedtfctech", "afc", "tfcoverdims"))
            helper.assertTrue(new ModLoadedCondition(modId)
                            .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY)
                            == ModList.get().isLoaded(modId),
                    "Mod condition must reflect the installed state of " + modId);
        String expectedMod = System.getProperty("tfie.gametest.expectedMod");
        if(expectedMod != null && !Set.of("all", "disabled-materials").contains(expectedMod))
            helper.assertTrue(ModList.get().isLoaded(expectedMod),
                    "The optional GameTest run must load " + expectedMod);
        for(String itemId : List.of("afc:wood/lumber/hevea", "tfcoverdims:wood/lumber/crimson"))
        {
            ResourceLocation id = ResourceLocation.parse(itemId);
            helper.assertTrue(new ItemExistsCondition(id)
                            .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY)
                            == BuiltInRegistries.ITEM.containsKey(id),
                    "Wood conditions must reflect the actual item registry for " + itemId);
            helper.assertTrue(BuiltInRegistries.ITEM.containsKey(id) == ModList.get().isLoaded(id.getNamespace()),
                    "The representative wood item must exist exactly when its mod is installed: " + itemId);
        }
        boolean disabledMaterials = "disabled-materials".equals(expectedMod);
        helper.assertTrue(new MetalEnabledCondition(Metal.TITANIUM)
                        .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY) != disabledMaterials,
                "TFCM titanium condition must reflect the active run configuration");
        helper.assertTrue(new OreEnabledCondition("hematite")
                        .test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY) != disabledMaterials,
                "TFCM hematite condition must reflect the active run configuration");

        StringWriter log = new StringWriter();
        var logger = (org.apache.logging.log4j.core.Logger) LogManager.getLogger(TFIE.class);
        var appender = WriterAppender.newBuilder().setName("tfie-gametest-conditional-log")
                .setTarget(log).setLayout(PatternLayout.createDefaultLayout()).build();
        appender.start();
        logger.addAppender(appender);
        try
        {
            var skipped = new LoggedRecipeCondition(
                    ResourceLocation.fromNamespaceAndPath("tfie", "test/missing_item"),
                    "missing referenced item", List.of(new ItemExistsCondition(
                    ResourceLocation.fromNamespaceAndPath("tfie", "missing"))));
            helper.assertTrue(!skipped.test(net.neoforged.neoforge.common.conditions.ICondition.IContext.EMPTY),
                    "Missing content must skip the recipe");
            helper.assertTrue(log.toString().contains("tfie:test/missing_item")
                            && log.toString().contains("missing referenced item"),
                    "Skipped recipe warnings must include the ID and reason");
        }
        finally
        {
            logger.removeAppender(appender);
            appender.stop();
        }
        helper.succeed();
    }

}
