import React, { JSX } from 'react';
import LandingPage from '../component/page/LandingPage';
import FormPage from '../component/page/FormPage';
import ErrorHandler from '../component/error/ErrorHandler';
import PageNotFoundError from '../component/error/types/PageNotFoundError';
import InternalServerError from '../component/error/types/InternalServerError';

type RouteType = {
  path: string;
  element: JSX.Element;
  errorElement?: JSX.Element;
};

type RoutePaths = {
  [key: string]: string;
};

const ROUTE_PATHS: RoutePaths = {
  ROOT: '/',
  FORM: '/form',
  OTHER: '*'
};

const ROUTES: RouteType[] = [
  {
    path: ROUTE_PATHS.ROOT,
    element: <LandingPage />,
    errorElement: <ErrorHandler error={new InternalServerError()} />
  },
  {
    path: ROUTE_PATHS.FORM,
    element: <FormPage />
  },
  {
    path: ROUTE_PATHS.OTHER,
    element: <ErrorHandler error={new PageNotFoundError()} />
  }
];

export { ROUTE_PATHS, ROUTES };
export type { RouteType, RoutePaths };
