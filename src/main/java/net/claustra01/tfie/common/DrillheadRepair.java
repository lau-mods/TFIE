package net.claustra01.tfie.common;

import blusunrize.immersiveengineering.api.tool.IDrillHead;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

import java.util.Map;

public final class DrillheadRepair
{
    private static final ResourceLocation IRON_HEAD = id("immersiveengineering", "drillhead_iron");
    private static final TagKey<Item> IRON_INGOTS = ingots("iron");
    private static final TagKey<Item> CAST_IRON_INGOTS = ingots("cast_iron");
    private static final Map<ResourceLocation, TagKey<Item>> REPAIR_MATERIALS = Map.ofEntries(
            Map.entry(IRON_HEAD, ingots("wrought_iron")),
            Map.entry(id("immersiveengineering", "drillhead_steel"), ingots("steel")),
            Map.entry(id("tfie", "drillhead_black_steel"), ingots("black_steel")),
            Map.entry(id("tfie", "drillhead_blue_steel"), ingots("blue_steel")),
            Map.entry(id("tfie", "drillhead_red_steel"), ingots("red_steel")),
            Map.entry(id("tfie", "drillhead_invar"), ingots("invar")),
            Map.entry(id("tfie", "drillhead_titanium"), ingots("titanium")),
            Map.entry(id("tfie", "drillhead_netherite"), ingots("netherite")),
            Map.entry(id("tfie", "drillhead_tungsten_steel"), ingots("tungsten_steel")));

    private DrillheadRepair()
    {
    }

    public static void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ResourceLocation headId = BuiltInRegistries.ITEM.getKey(left.getItem());
        TagKey<Item> repairMaterial = REPAIR_MATERIALS.get(headId);
        if(repairMaterial == null)
            return;

        if(!(left.getItem() instanceof IDrillHead head) || head.getHeadDamage(left) <= 0 || right.isEmpty())
            return;
        if(!right.is(repairMaterial))
        {
            if(headId.equals(IRON_HEAD) && (right.is(IRON_INGOTS) || right.is(CAST_IRON_INGOTS)))
            {
                event.setOutput(ItemStack.EMPTY);
                event.setCost(0);
                event.setMaterialCost(0);
                event.setCanceled(true);
            }
            return;
        }

        ItemStack output = left.copy();
        int consumed = 0;
        int repaired = Math.min(head.getHeadDamage(output), head.getMaximumHeadDamage(output) / 4);
        int lastRepair = 0;
        while(repaired > 0 && consumed < right.getCount())
        {
            head.damageHead(output, -repaired);
            lastRepair = repaired;
            consumed++;
            repaired = Math.min(head.getHeadDamage(output), head.getMaximumHeadDamage(output) / 4);
        }

        int cost = Math.max(1, lastRepair / 200);
        String name = event.getName();
        if(name == null || name.isEmpty())
        {
            if(output.has(DataComponents.CUSTOM_NAME))
            {
                output.remove(DataComponents.CUSTOM_NAME);
                cost += 5;
            }
        }
        else if(!name.equals(left.getHoverName().getString()))
        {
            cost += 5;
            if(left.has(DataComponents.CUSTOM_NAME))
                cost += 2;
            output.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        }

        event.setOutput(output);
        event.setCost(cost);
        event.setMaterialCost(consumed);
    }

    private static TagKey<Item> ingots(String metal)
    {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/" + metal));
    }

    private static ResourceLocation id(String namespace, String path)
    {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
