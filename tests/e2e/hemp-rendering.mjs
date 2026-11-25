import { resolve } from 'node:path';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode spectator');
  command(client, 'weather clear');
  command(client, 'time set noon');
  command(client, 'fill -12 99 -2 12 104 8 minecraft:air');
  // Keep the display platform continuous below the TFC soil fixtures.
  command(client, 'fill -11 99 -1 11 99 7 minecraft:smooth_quartz');
  command(client, 'fill -11 100 -1 11 100 7 minecraft:smooth_quartz');
  command(client, 'kill @e[type=item]');

  const expectedBlocks = [];
  const place = (x, y, z, state) => {
    command(client, `setblock ${x} ${y} ${z} ${state}`);
    expectedBlocks.push([x, y, z, state.split('[')[0]]);
  };

  const cultivated = [-8, -4, 0, 4, 8];
  for (let age = 0; age <= 4; age++) {
    const x = cultivated[age];
    place(x, 100, 1, 'tfc:farmland/entisol');
    place(x, 101, 1, `tfie:crop/hemp[age=${age},part=bottom]`);
    if (age === 4) {
      place(x, 102, 1, 'tfie:crop/hemp[age=4,part=top]');
    }
  }

  for (const [x, support, block, mature] of [
    [-8, 'tfc:farmland/entisol', 'tfie:dead_crop/hemp', false],
    [4, 'tfc:farmland/entisol', 'tfie:dead_crop/hemp', true],
    [-4, 'tfc:dirt/entisol', 'tfie:wild_crop/hemp', false],
    [8, 'tfc:dirt/entisol', 'tfie:wild_crop/hemp', true],
  ]) {
    place(x, 100, 5, support);
    place(x, 101, 5, `${block}[mature=${mature},part=bottom]`);
    if (mature || block.includes('wild_crop')) {
      place(x, 102, 5, `${block}[mature=${mature},part=top]`);
    }
  }

  const verifyFixtures = async () => {
    const blocks = await waitFor(
      () => expectedBlocks.map(([x, y, z, expected]) => ({
        x,
        y,
        z,
        expected,
        actual: mct(client, 'block', 'get', String(x), String(y), String(z)).type,
      })),
      (observed) => observed.every(({ expected, actual }) => expected === actual),
      15000,
    );
    const mismatch = blocks.find(({ expected, actual }) => expected !== actual);
    if (mismatch) {
      throw new Error(`Expected ${mismatch.expected} at ${mismatch.x} ${mismatch.y} ${mismatch.z}, found ${mismatch.actual}`);
    }
  };

  await camera(client, { x: 0, y: 104, z: -10, yaw: 0, pitch: 18 });
  await verifyFixtures();
  screenshot(client, 'AC-F014-006', 'all-cultivated-dead-and-wild-stages');
  await camera(client, { x: 6, y: 102.8, z: -2, yaw: 0, pitch: 10 });
  await verifyFixtures();
  screenshot(client, 'AC-F014-006', 'mature-halves-and-cutout-leaves');
});
