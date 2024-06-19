import React from 'react';
import ErrorPage from './ErrorPage';
import { render, screen } from '@testing-library/react';
import { ERROR_MESSAGES } from '../error/ErrorMessages';
import * as router from 'react-router';
import { Location } from '@remix-run/router';

describe('error page tests', () => {
  const mockLocation: Location = {
    state: {},
    key: '',
    pathname: '',
    hash: '',
    search: ''
  }
  const expectedPageNotFoundMessage: string = ERROR_MESSAGES.PAGE_NOT_FOUND;
  const expectedInternalErrorMessage: string = ERROR_MESSAGES.INTERNAL_SERVER_ERROR;

  it('should render the page with default error message when no error message is passed in location state', () => {
    mockLocation.state = {};
    jest.spyOn(router, 'useLocation')
      .mockImplementationOnce(() => mockLocation);

    render(<ErrorPage />);

    const renderedErrorPage: HTMLElement = screen.getByTestId('error-page');
    expect(renderedErrorPage).toBeInTheDocument();
    expect(renderedErrorPage)
      .toHaveTextContent(expectedInternalErrorMessage);
  });

  it('should render the page with default error message when location state is undefined', () => {
    mockLocation.state = undefined;
    jest.spyOn(router, 'useLocation')
      .mockImplementationOnce(() => mockLocation);

    render(<ErrorPage />);

    const renderedErrorPage: HTMLElement = screen.getByTestId('error-page');
    expect(renderedErrorPage).toBeInTheDocument();
    expect(renderedErrorPage)
      .toHaveTextContent(expectedInternalErrorMessage);
  });

  it('should render the page with custom error message when an error message is passed in location state', () => {
    mockLocation.state = {errorMessage: expectedPageNotFoundMessage};
    jest.spyOn(router, 'useLocation')
      .mockImplementationOnce(() => mockLocation);

    render(<ErrorPage />);

    const renderedErrorPage: HTMLElement = screen.getByTestId('error-page');
    expect(renderedErrorPage).toBeInTheDocument();
    expect(renderedErrorPage)
      .toHaveTextContent(expectedPageNotFoundMessage);
  });
});
