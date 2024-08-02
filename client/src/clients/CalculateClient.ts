import { post } from '../utils/HttpClient';
import { AxiosResponse } from 'axios';
import {config} from '../config/CalculateConfig';
import { logHttpError } from '../utils/Logger';
import ApiRequestError from '../components/error/types/ApiRequestError';

const CALCULATE_URL: string = `${config.SERVER_URL}${config.CALCULATE_ENDPOINT}`;

export type CalculateRequest = {
  location: {
    long: number,
    lat: number
  },
  panelSize: {
    height: number,
    width: number
  },
  panelEfficiency: number,
  energyTariff: {
    currencyCode: string,
    amount: number
  }
}

export type CalculateResponse = {
  energyGenPerYear: number,
  energyGenPerMonth: {
    [key: string]: number
  },
  savingsPerYear: {
    currencyCode: string,
    amount: number
  }
}

const doCalculate = async (request: CalculateRequest): Promise<CalculateResponse> => {
  try {
    const response: AxiosResponse<CalculateResponse, never> = await post(CALCULATE_URL, request);
    return response.data;

  } catch (err) {
    const apiRequestError: ApiRequestError = err as ApiRequestError;
    logHttpError<CalculateRequest>(CALCULATE_URL, request, apiRequestError);
    throw apiRequestError;
  }
};

export { doCalculate };
