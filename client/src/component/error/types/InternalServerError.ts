import { ERROR_MESSAGES } from '../ErrorMessages';

export default class InternalServerError extends Error {
  constructor() {
    super(ERROR_MESSAGES.INTERNAL_SERVER_ERROR);
  }
}
