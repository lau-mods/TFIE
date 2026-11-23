import { resolve } from 'node:path';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];

  command(client, 'gamemode creative');
  command(client, 'time set noon');
  command(client, 'weather clear');
  try {
    mct(client, 'gui', 'close');
  } catch (error) {
    if (!String(error).includes('GUI_NOT_OPEN')) throw error;
  }
  command(client, 'kill @e[type=item]');
  command(client, 'fill -3 101 -7 3 101 3 minecraft:smooth_quartz');
  command(client, 'item replace entity @s hotbar.0 with tfie:treated_wood_lumber');
  command(client, 'item replace entity @s hotbar.1 with tfie:tool_head/ie_hammer');
  command(client, 'item replace entity @s hotbar.2 with tfie:tool_head/wirecutter');
  command(client, 'summon item -1.5 102.2 -1.5 {Item:{id:"tfie:treated_wood_lumber",count:1},Age:-32768}');
  command(client, 'summon item 0 102.2 -1.5 {Item:{id:"tfie:tool_head/ie_hammer",count:1},Age:-32768}');
  command(client, 'summon item 1.5 102.2 -1.5 {Item:{id:"tfie:tool_head/wirecutter",count:1},Age:-32768}');
  await camera(client, { x: 0, y: 102, z: -5, yaw: 0, pitch: 28 });

  const entities = await waitFor(() => mct(client, 'entity', 'list', '--radius', '10'),
      value => (JSON.stringify(value).match(/minecraft:item/g) ?? []).length >= 3);
  if ((JSON.stringify(entities).match(/minecraft:item/g) ?? []).length < 3) {
    throw new Error('The three dropped TFIE items are missing');
  }
  await new Promise(resolve => setTimeout(resolve, 11_000));
  for (const [slot, label] of [['1', 'treated-lumber'], ['2', 'hammer-head'], ['3', 'wirecutter-head']]) {
    await mct(client, 'input', 'key', 'press', slot);
    await camera(client, { x: 0, y: 102, z: -5, yaw: 0, pitch: 28 });
    const rotation = await waitFor(() => mct(client, 'rotation', 'get'),
        value => Math.abs(value.pitch - 28) < 2);
    if (Math.abs(rotation.pitch - 28) >= 2) throw new Error('The camera did not face the dropped items');
    await new Promise(resolve => setTimeout(resolve, 4_000));
    screenshot(client, 'AC-F001-004', `held-${label}-and-dropped-items`);
  }

  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const layout = await mct(client, 'gui', 'layout');
  const left = layout.guiLeft;
  const top = layout.guiTop;
  const height = layout.backgroundHeight;
  if (![left, top, height].every(Number.isFinite)) throw new Error('Creative inventory bounds are unavailable');

  let tab = null;
  for (const y of [top - 14, top + height + 14]) {
    for (let index = 0; index < 7; index++) {
      const x = left + 14 + 28 * index;
      await mct(client, 'input', 'click', String(x), String(y));
      const snapshot = await mct(client, 'gui', 'snapshot');
      const slots = snapshot.slots ?? [];
      if (['tfie:treated_wood_lumber', 'tfie:tool_head/ie_hammer', 'tfie:tool_head/wirecutter']
          .every((id, slot) => JSON.stringify(slots[slot]?.item ?? {}).includes(id))) {
        tab = { x, y };
        break;
      }
    }
    if (tab) break;
  }
  if (!tab) throw new Error('The TFIE creative tab did not open with its three registered items');
  await mct(client, 'input', 'mouse-move', String(tab.x), String(tab.y));
  screenshot(client, 'AC-F001-004', 'tfie-tab-name-and-icon');
  await mct(client, 'input', 'mouse-move', String(left + 100), String(top + 100));
  screenshot(client, 'AC-F001-004', 'tfie-tab-title-and-icon');
  const itemLayout = await mct(client, 'gui', 'layout');
  for (const [slot, label] of [[0, 'treated-lumber'], [1, 'hammer-head'], [2, 'wirecutter-head']]) {
    const position = itemLayout.slots[slot];
    await mct(client, 'input', 'mouse-move', String(position.centerX), String(position.centerY));
    screenshot(client, 'AC-F001-004', `tab-tooltip-${label}`);
  }
  await mct(client, 'input', 'key', 'press', 'escape');
});
