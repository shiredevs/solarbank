import { server } from './MockServer';
import { http, HttpResponse } from 'msw';

export type RequestData = {
  [key: string]: string | number | object;
};

export type ResponseData = {
  [key: string]: string | number | object;
};

const isMatching = (actualRequest: ResponseData, expectedRequest: RequestData) => {
  const actual = JSON.stringify(actualRequest);
  const expected = JSON.stringify(expectedRequest);

  return actual === expected;
};

const interceptPost = (
  expectedRequest: RequestData,
  mockResponse: ResponseData,
  status: number,
  basePath: string,
  endpoint: string
): void => {
  server.use(
    http.post(`${basePath}${endpoint}`, async ({ request }) => {
      const actualRequest = (await request.json()) as RequestData;
      let response: HttpResponse;

      if (isMatching(actualRequest, expectedRequest)) {
        response = HttpResponse.json(mockResponse, { status: status });
      } else {
        response = HttpResponse.json('Request does not match expected', { status: 400 });
      }

      return response;
    })
  );
};

export { interceptPost };
