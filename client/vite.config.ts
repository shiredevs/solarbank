import { defineConfig, loadEnv } from 'vite';
// @ts-ignore
import react from '@vitejs/plugin-react';

const validateEnvs = (env: Record<string, string>): void => {
  const port: string = env.PORT;
  const host: string = env.HOST;
  const keyPath: string = env.SSL_KEY_FILE;
  const certPath: string = env.SSL_CRT_FILE;

  if (!port || !keyPath || !certPath || !host) {
    throw TypeError(
      'Failed to parse ssl parameters to start server. Please ensure you have a .env with correct parameters, see README.md for further details.'
    );
  }
};

// noinspection JSUnusedGlobalSymbols
export default defineConfig(({ mode }) => {
  const env: Record<string, string> = loadEnv(mode, process.cwd(), '');

  mode === 'development' && validateEnvs(env);

  return {
    plugins: [react()],
    build: {
      outDir: 'build'
    },
    server: {
      port: parseInt(env.PORT),
      host: env.HOST,
      https: {
        key: env.SSL_KEY_FILE,
        cert: env.SSL_CRT_FILE
      }
    }
  };
});
