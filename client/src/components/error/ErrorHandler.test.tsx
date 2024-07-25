import { render, screen } from '@testing-library/react';
import PageNotFoundError from './types/PageNotFoundError';
import ErrorHandler from './ErrorHandler';
import ERROR_MESSAGES from './ErrorMessages';
import * as logger from '../../utils/Logger';
import * as router from 'react-router';
import InternalServerError from './types/InternalServerError';

describe('error handler tests', () => {
  let loggedError: Error;
  const INTERNAL_SERVER_ERROR_MESSAGE: string = ERROR_MESSAGES.INTERNAL_SERVER_ERROR;

  beforeEach(() => {
    jest.spyOn(logger, 'logError').mockImplementationOnce(error => {
      loggedError = error;
    });
  });

  it('renders expected error page when user navigate to an invalid url', () => {
    const pageError: PageNotFoundError = new PageNotFoundError();

    render(<ErrorHandler error={pageError} />);

    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(pageError);
    const errorPage: HTMLElement = screen.getByRole('paragraph');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(ERROR_MESSAGES.PAGE_NOT_FOUND);
  });

  it('renders expected error page when the application crashes', () => {
    const childError: TypeError = new TypeError('application crash error');
    jest.spyOn(router, 'useRouteError').mockReturnValue(childError);
    const parentError: InternalServerError = new InternalServerError();

    render(<ErrorHandler error={parentError} />);

    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(childError);
    const errorPage: HTMLElement = screen.getByRole('paragraph');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(INTERNAL_SERVER_ERROR_MESSAGE);
  });

  it('renders expected error page even if the error message is undefined', () => {
    const error: Error = new Error(undefined);
    render(<ErrorHandler error={error} />);

    expect(loggedError).toBeDefined();
    expect(loggedError).toBe(error);
    const errorPage: HTMLElement = screen.getByRole('paragraph');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(INTERNAL_SERVER_ERROR_MESSAGE);
  });

  it('renders expected error page when the child error is undefined', () => {
    jest.spyOn(router, 'useRouteError').mockReturnValue(undefined);

    const parentError: InternalServerError = new InternalServerError();

    render(<ErrorHandler error={parentError} />);

    expect(loggedError).toBeUndefined();
    const errorPage: HTMLElement = screen.getByRole('paragraph');
    expect(errorPage).toBeInTheDocument();
    expect(errorPage).toHaveTextContent(INTERNAL_SERVER_ERROR_MESSAGE);
  });
});
