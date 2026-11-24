package net.claustra01.tfie.common;

import net.claustra01.tfie.TFIE;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs
{
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TFIE.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
                    .title(Component.translatable("tfie.creative_tab.main"))
                    .icon(() -> ModItems.TREATED_WOOD_LUMBER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.TREATED_WOOD_LUMBER.get());
                        output.accept(ModItems.IE_HAMMER_HEAD.get());
                        output.accept(ModItems.WIRECUTTER_HEAD.get());
                        output.accept(ModItems.MOLD_SHEET.get());
                        output.accept(ModItems.ARC_FURNACE_BLUEPRINT.get());
                        output.accept(ModItems.DRILLHEAD_BLACK_STEEL.get());
                        output.accept(ModItems.DRILLHEAD_BLUE_STEEL.get());
                        output.accept(ModItems.DRILLHEAD_RED_STEEL.get());
                        output.accept(ModItems.DRILLHEAD_INVAR.get());
                        output.accept(ModItems.DRILLHEAD_TITANIUM.get());
                        output.accept(ModItems.DRILLHEAD_NETHERITE.get());
                        output.accept(ModItems.DRILLHEAD_TUNGSTEN_STEEL.get());
                        output.accept(ModItems.WILD_HEMP_CROP.get());
                    })
                    .build());

    private ModCreativeTabs()
    {
    }

    public static void register(IEventBus eventBus)
    {
        TABS.register(eventBus);
    }
}
