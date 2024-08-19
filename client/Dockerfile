FROM node:20.15.1-alpine AS build-stage
ARG SERVER_URL
ARG CALCULATE_ENDPOINT
WORKDIR /solarbank/client
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build-stage /solarbank/client/build /usr/share/nginx/html
COPY --from=build-stage /solarbank/client/nginx/server.conf /etc/nginx/conf.d/server.conf.template
COPY --from=build-stage /solarbank/client/nginx/start-nginx.sh ./start-nginx.sh
EXPOSE ${PORT}
RUN chmod +x ./start-nginx.sh
CMD ["./start-nginx.sh"]
