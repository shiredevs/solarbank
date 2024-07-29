import ERROR_MESSAGES from '../ErrorMessages';

export default class ApiRequestError extends Error {
  public name: string;
  public message: string;
  public parentError: Error;

  public constructor(parentError: Error) {
    super();
    this.name = ApiRequestError.name;
    this.message = `${ERROR_MESSAGES.API_REQUEST_ERROR}`;
    this.parentError = parentError;
  }
}
