import { CalculateRequest, CalculateResponse, doCalculate } from './CalculateClient';
import { interceptPost } from '../test-setup/RequestInterceptor';
import { config } from './config/CalculateConfig';
import ApiRequestError from '../components/error/types/ApiRequestError';
import { validRequest, validResponse } from '../test-setup/calculate-response';

describe('calculate client tests', () => {
  it('request to calculate, valid request body, expected response returned', async (): Promise<void> => {
    interceptPost(
      validRequest as CalculateRequest,
      validResponse as CalculateResponse,
      200,
      config.SERVER_URL as string,
      config.CALCULATE_ENDPOINT as string
    );

    const actualResponse: CalculateResponse = await doCalculate(validRequest);

    expect(actualResponse).toEqual(validResponse);
  });

  it('request to calculate, server error, throws expected exception', async (): Promise<void> => {
    let response;

    interceptPost(
      validRequest as CalculateRequest,
      validResponse as CalculateResponse,
      500,
      config.SERVER_URL as string,
      config.CALCULATE_ENDPOINT as string
    );

    try {
      response = await doCalculate(validRequest);
    } catch (err) {
      expect(err).toBeInstanceOf(ApiRequestError);
    }

    expect(response).toBeUndefined();
  });
});
