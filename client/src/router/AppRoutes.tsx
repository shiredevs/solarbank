import React, {JSX} from 'react';
import LandingPage from '../component/page/LandingPage';
import FormPage from '../component/page/FormPage';
import ErrorPage from '../component/page/ErrorPage';
import { ERROR_MESSAGES } from '../component/error/ErrorMessages';
import { Navigate } from 'react-router-dom';
import ErrorHandler from '../component/error/ErrorHandler';

type RouteType = {
  path: string,
  element: JSX.Element
  errorElement?: JSX.Element
}

type RoutePaths = {
  [key: string]: string;
}

const ROUTE_PATHS: RoutePaths = {
  ROOT: '/',
  FORM: '/form',
  ERROR: '/error',
  OTHER: '*'
};

const ROUTES: RouteType[] = [
  {
    path: ROUTE_PATHS.ROOT,
    element: <LandingPage />,
    errorElement: <ErrorHandler />
  },
  {
    path: ROUTE_PATHS.FORM,
    element: <FormPage />
  },
  {
    path: ROUTE_PATHS.ERROR,
    element: <ErrorPage />
  },
  {
  path: ROUTE_PATHS.OTHER,
  element: <Navigate
    to={ROUTE_PATHS.ERROR}
    replace={true}
    state={{errorMessage: ERROR_MESSAGES.PAGE_NOT_FOUND}}
  />
  }
]

export { ROUTE_PATHS, ROUTES };
export type { RouteType, RoutePaths };
