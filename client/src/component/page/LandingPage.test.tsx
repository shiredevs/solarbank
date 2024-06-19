import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import LandingPage from './LandingPage';
import '../../icon/IconLibrary';
import * as router from 'react-router';
import { NavigateFunction, To } from 'react-router-dom';
import {ROUTE_PATHS}  from '../../router/AppRoutes';

describe('landing page tests', () => {
  let capturedRedirectPath: string;
  const mockNavigate: NavigateFunction = (path: To | number): void => {
    capturedRedirectPath = path as string;
  };

  beforeEach(() => {
    jest.spyOn(router, 'useNavigate')
      .mockImplementationOnce(() => mockNavigate);

    render(<LandingPage />);
  });

  it('renders header', () => {
    const heading: HTMLElement = screen.getByText(/Welcome to Solarbank/i);

    expect(heading).toBeInTheDocument();
  });

  it('renders landing page icon', () => {
    const icon: HTMLElement = screen.getByTestId('landing-page-icon');

    expect(icon).toBeInTheDocument();
    expect(icon).toHaveClass('icon');
    expect(icon.getAttribute('data-icon')).toBe('cloud-sun');
  });

  it('renders button', () => {
    const button: HTMLElement = screen.getByTestId('landing-page-button');

    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('button');
    expect(button).toHaveTextContent('begin');
  });

  it('button redirects user to form page when clicked', () => {
    const button: HTMLElement = screen.getByTestId('landing-page-button');

    fireEvent.click(button);

    expect(button).toBeInTheDocument();
    expect(capturedRedirectPath).toBe(ROUTE_PATHS.FORM);
  });
})
