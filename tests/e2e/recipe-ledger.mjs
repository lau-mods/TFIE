import { captureJeiRecipes } from './open-jei-recipes.mjs';

const { scenario, command, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'clear @s minecraft:barrier');
  await mct(client, 'inventory', 'held', '--not-type', 'minecraft:barrier', '--wait', '10');
  await command(client, 'reload');
  await command(client, 'list');
  // A post-reload inventory packet reaches the client after its resource reload has been applied.
  command(client, 'item replace entity @s weapon.mainhand with minecraft:barrier');
  await mct(client, 'inventory', 'held', '--type', 'minecraft:barrier', '--wait', '30');
  command(client, 'clear @s minecraft:barrier');
  await mct(client, 'inventory', 'held', '--not-type', 'minecraft:barrier', '--wait', '10');
  const disabledMaterials = process.env.TFIE_E2E_EXPECTED_MOD === 'disabled-materials';
  const captures = [
    { slot: 0, key: 'u', acId: 'AC-F027-006', label: 'wool-without-crusher-use' },
    { slot: 1, key: 'u', tab: 1, acId: 'AC-F027-006',
      label: 'hematite-without-ie-crusher-use-after-reload' },
    { slot: 2, key: 'u', tab: 2, acId: 'AC-F027-006',
      label: 'steel-plate-without-arc-recycling-after-reload' },
    { slot: 3, key: 'u', tab: 2, acId: 'AC-F027-006', label: 'adopted-briquet-arc-remains' },
  ];
  if (disabledMaterials) {
    captures.push({ slot: 1, key: 'u', tab: 2, acId: 'AC-F027-006',
      label: 'disabled-hematite-without-restored-ie-arc-route' });
  }
  await captureJeiRecipes(client, [
    [0, 'minecraft:white_wool'],
    [1, 'tfc:ore/normal_hematite/granite'],
    [2, 'immersiveengineering:plate_steel'],
    [3, 'tfcm:ore_processing/briquet/native_copper'],
  ], captures);
});
