import { CalculateRequest, CalculateResponse } from '../clients/CalculateClient';

const validRequest: CalculateRequest = {
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

const validResponse: CalculateResponse = {
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

export { validRequest, validResponse };
