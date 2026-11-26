package net.claustra01.tfie.data.providers;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.List;

public final class BuiltinItemTags extends ItemTagsProvider
{
    private static final TagKey<Item> SALTPETER = tag("c", "dusts/saltpeter");
    private static final TagKey<Item> SULFUR = tag("c", "dusts/sulfur");
    private static final TagKey<Item> FORGE_FUEL = tag("tfc", "forge_fuel");
    private static final TagKey<Item> LEATHER_KNAPPING = tag("tfc", "leather_knapping");
    private static final TagKey<Item> MOLDS = tag("tfie", "molds");
    private static final TagKey<Item> FORBIDDEN_IN_CRATES = tag(
            "immersiveengineering", "forbidden_in_crates");
    private static final TagKey<Item> HEMP_FABRIC = tag("c", "fabric_hemp");
    private static final TagKey<Item> HEMP_FIBER = tag("c", "fiber_hemp");
    private static final TagKey<Item> STONE_BRICKS = tag("tfie", "stone_bricks_no_variants");
    private static final List<String> ROCKS = List.of(
            "granite", "diorite", "gabbro", "shale", "claystone", "limestone", "conglomerate",
            "dolomite", "chert", "chalk", "tuff", "rhyolite", "basalt", "andesite", "dacite",
            "quartzite", "slate", "phyllite", "schist", "gneiss", "marble");

    public BuiltinItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                           ExistingFileHelper existingFileHelper)
    {
        super(output, registries, CompletableFuture.completedFuture((TagsProvider.TagLookup<Block>) key -> null),
                TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries)
    {
        tag(SALTPETER).add(item("tfc:powder/saltpeter"));
        tag(SULFUR).add(item("tfc:powder/sulfur"));
        tag(FORGE_FUEL).add(item("immersiveengineering:coal_coke"));
        tag(LEATHER_KNAPPING).add(item("immersiveengineering:ersatz_leather"));
        tag(MOLDS).add(ModItems.MOLD_SHEET.get(),
                item("immersiveengineering:mold_plate"),
                item("immersiveengineering:mold_gear"),
                item("immersiveengineering:mold_rod"),
                item("immersiveengineering:mold_bullet_casing"),
                item("immersiveengineering:mold_wire"),
                item("immersiveengineering:mold_packing_4"),
                item("immersiveengineering:mold_packing_9"),
                item("immersiveengineering:mold_unpacking"));
        tag(FORBIDDEN_IN_CRATES).add(item("minecraft:white_shulker_box"));
        tag(HEMP_FABRIC).add(item("tfc:burlap_cloth"));
        tag(HEMP_FIBER).add(item("tfc:jute_fiber"));
        var stoneBricks = tag(STONE_BRICKS);
        ROCKS.forEach(rock -> stoneBricks.add(item("tfc:rock/bricks/" + rock)));
        stoneBricks.add(item("minecraft:stone_bricks"));
    }

    private static TagKey<Item> tag(String namespace, String path)
    {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }
}
