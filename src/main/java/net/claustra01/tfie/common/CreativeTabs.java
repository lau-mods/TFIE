package net.claustra01.tfie.common;

import java.util.function.Supplier;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.blocks.Blocks;
import net.claustra01.tfie.common.items.Items;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.TFCCreativeTabs.Id;

@SuppressWarnings({"unused", "SameParameterValue"})
public class CreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFIE.MOD_ID);

    public static final Id MAIN = register("main", () -> new ItemStack(Items.TREATED_WOOD_LUMBER.get()), CreativeTabs::fillTab);

    private static Id register(String name, Supplier<ItemStack> icon, CreativeModeTab.DisplayItemsGenerator displayItems)
    {
        final var holder = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
            .icon(icon)
            .title(Component.translatable("tfie.creative_tab." + name))
            .displayItems(displayItems)
            .build());
        return new Id(holder, displayItems);
    }

    private static void fillTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output out)
    {
        out.accept(Items.WIRECUTTER_HEAD);
        out.accept(Items.HAMMER_HEAD);
        out.accept(Items.MOLD_SHEET);
        out.accept(Items.MOLD_BLOCK);
        out.accept(Items.DRILLHEAD_BLACK_STEEL);
        out.accept(Items.DRILLHEAD_BLUE_STEEL);
        out.accept(Items.DRILLHEAD_RED_STEEL);
        out.accept(Items.TREATED_WOOD_LUMBER);
        Blocks.WILD_CROPS.values().forEach(out::accept);
    }
}
