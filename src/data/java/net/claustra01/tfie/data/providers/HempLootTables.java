package net.claustra01.tfie.data.providers;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.blocks.HempCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class HempLootTables implements LootTableSubProvider
{
    public static final ResourceKey<LootTable> CROP = key("blocks/crop/hemp");
    public static final ResourceKey<LootTable> DEAD_CROP = key("blocks/dead_crop/hemp");
    public static final ResourceKey<LootTable> WILD_CROP = key("blocks/wild_crop/hemp");
    public static final Set<ResourceKey<LootTable>> TABLES = Set.of(CROP, DEAD_CROP, WILD_CROP);

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output)
    {
        Block crop = ModBlocks.HEMP_CROP.get();
        output.accept(CROP, LootTable.lootTable()
                .withPool(pool(stage -> stage.add(LootItem.lootTableItem(item("immersiveengineering:hemp_fiber"))
                        .when(state(crop, properties -> properties
                                .hasProperty(((HempCropBlock) crop).getAgeProperty(), 4)
                                .hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                        .apply(SetItemCountFunction.setCount(new CropYieldProvider(
                                ConstantValue.exactly(0), UniformGenerator.between(6, 10)))))))
                .withPool(pool(stage -> stage.add(LootItem.lootTableItem(item("immersiveengineering:seed"))
                        .when(state(crop, properties -> properties
                                .hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))))));

        Block dead = ModBlocks.DEAD_HEMP_CROP.get();
        output.accept(DEAD_CROP, LootTable.lootTable()
                .withPool(pool(stage -> stage.add(AlternativesEntry.alternatives(
                        LootItem.lootTableItem(item("immersiveengineering:seed"))
                                .when(state(dead, properties -> properties
                                        .hasProperty(DeadCropBlock.MATURE, true)
                                        .hasProperty(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))),
                        LootItem.lootTableItem(item("immersiveengineering:seed"))
                                .when(state(dead, properties -> properties
                                        .hasProperty(DeadCropBlock.MATURE, false)
                                        .hasProperty(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM))))))));

        Block wild = ModBlocks.WILD_HEMP_CROP.get();
        output.accept(WILD_CROP, LootTable.lootTable()
                .withPool(pool(stage -> stage.add(LootItem.lootTableItem(item("immersiveengineering:hemp_fiber"))
                        .when(state(wild, properties -> properties
                                .hasProperty(WildCropBlock.MATURE, true)
                                .hasProperty(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))))
                .withPool(pool(stage -> stage.add(LootItem.lootTableItem(item("immersiveengineering:seed"))
                        .when(state(wild, properties -> properties
                                .hasProperty(WildDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))))));
    }

    private static LootPool.Builder pool(Consumer<LootPool.Builder> entries)
    {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(ExplosionCondition.survivesExplosion());
        entries.accept(pool);
        return pool;
    }

    private static LootItemCondition.Builder state(Block block,
                                                    Consumer<StatePropertiesPredicate.Builder> properties)
    {
        StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties();
        properties.accept(builder);
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(builder);
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static ResourceKey<LootTable> key(String path)
    {
        return ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(TFIE.MOD_ID, path));
    }
}
