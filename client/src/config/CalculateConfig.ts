import { logError } from '../utils/Logger';
import ERROR_MESSAGES from '../components/error/ErrorMessages';
import ConfigurationError from '../components/error/types/ConfigurationError';
import { containsUndefined } from './ConfigValidator';

export type CalculateConfig = {
  [key: string]: string | undefined;
};

const config: CalculateConfig = {
  SERVER_URL: process.env.SERVER_URL,
  CALCULATE_ENDPOINT: process.env.CALCULATE_ENDPOINT
};

if (containsUndefined(config)) {
  logError(ERROR_MESSAGES.CONFIGURATION_MISSING);
  throw new ConfigurationError();
}

export { config };
