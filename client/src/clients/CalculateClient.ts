import { post } from './httpClient/HttpClient';
import { AxiosResponse } from 'axios';
import { config } from './config/CalculateConfig';

const CALCULATE_URL: string = `${config.SERVER_URL}${config.CALCULATE_ENDPOINT}`;

export type CalculateRequest = {
  Location: {
    Longitude: number;
    Latitude: number;
  };
  PanelSize: {
    Height: number;
    Width: number;
  };
  PanelEfficiency: number;
  EnergyTariff: {
    CurrencyCode: string;
    Amount: number;
  };
};

export type CalculateResponse = {
  EnergyGenPerYear: number;
  EnergyGenPerMonth: {
    [key: string]: number;
  };
  SavingsPerYear: {
    CurrencyCode: string;
    Amount: number;
  };
};

const doCalculate = async (request: CalculateRequest): Promise<CalculateResponse> => {
  const response: AxiosResponse<CalculateResponse, never> = await post(CALCULATE_URL, request);

  return response.data;
};

export { doCalculate };
