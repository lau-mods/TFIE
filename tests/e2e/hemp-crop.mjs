import { resolve } from 'node:path';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];

  command(client, 'gamemode creative');
  command(client, 'time set noon');
  command(client, 'weather clear');
  command(client, 'gamerule randomTickSpeed 0');
  command(client, 'gamerule doDaylightCycle false');
  command(client, 'fill -9 99 -10 7 99 2 minecraft:stone');
  command(client, 'fill -9 100 -10 7 100 -2 minecraft:stone');
  command(client, 'fill -8 100 -1 2 100 1 tfc:farmland/andisol');
  command(client, 'fill 3 100 -1 6 100 1 tfc:grass/andisol');
  command(client, 'setblock -7 101 0 tfie:crop/hemp[age=0,part=bottom]');
  command(client, 'setblock -6 101 0 tfie:crop/hemp[age=1,part=bottom]');
  command(client, 'setblock -5 101 0 tfie:crop/hemp[age=2,part=bottom]');
  command(client, 'setblock -4 101 0 tfie:crop/hemp[age=3,part=bottom]');
  command(client, 'setblock -3 101 0 tfie:crop/hemp[age=4,part=bottom]');
  command(client, 'setblock -3 102 0 tfie:crop/hemp[age=4,part=top]');
  command(client, 'setblock 0 101 0 tfie:dead_crop/hemp[mature=false,part=bottom]');
  command(client, 'setblock 1 101 0 tfie:dead_crop/hemp[mature=true,part=bottom]');
  command(client, 'setblock 1 102 0 tfie:dead_crop/hemp[mature=true,part=top]');
  command(client, 'setblock 4 101 0 tfie:wild_crop/hemp[mature=false,part=bottom]');
  command(client, 'setblock 4 102 0 tfie:wild_crop/hemp[mature=false,part=top]');
  command(client, 'setblock 5 101 0 tfie:wild_crop/hemp[mature=true,part=bottom]');
  command(client, 'setblock 5 102 0 tfie:wild_crop/hemp[mature=true,part=top]');
  command(client, 'kill @e[type=item]');
  await camera(client, { x: -0.5, y: 101, z: -8, yaw: 0, pitch: 7 }, 8_000);
  const position = mct(client, 'position', 'get');
  if (!Number.isFinite(position.y) || Math.abs(position.y - 101) > 0.5) {
    throw new Error(`The camera fell below the field: ${JSON.stringify(position)}`);
  }
  for (const [x, support, crop, top] of [
    [-7, 'tfc:farmland/andisol', 'tfie:crop/hemp', false],
    [-6, 'tfc:farmland/andisol', 'tfie:crop/hemp', false],
    [-5, 'tfc:farmland/andisol', 'tfie:crop/hemp', false],
    [-4, 'tfc:farmland/andisol', 'tfie:crop/hemp', false],
    [-3, 'tfc:farmland/andisol', 'tfie:crop/hemp', true],
    [0, 'tfc:farmland/andisol', 'tfie:dead_crop/hemp', false],
    [1, 'tfc:farmland/andisol', 'tfie:dead_crop/hemp', true],
    [4, 'tfc:grass/andisol', 'tfie:wild_crop/hemp', true],
    [5, 'tfc:grass/andisol', 'tfie:wild_crop/hemp', true],
  ]) {
    for (const [y, id] of [[99, 'minecraft:stone'], [100, support], [101, crop],
      ...(top ? [[102, crop]] : [])]) {
      const block = mct(client, 'block', 'get', String(x), String(y), '0');
      if (!JSON.stringify(block).includes(id)) {
        throw new Error(`Expected ${id} at ${x} ${y} 0, got ${JSON.stringify(block)}`);
      }
    }
  }
  screenshot(client, 'AC-F014-006', 'all-hemp-growth-dead-and-wild-stages');
});
