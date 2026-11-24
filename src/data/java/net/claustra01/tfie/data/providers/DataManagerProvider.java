package net.claustra01.tfie.data.providers;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.google.common.collect.ImmutableMap;
import net.claustra01.tfie.TFIE;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.util.data.DataManager;

/**
 * {@link net.dries007.tfc.data.providers.DataManagerProvider}
 */
public abstract class DataManagerProvider<T> implements DataProvider
{
    final DataManager<T> manager;
    private final CompletableFuture<HolderLookup.Provider> lookup;
    private final ImmutableMap.Builder<ResourceLocation, T> elements;
    private final PackOutput.PathProvider path;

    protected DataManagerProvider(DataManager<T> manager, PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        this.manager = manager;
        this.lookup = lookup;
        this.elements = ImmutableMap.builder();
        this.path = output.createPathProvider(PackOutput.Target.DATA_PACK, TerraFirmaCraft.MOD_ID + "/" + manager.getName());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        return lookup.thenCompose(provider -> {
            addData(provider);
            final Map<ResourceLocation, T> map = elements.buildOrThrow();
            return CompletableFuture.allOf(map.entrySet()
                .stream()
                .map(e -> DataProvider.saveStable(output, provider, manager.codec(), e.getValue(), path.json(e.getKey())))
                .toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public final String getName()
    {
        return "Data Manager (" + manager.getName() + ")";
    }

    protected final void add(String name, T value)
    {
        add(TFIE.identifier(name.toLowerCase(Locale.ROOT)), value);
    }

    protected final void add(ResourceLocation name, T value)
    {
        elements.put(name, value);
    }

    protected final void add(DataManager.Reference<T> reference, T value)
    {
        elements.put(reference.id(), value);
    }

    protected abstract void addData(HolderLookup.Provider provider);
}
