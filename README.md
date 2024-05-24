# Solarbank
## Quickstart
### Client
For first time setup navigate to the [client](./client) folder. The client requires the latest LTS version of node, see [.nvmrc](client/.nvmrc) for current version used.

If your using [nvm](https://github.com/nvm-sh/nvm) simply install and use required node version with:
```shell
nvm install && use
```

Then pull dependencies by running:
```shell
npm ci
```

The client runs locally with `https` enabled, to use a self-signed certificate you need to copy this
[template](./client/environment/template.env.local) and rename as `.env.local` and update `SSL_CERT_PATH` to
point at your self-signed certificate and `SSL_KEY_FILE` to point at your private key file.

To serve the client at https://localhost:3030 run:
```shell
npm run:local
```
