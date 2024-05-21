# Solarbank
## Quickstart
### Client
For first time setup navigate to the [client](./client) folder. The client requires the latest LTS version of node, see [.nvmrc](client/.nvmrc) for current version used.

If your using [nvm](https://github.com/nvm-sh/nvm) simply install and use required node version by running:
```shell
nvm install && use
```
To pull dependencies run:
```shell
npm ci
```
To start the ui run:
```shell
npm run start
```
The ui will be served at http://localhost:3000.

To run the tests run:
```shell
npm run test
```
To build a static bundle:
```shell
npm run build
```
