#!/bin/sh
PIDFILE="/var/run/haproxy.pid"
echo "DEBUG: restarting haproxy"
haproxy -f /tmp/haproxy.cfg -p "$PIDFILE" -D -sf $(cat $PIDFILE)
