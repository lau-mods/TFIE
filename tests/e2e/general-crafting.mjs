import { createWriteStream, mkdtempSync, readFileSync, readdirSync, rmSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join, resolve } from 'node:path';
import { pipeline } from 'node:stream/promises';
import { pathToFileURL } from 'node:url';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  const options = resolve(process.env.MCT_HOME, 'clients', client, 'minecraft', 'options.txt');
  if (!/^lang:en_us$/m.test(readFileSync(options, 'utf8'))) {
    throw new Error('The general crafting recipe review requires the client language to be en_us');
  }

  command(client, 'gamemode creative');
  let inventoryOpen = false;
  let inventoryError;
  for (let attempt = 0; attempt < 3 && !inventoryOpen; attempt++) {
    try {
      await mct(client, 'input', 'key', 'press', 'e');
      await mct(client, 'gui', 'wait-open', '--timeout', '5');
      inventoryOpen = true;
    } catch (error) {
      inventoryError = error;
      const status = await mct(client, 'status', 'all');
      inventoryOpen = status.screenCategory === 'screen' || status.screen?.category === 'screen';
      if (!inventoryOpen) await new Promise(resolve => setTimeout(resolve, 1_000));
    }
  }
  if (!inventoryOpen) throw inventoryError;

  const recipes = [
    ['immersiveengineering:craftingtable', 'crafting-table-treated-lumber'],
    ['immersiveengineering:workbench', 'workbench-treated-slabs'],
    ['immersiveengineering:blueprint[immersiveengineering:blueprint="components"]', 'components-blueprint'],
    ['immersiveengineering:fluorescent_tube', 'fluorescent-tube'],
    ['immersiveengineering:chemthrower', 'chemthrower-fixed-bucket'],
    ['immersiveengineering:jerrycan', 'jerrycan-fixed-buckets'],
    ['immersiveengineering:treated_post', 'treated-post'],
    ['immersiveengineering:steel_post', 'steel-post'],
    ['immersiveengineering:alu_post', 'aluminum-post'],
  ];

  for (const [item, label] of recipes) {
    command(client, `item replace entity @s hotbar.0 with ${item}`);
    await new Promise(resolve => setTimeout(resolve, 300));
    const layout = await mct(client, 'gui', 'layout');
    const hotbar = Array.isArray(layout.slots) ? layout.slots.slice(-9) : [];
    if (!hotbar[0]) throw new Error(`The hotbar slot is unavailable for ${label}`);
    await mct(client, 'input', 'mouse-move', String(hotbar[0].centerX), String(hotbar[0].centerY));
    await mct(client, 'input', 'key', 'press', 'r');
    const isRecipes = value => value.screen?.type === 'RecipesGui';
    const status = await waitFor(() => mct(client, 'status', 'all'), isRecipes, 10_000);
    if (!isRecipes(status)) throw new Error(`JEI did not open the ${label} crafting recipe`);
    await new Promise(resolve => setTimeout(resolve, 800));
    screenshot(client, 'AC-F011-009', label);
    await mct(client, 'input', 'key', 'press', 'escape');
  }

  const mods = resolve(process.env.MCT_HOME, 'clients', client, 'minecraft', 'mods');
  const firmalifeJar = readdirSync(mods).find(file => /^firmalife.*\.jar$/i.test(file));
  const hasFirmalife = firmalifeJar !== undefined;
  const categoryOrder = resolve(process.env.MCT_HOME, 'clients', client, 'minecraft',
      'config/jei/client/recipe-category-sort-order.ini');
  const firstCategory = readFileSync(categoryOrder, 'utf8')
      .split('\n')
      .find(line => line.startsWith('\\='));
  if (firstCategory !== '\\=minecraft:crafting') {
    throw new Error('The Ersatz Leather review requires Crafting to be JEI\'s first recipe category');
  }
  command(client, 'item replace entity @s hotbar.0 with immersiveengineering:ersatz_leather');
  await new Promise(resolve => setTimeout(resolve, 300));
  const layout = await mct(client, 'gui', 'layout');
  const hotbar = Array.isArray(layout.slots) ? layout.slots.slice(-9) : [];
  if (!hotbar[0]) throw new Error('The hotbar slot is unavailable for Ersatz Leather');
  await mct(client, 'input', 'mouse-move', String(hotbar[0].centerX), String(hotbar[0].centerY));
  await mct(client, 'input', 'key', 'press', 'r');
  const isRecipes = value => value.screen?.type === 'RecipesGui';
  const status = await waitFor(() => mct(client, 'status', 'all'), isRecipes, 10_000);
  if (!isRecipes(status)) throw new Error('JEI did not open the Ersatz Leather crafting recipe');
  await new Promise(resolve => setTimeout(resolve, 800));
  if (hasFirmalife) {
    const jeiConfig = readFileSync(resolve(process.env.MCT_HOME, 'clients', client, 'minecraft',
        'config/jei/client/default/jei-client.ini'), 'utf8');
    if (!/centerSearch = STANDARD/.test(jeiConfig)
        || !/\[recipes\][\s\S]*navigationMode = PAGED/.test(jeiConfig)
        || !/recipeGuiHeight = 350/.test(jeiConfig)
        || !/recipeGuiWidth = 198/.test(jeiConfig)) {
      throw new Error('The Ersatz Leather review requires JEI\'s standard paged recipe layout');
    }
    const { width, height, scaleFactor } = await mct(client, 'screen', 'size');
    const recipeGuiTop = 45 + Math.max(0, height - 58 - 350) / 2;
    await mct(client, 'input', 'click', String(Math.round(width / 2 + 86)),
        String(Math.round(recipeGuiTop + 25)));
    await new Promise(resolve => setTimeout(resolve, 800));
    const evidenceDirectory = mkdtempSync(join(tmpdir(), 'tfie-ersatz-'));
    const rawHoneyTexture = join(evidenceDirectory, 'raw_honey.png');
    const recipeCapture = join(evidenceDirectory, 'recipe.png');
    try {
      const yauzlPath = resolve(process.env.MCT_HOME, '../pilot/node_modules/yauzl/index.js');
      const { default: yauzl } = await import(pathToFileURL(yauzlPath));
      await new Promise((resolveEntry, reject) => {
        yauzl.open(resolve(mods, firmalifeJar), { lazyEntries: true }, (openError, zip) => {
          if (openError) return reject(openError);
          zip.readEntry();
          zip.on('entry', entry => {
            if (!/assets\/firmalife\/textures\/item\/.*raw_honey\.png$/.test(entry.fileName)) {
              zip.readEntry();
              return;
            }
            zip.openReadStream(entry, async (streamError, stream) => {
              if (streamError) return reject(streamError);
              try {
                await pipeline(stream, createWriteStream(rawHoneyTexture));
                zip.close();
                resolveEntry();
              } catch (error) {
                reject(error);
              }
            });
          });
          zip.on('end', () => reject(new Error('FirmaLife Raw Honey texture is unavailable')));
          zip.on('error', reject);
        });
      });
      await mct(client, 'screenshot', '--gui', '--output', recipeCapture);
      const recipeRegion = [width / 2 - 70, height / 2 - 100, 75, 200]
          .map(value => Math.round(value * scaleFactor))
          .join(',');
      const rawHoney = await mct(client, 'image', 'locate-template', recipeCapture, rawHoneyTexture,
          '--region', recipeRegion,
          '--scale', String(scaleFactor), '--stride', '1', '--alpha-threshold', '240');
      if (!rawHoney.match || rawHoney.match.score > 12) {
        throw new Error('JEI did not select tfie:crafting/ersatz_leather_firmalife');
      }
      await mct(client, 'input', 'mouse-move',
          String(Math.round(rawHoney.match.center.x / scaleFactor)),
          String(Math.round(rawHoney.match.center.y / scaleFactor)));
      await new Promise(resolve => setTimeout(resolve, 500));
    } finally {
      rmSync(evidenceDirectory, { recursive: true, force: true });
    }
  }
  screenshot(client, 'AC-F011-009', hasFirmalife
    ? 'ersatz-leather-raw-honey'
    : 'ersatz-leather-without-firmalife');
  await mct(client, 'input', 'key', 'press', 'escape');

  await mct(client, 'input', 'key', 'press', 'escape');
});
