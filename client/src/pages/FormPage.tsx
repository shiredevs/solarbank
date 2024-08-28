import { JSX, useState } from 'react';
import Button from '../components/Button';
import { CalculateRequest, CalculateResponse, doCalculate } from '../clients/CalculateClient';
import ResultPage from './ResultPage';
import ErrorHandler from '../components/error/ErrorHandler';

// todo: replace with form component for user input
const staticRequest: CalculateRequest = {
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

const FormPage = (): JSX.Element => {
  const [result, setResult] = useState<CalculateResponse | Error | null>(null);

  const handleCalculate = async (calculateRequest: CalculateRequest) => {
    try {
      setResult(await doCalculate(calculateRequest));
    } catch (err) {
      setResult(err as Error);
    }
  };

  return (
    <div role="form-page-container">
      {result instanceof Error ? (
        <ErrorHandler error={result} />
      ) : result ? (
        <ResultPage result={result} />
      ) : (
        <Button handleClick={() => handleCalculate(staticRequest)} label={'submit'} />
      )}
    </div>
  );
};

export default FormPage;
