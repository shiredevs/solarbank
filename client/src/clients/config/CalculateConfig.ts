import { Config } from './ConfigValidator';

export type CalculateConfig = Config & {
  SERVER_URL: string | undefined;
  CALCULATE_ENDPOINT: string | undefined;
};

const config: CalculateConfig = {
  SERVER_URL: process.env.SERVER_URL,
  CALCULATE_ENDPOINT: process.env.CALCULATE_ENDPOINT
};

export { config };
