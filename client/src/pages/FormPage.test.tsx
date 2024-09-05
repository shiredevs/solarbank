import FormPage from './FormPage';
import { fireEvent, render, screen, act } from '@testing-library/react';
import * as CalculateClient from '../clients/CalculateClient';
import ApiRequestError from '../components/error/types/ApiRequestError';
import InternalServerError from '../components/error/types/InternalServerError';
import ERROR_MESSAGES from '../components/error/ErrorMessages';
import { CalculateResponse } from '../clients/CalculateClient';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import ROUTE_PATHS from '../components/router/RoutePaths';
import ResultPage from './ResultPage';

const triggerFormSubmission = async () => {
  const button: HTMLElement = screen.getByRole('button');

  await act(async () => {
    fireEvent.click(button);
  });
};

describe('form page tests', () => {
  it('should render form page with a submit button if the form has not been submitted yet', () => {
    render(<FormPage />);

    const formPage: HTMLElement = screen.getByRole('form-page-container');
    expect(formPage).toBeInTheDocument();
    const button: HTMLElement = screen.getByRole('button');
    expect(button).toBeInTheDocument();
    expect(button).toHaveTextContent('submit');
  });

  it('should render error page when the form is submitted but an error occurs', async () => {
    jest
      .spyOn(CalculateClient, 'doCalculate')
      .mockRejectedValue(new ApiRequestError(new InternalServerError()));

    render(<FormPage />);
    await triggerFormSubmission();

    const renderedErrorPage: HTMLElement = screen.getByRole('paragraph');
    expect(renderedErrorPage).toHaveTextContent(ERROR_MESSAGES.API_REQUEST_ERROR);
  });

  it('should render results page when form is submitted and result is successfully retrieved', async () => {
    const mockResponse: CalculateResponse = {
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
    jest.spyOn(CalculateClient, 'doCalculate').mockResolvedValueOnce(mockResponse);

    render(
      <MemoryRouter initialEntries={[ROUTE_PATHS.FORM]}>
        <Routes>
          <Route path={ROUTE_PATHS.FORM} element={<FormPage />} />
          <Route path={ROUTE_PATHS.RESULT} element={<ResultPage />} />
        </Routes>
      </MemoryRouter>
    );

    const renderedFormPage: HTMLElement = screen.getByRole('form-page-container');
    expect(renderedFormPage).toBeInTheDocument();

    await triggerFormSubmission();

    const renderedResultsPage: HTMLElement = screen.getByRole('result-page-container');
    expect(renderedResultsPage).toBeInTheDocument();
  });
});
