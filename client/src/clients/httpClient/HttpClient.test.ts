import { post } from './HttpClient';
import axios, { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import ApiRequestError from '../../components/error/types/ApiRequestError';
import * as logger from '../../utils/Logger';
import errorMessages from '../../components/error/ErrorMessages';
import { interceptPost, RequestData, ResponseData } from '../../testSetup/RequestInterceptor';

describe('http client tests', () => {
  const MOCK_BASEPATH: string = 'https://localhost:8080';
  const MOCK_ENDPOINT: string = '/api/v1/some_endpoint';
  const MOCK_URL: string = `${MOCK_BASEPATH}${MOCK_ENDPOINT}`;

  let actualRequest: InternalAxiosRequestConfig;

  const extractErrors = (err: Error) => {
    expect(err).toBeInstanceOf(ApiRequestError);
    const apiRequestError: ApiRequestError = err as ApiRequestError;
    expect(apiRequestError.parentError).toBeInstanceOf(AxiosError);
    const axiosError: AxiosError = (err as ApiRequestError).parentError as AxiosError;
    return { apiRequestError, axiosError };
  };

  beforeEach(() => {
    jest.spyOn(logger, 'logRequest').mockImplementationOnce(request => {
      actualRequest = request;
      logger.logRequest(request);
    });
  });

  it('returns 200 and expected response body with valid request', async (): Promise<void> => {
    const requestBody: RequestData = { test: 'test_request_body' };
    const expectedResponseBody: ResponseData = { result: 'test_response_body' };
    const expectedStatus: number = 200;

    interceptPost(requestBody, expectedResponseBody, expectedStatus, MOCK_BASEPATH, MOCK_ENDPOINT);

    const actualResponse: AxiosResponse<ResponseData, never> = await post(MOCK_URL, requestBody);

    expect(actualRequest.url).toEqual(MOCK_URL);
    expect(actualRequest.data).toEqual(JSON.stringify(requestBody));
    expect(actualResponse.status).toEqual(expectedStatus);
    expect(actualResponse.data.result).toEqual(expectedResponseBody.result);
  });

  it('returns 200 with empty request body', async (): Promise<void> => {
    const expectedStatus: number = 200;

    interceptPost({}, {}, expectedStatus, MOCK_BASEPATH, MOCK_ENDPOINT);

    const actualResponse: AxiosResponse<ResponseData, never> = await post(MOCK_URL, {});

    expect(actualResponse.status).toEqual(expectedStatus);
  });

  it('throws expected axios error when given invalid endpoint', async (): Promise<void> => {
    let response;
    const invalidEndpoint: string = '/invalid-endpoint';

    interceptPost({}, {}, 404, MOCK_BASEPATH, invalidEndpoint);

    try {
      response = await post(`${MOCK_BASEPATH}${invalidEndpoint}`, {});
    } catch (err) {
      const { apiRequestError, axiosError } = extractErrors(err as Error);
      expect(apiRequestError.message).toEqual(errorMessages.API_REQUEST_ERROR);
      expect(axiosError.code).toEqual(AxiosError.ERR_BAD_REQUEST);
    }

    expect(response).toBeUndefined();
  });

  it('throws expected axios error when the server is unavailable', async (): Promise<void> => {
    let response;

    interceptPost({}, {}, 500, MOCK_BASEPATH, MOCK_ENDPOINT);

    try {
      response = await post(MOCK_URL, {});
    } catch (err) {
      const { apiRequestError, axiosError } = extractErrors(err as Error);
      expect(apiRequestError.message).toEqual(errorMessages.API_REQUEST_ERROR);
      expect(axiosError.code).toEqual(AxiosError.ERR_BAD_RESPONSE);
    }

    expect(response).toBeUndefined();
  });

  it('throws expected api request error when unhandled error occurs', async () => {
    jest.spyOn(axios, 'post').mockRejectedValue(new Error('unhandled error'));

    await expect(post('/', {})).rejects.toThrowError(ApiRequestError);
  });
});
