import React from 'react';
import { render, screen } from '@testing-library/react';
import PageNotFoundError from './types/PageNotFoundError';
import ErrorHandler from './ErrorHandler';
import { ERROR_MESSAGES } from './ErrorMessages';
import * as logger from '../../logger/Logger';
import * as router from 'react-router';
import InternalServerError from './types/InternalServerError';

describe('error handler tests', () => {
  let loggedError: Error;

  beforeEach(() => {
    jest.spyOn(logger, 'logError')
      .mockImplementationOnce((error)=> {
       loggedError = error;
    });
  });

  it('renders expected error page when user navigate to an invalid url', () => {
    const pageError: PageNotFoundError = new PageNotFoundError();

    render(<ErrorHandler error={pageError} />);

    const errorPage: HTMLElement = screen.getByTestId( 'error-page');
    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(pageError);
    expect(errorPage).toBeInTheDocument();
    expect(errorPage.textContent).toBe(ERROR_MESSAGES.PAGE_NOT_FOUND);
  });

  it('renders expected error page when the application crashes', () => {
    const childError: TypeError = new TypeError('application crash error');
    jest.spyOn(router, 'useRouteError')
      .mockReturnValue(childError);
    const parentError: InternalServerError = new InternalServerError();

    render(<ErrorHandler error={parentError} />);

    const errorPage: HTMLElement = screen.getByTestId( 'error-page');
    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(childError);
    expect(errorPage).toBeInTheDocument();
    expect(errorPage.textContent).toBe(ERROR_MESSAGES.INTERNAL_SERVER_ERROR);
  });

  it('renders expected error page even if the error message is undefined', () => {
    const error: Error = new Error(undefined);
    render(<ErrorHandler error={error} />);

    const errorPage: HTMLElement = screen.getByTestId( 'error-page');
    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(error);
    expect(errorPage).toBeInTheDocument();
    expect(errorPage.textContent).toBe(ERROR_MESSAGES.INTERNAL_SERVER_ERROR);
  });

  it('renders expected error page when the child error is undefined', () => {
    jest.spyOn(router, 'useRouteError')
      .mockReturnValue(undefined);

    const parentError: InternalServerError = new InternalServerError();

    render(<ErrorHandler error={parentError} />);

    const errorPage: HTMLElement = screen.getByTestId( 'error-page');
    expect(loggedError).toBeUndefined();
    expect(errorPage).toBeInTheDocument();
    expect(errorPage.textContent).toBe(ERROR_MESSAGES.INTERNAL_SERVER_ERROR);
  });
});
