/**
 * @jest-environment node
 * */

import nock from 'nock';
import { CalculateRequest, CalculateResponse, doCalculate } from './CalculateClient';
import { interceptPostRequest } from '../setupTests';
import { config } from './config/CalculateConfig';
import ApiRequestError from '../components/error/types/ApiRequestError';

jest.mock('./config/CalculateConfig', () => ({
  config: {
    SERVER_URL: 'https://localhost:8080',
    CALCULATE_ENDPOINT: '/api/V1/calculate'
  }
}));

describe('calculate client tests', () => {
  let mockRequest: CalculateRequest;
  let expectedResponse: CalculateResponse;

  beforeAll(() => {
    mockRequest = {
      location: {
        long: 1234.12,
        lat: 1234.51
      },
      panelSize: {
        height: 20,
        width: 20
      },
      panelEfficiency: 100,
      energyTariff: {
        currencyCode: 'GBP',
        amount: 120
      }
    };

    expectedResponse = {
      energyGenPerYear: 1000,
      energyGenPerMonth: {
        January: 500,
        February: 500
      },
      savingsPerYear: {
        currencyCode: 'GBP',
        amount: 5000
      }
    };
  });

  afterEach((): void => {
    jest.restoreAllMocks();
    nock.cleanAll();
  });

  it('request to calculate, valid request body, expected response returned', async (): Promise<void> => {
    interceptPostRequest(
      mockRequest,
      expectedResponse,
      200,
      config.SERVER_URL as string,
      config.CALCULATE_ENDPOINT as string
    );

    const actualResponse: CalculateResponse = await doCalculate(mockRequest);

    expect(actualResponse).toEqual(expectedResponse);
  });

  it('request to calculate, server error, throws expected exception', async (): Promise<void> => {
    let response;

    interceptPostRequest(
      mockRequest,
      expectedResponse,
      500,
      config.SERVER_URL as string,
      config.CALCULATE_ENDPOINT as string
    );

    try {
      response = await doCalculate(mockRequest);
    } catch (err) {
      expect(err).toBeInstanceOf(ApiRequestError);
    }

    expect(response).toBeUndefined();
  });
});
