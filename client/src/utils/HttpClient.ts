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

const post = async <RequestData extends object, ResponseData extends object>(
  url: string,
  request: RequestData
): Promise<AxiosResponse<ResponseData, never>> => {
  try {
    return await axios.post(url, request);
  } catch (err) {
    throw new ApiRequestError(err as Error);
  }
};

export { post };
