import { JSX } from 'react';
import ErrorPage from '../../pages/ErrorPage';
import { useRouteError } from 'react-router-dom';
import InternalServerError from './types/InternalServerError';
import logError from '../../utils/Logger';

type ErrorHandlerProps = {
  error: Error;
};

const ErrorHandler = (props: ErrorHandlerProps): JSX.Element => {
  const error: Error = props.error;
  const message: string = error.message;
  let childError: Error;

  if (error instanceof InternalServerError) {
    childError = useRouteError() as Error;
  } else {
    childError = error;
  }

  logError(childError);

  return <ErrorPage message={message} />;
};

export default ErrorHandler;
