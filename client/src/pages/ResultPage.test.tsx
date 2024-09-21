import ResultPage from './ResultPage';
import { CalculateResponse } from '../clients/CalculateClient';
import { render, screen } from '@testing-library/react';
import * as router from 'react-router';
import { Location } from 'react-router-dom';
import { validResponse } from '../testSetup/CalculateResponse';

const mockLocation = (mockResult: CalculateResponse): void => {
  const mockLocation: Location = {
    state: mockResult,
    pathname: '/form',
    search: '/form',
    hash: 'hash',
    key: '1234'
  };
  jest.spyOn(router, 'useLocation').mockReturnValue(mockLocation);
};

describe('result page tests', () => {
  it('renders result page as expected when all results are defined', () => {
    const mockResult: CalculateResponse = validResponse;
    mockLocation(mockResult);

    render(<ResultPage />);

    expect(screen.getByRole('result-page-container')).toBeInTheDocument();
    const energyGenPerYearCard: HTMLElement = screen.getByRole('energy-gen-per-year-card');
    expect(energyGenPerYearCard).toBeInTheDocument();
    expect(energyGenPerYearCard).toHaveTextContent(
      `annual energy generation of ${mockResult.EnergyGenPerYear}`
    );
    const savingsPerYearCard: HTMLElement = screen.getByRole('savings-per-year-card');
    expect(savingsPerYearCard).toBeInTheDocument();
    expect(savingsPerYearCard).toHaveTextContent(
      `annual energy savings of ${mockResult.SavingsPerYear.Amount} ${mockResult.SavingsPerYear.CurrencyCode}`
    );
    const energyGenPerMonthCard: HTMLElement = screen.getByRole('energy-gen-per-month-card');
    expect(energyGenPerMonthCard).toBeInTheDocument();
    expect(energyGenPerMonthCard).toHaveTextContent(
      'energy generation by month - January: 500 kWh February: 500 kWh'
    );
  });

  it('renders result page as expected when some results are defined', () => {
    const mockResult: CalculateResponse = {
      EnergyGenPerYear: NaN,
      EnergyGenPerMonth: {
        Jan: NaN,
        Feb: 1000,
        Mar: 1000,
        Apr: NaN
      },
      SavingsPerYear: {
        CurrencyCode: '',
        Amount: 5000
      }
    };
    mockLocation(mockResult);

    render(<ResultPage />);

    expect(screen.getByRole('result-page-container')).toBeInTheDocument();
    const energyGenPerYearCard: HTMLElement = screen.getByRole('energy-gen-per-year-card');
    expect(energyGenPerYearCard).toBeInTheDocument();
    expect(energyGenPerYearCard).toHaveTextContent('not available');
    const savingsPerYearCard: HTMLElement = screen.getByRole('savings-per-year-card');
    expect(savingsPerYearCard).toBeInTheDocument();
    expect(savingsPerYearCard).toHaveTextContent('not available');
    const energyGenPerMonthCard: HTMLElement = screen.getByRole('energy-gen-per-month-card');
    expect(energyGenPerMonthCard).toBeInTheDocument();
    expect(energyGenPerMonthCard).toHaveTextContent(
      'energy generation by month - Jan: unknown kWh Feb: 1000 kWh Mar: 1000 kWh Apr: unknown kWh'
    );
  });
});
