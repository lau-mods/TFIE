package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class MaterialItemTagsProvider extends IntrinsicHolderTagsProvider<Item>
{
    public MaterialItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
            ExistingFileHelper existingFiles)
    {
        super(output, Registries.ITEM, lookup, item -> item.builtInRegistryHolder().key(), TFIE.MOD_ID,
                existingFiles);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup)
    {
        tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("immersiveengineering:forbidden_in_crates")))
                .add(Items.BUNDLE);
        tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:dusts/saltpeter")))
                .addOptional(ResourceLocation.parse("tfc:powder/saltpeter"));
        tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("c:dusts/sulfur")))
                .addOptional(ResourceLocation.parse("tfc:powder/sulfur"));
        tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("tfc:forge_fuel")))
                .addOptional(ResourceLocation.parse("immersiveengineering:coal_coke"));
        tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("tfc:leather_knapping")))
                .addOptional(ResourceLocation.parse("immersiveengineering:ersatz_leather"));
    }
}
