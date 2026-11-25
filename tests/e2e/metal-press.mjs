import { readFileSync } from 'node:fs';
import { resolve } from 'node:path';
import { formMultiblock, interactWithBlock } from './interact-with-block.mjs';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

const pressX = -3;
const pressZ = 1;
const outputX = pressX + 3;

await scenario(async ({ clients }) => {
  const client = clients[0];
  // The AC compares the en_us mold names, so the client must run in en_us before any screenshot.
  const options = resolve(process.env.MCT_HOME, 'clients', client, 'minecraft', 'options.txt');
  if (!/^lang:en_us$/m.test(readFileSync(options, 'utf8'))) {
    throw new Error(`${options} must set lang:en_us; set MCT_CLIENT_LANGUAGE=en_us in the ${client} client environment`);
  }
  command(client, 'gamemode creative');
  command(client, 'fill -10 100 -14 10 100 8 minecraft:smooth_quartz');
  command(client, 'setblock -2 101 -8 immersiveengineering:workbench');

  // Each mold is paired with the shape it presses, plus the Molds Blueprint, in one overview shot.
  const scene = [
    ['tfie:mold_sheet', 0, -6], ['tfc:metal/sheet/copper', 2, -6],
    ['immersiveengineering:mold_plate', 0, -4], ['tfcm:metal/plate/copper', 2, -4],
    ['immersiveengineering:mold_rod', 0, -2], ['tfc:metal/rod/copper', 2, -2],
    ['immersiveengineering:mold_wire', 4, -6], ['tfcm:metal/wire/copper', 4, -4],
    ['immersiveengineering:blueprint', 4, -2],
  ];
  for (const [item, x, z] of scene) {
    const components = item === 'immersiveengineering:blueprint'
      ? ',components:{"immersiveengineering:blueprint":"molds"}' : '';
    command(client, `summon minecraft:item ${x} 101.3 ${z} {Item:{id:"${item}",count:1${components}},Age:-32768}`);
  }
  command(client, 'gamemode spectator');
  await camera(client, { x: 2, y: 104, z: -11, yaw: 0, pitch: 30 });
  await new Promise(resolve => setTimeout(resolve, 2_000));
  screenshot(client, 'AC-F003-005', 'molds-blueprint-and-pressed-shapes');
  command(client, 'gamemode creative');

  // Item names for the two similar molds and the blueprint, distinct from their models and uses.
  for (const [slot, item] of [[0, 'tfie:mold_sheet'], [1, 'immersiveengineering:mold_plate'],
    [2, 'immersiveengineering:mold_rod'], [3, 'immersiveengineering:mold_wire'],
    [4, 'immersiveengineering:blueprint[immersiveengineering:blueprint="molds"]']]) {
    command(client, `item replace entity @s hotbar.${slot} with ${item}`);
  }
  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const inventory = await mct(client, 'gui', 'layout');
  const hotbar = Array.isArray(inventory.slots) ? inventory.slots.slice(-9) : [];
  for (const [slot, label] of [[0, 'sheet-mold-name'], [1, 'plate-mold-name']]) {
    const position = hotbar[slot];
    if (!position) throw new Error(`The hotbar slot for the ${label} is unavailable`);
    await mct(client, 'input', 'mouse-move', String(position.centerX), String(position.centerY));
    await new Promise(resolve => setTimeout(resolve, 500));
    screenshot(client, 'AC-F003-005', label);
  }

  // The Engineer's Workbench JEI category shows the Molds Blueprint and the production recipe of each mold.
  for (const [slot, label] of [[0, 'sheet'], [1, 'plate'], [2, 'rod'], [3, 'wire']]) {
    const position = hotbar[slot];
    if (!position) throw new Error(`The hotbar slot for the ${label} mold is unavailable`);
    await mct(client, 'input', 'mouse-move', String(position.centerX), String(position.centerY));
    await mct(client, 'input', 'key', 'press', 'r');
    const isRecipes = value => value.screen?.type === 'RecipesGui';
    const status = await waitFor(() => mct(client, 'status', 'all'), isRecipes, 10_000);
    if (!isRecipes(status)) {
      throw new Error(`JEI did not open the Engineer's Workbench recipe for the ${label} mold`);
    }
    await new Promise(resolve => setTimeout(resolve, 1_000));
    screenshot(client, 'AC-F003-005', `workbench-${label}-mold-recipe`);
    await mct(client, 'input', 'key', 'press', 'escape');
  }
  await mct(client, 'input', 'key', 'press', 'escape');

  // A formed Metal Press that takes a two-ingot stack on its belt and inserts the result into a chest.
  command(client, 'item replace entity @s hotbar.2 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'hotbar', '2');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:hammer', '--wait', '10');
  command(client, `setblock ${outputX} 102 ${pressZ} minecraft:chest`);
  command(client, `place template immersiveengineering:multiblocks/metal_press ${pressX} 101 ${pressZ} none none 1 0`);
  await formMultiblock(client, camera, mct, waitFor, [pressX + 1, 102, pressZ], 1,
      [pressX + 1, 102, pressZ], 'immersiveengineering:metal_press');

  // Install the sheet mold by using it on the press.
  command(client, 'item replace entity @s hotbar.2 with tfie:mold_sheet');
  await mct(client, 'inventory', 'hotbar', '2');
  await mct(client, 'inventory', 'held', '--type', 'tfie:mold_sheet', '--wait', '10');
  await interactWithBlock(client, camera, mct, [pressX + 1, 102, pressZ], 1);
  const moldInstalled = held => !JSON.stringify(held).includes('tfie:mold_sheet');
  const held = await waitFor(() => mct(client, 'inventory', 'held'), moldInstalled, 5_000);
  if (!moldInstalled(held)) throw new Error('The Metal Press did not take the sheet mold');

  command(client, 'kill @e[type=item]');
  command(client, `setblock ${pressX + 1} 104 ${pressZ} immersiveengineering:capacitor_creative`);

  command(client, 'gamemode spectator');
  const view = { x: pressX + 1.5, y: 103.8, z: pressZ + 5.5, yaw: 180, pitch: 16 };
  await camera(client, view);
  command(client, 'tick freeze');
  try {
    // The sheet recipe consumes two ingots, so the stack must arrive as one two-item entity.
    // The belt takes items on its first (input) block; processTick 8 keeps the stack on that belt, in view.
    command(client, `summon minecraft:item ${pressX + 0.5} 102.2 ${pressZ + 0.5} `
        + '{Item:{id:"tfc:metal/ingot/copper",count:2},Age:-32768}');
    command(client, 'tick step 8');
    await new Promise(resolve => setTimeout(resolve, 2_000));
    screenshot(client, 'AC-F003-005', 'metal-press-sheet-processing');
  } finally {
    command(client, 'tick unfreeze');
  }

  // The press pushes its output into the chest at its output port.
  const findSheet = async () => {
    command(client, `execute if items block ${outputX} 102 ${pressZ} container.0 `
        + 'tfc:metal/sheet/copper run say sheet-output-found');
    command(client,
        'execute if entity @e[type=item,nbt={Item:{id:"tfc:metal/sheet/copper"}}] run say sheet-output-found');
    return mct(client, 'chat', 'last');
  };
  const message = await waitFor(findSheet,
      value => JSON.stringify(value).includes('sheet-output-found'), 20_000);
  if (!JSON.stringify(message).includes('sheet-output-found')) {
    await mct(client, 'chat', 'command', `data get block ${pressX + 1} 102 ${pressZ}`);
    const press = await mct(client, 'chat', 'last');
    throw new Error('The Metal Press did not put a tfc:metal/sheet/copper into the output chest: '
        + JSON.stringify(message) + ' press=' + JSON.stringify(press));
  }

});
