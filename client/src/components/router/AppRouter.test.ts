import router from './AppRouter';
import ROUTES from './AppRoutes';
import ROUTE_PATHS from './RoutePaths';
import { AgnosticDataRouteObject } from '@remix-run/router';

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

  it('Should include error boundary for root route only', () => {
    const mappedRoutes: AgnosticDataRouteObject[] = router.routes;

    mappedRoutes.forEach(route => {
      const mappedPath: string = route.path as string;
      const errorBoundary: boolean = route.hasErrorBoundary as boolean;

      if (mappedPath === ROUTE_PATHS.ROOT) {
        expect(errorBoundary).toBe(true);
      } else {
        expect(errorBoundary).toBe(false);
      }
    });
  });
});
