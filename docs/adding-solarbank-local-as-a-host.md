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

Now instead of using `localhost` you can specify `host=solarbank.local` in your `.env.local` [here](../client/environment).
