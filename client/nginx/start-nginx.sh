#!/bin/sh
set -e

# replace env references with actual values and output a new server.conf
envsubst '${PORT} ${SSL_CRT_FILE} ${SSL_KEY_FILE}' < /etc/nginx/conf.d/server.conf.template > /etc/nginx/conf.d/server.conf

exec nginx -g 'daemon off;'
