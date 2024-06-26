const logError = (error: Error): void => {
  //TODO: replace with logging implementation
  console.log(`${error.stack ? error.stack : error.message}`);
};

export default logError;
