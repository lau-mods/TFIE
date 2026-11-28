const { scenario, command, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

async function requireScreen(client, expected, description) {
  const status = await waitFor(() => mct(client, 'status', 'all'),
      value => expected.test(value.screen?.type ?? ''), 15_000);
  if (!expected.test(status.screen?.type ?? '')) {
    throw new Error(`${description}: ${JSON.stringify(status.screen)}`);
  }
}

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode creative');
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:manual');
  await mct(client, 'inventory', 'hotbar', '0');
  await mct(client, 'inventory', 'held', '--type', 'immersiveengineering:manual', '--wait', '30');
  await mct(client, 'inventory', 'use');
  await requireScreen(client, /^ManualScreen$/, 'Engineer’s Manual did not open');

  const { width, height, scaleFactor } = await mct(client, 'screen', 'size');
  let manualScale = Math.max(1, Math.min(4, Math.floor(width * scaleFactor / 320),
      Math.floor(height * scaleFactor / 240)));
  if (manualScale % 2 !== 0) manualScale++;
  const scale = manualScale / scaleFactor;
  const pointer = (action, x, y) => mct(client, 'input', action,
      String(Math.floor(width / 2) + Math.round(x * scale)),
      String(Math.floor(height / 2) + Math.round(y * scale)));
  const entries = [
    ['Blueprint Recipes', 'blueprint'],
    ['Chemical Ingredients', 'chemicals'],
    ['High-End Drill Heads', 'drill'],
    ['Kinetic Dynamo Compatibilities', 'dynamo'],
    ['Electric Crucible?', 'external_heater'],
    ['Hemp', 'hemp'],
    ['Herbicide Additions', 'herbicide'],
    ['Jute', 'jute'],
    ['New Molds', 'mold'],
    ['Multiblock Changes', 'multiblocks'],
  ];
  for (const [title, id] of entries) {
    await mct(client, 'input', 'type', title);
    await pointer('click', -43, -75);
    await requireScreen(client, /^ManualScreen$/, `Manual entry ${title} did not open`);
    screenshot(client, id === 'external_heater' ? 'AC-F026-002' :
        id === 'dynamo' ? 'AC-F026-005' : 'AC-F026-001',
        `engineers-manual-unchanged-${id}`);
    await pointer('click', -64, -84);
  }
  await mct(client, 'input', 'key', 'press', 'escape');
});
