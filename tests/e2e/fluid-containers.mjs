const { scenario, command, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode creative');
  try {
    await mct(client, 'gui', 'close');
  } catch (error) {
    if (!String(error).includes('GUI_NOT_OPEN')) throw error;
  }
  const items = ['immersiveengineering:capacitor_lv',
    'immersiveengineering:toolupgrade_drill_lube', 'immersiveengineering:chemthrower'];
  for (const [slot, item] of items.entries()) {
    command(client, `item replace entity @s hotbar.${slot} with ${item}`);
  }

  await new Promise(resolve => setTimeout(resolve, 11_000));
  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const inventory = await mct(client, 'gui', 'layout');
  const snapshot = await mct(client, 'gui', 'snapshot');
  const positions = items.map(item => {
    const index = (snapshot.slots ?? []).findLastIndex(slot =>
      JSON.stringify(slot.item ?? {}).includes(item));
    return index >= 0 ? inventory.slots?.[index] : null;
  });

  const openRecipe = async slot => {
    const position = positions[slot];
    if (!position) throw new Error(`${items[slot]} is unavailable in the player inventory`);
    await mct(client, 'input', 'mouse-move', String(position.centerX), String(position.centerY));
    await new Promise(resolve => setTimeout(resolve, 250));
    await mct(client, 'input', 'key', 'press', 'r');
    const isRecipe = value => value.screen?.type === 'RecipesGui';
    const status = await waitFor(() => mct(client, 'status', 'all'), isRecipe, 10_000);
    if (!isRecipe(status)) throw new Error(`JEI did not open the recipe in hotbar slot ${slot}`);
  };

  await openRecipe(0);
  for (const [delay, label] of [[250, 'redstone-acid-candidate-a'],
    [1_100, 'redstone-acid-candidate-b'], [1_100, 'redstone-acid-candidate-c']]) {
    await new Promise(resolve => setTimeout(resolve, delay));
    screenshot(client, 'AC-F022-004', label);
  }
  await mct(client, 'input', 'key', 'press', 'escape');

  await openRecipe(1);
  await new Promise(resolve => setTimeout(resolve, 500));
  screenshot(client, 'AC-F022-004', 'plant-oil-capacity-filtered-candidate');
  await mct(client, 'input', 'key', 'press', 'escape');

  await openRecipe(2);
  await new Promise(resolve => setTimeout(resolve, 500));
  screenshot(client, 'AC-F022-004', 'chemthrower-fixed-minecraft-bucket');
  await mct(client, 'input', 'key', 'press', 'escape');
  await mct(client, 'input', 'key', 'press', 'escape');
});
