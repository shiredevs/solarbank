export type Config = {
  [key: string]: string | undefined;
};

const validate = (config: Config): boolean => {
  for (const property in config) {
    if (config[property] === undefined) {
      return false;
    }
  }
  return true;
};

export { validate };
