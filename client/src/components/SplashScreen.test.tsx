import React from 'react';
import { render, screen } from '@testing-library/react';
import SplashScreen from './SplashScreen';

test('renders learn react link', () => {
  render(<SplashScreen />);
  const linkElement: HTMLElement = screen.getByText(/Welcome to Solarbank/i);
  expect(linkElement).toBeInTheDocument();
});
