import '@testing-library/jest-dom'; // provides expect() and other matchers in test
import { server } from './test-setup/mockServer';

// required to mock config provide from process.env
jest.mock('./clients/config/CalculateConfig', () => ({
  config: {
    SERVER_URL: 'https://localhost:8080',
    CALCULATE_ENDPOINT: '/api/V1/calculate'
  }
}));

beforeAll(() => {
  server.listen();
});

afterEach(() => {
  server.resetHandlers();
  jest.restoreAllMocks();
});

afterAll(() => {
  server.close();
});
