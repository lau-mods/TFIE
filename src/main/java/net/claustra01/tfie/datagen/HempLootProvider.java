package net.claustra01.tfie.datagen;

import blusunrize.immersiveengineering.common.register.IEItems;
import net.claustra01.tfie.common.ModBlocks;
import net.claustra01.tfie.common.crop.HempCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class HempLootProvider extends LootTableProvider
{
    public HempLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup)
    {
        super(output, Set.of(), List.of(new SubProviderEntry(HempBlocks::new, LootContextParamSets.BLOCK)),
                lookup);
    }

    private static final class HempBlocks extends BlockLootSubProvider
    {
        private HempBlocks(HolderLookup.Provider lookup)
        {
            super(Set.<Item>of(), FeatureFlags.DEFAULT_FLAGS, lookup);
        }

        @Override
        protected void generate()
        {
            Block crop = ModBlocks.HEMP_CROP.get();
            Block dead = ModBlocks.DEAD_HEMP_CROP.get();
            Block wild = ModBlocks.WILD_HEMP_CROP.get();
            add(crop, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(IEItems.Ingredients.HEMP_FIBER.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(HempCropBlock.AGE, 4)
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM)))
                                    .apply(SetItemCountFunction.setCount(new CropYieldProvider(
                                            ConstantValue.exactly(0), UniformGenerator.between(6, 10)))))
                            .when(ExplosionCondition.survivesExplosion()))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(IEItems.Misc.HEMP_SEEDS.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(crop)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM))))
                            .when(ExplosionCondition.survivesExplosion())));
            add(dead, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(AlternativesEntry.alternatives(
                            LootItem.lootTableItem(IEItems.Misc.HEMP_SEEDS.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dead)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(DeadCropBlock.MATURE, true)
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM)))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))),
                            LootItem.lootTableItem(IEItems.Misc.HEMP_SEEDS.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dead)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(DeadCropBlock.MATURE, false)
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM)))))
                    .when(ExplosionCondition.survivesExplosion())));
            add(wild, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(IEItems.Ingredients.HEMP_FIBER.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(wild)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(WildCropBlock.MATURE, true)
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM)))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(ExplosionCondition.survivesExplosion()))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(IEItems.Misc.HEMP_SEEDS.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(wild)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(DoubleCropBlock.PART,
                                                            DoubleCropBlock.Part.BOTTOM))))
                            .when(ExplosionCondition.survivesExplosion())));
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return List.of(ModBlocks.HEMP_CROP.get(), ModBlocks.DEAD_HEMP_CROP.get(),
                    ModBlocks.WILD_HEMP_CROP.get());
        }
    }
}
