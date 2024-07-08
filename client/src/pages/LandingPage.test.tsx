import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import LandingPage from './LandingPage';
import '../utils/IconLibrary';
import * as router from 'react-router';
import { NavigateFunction, To } from 'react-router-dom';
import { ROUTE_PATHS } from '../components/router/AppRoutes';

describe('landing page tests', () => {
  let capturedRedirectPath: string;
  const mockNavigate: NavigateFunction = (path: To | number): void => {
    capturedRedirectPath = path as string;
  };

  beforeEach(() => {
    jest.spyOn(router, 'useNavigate').mockImplementationOnce(() => mockNavigate);

    render(<LandingPage />);
  });

  it('renders heading', () => {
    const heading: HTMLElement = screen.getByRole('heading');

    expect(heading).toBeInTheDocument();
    expect(heading).toHaveClass('heading');
    expect(heading).toHaveTextContent('Solarbank');
  });

  it('renders description', () => {
    const description: HTMLElement = screen.getByRole('paragraph');

    expect(description).toBeInTheDocument();
    expect(description).toHaveClass('paragraph');
    expect(description).toHaveTextContent('estimate your solar energy savings');
  });

  it('renders landing page icon', () => {
    const icon: HTMLElement = screen.getByRole('img');

    expect(icon).toBeInTheDocument();
    expect(icon).toHaveClass('icon');
    expect(icon.getAttribute('data-icon')).toBe('cloud-sun');
  });

  it('renders button', () => {
    const button: HTMLElement = screen.getByRole('button');

    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('button');
    expect(button).toHaveTextContent('begin');
  });

  it('button redirects user to form page when clicked', () => {
    const button: HTMLElement = screen.getByRole('button');

    fireEvent.click(button);

    expect(button).toBeInTheDocument();
    expect(capturedRedirectPath).toBe(ROUTE_PATHS.FORM);
  });
});
