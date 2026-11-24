package net.claustra01.tfie.common.blocks;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.items.Items;
import net.claustra01.tfie.util.IECrop;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.common.blocks.TFCBlocks.Id;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;

@SuppressWarnings("unused")
public class Blocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TFIE.MOD_ID);
    public static final Map<IECrop, Id<Block>> CROPS = Helpers.mapOf(IECrop.class, crop ->
        registerNoItem("crop/" + crop.name(), crop::create)
    );

    public static final Map<IECrop, Id<Block>> DEAD_CROPS = Helpers.mapOf(IECrop.class, crop ->
        registerNoItem("dead_crop/" + crop.name(), crop::createDead)
    );

    public static final Map<IECrop, Id<Block>> WILD_CROPS = Helpers.mapOf(IECrop.class, crop ->
        register("wild_crop/" + crop.name(), crop::createWild)
    );

    private static <T extends Block> Id<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(Blocks.BLOCKS, Items.ITEMS, name, blockSupplier, blockItemFactory));
    }
}
