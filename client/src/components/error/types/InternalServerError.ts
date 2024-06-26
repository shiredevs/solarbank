import ERROR_MESSAGES from '../ErrorMessages';

export default class InternalServerError extends Error {
  public name: string;
  public message: string;

  public constructor() {
    super();
    this.name = InternalServerError.name;
    this.message = ERROR_MESSAGES.INTERNAL_SERVER_ERROR;
  }
}
