import { CalculateRequest, CalculateResponse } from '../clients/CalculateClient';

const validRequest: CalculateRequest = {
  Location: {
    Longitude: 180,
    Latitude: 90
  },
  PanelSize: {
    Height: 2.0,
    Width: 3.0
  },
  PanelEfficiency: 0.9,
  EnergyTariff: {
    CurrencyCode: 'GBP',
    Amount: 0.28
  }
};

const validResponse: CalculateResponse = {
  EnergyGenPerYear: 1000,
  EnergyGenPerMonth: {
    January: 500,
    February: 500
  },
  SavingsPerYear: {
    CurrencyCode: 'GBP',
    Amount: 5000
  }
};

export { validRequest, validResponse };
