import { JSX } from 'react';
import { CalculateResponse } from '../clients/CalculateClient';

type ResultProps = {
  result: CalculateResponse;
};

const reduceToString = (energyGenPerMonth: { [key: string]: number }) => {
  return Object.entries(energyGenPerMonth).reduce((output, [month, value]) => {
    return `${output} ${month ? month : 'unknown'}: ${value ? value : 'unknown'} kWh`;
  }, 'energy generation by month - ');
};

const ResultPage = (props: ResultProps): JSX.Element => {
  const { energyGenPerYear, energyGenPerMonth, savingsPerYear } = props.result;

  return (
    <div role="result-page-container">
      <p role="energy-gen-per-year-card">
        {energyGenPerYear ? `annual energy generation of ${energyGenPerYear} kWh` : 'not available'}
      </p>
      <p role="savings-per-year-card">
        {savingsPerYear.amount && savingsPerYear.currencyCode
          ? `annual energy savings of ${savingsPerYear.amount} ${savingsPerYear.currencyCode}`
          : 'not available'}
      </p>
      <p role="energy-gen-per-month-card">
        {energyGenPerMonth ? reduceToString(energyGenPerMonth) : 'not available'}
      </p>
    </div>
  );
};

export default ResultPage;
