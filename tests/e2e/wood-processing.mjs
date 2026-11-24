import { formMultiblock, interactWithBlock } from './interact-with-block.mjs';

const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode creative');
  command(client, 'weather clear');
  const view = { x: 0, y: 106, z: -29, yaw: 180, pitch: 35 };
  await camera(client, view);
  command(client, 'fill -9 100 -42 9 100 -27 minecraft:smooth_quartz');
  command(client, 'item replace entity @s hotbar.4 with immersiveengineering:sawblade');
  command(client, 'item replace entity @s hotbar.5 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'hotbar', '5');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:hammer', '--wait', '10');
  const millOrigins = [-6, 1];
  for (const x of millOrigins) {
    command(client, `place template immersiveengineering:multiblocks/sawmill ${x} 101 -39 none none 1 0`);
    await formMultiblock(client, camera, mct, waitFor, [x + 2, 101, -37], 1,
        [x + 2, 102, -38], 'immersiveengineering:sawmill');
  }
  await mct(client, 'inventory', 'hotbar', '4');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:sawblade', '--wait', '10');
  await interactWithBlock(client, camera, mct, [3, 102, -38], 1);
  const bladeInstalled = held => !JSON.stringify(held).includes('immersiveengineering:sawblade');
  const held = await waitFor(() => mct(client, 'inventory', 'held'), bladeInstalled, 5_000);
  if (!bladeInstalled(held)) throw new Error('The Sawmill did not take the sawblade');
  command(client, 'setblock 0 101 -33 tfc:wood/barrel/oak[sealed=true]');
  command(client, 'gamemode spectator');
  await camera(client, view);
  command(client, 'tick freeze');
  try {
    for (const x of millOrigins) {
      command(client, `setblock ${x + 2} 103 -39 immersiveengineering:capacitor_creative`);
      command(client, `summon minecraft:item ${x + 0.5} 102.8 -37.5 {Item:{id:"tfc:wood/log/oak",count:1},PickupDelay:32767s}`);
    }
    // Let the inputs reach the belts, then hold the scene before either job can finish.
    command(client, 'tick step 42');
    await new Promise(resolve => setTimeout(resolve, 2_700));
    screenshot(client, 'AC-F013-009', 'formed-sawmills-without-and-with-blade');
  } finally {
    command(client, 'tick unfreeze');
  }

  command(client, 'gamemode creative');
  command(client, 'setblock 0 101 -31 minecraft:chest');
  for (const [slot, item] of [
    [0, 'tfc:wood/log/oak'], [1, 'tfc:wood/stripped_log/oak'],
    [2, 'tfc:wood/planks/oak'], [3, 'tfie:treated_wood_lumber'],
    [1, 'afc:wood/stripped_log/hevea'], [2, 'tfcoverdims:wood/planks/crimson'],
  ]) {
    command(client, `item replace block 0 101 -31 container.${slot} with ${item}`);
  }
  await interactWithBlock(client, camera, mct, [0, 101, -31], 1);
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const { slots } = await mct(client, 'gui', 'layout');
  const { width, height } = await mct(client, 'screen', 'size');
  for (const [slot, key, label, tab] of [
    [0, 'u', 'jei-log-sawmill-stage-outputs', 6],
    [1, 'u', 'jei-stripped-log-and-stripped-wood-choice', 6],
    [2, 'u', 'jei-planks-to-four-lumber', 4],
    [3, 'r', 'jei-treated-lumber-barrel'],
    [3, 'u', 'jei-treated-lumber-crafting', 0],
  ]) {
    const { centerX, centerY } = slots[slot];
    await mct(client, 'input', 'mouse-move', String(centerX), String(centerY));
    await mct(client, 'input', 'key', 'press', key);
    const isJei = status => status.screen?.type === 'RecipesGui';
    const status = await waitFor(() => mct(client, 'status', 'all'), isJei, 10_000);
    if (!isJei(status)) throw new Error(`JEI did not open for chest slot ${slot}`);
    if (tab !== undefined) {
      // JEI's category strip is left-aligned to its centered recipe panel.
      await mct(client, 'input', 'click', String(Math.round(width / 2 - 85 + 24 * tab)),
          String(Math.round(height * 0.15)));
      await mct(client, 'input', 'mouse-move', String(Math.round(width / 2)), String(height - 2));
    } else {
      await mct(client, 'input', 'mouse-move', String(Math.round(width / 2 - 53)),
          String(Math.round(height * 0.42)));
    }
    screenshot(client, 'AC-F013-009', label);
    await mct(client, 'input', 'key', 'press', 'escape');
  }
  await mct(client, 'input', 'key', 'press', 'escape');
});
