import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

const validateEnvs = (port: string, host: string, keyPath: string, certPath: string): void => {
  if (!port || !keyPath || !certPath || !host) {
    throw TypeError(
      'Failed to parse ssl parameters to start server. Please ensure you have a .env with correct parameters, see README.md for further details.'
    );
  }
};

// noinspection JSUnusedGlobalSymbols
export default defineConfig(({ mode }) => {
  const env: Record<string, string> = loadEnv(mode, process.cwd(), '');

  const port: string = env.PORT;
  const host: string = env.HOST;
  const keyPath: string = env.SSL_KEY_FILE;
  const certPath: string = env.SSL_CRT_FILE;

  mode === 'development' && validateEnvs(port, host, keyPath, certPath);

  return {
    plugins: [react()],
    build: {
      outDir: 'build'
    },
    define: {
      'process.env': env
    },
    server: {
      port: parseInt(port),
      host: host,
      https: {
        key: keyPath,
        cert: certPath
      }
    }
  };
});
