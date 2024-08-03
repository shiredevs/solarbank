import ERROR_MESSAGES from '../ErrorMessages';

export default class ConfigurationError extends Error {
  public name: string;
  public message: string;

  public constructor() {
    super();
    this.name = ConfigurationError.name;
    this.message = ERROR_MESSAGES.CONFIGURATION_MISSING;
  }
}
