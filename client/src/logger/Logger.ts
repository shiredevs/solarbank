
const logError = (error: Error): void => {
  //TODO: replace with logging implementation
  console.log(`${error.name}: ${error.message}\nTrace: ${error.stack}`);
}

export {
  logError
}
