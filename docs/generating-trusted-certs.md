# Generating trusted certificates for local development
## Prerequisites
- `mkcert` on command line
## First time setup
First create a local CA and root certificate:
```shell
mkcert -install
```
To see the location of your CA certificate:
```shell
mkcert -CAROOT
```

## Generating certificates
To generate SSL certs signed by your local CA:
```shell
mkcert <hostname> # for example localhost
```
This will generate a certificate and key `.pem` in the current directory, signed by your local CA.
If you want to generate a `.p12` file then you can run with `--pkcs12` flag. The password will default to `changeit`.

## Troubleshooting
You may need to manually add the local CA to your browser of choice. For example in Firefox
you go to:

`settings -> privacy & security -> view certificates -> authorities -> import`

Point at the location of your root certificate (can be found with `mkcert -caroot`).
