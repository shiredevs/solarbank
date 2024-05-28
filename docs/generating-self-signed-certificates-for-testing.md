# Generating self-signed certificates for testing
## Prequisities
- openssl on command line

## Create certificate
This will generate sef-signed certificate which should be used for testing only.

- Generate a private key:
```shell
openssl genpkey -algorithm RSA -out test-key.pem -aes256
```
- Generate self signed cert using key valid for 365 days:
```shell
openssl req -x509 -key test-key.pem -out test-cert.pem -days 365
```
- Package the key and cert into a .p12 keystore:
```shell
openssl pkcs12 -export -in cert.pem -inkey key.pem -out test-key-store.p12
```
