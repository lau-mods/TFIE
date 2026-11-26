import { readFileSync } from 'node:fs';
import { resolve } from 'node:path';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, mct } = await import(process.env.HARNESS_E2E_LIB);

const heads = [
  ['immersiveengineering:drillhead_iron', 'item.immersiveengineering.drillhead_iron'],
  ['immersiveengineering:drillhead_steel', 'item.immersiveengineering.drillhead_steel'],
  ['tfie:drillhead_black_steel', 'item.tfie.drillhead_black_steel'],
  ['tfie:drillhead_blue_steel', 'item.tfie.drillhead_blue_steel'],
  ['tfie:drillhead_red_steel', 'item.tfie.drillhead_red_steel'],
  ['tfie:drillhead_invar', 'item.tfie.drillhead_invar'],
  ['tfie:drillhead_titanium', 'item.tfie.drillhead_titanium'],
  ['tfie:drillhead_netherite', 'item.tfie.drillhead_netherite'],
  ['tfie:drillhead_tungsten_steel', 'item.tfie.drillhead_tungsten_steel'],
];

await scenario(async ({ clients }) => {
  const client = clients[0];
  const options = resolve(process.env.MCT_HOME, 'clients', client, 'minecraft', 'options.txt');
  if (!/^lang:en_us$/m.test(readFileSync(options, 'utf8'))) {
    throw new Error('Drill head screenshots require the configured en_us client');
  }
  command(client, 'gamemode creative');
  command(client, 'time set noon');
  command(client, 'weather clear');
  command(client, 'kill @e[type=item]');
  command(client, 'kill @e[type=armor_stand]');
  command(client, 'fill -12 100 -8 12 100 8 minecraft:smooth_quartz');

  // Item entity names use the shipped translation keys, so all nine material names and models fit one view.
  for (let index = 0; index < heads.length; index++) {
    const [item, translation] = heads[index];
    const x = (index % 5) * 3 - 6;
    const z = Math.floor(index / 5) * 4 - 2;
    command(client, `summon minecraft:item ${x} 101.3 ${z} {Item:{id:"${item}",count:1},Age:-32768,`
      + `CustomName:'{"translate":"${translation}"}',CustomNameVisible:1b}`);
  }
  command(client, 'gamemode spectator');
  await camera(client, { x: 0, y: 108, z: -13, yaw: 0, pitch: 35 });
  await new Promise(resolve => setTimeout(resolve, 2_000));
  screenshot(client, 'AC-F009-005', 'all-nine-head-models-and-material-names');

  // Four TFCM additions are installed on drills together; the component is IE's normal internal item storage.
  const attached = heads.slice(5);
  for (let index = 0; index < attached.length; index++) {
    const [head] = attached[index];
    const x = index * 3 - 4.5;
    const drill = `{id:"immersiveengineering:drill",count:1,components:{`
      + `"immersiveengineering:items":[{slot:0,item:{id:"${head}",count:1}}]}}`;
    command(client, `summon minecraft:armor_stand ${x} 101 -1 {NoGravity:1b,ShowArms:1b,`
      + `CustomName:'{"translate":"item.${head.replace(':', '.')}"}',CustomNameVisible:1b,`
      + `Pose:{RightArm:[-90f,0f,0f]},HandItems:[${drill},{}]}`);
  }
  await camera(client, { x: 0, y: 103.2, z: -9, yaw: 0, pitch: 8 });
  await new Promise(resolve => setTimeout(resolve, 2_000));
  screenshot(client, 'AC-F009-005', 'tfcm-heads-attached-to-ie-drills');

  command(client, 'gamemode creative');
  for (let index = 0; index < heads.length; index++) {
    const damage = index === heads.length - 1 ? '[minecraft:damage=20312]' : '';
    command(client, `item replace entity @s hotbar.${index} with ${heads[index][0]}${damage}`);
  }
  await mct(client, 'input', 'key', 'press', 'e');
  await mct(client, 'gui', 'wait-open', '--timeout', '10');
  const inventory = await mct(client, 'gui', 'layout');
  const hotbar = Array.isArray(inventory.slots) ? inventory.slots.slice(-9) : [];
  if (hotbar.length !== 9) throw new Error('The inventory hotbar slots are unavailable');
  await mct(client, 'input', 'mouse-move', String(hotbar[8].centerX), String(hotbar[8].centerY));
  await new Promise(resolve => setTimeout(resolve, 1_000));
  screenshot(client, 'AC-F009-005', 'inventory-icons-damage-bar-and-head-tooltip');
  await mct(client, 'input', 'key', 'press', 'escape');

  command(client, 'item replace entity @s weapon.mainhand with immersiveengineering:drill'
    + '[immersiveengineering:items=[{slot:0,item:{id:"tfie:drillhead_tungsten_steel",count:1}}]]');
  await camera(client, { x: 0, y: 102, z: -4, yaw: 0, pitch: 12 });
  await new Promise(resolve => setTimeout(resolve, 1_000));
  screenshot(client, 'AC-F009-005', 'held-drill-with-tungsten-steel-head');
});
