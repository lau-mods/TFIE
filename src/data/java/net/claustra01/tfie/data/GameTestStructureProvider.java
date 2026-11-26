package net.claustra01.tfie.data;

import com.google.common.hash.Hashing;
import net.claustra01.tfie.TFIE;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class GameTestStructureProvider implements DataProvider
{
    private final PackOutput.PathProvider structures;

    public GameTestStructureProvider(PackOutput output)
    {
        structures = output.createPathProvider(PackOutput.Target.DATA_PACK, "structure");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        CompoundTag structure = new CompoundTag();
        structure.putInt("DataVersion", 3955);
        structure.put("size", coordinates(1, 1, 1));

        CompoundTag platform = new CompoundTag();
        platform.putString("Name", "minecraft:polished_andesite");
        ListTag palette = new ListTag();
        palette.add(platform);
        structure.put("palette", palette);

        CompoundTag block = new CompoundTag();
        block.put("pos", coordinates(0, 0, 0));
        block.putInt("state", 0);
        ListTag blocks = new ListTag();
        blocks.add(block);
        structure.put("blocks", blocks);
        structure.put("entities", new ListTag());

        try(ByteArrayOutputStream bytes = new ByteArrayOutputStream())
        {
            NbtIo.writeCompressed(structure, bytes);
            byte[] contents = bytes.toByteArray();
            for(String namespace : new String[]{TFIE.MOD_ID, "tfie_restart"})
                output.writeIfNeeded(structures.file(
                        ResourceLocation.fromNamespaceAndPath(namespace, "gametest/empty_platform"), "nbt"),
                        contents, Hashing.sha256().hashBytes(contents));
            return CompletableFuture.completedFuture(null);
        }
        catch(IOException exception)
        {
            return CompletableFuture.failedFuture(exception);
        }
    }

    @Override
    public String getName()
    {
        return "TFIE GameTest Structures";
    }

    private static ListTag coordinates(int x, int y, int z)
    {
        ListTag values = new ListTag();
        values.add(IntTag.valueOf(x));
        values.add(IntTag.valueOf(y));
        values.add(IntTag.valueOf(z));
        return values;
    }
}
