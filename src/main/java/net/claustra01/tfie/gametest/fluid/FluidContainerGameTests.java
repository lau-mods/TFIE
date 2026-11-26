package net.claustra01.tfie.gametest.fluid;

import blusunrize.immersiveengineering.common.crafting.fluidaware.IngredientFluidStack;
import blusunrize.immersiveengineering.common.crafting.fluidaware.ShapelessFluidAwareRecipe;
import net.claustra01.tfie.TFIE;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.config.TFCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@GameTestHolder(TFIE.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FluidContainerGameTests
{
    private static final String TEMPLATE = "gametest/empty_platform";
    private static final List<String> IE_FLUIDS = List.of(
            "creosote", "plantoil", "ethanol", "biodiesel", "high_power_biodiesel",
            "concrete", "herbicide", "redstone_acid", "acetaldehyde", "phenolic_resin");
    private static final List<TagKey<Fluid>> CONTAINER_TAGS = List.of(
            fluidTag("tfc:usable_in_barrel"),
            fluidTag("tfc:usable_in_wooden_bucket"),
            fluidTag("tfc:usable_in_red_steel_bucket"));

    private FluidContainerGameTests()
    {
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void fluid_tags_and_container_io(GameTestHelper helper)
    {
        ItemStack woodenBucket = item("tfc:wooden_bucket").getDefaultInstance();
        ItemStack redSteelBucket = item("tfc:metal/bucket/red_steel").getDefaultInstance();
        IFluidHandlerItem woodenHandler = itemHandler(woodenBucket);
        IFluidHandlerItem redSteelHandler = itemHandler(redSteelBucket);
        helper.assertTrue(woodenHandler.getTankCapacity(0) == TFCConfig.SERVER.woodenBucketCapacity.get(),
                "TFIE must not change the Wooden Bucket capacity");
        helper.assertTrue(redSteelHandler.getTankCapacity(0) == TFCConfig.SERVER.metalBucketCapacity.get(),
                "TFIE must not change the Red Steel Bucket capacity");

        BlockPos barrelPos = helper.absolutePos(new BlockPos(1, 2, 1));
        helper.getLevel().setBlockAndUpdate(barrelPos, block("tfc:wood/barrel/oak").defaultBlockState());
        BarrelBlockEntity barrel = (BarrelBlockEntity)helper.getLevel().getBlockEntity(barrelPos);
        IFluidHandler barrelHandler = barrel.getSidedFluidInventory(null);
        helper.assertTrue(barrelHandler.getTankCapacity(0) == TFCConfig.SERVER.barrelCapacity.get(),
                "TFIE must not change the Barrel capacity");

        for(String path : IE_FLUIDS)
        {
            Fluid fluid = fluid("immersiveengineering:" + path);
            for(TagKey<Fluid> tag : CONTAINER_TAGS)
                helper.assertTrue(fluid.defaultFluidState().is(tag), fluid + " is missing from " + tag.location());
            assertFillAndDrain(helper, itemHandler(item("tfc:wooden_bucket").getDefaultInstance()), fluid,
                    TFCConfig.SERVER.woodenBucketCapacity.get());
            assertFillAndDrain(helper, itemHandler(item("tfc:metal/bucket/red_steel").getDefaultInstance()), fluid,
                    TFCConfig.SERVER.metalBucketCapacity.get());
            assertFillAndDrain(helper, barrelHandler, fluid, TFCConfig.SERVER.barrelCapacity.get());
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void fluid_ingredient_candidates_and_partial_return(GameTestHelper helper)
    {
        Fluid creosote = fluid("immersiveengineering:creosote");
        int bucketAmount = Math.min(TFCConfig.SERVER.woodenBucketCapacity.get(),
                TFCConfig.SERVER.metalBucketCapacity.get());
        IngredientFluidStack fullBucketIngredient = new IngredientFluidStack(
                SizedFluidIngredient.of(creosote, bucketAmount));
        Set<Item> candidates = fullBucketIngredient.getItems()
                .map(ItemStack::getItem)
                .collect(Collectors.toSet());
        helper.assertTrue(candidates.contains(creosote.getBucket()),
                "The tagged Minecraft Bucket must remain a display candidate");
        helper.assertTrue(candidates.contains(item("tfc:wooden_bucket")),
                "The Wooden Bucket must be a candidate when it accepts enough Creosote");
        helper.assertTrue(candidates.contains(item("tfc:metal/bucket/red_steel")),
                "The Red Steel Bucket must be a candidate when it accepts enough Creosote");
        helper.assertTrue(!candidates.contains(item("tfc:metal/bucket/blue_steel")),
                "Tag membership alone must not make the Blue Steel Bucket accept Creosote");
        helper.assertTrue(!candidates.contains(item("tfc:ceramic/jug")),
                "A container that rejects the fluid or lacks capacity must not be displayed");

        IngredientFluidStack oversized = new IngredientFluidStack(SizedFluidIngredient.of(creosote,
                TFCConfig.SERVER.metalBucketCapacity.get() + 1));
        helper.assertTrue(oversized.getItems().noneMatch(stack -> stack.is(item("tfc:metal/bucket/red_steel"))),
                "A container below the required capacity must not be displayed");

        int required = Math.max(1, TFCConfig.SERVER.metalBucketCapacity.get() / 4);
        IngredientFluidStack ingredient = new IngredientFluidStack(SizedFluidIngredient.of(creosote, required));
        ItemStack displayCandidate = ingredient.getItems()
                .filter(stack -> stack.is(item("tfc:metal/bucket/red_steel")))
                .findFirst()
                .orElseThrow();
        FluidStack displayContent = contained(displayCandidate);
        helper.assertTrue(displayContent.is(creosote) && displayContent.getAmount() == required,
                "The display candidate must contain exactly the required fluid amount");
        ItemStack filled = filled(item("tfc:metal/bucket/red_steel"), creosote,
                TFCConfig.SERVER.metalBucketCapacity.get());
        helper.assertTrue(ingredient.test(filled), "The ingredient must match the fluid type and amount");
        helper.assertTrue(!ingredient.test(filled(item("tfc:metal/bucket/red_steel"),
                        fluid("immersiveengineering:ethanol"), TFCConfig.SERVER.metalBucketCapacity.get())),
                "The same container item with the wrong fluid must not match");
        helper.assertTrue(!new IngredientFluidStack(SizedFluidIngredient.of(creosote,
                        TFCConfig.SERVER.metalBucketCapacity.get() + 1)).test(filled),
                "The right fluid below the required amount must not match");

        ShapelessFluidAwareRecipe recipe = new ShapelessFluidAwareRecipe("",
                NonNullList.of(Ingredient.EMPTY, new Ingredient(ingredient)),
                Items.BRICK.getDefaultInstance());
        CraftingInput input = CraftingInput.of(1, 1, List.of(filled));
        helper.assertTrue(recipe.matches(input, helper.getLevel()),
                "The fluid-aware craft must accept the filled TFC container");
        ItemStack returned = recipe.getRemainingItems(input).getFirst();
        FluidStack remainder = contained(returned);
        helper.assertTrue(returned.is(item("tfc:metal/bucket/red_steel"))
                        && remainder.is(creosote)
                        && remainder.getAmount() == TFCConfig.SERVER.metalBucketCapacity.get() - required,
                "The craft must return the container with only the required amount drained");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = TFIE.MOD_ID)
    public static void fixed_bucket_recipes_reject_tfc_containers(GameTestHelper helper)
    {
        for(String id : List.of("immersiveengineering:crafting/chemthrower",
                "immersiveengineering:crafting/jerrycan"))
        {
            Object value = helper.getLevel().getRecipeManager().byKey(ResourceLocation.parse(id))
                    .orElseThrow().value();
            helper.assertTrue(value instanceof ShapedRecipe, id + " must remain a shaped item recipe");
            ShapedRecipe recipe = (ShapedRecipe)value;
            List<ItemStack> valid = recipe.getIngredients().stream()
                    .map(FluidContainerGameTests::firstMatch)
                    .toList();
            helper.assertTrue(recipe.matches(CraftingInput.of(recipe.getWidth(), recipe.getHeight(), valid),
                            helper.getLevel()), id + " must match its fixed Minecraft Bucket input");
            for(String tfcBucket : List.of("tfc:wooden_bucket", "tfc:metal/bucket/blue_steel",
                    "tfc:metal/bucket/red_steel"))
            {
                List<ItemStack> replaced = valid.stream()
                        .map(stack -> stack.is(Items.BUCKET) ? item(tfcBucket).getDefaultInstance() : stack.copy())
                        .toList();
                helper.assertTrue(!recipe.matches(CraftingInput.of(recipe.getWidth(), recipe.getHeight(), replaced),
                                helper.getLevel()), id + " must reject " + tfcBucket);
            }
        }
        for(String id : List.of("tfie:crafting/chemthrower_tfc", "tfie:crafting/jerrycan_tfc"))
            helper.assertTrue(helper.getLevel().getRecipeManager().byKey(ResourceLocation.parse(id)).isEmpty(),
                    id + " must not be added");
        helper.succeed();
    }

    private static void assertFillAndDrain(GameTestHelper helper, IFluidHandler handler, Fluid fluid, int capacity)
    {
        int filled = handler.fill(new FluidStack(fluid, capacity), FluidAction.EXECUTE);
        helper.assertTrue(filled == capacity, fluid + " did not fill the container to its normal capacity");
        FluidStack drained = handler.drain(capacity, FluidAction.EXECUTE);
        helper.assertTrue(drained.is(fluid) && drained.getAmount() == capacity,
                fluid + " did not drain normally from the container");
    }

    private static ItemStack filled(Item item, Fluid fluid, int amount)
    {
        IFluidHandlerItem handler = itemHandler(item.getDefaultInstance());
        int filled = handler.fill(new FluidStack(fluid, amount), FluidAction.EXECUTE);
        if(filled != amount)
            throw new IllegalStateException(item + " accepted only " + filled + " mB of " + fluid);
        return handler.getContainer();
    }

    private static FluidStack contained(ItemStack stack)
    {
        IFluidHandlerItem handler = itemHandler(stack);
        return handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
    }

    private static IFluidHandlerItem itemHandler(ItemStack stack)
    {
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if(handler == null)
            throw new IllegalStateException(stack + " has no fluid capability");
        return handler;
    }

    private static ItemStack firstMatch(Ingredient ingredient)
    {
        return ingredient.isEmpty() ? ItemStack.EMPTY : ingredient.getItems()[0].copy();
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static Block block(String id)
    {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }

    private static Fluid fluid(String id)
    {
        return BuiltInRegistries.FLUID.get(ResourceLocation.parse(id));
    }

    private static TagKey<Fluid> fluidTag(String id)
    {
        return TagKey.create(Registries.FLUID, ResourceLocation.parse(id));
    }
}
