package net.claustra01.tfie.common;

import com.mojang.serialization.Codec;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents
{
    private static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TFIE.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ARC_FURNACE_MODE =
            COMPONENTS.registerComponentType("arc_furnace_mode",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    private ModDataComponents()
    {
    }

    public static void register(IEventBus eventBus)
    {
        COMPONENTS.register(eventBus);
    }
}
