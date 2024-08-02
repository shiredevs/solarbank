import { containsUndefined } from './ConfigValidator';

describe('config validator tests', () => {
  it('request to validate, no undefined properties, returns false', () => {
    containsUndefined({ var1: 'defined', var2: 'defined' });
  });

  it('request to validate, undefined properties, returns true', () => {
    containsUndefined({ var1: 'a', var2: undefined });
  });
});
