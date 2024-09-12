import { createMemoryRouter } from 'react-router-dom';
import { act, fireEvent, render, screen, waitFor } from '@testing-library/react';
import router from './components/router/AppRouter';
import './utils/IconLibrary';
import ROUTE_PATHS from './components/router/RoutePaths';
import { Router } from '@remix-run/router';
import ERROR_MESSAGES from './components/error/ErrorMessages';
import App from './App';
import { CalculateConfig, config } from './clients/config/CalculateConfig';
import { interceptPost } from './testSetup/RequestInterceptor';
import { validRequest, validResponse } from './testSetup/CalculateResponse';

describe('application integration tests', (): void => {
  let appRouter: Router;

  const renderApp = (config: CalculateConfig): void => {
    appRouter = createMemoryRouter(router.routes, { initialEntries: [ROUTE_PATHS.ROOT] });
    render(<App router={appRouter} calculateConfig={config} />);
  };

  const simulateUserSubmittingForm = async (expectedResponseStatus: number) => {
    interceptPost(
      validRequest,
      validResponse,
      expectedResponseStatus,
      config.SERVER_URL as string,
      config.CALCULATE_ENDPOINT as string
    );

    renderApp(config);
    const beginButton: HTMLElement = screen.getByRole('button');
    await act(() => fireEvent.click(beginButton));
    const submitButton: HTMLElement = screen.getByRole('button');
    await act(async () => fireEvent.click(submitButton));
  };

  describe('happy path tests', () => {
    it('should navigate to landing page when the app is first loaded', () => {
      renderApp(config);

      expect(screen.getByRole('landing-page-container')).toBeInTheDocument();
    });

    it('should navigate to form page when user clicks begin', async (): Promise<void> => {
      renderApp(config);

      const beginButton: HTMLElement = screen.getByRole('button');
      await act(() => fireEvent.click(beginButton));

      const formPage: HTMLElement = screen.getByRole('form-page-container');

      expect(formPage).toBeInTheDocument();
      expect(formPage).toHaveTextContent('submit');
    });

    it('should navigate to results page when user submits form and results retrieved successfully', async (): Promise<void> => {
      await simulateUserSubmittingForm(200);

      const energyGenYearCard: HTMLElement = await waitFor(() =>
        screen.getByRole('energy-gen-per-year-card')
      );
      expect(energyGenYearCard).toHaveTextContent(
        `annual energy generation of ${validResponse.EnergyGenPerYear} kWh`
      );
      const savingsCard: HTMLElement = await waitFor(() =>
        screen.getByRole('savings-per-year-card')
      );
      expect(savingsCard).toHaveTextContent(
        `annual energy savings of ${validResponse.SavingsPerYear.Amount} ${validResponse.SavingsPerYear.CurrencyCode}`
      );
      const energyGenMonthCard: HTMLElement = await waitFor(() =>
        screen.getByRole('energy-gen-per-month-card')
      );
      expect(energyGenMonthCard).toHaveTextContent(
        `energy generation by month - January: ${validResponse.EnergyGenPerMonth.January} kWh February: ${validResponse.EnergyGenPerMonth.February} kWh`
      );
    });

    it('should navigate back from results page to form page if the user clicks back in the browser', async (): Promise<void> => {
      await simulateUserSubmittingForm(200);

      const resultPage: HTMLElement = await waitFor(() =>
        screen.getByRole('result-page-container')
      );
      expect(resultPage).toBeInTheDocument();

      act(() => {
        appRouter.navigate(-1);
      });

      const formPage: HTMLElement = await waitFor(() => screen.getByRole('form-page-container'));
      expect(formPage).toBeInTheDocument();
    });
  });

  describe('unhappy path tests', () => {
    it('should navigate to error page when user submits the form and result are not retrieved successfully', async (): Promise<void> => {
      await simulateUserSubmittingForm(500);

      const errorPage: HTMLElement = await waitFor(() => screen.getByRole('error-page-container'));

      expect(errorPage).toBeInTheDocument();
      expect(errorPage).toHaveTextContent(ERROR_MESSAGES.API_REQUEST_ERROR);
    });

    it('should navigate to error page when user navigates to unknown path', async (): Promise<void> => {
      renderApp(config);
      await waitFor(() => appRouter.navigate('/invalid'));

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
