# Solarbank
## Quickstart
### Client
#### Prerequisites
- Node.js - see [.nvmrc](client/.nvmrc) for current version used

If your using [node version manager](https://github.com/nvm-sh/nvm) simply install and use required node version with:
```shell
nvm install && use
```

#### Starting the client 
For first time setup navigate to the [client](./client) folder and pull dependencies by running:
```shell
npm ci
```

The client runs locally with `https` enabled, to provide certificates you need to copy this
[template](./client/environment/template.env.local) and rename as `.env.local`. Then update `SSL_CERT_PATH` to
point at your certificate and `SSL_KEY_FILE` to point at your private key. 

If you want to generate trusted local certificates then follow this [guide](docs/generating-trusted-certs.md). 

To serve the client at https://localhost:3030 run:
```shell
npm run:local
```
To run the tests:
```shell
npm run test
```

### Server
#### Prerequisites 
- Java 21
- Maven

The server runs locally with `https` enabled and requires trusted or self-signed PKCS12 certificates. 

To provide certificates you need to copy this [template](./server/src/main/resources/template-application-local-yml) and rename it to `application-local.yml`. 
Then update `key-store` to point at your `.p12` keystore file and `key-store-password` with your store password.

If you want to generate trusted local certificates then follow this [guide](docs/generating-trusted-certs.md). 
The certificates need to be generated and stored in `.p12` format using the `--pkcs12` flag.

To start the springboot server at https://localhost:8080 `cd` into the [server](./server) folder and run:
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
To run the tests:
```shell
mvn test
```
