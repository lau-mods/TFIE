export async function interactWithBlock(client, camera, mct, [x, y, z], side) {
  await camera(client, { x: x + 0.5, y: Math.max(101, y - 1), z: z + 0.5 + 2.5 * side,
    yaw: side > 0 ? 180 : 0, pitch: 15 });
  // MC Pilot's place action sends an explicit hit on the neighboring block.
  await mct(client, 'block', 'place', String(x), String(y), String(z + side),
      '--face', side > 0 ? 'south' : 'north');
}

export async function formMultiblock(client, camera, mct, waitFor, trigger, side, master, id) {
  await interactWithBlock(client, camera, mct, trigger, side);
  const formed = await waitFor(() => mct(client, 'block', 'get', ...master.map(String)),
      block => block.type === id, 5_000);
  if (formed?.type !== id) throw new Error(`${id} did not form at ${master.join(' ')}`);
}
