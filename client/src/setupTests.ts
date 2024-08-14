import '@testing-library/jest-dom'; // provides expect() and other matchers in test
import nock, { RequestBodyMatcher } from 'nock';

export type MockRequestData = {
  [key: string]: string | number | object;
};

export type MockResponseData = {
  [key: string]: string | number | object;
};

const MOCK_BASEPATH: string = 'https://localhost:8080';

jest.mock('./config/CalculateConfig', () => ({
  config: {
    SERVER_URL: MOCK_BASEPATH,
    CALCULATE_ENDPOINT: '/api/V1/calculate'
  }
}));

const interceptPostRequest = (
  request: MockRequestData,
  response: MockResponseData,
  status: number,
  endpoint: string
): void => {
  nock(MOCK_BASEPATH)
    .post(endpoint, request as RequestBodyMatcher)
    .reply(status, response);
};

export { interceptPostRequest };
