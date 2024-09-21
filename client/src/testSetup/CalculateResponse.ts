import { CalculateRequest, CalculateResponse } from '../clients/CalculateClient';

const validRequest: CalculateRequest = {
  Location: {
    Longitude: 180,
    Latitude: 90
  },
  PanelSize: {
    Height: 20,
    Width: 20
  },
  PanelEfficiency: 0.1,
  EnergyTariff: {
    CurrencyCode: 'GBP',
    Amount: 120
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
