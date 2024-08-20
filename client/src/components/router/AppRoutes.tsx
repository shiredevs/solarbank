import { JSX } from 'react';
import LandingPage from '../../pages/LandingPage';
import FormPage from '../../pages/FormPage';
import ErrorHandler from '../error/ErrorHandler';
import PageNotFoundError from '../error/types/PageNotFoundError';
import InternalServerError from '../error/types/InternalServerError';
import ROUTE_PATHS from './RoutePaths';

export type RouteType = {
  path: string;
  element: JSX.Element;
  errorElement?: JSX.Element;
};

const ROUTES: RouteType[] = [
  {
    path: ROUTE_PATHS.ROOT,
    element: <LandingPage />,
    errorElement: <ErrorHandler error={new InternalServerError()} /> // only applies on exceptions thrown during render
  },
  {
    path: ROUTE_PATHS.FORM,
    element: <FormPage />,
    errorElement: <ErrorHandler error={new InternalServerError()} />
  },
  {
    path: ROUTE_PATHS.OTHER,
    element: <ErrorHandler error={new PageNotFoundError()} />
  }
];

export default ROUTES;
