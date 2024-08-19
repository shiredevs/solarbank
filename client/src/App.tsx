import './assets/styles/global.css';
import { RouterProvider } from 'react-router-dom';
import { Router } from '@remix-run/router';
import { CalculateConfig } from './clients/config/CalculateConfig';
import { validate } from './clients/config/ConfigValidator';
import ErrorHandler from './components/error/ErrorHandler';
import ConfigurationError from './components/error/types/ConfigurationError';

type AppProps = {
  router: Router;
  calculateConfig: CalculateConfig;
};

const App = (props: AppProps) => {
  const { router, calculateConfig } = props;
  const isValidConfig: boolean = validate(calculateConfig);

  return (
    <div>
      {isValidConfig ? (
        <RouterProvider router={router} />
      ) : (
        <ErrorHandler error={new ConfigurationError()} />
      )}
    </div>
  );
};

export default App;
