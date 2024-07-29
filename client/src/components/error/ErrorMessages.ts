export type ErrorMessages = {
  [key: string]: string;
};

const ERROR_MESSAGES: ErrorMessages = {
  PAGE_NOT_FOUND: 'Sorry, we could not find this page.',
  INTERNAL_SERVER_ERROR: 'Something went wrong please try again later.',
  API_REQUEST_ERROR: 'The server is currently unavailable, please try again later'
};

export default ERROR_MESSAGES;
