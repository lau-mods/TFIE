const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const client = clients[0];
  command(client, 'gamemode spectator');
  command(client, 'weather clear');
  command(client, 'time set noon');
  command(client, 'fill -22 99 -2 22 116 34 minecraft:air');
  command(client, 'fill -22 99 -2 22 99 34 minecraft:smooth_quartz');

  const crops = [
    'alfalfa', 'barley', 'beet', 'cabbage', 'canola', 'carrot', 'cassava', 'garlic',
    'green_bean', 'jute', 'lentil', 'maize', 'melon', 'oat', 'onion', 'papyrus',
    'peanut', 'potato', 'pumpkin', 'radish', 'red_bell_pepper', 'rice', 'rye',
    'soybean', 'squash', 'sugarcane', 'tomato', 'wheat', 'yellow_bell_pepper',
  ];
  const soils = [
    'entisol', 'aridisol', 'oxisol', 'fluvisol',
    'andisol', 'podzol', 'alfisol', 'mollisol',
  ];
  const fixtures = [];

  const placeCloche = (x, z, crop, soil, growth) => {
    command(client, `setblock ${x} 100 ${z} immersiveengineering:cloche[facing=north,multiblockslave=false]`);
    // setblock bypasses IE's placement callback, so reproduce its two split-model dummy blocks.
    command(client, `setblock ${x} 101 ${z} immersiveengineering:cloche[facing=north,multiblockslave=true]`);
    command(client, `data merge block ${x} 101 ${z} {dummy:1}`);
    command(client, `setblock ${x} 102 ${z} immersiveengineering:cloche[facing=north,multiblockslave=true]`);
    command(client, `data merge block ${x} 102 ${z} {dummy:2}`);
    // Merge the master last so its inventory update reaches the client after the structure exists.
    command(client, `data merge block ${x} 100 ${z} {Items:[{Slot:0b,id:"tfc:dirt/${soil}",count:1},{Slot:1b,id:"tfc:seeds/${crop}",count:1}],tank:{Fluid:{id:"minecraft:water",amount:4000}},energy:16000,fertilizerAmount:6000,fertilizerMod:1.0f,growth:${growth}f}`);
    fixtures.push([x, 100, z], [x, 101, z], [x, 102, z]);
  };

  const waitForClocheFixtures = async () => {
    const loaded = await waitFor(
      () => fixtures.map(([x, y, z]) => mct(client, 'block', 'get', String(x), String(y), String(z)).type),
      blocks => blocks.every(type => type === 'immersiveengineering:cloche'),
      15000,
    );
    if (!loaded.every(type => type === 'immersiveengineering:cloche')) {
      throw new Error('Not every Garden Cloche fixture loaded');
    }
    await new Promise(resolve => setTimeout(resolve, 2500));
  };

  crops.forEach((crop, index) => {
    const x = -14 + (index % 10) * 3;
    const z = Math.floor(index / 10) * 4;
    placeCloche(x, z, crop, soils[index % soils.length], 124000);
  });

  await waitForClocheFixtures();
  await camera(client, { x: -0.5, y: 108, z: -20, yaw: 0, pitch: 19 });
  screenshot(client, 'AC-F015-006', 'all-29-crops-and-eight-dirt-soils');

  command(client, 'fill -18 100 -1 18 103 10 minecraft:air replace immersiveengineering:cloche');
  fixtures.length = 0;

  const doubles = [
    ['green_bean', 4], ['jute', 3], ['maize', 3],
    ['papyrus', 3], ['sugarcane', 4], ['tomato', 4],
  ];
  doubles.forEach(([crop, boundary], index) => {
    const x = -12 + (index % 3) * 12;
    const z = Math.floor(index / 3) * 6;
    // TFC double crops have max age 7; show immediately before, at, and mature.
    placeCloche(x - 3, z, crop, soils[index], Math.floor(((boundary - 0.51) / 7) * 128000));
    placeCloche(x, z, crop, soils[index], Math.floor((boundary / 7) * 128000));
    placeCloche(x + 3, z, crop, soils[index], 124000);
  });

  await waitForClocheFixtures();
  await camera(client, { x: 0, y: 107, z: -20, yaw: 0, pitch: 17 });
  screenshot(client, 'AC-F015-006', 'six-double-crops-before-at-and-after-boundary');
});
