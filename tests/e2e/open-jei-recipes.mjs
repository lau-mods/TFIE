const { command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

export async function captureJeiRecipes(client, items, captures) {
  command(client, 'gamemode creative');
  command(client, 'weather clear');
  command(client, 'setblock 0 101 0 minecraft:chest');
  command(client, 'setblock 0 100 3 minecraft:stone');
  for (const [slot, item] of items) {
    command(client, `item replace block 0 101 0 container.${slot} with ${item}`);
  }
  await camera(client, { x: 0.5, y: 101, z: 3.5, yaw: 180, pitch: 20 });
  await mct(client, 'block', 'interact', '0', '101', '0');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const { slots } = await mct(client, 'gui', 'layout');
  const { width, height } = await mct(client, 'screen', 'size');

  for (const { slot, key, acId, label, tab, fluidRecipe, hover } of captures) {
    const { centerX, centerY } = slots[slot];
    await mct(client, 'input', 'mouse-move', String(centerX), String(centerY));
    const isJei = status => status.screen?.type === 'RecipesGui';
    const status = await waitFor(async () => {
      const current = await mct(client, 'status', 'all');
      if (!isJei(current)) {
        await mct(client, 'input', 'mouse-move', String(centerX), String(centerY));
        await mct(client, 'input', 'key', 'press', key);
      }
      return current;
    }, isJei, 10_000);
    if (!isJei(status)) throw new Error(`JEI did not open for chest slot ${slot}`);
    if (fluidRecipe) {
      await mct(client, 'input', 'mouse-move', String(Math.round(width / 2 - 34)),
          String(Math.round(height / 2)));
      await mct(client, 'input', 'key', 'press', 'r');
    }
    if (tab !== undefined) {
      await mct(client, 'input', 'click',
          String(Math.floor((width - 198) / 2) + 14 + 24 * tab), '36');
    }
    if (hover) {
      await mct(client, 'input', 'mouse-move', String(Math.round(width / 2 + hover.x)),
          String(Math.round(height / 2 + hover.y)));
    } else {
      await mct(client, 'input', 'mouse-move', String(Math.round(width / 2)), String(height - 2));
    }
    screenshot(client, acId, label);
    await mct(client, 'input', 'key', 'press', 'escape');
  }
  await mct(client, 'input', 'key', 'press', 'escape');
}
