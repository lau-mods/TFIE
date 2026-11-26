import { captureJeiRecipes } from './open-jei-recipes.mjs';

const { scenario } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  await captureJeiRecipes(client, [
    [0, 'tfc:seeds/alfalfa'],
    [1, 'tfc:seeds/canola'],
    [2, 'tfc:food/barley_grain'],
    [3, 'immersiveengineering:ethanol_bucket'],
    [4, 'tfc:ore/bituminous_coal'],
    [5, 'tfc:sand/black'],
    [6, 'immersiveengineering:slab_concrete'],
    [7, 'immersiveengineering:clinker_brick'],
    [8, 'immersiveengineering:slag_glass'],
    [9, 'immersiveengineering:herbicide_bucket'],
    [10, 'tfc:bucket/limewater'],
    [11, 'tfc:bucket/salt_water'],
    [12, 'tfc:bucket/brine'],
    [13, 'tfc:bucket/milk_vinegar'],
    [14, 'minecraft:white_wool'],
  ], [
    { slot: 0, key: 'u', acId: 'AC-F016-004', label: 'seed-usage-squeezer-without-fermenter', tab: 3 },
    { slot: 1, key: 'u', acId: 'AC-F016-004', label: 'squeezer-seeds-to-80mb-plant-oil', tab: 3 },
    { slot: 2, key: 'u', acId: 'AC-F017-005', label: 'grain-tag-fermenter-usage', tab: 2 },
    { slot: 3, key: 'r', acId: 'AC-F017-005', label: 'base-fermenter-ethanol-without-raw-honey', fluidRecipe: true },
    { slot: 4, key: 'u', acId: 'AC-F018-010', label: 'coke-oven-coal-to-coke-and-creosote', tab: 2 },
    { slot: 5, key: 'r', acId: 'AC-F018-010', label: 'crusher-sandstone-with-saltpeter-byproduct', tab: 1 },
    { slot: 6, key: 'r', acId: 'AC-F018-010', label: 'chisel-concrete-to-two-slabs', tab: 2 },
    { slot: 7, key: 'r', acId: 'AC-F018-010', label: 'heating-bricks-to-clinker', tab: 2 },
    { slot: 8, key: 'r', acId: 'AC-F018-010', label: 'heating-slag-to-slag-glass-380c', tab: 1 },
    { slot: 9, key: 'r', acId: 'AC-F018-010', label: 'mixer-base-herbicide', fluidRecipe: true },
    { slot: 10, key: 'r', acId: 'AC-F018-010', label: 'mixer-base-limewater', fluidRecipe: true },
    { slot: 11, key: 'r', acId: 'AC-F018-010', label: 'mixer-base-salt-water', fluidRecipe: true },
    { slot: 12, key: 'r', acId: 'AC-F018-010', label: 'refinery-base-brine', fluidRecipe: true },
    { slot: 13, key: 'r', acId: 'AC-F018-010', label: 'refinery-single-base-milk-vinegar', fluidRecipe: true },
    { slot: 14, key: 'u', acId: 'AC-F018-010', label: 'wool-usage-without-crusher' },
  ]);
});
