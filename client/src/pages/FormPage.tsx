import { JSX, useState } from 'react';
import Button from '../components/Button';
import { CalculateRequest, CalculateResponse, doCalculate } from '../clients/CalculateClient';
import ErrorHandler from '../components/error/ErrorHandler';
import { Navigate } from 'react-router-dom';
import ROUTE_PATHS from '../components/router/RoutePaths';

// todo: replace with form component for user input
const staticRequest: CalculateRequest = {
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
        <Navigate to={ROUTE_PATHS.RESULT} state={result} replace={false} />
      ) : (
        <Button handleClick={() => handleCalculate(staticRequest)} label={'submit'} />
      )}
    </div>
  );
};

export default FormPage;
