import { createMemoryRouter } from 'react-router-dom';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import React from 'react';
import router from '../router/AppRouter';
import '../icon/IconLibrary';
import { ROUTE_PATHS } from '../router/AppRoutes';
import { Router } from '@remix-run/router';
import { ERROR_MESSAGES } from './error/ErrorMessages';
import App from './App';

describe('app integration tests', (): void => {
  let inMemoryRouter: Router;

  beforeEach((): void => {
    inMemoryRouter = createMemoryRouter(
      router.routes,
      {initialEntries: [ROUTE_PATHS.ROOT]}
    );

    render(<App router={inMemoryRouter} />);

    expect(screen.getByTestId('landing-page')).toBeInTheDocument();
  });

  it('should navigate to form page when user clicks begin', (): void => {
    const button: HTMLElement = screen.getByText('begin');
    fireEvent.click(button);

    expect(screen.getByTestId('form-page')).toBeInTheDocument();
  });

  it('should navigate to error page when user navigates to unknown path', async (): Promise<void> => {
    await waitFor(() => inMemoryRouter.navigate('/invalid'));

    const errorPage: HTMLElement = screen.getByTestId('error-page');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(ERROR_MESSAGES.PAGE_NOT_FOUND);
  });

  // todo: test unhandled error path once an error can be thrown inside the application, most likely via a failed api call to the backend
});
