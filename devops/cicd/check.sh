#!/bin/sh

CURL_CMD="curl -s -f $HEALTH_CHECK && echo UP||echo DOWN"

#echo $CURL_CMD
response=$(eval $CURL_CMD)

echo $response
