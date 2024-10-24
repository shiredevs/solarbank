import { JSX } from 'react';
import ERROR_MESSAGES from '../components/error/ErrorMessages';

type ErrorProps = {
  message: string;
};

const ErrorPage = (props: ErrorProps): JSX.Element => {
  return (
    <div role="error-page-container">
      <p>{props?.message || ERROR_MESSAGES.INTERNAL_SERVER_ERROR}</p>
    </div>
  );
};

export default ErrorPage;
