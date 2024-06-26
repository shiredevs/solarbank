import ERROR_MESSAGES from '../ErrorMessages';

export default class PageNotFoundError extends Error {
  public name: string;
  public message: string;

  public constructor() {
    super();
    this.name = PageNotFoundError.name;
    this.message = ERROR_MESSAGES.PAGE_NOT_FOUND;
  }
}
