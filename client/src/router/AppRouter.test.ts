import router from './AppRouter';
import { ROUTES, ROUTE_PATHS } from './AppRoutes';
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
});
