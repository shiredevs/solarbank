import '@testing-library/jest-dom'; // provides expect() and other matchers in test
import nock, { RequestBodyMatcher } from 'nock';

export type MockRequestData = {
  [key: string]: string | number | object;
};

export type MockResponseData = {
  [key: string]: string | number | object;
};

const interceptPostRequest = (
  request: MockRequestData,
  response: MockResponseData,
  status: number,
  basePath: string,
  endpoint: string
): void => {
  nock(basePath)
    .post(endpoint, request as RequestBodyMatcher)
    .reply(status, response);
};

export { interceptPostRequest };
