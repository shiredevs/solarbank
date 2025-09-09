# Adding solarbank.local as a host
## Prerequisite
- linux distribution
- windows

## Setup Linux
To add a custom host, open:
```text
/etc/hosts
```
And add:
```text
127.0.0.1       solarbank.local
```
## Setup Windows
To add a custom host, open:
```text
"C:\Windows\System32\drivers\etc\hosts"
```
And add:
```text
127.0.0.1       solarbank.local
```
Now instead of using `localhost` for the client and server you can instead:
- specify `HOST=solarbank.local` in your `.env.local` [here](../client/environment) for the client
- if you're having connection issues for the server in the browser, it may be that it's trying to use ipv6. 
You can try adding this in hosts in addition to the above:

```text
::1       solarbank.local
```