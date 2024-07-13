import ErrorPage from './ErrorPage';
import { render, screen } from '@testing-library/react';
import ERROR_MESSAGES from '../components/error/ErrorMessages';

describe('error page tests', () => {
  const expectedPageNotFoundMessage: string = ERROR_MESSAGES.PAGE_NOT_FOUND;
  const expectedInternalErrorMessage: string = ERROR_MESSAGES.INTERNAL_SERVER_ERROR;

  it('should render the page with default error message when no error message is passed in location state', () => {
    render(<ErrorPage message={''} />);

    const renderedErrorPage: HTMLElement = screen.getByRole('paragraph');
    expect(renderedErrorPage).toBeInTheDocument();
    expect(renderedErrorPage).toHaveTextContent(expectedInternalErrorMessage);
  });

  it('should render the page with custom error message when an error message is passed in location state', () => {
    render(<ErrorPage message={expectedPageNotFoundMessage} />);

    const renderedErrorPage: HTMLElement = screen.getByRole('paragraph');
    expect(renderedErrorPage).toBeInTheDocument();
    expect(renderedErrorPage).toHaveTextContent(expectedPageNotFoundMessage);
  });
});
