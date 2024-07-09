// noinspection JSUnusedGlobalSymbols
export default {
  preset: 'ts-jest',
  testEnvironment: 'jest-environment-jsdom',
  transform: { '^.+\\.(ts|tsx)$': 'ts-jest' },
  setupFilesAfterEnv: ['./src/setupTests.ts'],
  moduleNameMapper: { '\\.css$': 'identity-obj-proxy' }
};
