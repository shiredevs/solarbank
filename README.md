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

To start the Springboot server `cd` into the [server](./server) folder and run:
```shell
mvn spring-boot:run
```
To run the tests:
```shell
mvn test
```
