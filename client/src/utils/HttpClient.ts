import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import ApiRequestError from '../components/error/types/ApiRequestError';
import { logRequest, logResponse } from './Logger';

axios.interceptors.request.use((request: InternalAxiosRequestConfig) => {
  logRequest(request);

  return request;
});

axios.interceptors.response.use((response: AxiosResponse) => {
  logResponse(response);

  return response;
});

const post = async <B extends object>(url: string, body: B): Promise<AxiosResponse> => {
  try {
    return await axios.post(url, body);
  } catch (err) {
    throw new ApiRequestError(err as Error);
  }
};

export { post };
