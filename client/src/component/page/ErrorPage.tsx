import React, { JSX } from 'react';
import { ERROR_MESSAGES } from '../error/ErrorMessages';

type ErrorProps = {
  message: string
}

const ErrorPage = (props: ErrorProps): JSX.Element => {

  return (
    <div data-testid="error-page">
      {props?.message || ERROR_MESSAGES.INTERNAL_SERVER_ERROR}
    </div>
  )
}

export default ErrorPage;
