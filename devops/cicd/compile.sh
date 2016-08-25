#!/bin/sh

npm update
bower update --allow-root

#mvn -Dmaven.test.skip=true package
mvn -Dmaven.test.skip=true compile

