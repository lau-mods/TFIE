package net.claustra01.tfie.data.tags;

import net.claustra01.tfie.TFIE;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class TreatedLumberFluidTags extends FluidTagsProvider
{
    private static final TagKey<Fluid> USABLE_IN_BARREL = TagKey.create(Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath("tfc", "usable_in_barrel"));

    public TreatedLumberFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                                  ExistingFileHelper existingFileHelper)
    {
        super(output, registries, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries)
    {
        tag(USABLE_IN_BARREL).add(BuiltInRegistries.FLUID.get(
                ResourceLocation.fromNamespaceAndPath("immersiveengineering", "creosote")));
    }
}
