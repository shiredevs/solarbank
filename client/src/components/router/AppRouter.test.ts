import router from './AppRouter';
import ROUTES from './AppRoutes';
import ROUTE_PATHS from './RoutePaths';
import { AgnosticDataRouteObject } from '@remix-run/router';

jest.mock('../../clients/config/CalculateConfig', () => ({
  config: {
    SERVER_URL: 'https://localhost:8080',
    CALCULATE_ENDPOINT: '/api/V1/calculate'
  }
}));

describe('app router tests', () => {
  it('Should contain expected routes', () => {
    const mappedRoutes: AgnosticDataRouteObject[] = router.routes;
    const expectedPaths: string[] = Object.values(ROUTE_PATHS);

    expect(mappedRoutes.length).toEqual(ROUTES.length);
    mappedRoutes.forEach(route => {
      const mappedPath: string = route.path as string;
      expect(expectedPaths).toContain(mappedPath);
    });
  });

  it('Should include error boundary for all routes except invalid routes', () => {
    const mappedRoutes: AgnosticDataRouteObject[] = router.routes;

    mappedRoutes.forEach(route => {
      const mappedPath: string = route.path as string;
      const errorBoundary: boolean = route.hasErrorBoundary as boolean;

      if (mappedPath === ROUTE_PATHS.OTHER) {
        expect(errorBoundary).toBe(false);
      } else {
        expect(errorBoundary).toBe(true);
      }
    });
  });
});
