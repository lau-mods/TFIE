import { mkdtemp } from 'node:fs/promises';
import path from 'node:path';
import { loadConfig } from '../../.harness/dist/config/config.js';
import { run } from '../../.harness/dist/core/process.js';
import { collectLogs, mcPilot, startRuntime, stopRuntime } from '../../.harness/dist/e2e/runtime.js';

const root = process.cwd();
const { runtime } = await loadConfig(root);
const output = await mkdtemp(path.join(root, '.harness-state/runtime/smoke-'));
console.log(output);
try {
  const { clients } = await startRuntime(root, runtime, output, run);
  await mcPilot(root, runtime, ['--client', clients[0], 'screenshot', '--output', path.join(output, 'joined.png')], run);
} finally {
  await stopRuntime(root, runtime, run);
  await collectLogs(root, runtime, output);
}
