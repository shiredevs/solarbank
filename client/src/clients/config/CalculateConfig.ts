import { Config } from './ConfigValidator';

export type CalculateConfig = Config & {
  SERVER_URL: string | undefined;
  CALCULATE_ENDPOINT: string | undefined;
};

const env = import.meta.env;

const config: CalculateConfig = {
  SERVER_URL: env.VITE_SERVER_URL,
  CALCULATE_ENDPOINT: env.VITE_CALCULATE_ENDPOINT
};

export { config };
