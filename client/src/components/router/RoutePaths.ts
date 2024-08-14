type RoutePaths = {
  [key: string]: string;
};

const ROUTE_PATHS: RoutePaths = {
  ROOT: '/',
  FORM: '/form',
  OTHER: '*'
};

export type { RoutePaths };

export default ROUTE_PATHS;
