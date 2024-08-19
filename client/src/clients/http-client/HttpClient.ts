import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import ApiRequestError from '../../components/error/types/ApiRequestError';
import { logHttpError, logRequest, logResponse } from '../../utils/Logger';

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
    logHttpError<RequestData>(url, request, err as Error);

    throw new ApiRequestError(err as Error);
  }
};

export { post };
