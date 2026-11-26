package net.claustra01.tfie.common.items;

import net.claustra01.tfie.common.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public final class ArcFurnaceBlueprintItem extends Item
{
    public ArcFurnaceBlueprintItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if(hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(stack);
        if(!level.isClientSide)
        {
            int current = stack.getOrDefault(ModDataComponents.ARC_FURNACE_MODE, 1);
            int next = current >= 1 && current < 8 ? current + 1 : 1;
            stack.set(ModDataComponents.ARC_FURNACE_MODE, next);
            player.displayClientMessage(Component.translatable("message.tfie.arc_furnace_blueprint.mode", next), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag)
    {
        int mode = stack.getOrDefault(ModDataComponents.ARC_FURNACE_MODE, 1);
        tooltip.add(Component.translatable("tooltip.tfie.arc_furnace_blueprint.mode", mode)
                .withStyle(mode >= 1 && mode <= 8 ? ChatFormatting.BLUE : ChatFormatting.RED));
        tooltip.add(Component.translatable("tooltip.tfie.arc_furnace_blueprint.use")
                .withStyle(ChatFormatting.GRAY));
    }
}
