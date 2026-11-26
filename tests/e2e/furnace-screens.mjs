import { captureJeiRecipes } from './open-jei-recipes.mjs';
import { formMultiblock, interactWithBlock } from './interact-with-block.mjs';
import { readFile } from 'node:fs/promises';
import { join } from 'node:path';

const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode creative');
  command(client, 'item replace entity @s hotbar.0 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=1]');
  command(client, 'item replace entity @s hotbar.1 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=5]');
  command(client, 'item replace entity @s hotbar.2 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=8]');
  command(client, 'item replace entity @s hotbar.3 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=0]');
  command(client, 'gamemode survival');
  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const inventory = await mct(client, 'gui', 'layout');
  const hotbarY = Math.max(...inventory.slots.map(slot => slot.centerY));
  const hotbar = inventory.slots.filter(slot => slot.centerY === hotbarY)
      .sort((first, second) => first.centerX - second.centerX);
  for (const [slot, mode] of [[0, 1], [1, 5], [2, 8], [3, 'invalid']]) {
    const position = hotbar[slot];
    if (!position) throw new Error(`Blueprint mode ${mode} is unavailable`);
    await mct(client, 'input', 'mouse-move', String(position.centerX), String(position.centerY));
    await mct(client, 'gui', 'layout');
    screenshot(client, 'AC-F006-012', `blueprint-mode-${mode}-tooltip`);
  }
  await mct(client, 'input', 'key', 'press', 'escape');
  command(client, 'gamemode creative');

  await mct(client, 'inventory', 'hotbar', '0');
  await mct(client, 'inventory', 'use');
  await waitFor(() => mct(client, 'inventory', 'held'), held =>
    JSON.stringify(held).includes('tfie:arc_furnace_mode') && JSON.stringify(held).includes('2'), 10_000);
  screenshot(client, 'AC-F006-012', 'blueprint-mode-switch-action-bar');

  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:manual');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:manual', '--wait', '10');
  await mct(client, 'inventory', 'use');
  const isManual = status => status.screen?.type === 'ManualScreen';
  const manualStatus = await waitFor(() => mct(client, 'status', 'all'), isManual, 10_000);
  if (!isManual(manualStatus)) throw new Error('The Engineer’s Manual did not open');
  const options = process.env.MCT_HOME
    ? await readFile(join(process.env.MCT_HOME, 'clients', client, 'minecraft', 'options.txt'), 'utf8')
    : '';
  const { width, height, scaleFactor } = await mct(client, 'screen', 'size');
  let manualScale = Math.max(1, Math.min(4, Math.floor(width * scaleFactor / 320),
      Math.floor(height * scaleFactor / 240)));
  if (manualScale % 2 !== 0) manualScale++;
  const scale = manualScale / scaleFactor;
  const manualPointer = (action, x, y) => mct(client, 'input', action,
      String(Math.floor(width / 2) + Math.round(x * scale)),
      String(Math.floor(height / 2) + Math.round(y * scale)));
  for (const [query, acId, label, nextPageClicks, controlsY, materialsY] of [
    [/^lang:zh_cn\r?$/m.test(options) ? '高炉' : 'blast furnace', 'AC-F007-005',
      'manual-blast-furnace-structure-page', 2, -39, -25],
    ['alloy kiln', 'AC-F008-004', 'manual-alloy-kiln-structure-page', 1, -39, -25],
  ]) {
    await mct(client, 'input', 'type', query);
    await manualPointer('click', -43, -75);
    for (let page = 0; page < nextPageClicks; page++) await manualPointer('click', 51, 85);
    for (const offset of [0, -16, 28]) await manualPointer('click', -53, controlsY + offset);
    await manualPointer('mouse-move', 57, materialsY);
    screenshot(client, acId, label);
    await manualPointer('click', -64, -84);
  }
  await mct(client, 'input', 'key', 'press', 'escape');

  command(client, 'fill -18 100 4 18 100 20 minecraft:smooth_stone');
  const machines = [
    ['immersiveengineering:multiblocks/arcfurnace', 'immersiveengineering:arc_furnace', -13, [2, 0, 4], [2, 1, 2]],
    ['immersiveengineering:multiblocks/blast_furnace', 'immersiveengineering:blast_furnace', -3, [1, 1, 2], [1, 1, 1]],
    ['immersiveengineering:multiblocks/improved_blast_furnace', 'immersiveengineering:advanced_blast_furnace', 4, [1, 1, 2], [1, 1, 1]],
    ['immersiveengineering:multiblocks/alloy_smelter', 'immersiveengineering:alloy_smelter', 12, [1, 1, 1], [0, 0, 1]],
  ];
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'hotbar', '0');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:hammer', '--wait', '10');
  for (const [template, , x] of machines) {
    command(client, `place template ${template} ${x} 101 10 none none 1 0`);
  }
  command(client, 'gamemode spectator');
  await camera(client, { x: 0, y: 111, z: 26, yaw: 180, pitch: 28 });
  screenshot(client, 'AC-F007-005', 'unformed-blast-furnace-structures-and-hopper');
  command(client, 'gamemode creative');
  for (const [, id, x, trigger, master] of machines) {
    const point = ([dx, dy, dz]) => [x + dx, 101 + dy, 10 + dz];
    await formMultiblock(client, camera, mct, waitFor, point(trigger), 1, point(master), id);
  }

  command(client, 'gamemode spectator');
  await camera(client, { x: 0, y: 111, z: 26, yaw: 180, pitch: 28 });
  screenshot(client, 'AC-F007-005', 'formed-blast-and-improved-blast-furnaces');
  screenshot(client, 'AC-F008-004', 'formed-alloy-kiln-with-furnace-line');
  command(client, 'gamemode creative');

  const arcMaster = [-11, 102, 12];
  command(client, 'item replace entity @s hotbar.0 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=5]');
  await interactWithBlock(client, camera, mct, arcMaster, 1);
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  let arcLayout = await mct(client, 'gui', 'layout');
  let blueprint = arcLayout.slots.at(-9);
  const mainInput = arcLayout.slots[0];
  if (!blueprint || !mainInput) throw new Error('Arc Furnace input slots are unavailable');
  await mct(client, 'input', 'click', String(blueprint.centerX), String(blueprint.centerY));
  await mct(client, 'input', 'click', String(mainInput.centerX), String(mainInput.centerY));
  screenshot(client, 'AC-F006-012', 'arc-blueprint-in-existing-main-input');
  await mct(client, 'input', 'click', String(mainInput.centerX), String(mainInput.centerY));
  await mct(client, 'input', 'click', String(blueprint.centerX), String(blueprint.centerY));
  await mct(client, 'input', 'key', 'press', 'escape');

  command(client, 'item replace entity @s hotbar.0 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=0]');
  await interactWithBlock(client, camera, mct, arcMaster, 1);
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  arcLayout = await mct(client, 'gui', 'layout');
  blueprint = arcLayout.slots.at(-9);
  await mct(client, 'input', 'click', String(blueprint.centerX), String(blueprint.centerY));
  await mct(client, 'input', 'click', String(arcLayout.slots[0].centerX), String(arcLayout.slots[0].centerY));
  await waitFor(() => mct(client, 'gui', 'layout'), layout =>
    JSON.stringify(layout).includes('Invalid Arc Furnace Blueprint mode'), 10_000);
  screenshot(client, 'AC-F006-012', 'arc-invalid-mode-stop-reason');
  await mct(client, 'input', 'key', 'press', 'escape');

  await captureJeiRecipes(client, [
    [0, 'tfc:metal/ingot/brass'],
    [1, 'tfc:metal/ingot/steel'],
    [2, 'tfcm:ore_processing/briquet/native_copper'],
    [3, 'immersiveengineering:insulating_glass'],
    [4, 'tfc:raw_iron_bloom'],
    [5, 'tfcm:metal/ingot/andesite_alloy'],
  ], [
    { slot: 0, key: 'r', tab: 2, acId: 'AC-F006-013',
      label: 'arc-brass-mode-blueprint-catalyst' },
    { slot: 1, key: 'r', tab: 1, acId: 'AC-F006-013', label: 'arc-steel-mode-blueprint-catalyst' },
    { slot: 2, key: 'u', tab: 2, acId: 'AC-F006-013',
      label: 'arc-briquet-without-blueprint' },
    { slot: 3, key: 'r', tab: 2, acId: 'AC-F006-013',
      label: 'arc-basic-glass-without-blueprint' },
    { slot: 4, key: 'r', tab: 0, acId: 'AC-F007-005', label: 'raw-iron-bloom-blast-furnace-recipe' },
    { slot: 5, key: 'r', tab: 1, acId: 'AC-F008-004',
      label: 'alloy-kiln-andesite-alloy-routes' },
    { slot: 3, key: 'r', tab: 1, acId: 'AC-F008-004', label: 'alloy-kiln-insulating-glass' },
  ]);
});
