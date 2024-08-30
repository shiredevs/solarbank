import { createMemoryRouter } from 'react-router-dom';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import router from './components/router/AppRouter';
import './utils/IconLibrary';
import ROUTE_PATHS from './components/router/RoutePaths';
import { Router } from '@remix-run/router';
import ERROR_MESSAGES from './components/error/ErrorMessages';
import App from './App';
import fetchMock from 'jest-fetch-mock';
import { CalculateConfig } from './clients/config/CalculateConfig';

describe('application integration tests', (): void => {
  const ROUTER: Router = createMemoryRouter(router.routes, { initialEntries: [ROUTE_PATHS.ROOT] });
  const CONFIG: CalculateConfig = {
    SERVER_URL: 'https://localhost:8080',
    CALCULATE_ENDPOINT: '/api/V1/calculate'
  };
  const renderApp = (config: CalculateConfig): void => {
    render(<App router={ROUTER} calculateConfig={config} />);
  };

  beforeAll((): void => {
    fetchMock.enableMocks(); // mock global.fetch, used by Router
  });

  afterAll((): void => {
    fetchMock.resetMocks();
  });

  describe('happy path tests', () => {
    it('should navigate to landing page when the app is first loaded', () => {
      renderApp(CONFIG);

      expect(screen.getByRole('landing-page-container')).toBeInTheDocument();
    });

    it('should navigate to form page when user clicks begin', (): void => {
      renderApp(CONFIG);
      const button: HTMLElement = screen.getByRole('button');
      fireEvent.click(button);

      const formPage: HTMLElement = screen.getByRole('form-page-container');

      expect(formPage).toBeInTheDocument();
      expect(formPage).toHaveTextContent('submit');
    });

    it('should navigate to results page when user submits form and results retrieved successfully', () => {});

    it('should navigate back from results page to form page if the user clicks back in the browser', () => {});
  });

  describe('unhappy path tests', () => {
    it('should navigate to error page when user submits the form and result are not retrieved successfully', () => {});

    it('should navigate to error page when user navigates to unknown path', async (): Promise<void> => {
      renderApp(CONFIG);
      await waitFor(() => ROUTER.navigate('/invalid'));

      const errorPage: HTMLElement = screen.getByRole('paragraph');

      expect(errorPage).toBeInTheDocument();
      expect(errorPage).toHaveTextContent(ERROR_MESSAGES.PAGE_NOT_FOUND);
    });

    it('should navigate to error page when the configuration is invalid', (): void => {
      const invalidConfig: CalculateConfig = {
        SERVER_URL: undefined,
        CALCULATE_ENDPOINT: '/api/V1/calculate'
      };
      renderApp(invalidConfig);

      const errorPage: HTMLElement = screen.getByRole('paragraph');

      expect(errorPage).toBeInTheDocument();
      expect(errorPage).toHaveTextContent(ERROR_MESSAGES.CONFIGURATION_MISSING);
    });
  });
});
