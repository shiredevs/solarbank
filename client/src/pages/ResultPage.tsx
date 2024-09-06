import { JSX } from 'react';
import { CalculateResponse } from '../clients/CalculateClient';
import { useLocation } from 'react-router-dom';

// todo: replace with result cards
const reduceToString = (energyGenPerMonth: { [key: string]: number }) => {
  return Object.entries(energyGenPerMonth).reduce((output, [month, value]) => {
    return `${output} ${month ? month : 'unknown'}: ${value ? value : 'unknown'} kWh`;
  }, 'energy generation by month - ');
};

const ResultPage = (): JSX.Element => {
  const { state } = useLocation();
  const { EnergyGenPerYear, EnergyGenPerMonth, SavingsPerYear }: CalculateResponse = state;

  return (
    <div role="result-page-container">
      <p role="energy-gen-per-year-card">
        {EnergyGenPerYear ? `annual energy generation of ${EnergyGenPerYear} kWh` : 'not available'}
      </p>
      <p role="savings-per-year-card">
        {SavingsPerYear.Amount && SavingsPerYear.CurrencyCode
          ? `annual energy savings of ${SavingsPerYear.Amount} ${SavingsPerYear.CurrencyCode}`
          : 'not available'}
      </p>
      <p role="energy-gen-per-month-card">
        {EnergyGenPerMonth ? reduceToString(EnergyGenPerMonth) : 'not available'}
      </p>
    </div>
  );
};

export default ResultPage;
