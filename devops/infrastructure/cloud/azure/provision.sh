#!/bin/sh

sed -i 's/{pwc-password}/pwc123/' configure-cluster.sh
sed -i 's/{pwc-password}/pwc123/' azuredeploy.js

./configure-cluster.sh CICD 5 5
