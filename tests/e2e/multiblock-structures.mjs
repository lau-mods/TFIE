import { readFile } from 'node:fs/promises';
import { join } from 'node:path';
import { formMultiblock } from './interact-with-block.mjs';

const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode creative');

  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:manual');
  await mct(client, 'inventory', 'hotbar', '0');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:manual', '--wait', '10');
  await mct(client, 'inventory', 'use');
  const isManual = status => status.screen?.type === 'ManualScreen';
  const status = await waitFor(() => mct(client, 'status', 'all'), isManual, 10_000);
  if (!isManual(status)) throw new Error('The Engineer’s Manual did not open');

  const options = process.env.MCT_HOME
    ? await readFile(join(process.env.MCT_HOME, 'clients', client, 'minecraft', 'options.txt'), 'utf8')
    : '';
  const { width, height, scaleFactor } = await mct(client, 'screen', 'size');
  // IE uses its own GUI scale; the first exact title match is on the book page.
  let manualScale = Math.max(1, Math.min(4, Math.floor(width * scaleFactor / 320),
      Math.floor(height * scaleFactor / 240)));
  if (manualScale % 2 !== 0) manualScale++;
  const scale = manualScale / scaleFactor;
  const manualPointer = (action, x, y) => mct(client, 'input', action,
      String(Math.floor(width / 2) + Math.round(x * scale)),
      String(Math.floor(height / 2) + Math.round(y * scale)));
  for (const [query, label, controlsY, materialsY] of [
    [/^lang:zh_cn\r?$/m.test(options) ? '电弧炉' : 'arc furnace', 'arc-furnace', -39, -25],
    ['grist mill', 'grist-mill', -41, -34],
    ['power loom', 'power-loom', -40, -30],
  ]) {
    await mct(client, 'input', 'type', query);
    await manualPointer('click', -43, -75);
    // Pause the build animation, then go up a layer and back down to restore
    // the complete unformed template instead of a single animation frame.
    for (const offset of [0, -16, 28]) {
      await manualPointer('click', -53, controlsY + offset);
    }
    await manualPointer('mouse-move', 57, materialsY);
    screenshot(client, 'AC-F012-008', `manual-${label}-structure-page`);
    await manualPointer('click', -64, -84);
  }
  await mct(client, 'input', 'key', 'press', 'escape');

  command(client, 'gamemode spectator');
  const front = { x: 0, y: 112, z: 52, yaw: 180, pitch: 40 };
  const rear = { x: -5, y: 107, z: 27, yaw: -29, pitch: 28 };
  await camera(client, front);
  command(client, 'fill -11 100 27 11 100 52 minecraft:smooth_stone');
  const machines = [
    ['immersiveengineering:multiblocks/arcfurnace', 'immersiveengineering:arc_furnace',
      -8, 4, [2, 0, 4], [2, 1, 2]],
    ['advancedtfctech:multiblocks/grist_mill', 'advancedtfctech:grist_mill',
      -1, 3, [2, 1, 1], [1, 0, 1]],
    ['advancedtfctech:multiblocks/power_loom', 'advancedtfctech:power_loom',
      5, 2, [1, 1, 4], [1, 0, 2]],
  ];
  for (const [template, , x, xSpan] of machines) {
    command(client, `place template ${template} ${x} 101 34 none none 1 0`);
    command(client, `place template ${template} ${x + xSpan} 101 43 180 none 1 0`);
  }
  await camera(client, front);
  screenshot(client, 'AC-F012-008', 'unformed-south-and-north-rotations');
  await camera(client, rear);
  screenshot(client, 'AC-F012-008', 'unformed-slab-hopper-fence-and-tfc-blocks');

  command(client, 'gamemode creative');
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:hammer', '--wait', '10');
  for (const [, id, x, xSpan, trigger, master] of machines) {
    for (const side of [1, -1]) {
      const position = ([dx, dy, dz]) => side === 1
        ? [x + dx, 101 + dy, 34 + dz]
        : [x + xSpan - dx, 101 + dy, 43 - dz];
      await formMultiblock(client, camera, mct, waitFor, position(trigger), side, position(master), id);
    }
  }

  command(client, 'gamemode spectator');
  await camera(client, front);
  screenshot(client, 'AC-F012-008', 'formed-machines-facing-south-and-north');
  await camera(client, rear);
  screenshot(client, 'AC-F012-008', 'formed-models-from-rear-quarter');
  command(client, 'gamemode creative');
});
