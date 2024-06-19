import React, { JSX } from 'react';
import { useLocation } from 'react-router-dom';
import { ERROR_MESSAGES } from '../error/ErrorMessages';
import { Location } from '@remix-run/router';

const ErrorPage = (): JSX.Element => {
  const location: Location = useLocation();

  return (
    <div
      data-testid="error-page"
    >
      {location?.state?.errorMessage || ERROR_MESSAGES.INTERNAL_SERVER_ERROR}
    </div>
  )
}

export default ErrorPage;
