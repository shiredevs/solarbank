import React from 'react';
import { createBrowserRouter, createRoutesFromElements, Route } from 'react-router-dom';
import { ROUTES, RouteType } from './AppRoutes';
import { Router } from '@remix-run/router';

const router: Router = createBrowserRouter(
  createRoutesFromElements(
    ROUTES.map((route: RouteType) => (
      <Route
        path={route.path}
        element={route.element}
        errorElement={route.errorElement ? route.errorElement : undefined}
      />
    ))
  )
);

export default router;
