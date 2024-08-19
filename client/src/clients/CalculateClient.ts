import { post } from './http-client/HttpClient';
import { AxiosResponse } from 'axios';
import { config } from './config/CalculateConfig';

const CALCULATE_URL: string = `${config.SERVER_URL}${config.CALCULATE_ENDPOINT}`;

export type CalculateRequest = {
  location: {
    long: number;
    lat: number;
  };
  panelSize: {
    height: number;
    width: number;
  };
  panelEfficiency: number;
  energyTariff: {
    currencyCode: string;
    amount: number;
  };
};

export type CalculateResponse = {
  energyGenPerYear: number;
  energyGenPerMonth: {
    [key: string]: number;
  };
  savingsPerYear: {
    currencyCode: string;
    amount: number;
  };
};

const doCalculate = async (request: CalculateRequest): Promise<CalculateResponse> => {
  const response: AxiosResponse<CalculateResponse, never> = await post(CALCULATE_URL, request);

  return response.data;
};

export { doCalculate };
