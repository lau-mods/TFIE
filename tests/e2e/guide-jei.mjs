import { captureJeiRecipes } from './open-jei-recipes.mjs';

const { scenario, command, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

async function useBlueprint(client, expectedMode) {
  await mct(client, 'inventory', 'use');
  const expected = `Arc Furnace mode: ${expectedMode}`;
  const actionbar = await waitFor(() => mct(client, 'hud', 'actionbar'), value =>
    value.text?.includes(expected), 10_000);
  if (!actionbar.text?.includes(expected)) {
    throw new Error(`The Blueprint did not switch to mode ${expectedMode}`);
  }
}

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode survival');
  command(client, 'item replace entity @s hotbar.0 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=1]');
  await mct(client, 'inventory', 'hotbar', '0');
  for (let expected = 2; expected <= 8; expected++) {
    await useBlueprint(client, expected);
  }
  await useBlueprint(client, 1);
  screenshot(client, 'AC-F026-003', 'blueprint-eight-modes-wrap-to-one');

  await captureJeiRecipes(client, [
    [0, 'tfc:metal/ingot/brass'],
    [1, 'tfc:metal/ingot/bronze'],
    [2, 'tfc:metal/ingot/bismuth_bronze'],
    [3, 'tfc:metal/ingot/black_bronze'],
    [4, 'tfcm:metal/ingot/constantan'],
    [5, 'tfc:metal/ingot/steel'],
    [6, 'tfcm:ore_processing/briquet/native_copper'],
    [7, 'immersiveengineering:insulating_glass'],
    [8, 'tfc:seeds/alfalfa'],
    [9, 'tfc:food/banana'],
    [10, 'minecraft:white_wool'],
    [11, 'immersiveengineering:plate_steel'],
  ], [
    { slot: 0, key: 'r', tab: 2, acId: 'AC-F026-003', label: 'copper-mode-1-brass-catalyst' },
    { slot: 1, key: 'r', tab: 2, acId: 'AC-F026-003', label: 'copper-mode-2-bronze-catalyst' },
    { slot: 2, key: 'r', tab: 1, acId: 'AC-F026-003', label: 'copper-mode-3-bismuth-bronze-catalyst' },
    { slot: 3, key: 'r', tab: 1, acId: 'AC-F026-003', label: 'copper-mode-4-black-bronze-catalyst' },
    { slot: 4, key: 'r', tab: 2, acId: 'AC-F026-003', label: 'copper-mode-5-constantan-catalyst' },
    { slot: 5, key: 'r', tab: 1, acId: 'AC-F026-003', label: 'steel-mode-1-catalyst' },
    { slot: 6, key: 'u', tab: 2, acId: 'AC-F026-003', label: 'briquet-has-no-blueprint-requirement' },
    { slot: 7, key: 'r', tab: 2, acId: 'AC-F026-003', label: 'insulating-glass-has-no-blueprint-requirement' },
    { slot: 8, key: 'u', tab: 4, hover: { x: 150, y: -20 }, acId: 'AC-F026-004',
      label: 'seed-uses-squeezer-for-80mb-plant-oil' },
    { slot: 9, key: 'u', tab: 1, hover: { x: 150, y: -20 }, acId: 'AC-F026-004',
      label: 'food-uses-fermenter-for-80mb-ethanol' },
    { slot: 10, key: 'u', acId: 'AC-F026-004', label: 'wool-has-no-removed-crusher-use' },
    { slot: 11, key: 'u', tab: 2, acId: 'AC-F026-004', label: 'arc-recycling-is-absent' },
  ]);
});
