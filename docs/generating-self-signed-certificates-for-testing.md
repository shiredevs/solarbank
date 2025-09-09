# Generating self-signed certificates for testing
## Prerequisites
- openssl on the command line

## Create certificate
This will generate self-signed certificates which should be used for testing only.

- Generate a private key:
```shell
openssl genpkey -algorithm RSA -out test-key.pem -aes256
```
- Generate a self-signed certificate using the key valid for 365 days:
```shell
openssl req -x509 -key test-key.pem -out test-cert.pem -days 365
```
- Package the key and cert into a .p12 keystore:
```shell
openssl pkcs12 -export -in cert.pem -inkey key.pem -out test-key-store.p12
```
