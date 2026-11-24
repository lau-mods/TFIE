package net.claustra01.tfie.data.providers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import blusunrize.immersiveengineering.api.IETags;
import blusunrize.immersiveengineering.common.register.IEItems;
import com.google.common.base.Preconditions;
import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModTags;
import net.claustra01.tfie.common.blocks.Blocks;
import net.claustra01.tfie.common.items.Items;
import net.claustra01.tfie.data.Accessors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.VanillaItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.internal.NeoForgeItemTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;

public class BuiltinItemTags extends TagsProvider<Item> implements Accessors
{
    private static final Field TAGS_TO_COPY = Helpers.uncheck(() -> {
        final Field field = ItemTagsProvider.class.getDeclaredField("tagsToCopy");
        field.setAccessible(true);
        return field;
    });

    private final ExistingFileHelper.IResourceType resourceType;
    private final Function<HolderLookup.Provider, ItemTagsProvider> vanillaItemTags;
    private final Function<HolderLookup.Provider, ItemTagsProvider> neoItemTags;
    private final CompletableFuture<TagsProvider.TagLookup<Block>> blockTags;
    private final Map<TagKey<Block>, TagKey<Item>> tagsToCopy = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    public BuiltinItemTags(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> lookup, CompletableFuture<TagLookup<Block>> blockTags)
    {
        super(event.getGenerator().getPackOutput(), Registries.ITEM, lookup, TFIE.MOD_ID, event.getExistingFileHelper());
        this.blockTags = blockTags;
        this.resourceType = new ExistingFileHelper.ResourceType(PackType.SERVER_DATA, ".json", Registries.tagsDirPath(registryKey));
        this.vanillaItemTags = provider -> new VanillaItemTagsProvider(event.getGenerator().getPackOutput(), lookup, blockTags)
        {{
            addTags(provider);
        }};
        this.neoItemTags = provider -> {
            final var tags = new NeoForgeItemTagsProvider(event.getGenerator().getPackOutput(), lookup, blockTags, event.getExistingFileHelper());
            tags.addTags(provider);
            return tags;
        };
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        // ===== Copy BlockTags => ItemTags ===== //

        // Uses the vanilla and neo builders to establish which tags need to be copied,
        // and our tag provider knows not to copy empty tags, so it saves us some effort
        this.tagsToCopy.putAll(Helpers.uncheck(() -> TAGS_TO_COPY.get(vanillaItemTags.apply(provider))));
        this.tagsToCopy.putAll(Helpers.uncheck(() -> TAGS_TO_COPY.get(neoItemTags.apply(provider))));

        // ===== Common Tags ===== //

        tag(commonTagOf(Registries.ITEM, "fabric_hemp")).add(TFCItems.BURLAP_CLOTH);
        tag(commonTagOf(Registries.ITEM, "fiber_hemp")).add(TFCItems.JUTE_FIBER);
        tag(commonTagOf(Registries.ITEM, "dusts/saltpeter")).add(TFCItems.ORE_POWDERS.get(Ore.SALTPETER));
        tag(commonTagOf(Registries.ITEM, "dusts/sulfur")).add(TFCItems.ORE_POWDERS.get(Ore.SULFUR));
        // ===== TFC Tags ===== //

        tag(TFCTags.Items.FORGE_FUEL).add(IEItems.Ingredients.COAL_COKE);
        tag(TFCTags.Items.LEATHER_KNAPPING).add(IEItems.Ingredients.ERSATZ_LEATHER);

        // ===== IE Tags ===== //

        tag(IETags.hammers)
            .add(TFCItems.METAL_ITEMS, Metal.ItemType.HAMMER)
            .add(TFCItems.ROCK_TOOLS, RockCategory.ItemType.HAMMER);

        // ===== Addon Tags ===== //

        tag(ModTags.Items.MOLDS)
            .add(Items.MOLD_BLOCK)
            .add(Items.MOLD_SHEET)
            .add(IEItems.Molds.MOLD_PLATE)
            .add(IEItems.Molds.MOLD_GEAR)
            .add(IEItems.Molds.MOLD_ROD)
            .add(IEItems.Molds.MOLD_BULLET_CASING)
            .add(IEItems.Molds.MOLD_WIRE)
            .add(IEItems.Molds.MOLD_PACKING_4)
            .add(IEItems.Molds.MOLD_PACKING_9)
            .add(IEItems.Molds.MOLD_UNPACKING);

        tag(ModTags.Items.STONE_BRICKS_NO_VARIANTS)
            .add(TFCBlocks.ROCK_BLOCKS, Rock.BlockType.BRICKS)
            .add(net.minecraft.world.level.block.Blocks.STONE_BRICKS);
    }

    @Override
    protected ItemTagAppender tag(TagKey<Item> tag)
    {
        return new ItemTagAppender(getOrCreateRawBuilder(tag));
    }

    @Override
    protected TagBuilder getOrCreateRawBuilder(TagKey<Item> tag)
    {
        if (existingFileHelper != null) existingFileHelper.trackGenerated(tag.location(), resourceType);
        return this.builders.computeIfAbsent(tag.location(), key -> new TagBuilder()
        {
            @Override
            public TagBuilder add(TagEntry entry)
            {
                Preconditions.checkArgument(!entry.getId().equals(BuiltInRegistries.BLOCK.getDefaultKey()), "Adding air to block tag");
                return super.add(entry);
            }
        });
    }

    private void copy(TagKey<Block> blockTag)
    {
        this.tagsToCopy.put(blockTag, TagKey.create(Registries.ITEM, blockTag.location()));
    }

    private void copy(TagKey<Block> blockTag, TagKey<Item> itemTag)
    {
        this.tagsToCopy.put(blockTag, itemTag);
    }

    @Override
    protected CompletableFuture<HolderLookup.Provider> createContentsProvider()
    {
        return super.createContentsProvider().thenCombine(blockTags, (lookup, tagLookup) -> {
            tagsToCopy.forEach((blockTag, itemTag) -> {
                tagLookup.apply(blockTag)
                    .map(TagBuilder::build)
                    .filter(e -> !e.isEmpty())
                    .ifPresentOrElse(content -> {
                        // N.B. Only copy the tag if the original is non-empty. We do this since we copy all vanilla tags by default,
                        // and we only really want to include the ones that we are adding to
                        final TagBuilder builder = getOrCreateRawBuilder(itemTag);
                        content.forEach(builder::add);
                    }, () -> {
                        // Throw an error if we try and copy a TFC tag that didn't exist
                        if (blockTag.location().getNamespace().equals("tfc")) throw new IllegalArgumentException("Copying empty or missing tag " + blockTag.location());
                    });
            });
            return lookup;
        });
    }

    @SuppressWarnings({"unused", "UnusedReturnValue", "SameParameterValue"})
    static class ItemTagAppender extends TagAppender<Item> implements Accessors
    {
        ItemTagAppender(TagBuilder builder)
        {
            super(builder);
        }

        ItemTagAppender add(ItemLike... items)
        {
            for (ItemLike item : items) add(key(item));
            return this;
        }

        ItemTagAppender add(Stream<? extends ItemLike> items)
        {
            items.forEach(item -> add(key(item)));
            return this;
        }

        ItemTagAppender add(Map<?, ? extends ItemLike> items)
        {
            return add(items.values().stream());
        }

        <T> ItemTagAppender addOnly(Map<T, ? extends ItemLike> items, Predicate<T> only)
        {
            return add(items.entrySet().stream().filter(e -> only.test(e.getKey())).map(Map.Entry::getValue));
        }

        ItemTagAppender addNotWhite(String itemName)
        {
            for (DyeColor c : Helpers.DYE_COLORS_NOT_WHITE) add(itemOf(ResourceLocation.withDefaultNamespace(c.getSerializedName() + "_" + itemName)));
            return this;
        }

        ItemTagAppender addAll(Map<?, ? extends Map<?, ? extends ItemLike>> items)
        {
            return add(items.values().stream().flatMap(m -> m.values().stream()));
        }

        <T1, T2, V extends ItemLike> ItemTagAppender add(Map<T1, Map<T2, V>> items, T2 key)
        {
            return add(pivot(items, key));
        }

        ItemTagAppender add(Food... foods)
        {
            for (Food food : foods) add(TFCItems.FOOD.get(food));
            return this;
        }

        @Override
        public ItemTagAppender addTag(TagKey<Item> tag)
        {
            return (ItemTagAppender) super.addTag(tag);
        }

        @Override
        @SafeVarargs
        public final ItemTagAppender addTags(TagKey<Item>... values)
        {
            return (ItemTagAppender) super.addTags(values);
        }

        ItemTagAppender remove(ItemLike... items)
        {
            for (ItemLike item : items) remove(key(item));
            return this;
        }

        private ResourceKey<Item> key(ItemLike item)
        {
            return BuiltInRegistries.ITEM.getResourceKey(item.asItem()).orElseThrow();
        }
    }

    // ===== Added methods ===== //

}
