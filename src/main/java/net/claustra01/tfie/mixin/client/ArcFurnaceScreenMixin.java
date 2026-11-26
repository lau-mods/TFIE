package net.claustra01.tfie.mixin.client;

import blusunrize.immersiveengineering.client.gui.ArcFurnaceScreen;
import blusunrize.immersiveengineering.client.gui.IEContainerScreen;
import blusunrize.immersiveengineering.common.gui.ArcFurnaceMenu;
import net.claustra01.tfie.common.arc.ArcFurnaceRecipeSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArcFurnaceScreen.class, remap = false)
public abstract class ArcFurnaceScreenMixin extends IEContainerScreen<ArcFurnaceMenu>
{
    protected ArcFurnaceScreenMixin(ArcFurnaceMenu menu, Inventory inventory, Component title,
                                    ResourceLocation background)
    {
        super(menu, inventory, title, background);
    }

    @Inject(method = "drawContainerBackgroundPre", at = @At("TAIL"))
    private void tfie$drawStopReason(GuiGraphics graphics, float partialTick, int mouseX, int mouseY,
                                     CallbackInfo callback)
    {
        ArcFurnaceScreen screen = (ArcFurnaceScreen)(Object)this;
        ArcFurnaceRecipeSelection.StopReason reason = ArcFurnaceRecipeSelection.stopReason(
                Minecraft.getInstance().level, slot -> screen.getMenu().getSlot(slot).getItem());
        if(reason != ArcFurnaceRecipeSelection.StopReason.NONE)
            graphics.drawCenteredString(Minecraft.getInstance().font,
                    Component.translatable(reason.translationKey()), leftPos + 88, topPos - 11, 0xFF5555);
    }
}
