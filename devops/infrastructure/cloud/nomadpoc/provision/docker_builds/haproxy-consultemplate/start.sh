#!/bin/sh
#haproxy -f /usr/local/etc/haproxy/haproxy.cfg
PIDFILE="/var/run/haproxy.pid"
consul-template -consul=$CONSUL_URL -wait=5s -template="/templates/service.ctmpl:/tmp/haproxy.cfg:hap.sh"
haproxy -f /tmp/haproxy.cfg -p "$PIDFILE" -D -st $(cat $PIDFILE)
