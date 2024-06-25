import { ERROR_MESSAGES } from '../ErrorMessages';

export default class PageNotFoundError extends Error {
  constructor() {
    super(ERROR_MESSAGES.PAGE_NOT_FOUND);
  }
}
