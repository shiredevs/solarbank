type RoutePaths = {
  [key: string]: string;
};

const ROUTE_PATHS: RoutePaths = {
  ROOT: '/',
  FORM: '/form',
  RESULT: '/result',
  OTHER: '*'
};

export type { RoutePaths };

export default ROUTE_PATHS;
