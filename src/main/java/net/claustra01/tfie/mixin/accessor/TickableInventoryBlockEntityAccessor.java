package net.claustra01.tfie.mixin.accessor;

import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TickableInventoryBlockEntity.class)
public interface TickableInventoryBlockEntityAccessor
{
    @Accessor("needsClientUpdate")
    boolean tfie$needsClientUpdate();

    @Accessor("needsClientUpdate")
    void tfie$setNeedsClientUpdate(boolean needsClientUpdate);
}
