# Adding solarbank.local as a host
## Prerequisite
- linux distribution
## Setup
To add a custom host open:
```text
/etc/hosts
```
And add:
```text
127.0.0.1       solarbank.local
```

Now instead of using `localhost` for the client and server you can instead:
- specify `HOST=solarbank.local` in your `.env.local` [here](../client/environment) for the client
- specify `server.address: solarbank.local` in your `application-local.yml` [here](../server/src/main/resources) for the server
