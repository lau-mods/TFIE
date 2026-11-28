import { formMultiblock, interactWithBlock } from './interact-with-block.mjs';

const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

const sleep = milliseconds => new Promise(resolve => setTimeout(resolve, milliseconds));

await scenario(async ({ clients }) => {
  const client = clients[0];
  const serverAddress = '127.0.0.1:25595';
  const arcOrigin = [-2, 101, 10];
  const arcPoint = ([x, y, z]) => [arcOrigin[0] + x, arcOrigin[1] + y, arcOrigin[2] + z];
  const arcMaster = arcPoint([2, 1, 2]);

  const blockData = async position => {
    await mct(client, 'chat', 'clear');
    const previous = await mct(client, 'chat', 'last');
    command(client, `data get block ${position.join(' ')}`);
    const response = await waitFor(() => mct(client, 'chat', 'last'),
        value => Boolean(value?.message?.timestamp)
            && value.message.timestamp !== previous?.message?.timestamp
            && JSON.stringify(value).includes('inventory'), 5_000);
    return JSON.stringify(response);
  };
  const itemCount = (state, item) => {
    const itemAt = state.indexOf(item);
    if (itemAt < 0) return 0;
    const stack = state.slice(Math.max(0, itemAt - 160), itemAt + 160);
    const count = stack.match(/count\\?"?\s*:\s*(\d+)/);
    return count ? Number(count[1]) : 1;
  };
  const reconnect = async reason => {
    try {
      await mct(client, 'chat', 'command', `kick @s ${reason}`);
    } catch (error) {
      if (!String(error).match(/disconnect|closed|timeout/i)) throw error;
    }
    const disconnected = status => ['disconnected', 'multiplayer', 'title']
        .includes(status.screen?.category);
    await waitFor(() => mct(client, 'status', 'all'), disconnected, 15_000);
    await mct(client, 'client', 'reconnect', '--address', serverAddress);
    await mct(client, 'client', 'wait-ready', client, '--timeout', '30');
  };

  await mct(client, 'chat', 'command', 'gamerule sendCommandFeedback true');
  command(client, 'gamemode creative');
  command(client, 'fill -8 100 4 22 100 20 minecraft:smooth_stone');
  command(client, `place template immersiveengineering:multiblocks/arcfurnace ${arcOrigin.join(' ')} none none 1 0`);
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'hotbar', '0');
  await formMultiblock(client, camera, mct, waitFor, arcPoint([2, 0, 4]), 1, arcMaster,
      'immersiveengineering:arc_furnace');

  command(client, 'item replace entity @s hotbar.0 with tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=8]');
  await mct(client, 'inventory', 'hotbar', '0');
  await mct(client, 'inventory', 'use');
  const readModeOneBlueprint = async () => {
    command(client, 'execute if items entity @s weapon.mainhand '
        + 'tfie:arc_furnace_blueprint[tfie:arc_furnace_mode=1] '
        + 'run say tfie-blueprint-mode-one');
    return JSON.stringify(await mct(client, 'chat', 'last'));
  };
  const heldBlueprint = await waitFor(readModeOneBlueprint,
      state => state.includes('tfie-blueprint-mode-one'), 10_000);
  if (!heldBlueprint.includes('tfie-blueprint-mode-one')) {
    throw new Error(`Blueprint did not cycle from mode 8 to mode 1: ${heldBlueprint}`);
  }

  for (const [slot, id, count] of [
    [0, 'tfc:metal/ingot/copper', 9],
    [2, 'tfcm:ore_processing/briquet/magnetite', 1],
    [12, 'tfc:metal/ingot/zinc', 1],
    [23, 'immersiveengineering:graphite_electrode', 1],
    [24, 'immersiveengineering:graphite_electrode', 1],
    [25, 'immersiveengineering:graphite_electrode', 1]
  ]) {
    await mct(client, 'chat', 'command', `data modify block ${arcMaster.join(' ')} `
        + `componentNBT[1].inventory.Items append value {Slot:${slot},id:"${id}",count:${count}}`);
  }

  // Full output slots force the completed Briquet process to retain its reservation and waiting state.
  for (let slot = 16; slot <= 21; slot++) {
    await mct(client, 'chat', 'command', `data modify block ${arcMaster.join(' ')} `
        + `componentNBT[1].inventory.Items append value {Slot:${slot},id:"minecraft:cobblestone",count:32}`);
  }
  await interactWithBlock(client, camera, mct, arcMaster, 1);
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const layout = await mct(client, 'gui', 'layout');
  const hotbar = layout.slots.slice(-9);
  await mct(client, 'input', 'click', String(hotbar[0].centerX), String(hotbar[0].centerY));
  await mct(client, 'input', 'click', String(layout.slots[1].centerX), String(layout.slots[1].centerY));

  const energyStart = arcPoint([1, 1, -1]);
  const energyEnd = arcPoint([3, 1, -1]);
  command(client, `fill ${energyStart.join(' ')} ${energyEnd.join(' ')} immersiveengineering:capacitor_creative`);

  const hasBothProcesses = value => value.includes('tfie:arcfurnace/alloy/brass')
      && value.includes('tfie:arcfurnace/briquet/magnetite')
      && value.includes('process_processTick');
  const running = await waitFor(() => blockData(arcMaster), hasBothProcesses, 15_000);
  if (!hasBothProcesses(running)) throw new Error(`Arc did not start both server processes: ${running}`);
  screenshot(client, 'AC-F028-003', 'server-selected-alloy-and-briquet-processes');
  await mct(client, 'input', 'key', 'press', 'escape');

  await sleep(7_000);
  command(client, `fill ${energyStart.join(' ')} ${energyEnd.join(' ')} minecraft:air`);
  const beforeReconnect = await waitFor(() => blockData(arcMaster),
      state => state.includes('energy: 0'), 15_000);
  for (const expected of ['tfie:arcfurnace/alloy/brass', 'tfie:arcfurnace/briquet/magnetite',
    'tfie:arc_furnace_blueprint', 'mode=1|', 'tfc:metal/ingot/copper',
    'tfc:metal/ingot/zinc', 'tfcm:ore_processing/briquet/magnetite', 'process_inputAmounts']) {
    if (!beforeReconnect.includes(expected)) {
      throw new Error(`Arc server state before reconnect lacks ${expected}: ${beforeReconnect}`);
    }
  }

  await reconnect('TFIE Arc persistence verification');
  const afterReconnect = await blockData(arcMaster);
  const progress = state => [...state.matchAll(/process_processTick\D+(\d+)/g)].map(match => match[1]);
  if (!hasBothProcesses(afterReconnect)
      || !afterReconnect.includes('mode=1|')
      || JSON.stringify(progress(afterReconnect)) !== JSON.stringify(progress(beforeReconnect))) {
    throw new Error(`Arc recipes or progress changed without FE across reconnect: before=${beforeReconnect} after=${afterReconnect}`);
  }

  // Releasing one output slot commits the waiting Briquet process once, with its server-created heat component.
  await mct(client, 'chat', 'command', `data remove block ${arcMaster.join(' ')} `
      + 'componentNBT[1].inventory.Items[{Slot:21}]');
  command(client, `fill ${energyStart.join(' ')} ${energyEnd.join(' ')} immersiveengineering:capacitor_creative`);
  const hasHeatedCastIron = value => value.includes('tfc:metal/ingot/cast_iron')
      && value.includes('tfc:heat') && value.includes('count: 1, Slot: 21');
  const committed = await waitFor(() => blockData(arcMaster), hasHeatedCastIron, 15_000);
  if (!hasHeatedCastIron(committed)) {
    throw new Error(`Waiting Arc process did not commit exactly one heated output: ${committed}`);
  }

  await interactWithBlock(client, camera, mct, arcMaster, 1);
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const resumedLayout = await mct(client, 'gui', 'layout');
  const output = resumedLayout.slots[21];
  if (output) {
    await mct(client, 'input', 'mouse-move', String(output.centerX), String(output.centerY));
    await sleep(500);
  }
  screenshot(client, 'AC-F028-004', 'saved-waiting-process-resumed-with-heated-output');
  await mct(client, 'input', 'key', 'press', 'escape');

  // Run the probabilistic Crusher recipe on the dedicated server; this is an actual byproduct, not JEI data.
  command(client, 'kill @e[type=minecraft:item]');
  const crusherOrigin = [12, 101, 10];
  const crusherPoint = ([x, y, z]) => [crusherOrigin[0] + x, crusherOrigin[1] + y, crusherOrigin[2] + z];
  const crusherMaster = crusherPoint([2, 1, 1]);
  command(client, `place template immersiveengineering:multiblocks/crusher ${crusherOrigin.join(' ')} none none 1 0`);
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:hammer');
  await mct(client, 'inventory', 'hotbar', '0');
  await formMultiblock(client, camera, mct, waitFor, crusherPoint([2, 1, 2]), 1,
      crusherMaster, 'immersiveengineering:crusher');
  command(client, `setblock ${crusherPoint([4, 2, 1]).join(' ')} immersiveengineering:capacitor_creative`);
  for (let index = 0; index < 12; index++) {
    const input = crusherPoint([2.5, 2.2, 1.5]);
    command(client, `summon minecraft:item ${input.join(' ')} `
        + '{Item:{id:"tfc:raw_sandstone/black",count:1},Age:-32768}');
    await sleep(200);
  }
  const byproductExists = async () => {
    command(client, 'execute if entity @e[type=minecraft:item,nbt={Item:{id:"tfc:powder/saltpeter"}}] '
        + 'run say tfie-seeded-byproduct-found');
    return JSON.stringify(await mct(client, 'chat', 'last'));
  };
  const byproduct = await waitFor(byproductExists,
      value => value.includes('tfie-seeded-byproduct-found'), 20_000);
  if (!byproduct.includes('tfie-seeded-byproduct-found')) {
    throw new Error('The server did not produce the seeded Crusher byproduct in twelve processes');
  }
  command(client, `setblock ${crusherPoint([4, 2, 1]).join(' ')} minecraft:air`);
  await sleep(1_000);
  await mct(client, 'chat', 'command',
      'data get entity @e[type=minecraft:item,nbt={Item:{id:"tfc:powder/saltpeter"}},limit=1] Item');
  const byproductBeforeReconnect = JSON.stringify(await mct(client, 'chat', 'last'));
  await reconnect('TFIE byproduct authority verification');
  await mct(client, 'chat', 'command',
      'data get entity @e[type=minecraft:item,nbt={Item:{id:"tfc:powder/saltpeter"}},limit=1] Item');
  const byproductAfterReconnect = JSON.stringify(await mct(client, 'chat', 'last'));
  if (itemCount(byproductAfterReconnect, 'tfc:powder/saltpeter')
      !== itemCount(byproductBeforeReconnect, 'tfc:powder/saltpeter')) {
    throw new Error(`Server byproduct changed across reconnect: before=${byproductBeforeReconnect} `
        + `after=${byproductAfterReconnect}`);
  }
  command(client, 'tp @e[type=minecraft:item,nbt={Item:{id:"tfc:sand/black"}}] 14.5 101.3 17');
  command(client, 'tp @e[type=minecraft:item,nbt={Item:{id:"tfc:powder/saltpeter"}}] 15.5 101.3 17');
  command(client, 'gamemode spectator');
  await camera(client, { x: 15, y: 103, z: 20, yaw: 180, pitch: 24 });
  await sleep(1_000);
  screenshot(client, 'AC-F028-003', 'server-produced-byproduct-after-reconnect');
  command(client, 'gamemode creative');

  command(client, 'item replace entity @s hotbar.0 with tfie:mold_sheet');
  command(client, 'item replace entity @s hotbar.1 with tfie:arc_furnace_blueprint');
  command(client, 'item replace entity @s hotbar.2 with immersiveengineering:mold_plate');
  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  screenshot(client, 'AC-F028-006', 'new-world-sheet-mold-and-arc-blueprint');
  await mct(client, 'input', 'key', 'press', 'escape');
});
