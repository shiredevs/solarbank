# Solarbank
## Quickstart
### Client
#### Prerequisites
- Node.js - see [.nvmrc](client/.nvmrc) for the current version used

If you're using [node version manager](https://github.com/nvm-sh/nvm) install and use the required node version with:
```shell
nvm install && nvm use
```

#### Starting the client 
To start the client, navigate to the [client](./client) folder and pull the dependencies by running:
```shell
npm ci
```

The client runs locally with `https` enabled, to provide certificates you need to copy this
[template](./client/environment/template.env.local) and rename as `.env.local`. Then update `SSL_CERT_PATH` to
point at your certificate and `SSL_KEY_FILE` to point at your private key. 

If you want to generate trusted local certificates then follow this [guide](docs/generating-trusted-certs.md) and update `HOST` in `.env.local` if you have specified a custom host. 

To serve the client at https://localhost:3030, run:
```shell
npm run start:local
```
To run the tests:
```shell
npm run test
```

### Server
#### Prerequisites 
- Java 21
- Maven

The server runs locally with `https` enabled, so you will need trusted or self-signed PKCS12 certificates. 

To provide certificates, you need to copy this [template](./server/src/main/resources/template-application-local-yml) and rename it to `application-local.yml`. 
Then update `key-store` to point at your `.p12` keystore file and `key-store-password` with your store password.

If you want to generate trusted local certificates, then follow this [guide](docs/generating-trusted-certs.md). 
The certificates need to be generated and stored in `.p12` format using the `--pkcs12` flag.

To start the springboot server `cd` into the [server](./server) folder and run:
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
If you have specified a custom host for your certificates, then navigate to the valid host, else use https://localhost:8080.

To run the tests:
```shell
mvn test
```
### Running with docker locally
#### Prerequisites
- docker engine

To start the application with docker, you must provide the required environment variables. 
To do this copy this [template](template.env.local) and rename as `.env`. Then update the required variables.

Run the below command from the project root to start the application:
```shell
docker compose up -d
```
If you have specified a custom host for your certificates, then navigate to the valid host, else use https://localhost:3030.

To stop the application run:
```shell
docker compose stop
```
To stop and delete the container, run:
```shell
docker compose down
```
To rebuild the image:
```shell
docker compose build
```
