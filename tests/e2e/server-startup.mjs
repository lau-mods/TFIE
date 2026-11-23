import { spawn } from 'node:child_process';
import { copyFile, mkdir, readFile, readdir, symlink, writeFile } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import { join, resolve } from 'node:path';
import { mkdtemp } from 'node:fs/promises';

process.env.HARNESS_MCT = resolve(process.env.MCT_HOME, '..', 'pilot/node_modules/.bin/mct');
const { scenario, command, camera, screenshot, waitFor, mct } = await import(process.env.HARNESS_E2E_LIB);

await scenario(async ({ clients }) => {
  const server = resolve(process.env.MCT_HOME, '..', 'server');
  const runningLog = await readFile(join(server, 'logs/latest.log'), 'utf8');
  const lines = runningLog.split('\n');
  if (lines.some((line, index) =>
      /NoClassDefFoundError|ClassNotFoundException|invalid dist/i.test(line)
      && lines.slice(index, index + 12).some(context => /net[./]claustra01[./]tfie|\[tfie\//i.test(context)))) {
    throw new Error('The dedicated server log contains a class loading failure');
  }
  const client = clients[0];
  await mct(client, 'status', 'world');
  const guiClosed = await waitFor(() => {
    try {
      const layout = mct(client, 'gui', 'layout');
      if (!Number.isFinite(layout?.guiLeft)) return true;
      mct(client, 'gui', 'close');
      return false;
    } catch (error) {
      if (String(error).includes('GUI_NOT_OPEN')) return true;
      throw error;
    }
  }, closed => closed, 10_000);
  if (!guiClosed) throw new Error('A GUI is still open before the dedicated server screenshot');
  command(client, 'fill -2 101 -2 2 101 2 minecraft:smooth_quartz');
  await camera(client, { x: 0, y: 102, z: -5, yaw: 0, pitch: 28 });
  screenshot(client, 'AC-F001-001', 'dedicated-server-world');

  const isolated = await mkdtemp(join(tmpdir(), 'tfie-required-mod-'));
  await mkdir(join(isolated, 'mods'));
  await symlink(join(server, 'libraries'), join(isolated, 'libraries'));
  for (const file of ['run.sh', 'user_jvm_args.txt', 'eula.txt']) {
    await copyFile(join(server, file), join(isolated, file));
  }
  await writeFile(join(isolated, 'server.properties'), 'server-ip=127.0.0.1\nserver-port=0\n');
  const mods = await readdir(join(server, 'mods'));
  const omitted = mods.filter(file => /tfcm|metallum/i.test(file));
  if (omitted.length !== 1) throw new Error('Expected one TFCM mod JAR for the missing dependency check');
  for (const file of mods.filter(file => !omitted.includes(file))) {
    await symlink(join(server, 'mods', file), join(isolated, 'mods', file));
  }

  const child = spawn('sh', ['run.sh', '--nogui'], { cwd: isolated, stdio: ['ignore', 'pipe', 'pipe'] });
  let output = '';
  for (const stream of [child.stdout, child.stderr]) stream.on('data', chunk => { output += chunk; });
  const timeout = setTimeout(() => child.kill(), 90000);
  await new Promise((resolveExit, reject) => {
    child.once('error', reject);
    child.once('exit', resolveExit);
  });
  clearTimeout(timeout);
  const startupLog = await readFile(join(isolated, 'logs/latest.log'), 'utf8').catch(() => output);
  await mkdir('build/test-results/startup', { recursive: true });
  await writeFile('build/test-results/startup/missing-tfcm.log', startupLog);
  if (!/Mod ID: 'tfcm', Requested by: 'tfie'|Mod tfie requires tfcm 2\.2\.1 or above/i.test(startupLog + output)
      || /Done \([\d.]+s\)! For help/i.test(startupLog)) {
    throw new Error('The server did not reject TFIE with TFCM named as the missing dependency');
  }
});
