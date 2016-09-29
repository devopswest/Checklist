#!/bin/sh

echo "***************************************"
echo "***         COMPILING CODE          ***"
echo "***************************************"
git pull

npm update
bower update --allow-root

#mvn -Dmaven.test.skip=true package
mvn -Dmaven.test.skip=true compile

