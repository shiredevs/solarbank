//TODO: replace with logging implementation
import { AxiosResponse, InternalAxiosRequestConfig } from 'axios';

const logError = (error: Error): void => {
  console.log(`${error.stack ? error.stack : error.message}`);
};

const logRequest = (request: InternalAxiosRequestConfig): void => {
  const method: string = request?.method ? request.method : 'unknown type of';
  const url: string = request?.url ? request.url : 'an unknown url';
  const body: string | undefined = request?.data ? JSON.stringify(request.data) : undefined;
  console.log(`${method} request made to ${url} ${body ? `with body ${body}` : 'with no body'}`);
};

const logResponse = (response: AxiosResponse): void => {
  const path: string = response?.request?.path ? response.request.path : 'unknown path';
  const body: string | undefined = response?.data ? JSON.stringify(response.data) : undefined;

  console.log(
    `${response?.status} response from request to ${path} ${body ? `with body ${body}` : 'with no body'}`
  );
};

export { logError, logRequest, logResponse };
