import { validate } from './ConfigValidator';

describe('config validator tests', () => {
  it('request to validate, no undefined properties, returns true', () => {
    expect(validate({ var1: 'defined', var2: 'defined' })).toBeTruthy();
  });

  it('request to validate, undefined properties, returns false', () => {
    expect(validate({ var1: 'a', var2: undefined })).toBeFalsy();
  });
});
