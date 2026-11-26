package net.claustra01.tfie.data.providers;

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

public final class BuiltinFluidTags extends FluidTagsProvider
{
    private static final TagKey<Fluid> USABLE_IN_BARREL = tag("usable_in_barrel");
    private static final TagKey<Fluid> USABLE_IN_WOODEN_BUCKET = tag("usable_in_wooden_bucket");
    private static final TagKey<Fluid> USABLE_IN_RED_STEEL_BUCKET = tag("usable_in_red_steel_bucket");

    private static final Fluid[] IE_FLUIDS = {
            fluid("creosote"),
            fluid("plantoil"),
            fluid("ethanol"),
            fluid("biodiesel"),
            fluid("high_power_biodiesel"),
            fluid("concrete"),
            fluid("herbicide"),
            fluid("redstone_acid"),
            fluid("acetaldehyde"),
            fluid("phenolic_resin")
    };

    public BuiltinFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                            ExistingFileHelper existingFileHelper)
    {
        super(output, registries, TFIE.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries)
    {
        tag(USABLE_IN_BARREL).add(IE_FLUIDS);
        tag(USABLE_IN_WOODEN_BUCKET).add(IE_FLUIDS);
        tag(USABLE_IN_RED_STEEL_BUCKET).add(IE_FLUIDS);
    }

    private static TagKey<Fluid> tag(String path)
    {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("tfc", path));
    }

    private static Fluid fluid(String path)
    {
        return BuiltInRegistries.FLUID.get(
                ResourceLocation.fromNamespaceAndPath("immersiveengineering", path));
    }
}
