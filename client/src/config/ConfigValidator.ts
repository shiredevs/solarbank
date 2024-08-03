const containsUndefined = <Config extends object>(config: Config): boolean => {
  for (const property in config) {
    if (config[property] === undefined) {
      return true;
    }
  }
  return false;
};

export { containsUndefined };
