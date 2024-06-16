import React from 'react';
import { render, screen } from '@testing-library/react';
import LandingPage from './LandingPage';
import '../icons/IconLibrary';

describe('landing page tests', () => {

  beforeEach(() => {
    render(<LandingPage />);
  })

  it('renders header', () => {
    const heading: HTMLElement = screen.getByText(/Welcome to Solarbank/i);

    expect(heading).toBeInTheDocument();
  })

  it('renders landing page icon', () => {
    const icon: HTMLElement = screen.getByTestId('landing-page-icon');

    expect(icon).toBeInTheDocument();
    expect(icon).toHaveClass('size-40');
    expect(icon.getAttribute('data-icon')).toBe('cloud-sun');
  })

  it('renders button', () => {
    const button: HTMLElement = screen.getByTestId('landing-page-button');

    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('bg-orange-800 text-white font-bold py-2 px-4 border border-blue-700 rounded');
    expect(button).toHaveTextContent('begin');
  })
})
