import { createMemoryRouter } from 'react-router-dom';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import React from 'react';
import router from './components/router/AppRouter';
import './utils/IconLibrary';
import { ROUTE_PATHS } from './components/router/AppRoutes';
import { Router } from '@remix-run/router';
import ERROR_MESSAGES from './components/error/ErrorMessages';
import App from './App';

describe('app integration tests', (): void => {
  let inMemoryRouter: Router;

  beforeEach((): void => {
    inMemoryRouter = createMemoryRouter(router.routes, { initialEntries: [ROUTE_PATHS.ROOT] });

    render(<App router={inMemoryRouter} />);

    expect(screen.getByRole('landing-page-container')).toBeInTheDocument();
  });

  it('should navigate to form page when user clicks begin', (): void => {
    const button: HTMLElement = screen.getByRole('button');
    fireEvent.click(button);

    const formPage: HTMLElement = screen.getByRole('paragraph');
    expect(formPage).toBeInTheDocument();
    expect(formPage).toHaveTextContent('form...');
  });

  it('should navigate to error page when user navigates to unknown path', async (): Promise<void> => {
    await waitFor(() => inMemoryRouter.navigate('/invalid'));

    const errorPage: HTMLElement = screen.getByRole('paragraph');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(ERROR_MESSAGES.PAGE_NOT_FOUND);
  });

  // todo: test unhandled error path once an error can be thrown inside the application, most likely via a failed api call to the backend
});
