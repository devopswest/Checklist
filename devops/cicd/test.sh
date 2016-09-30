#!/bin/sh

echo "***************************************"
echo "***           TESTING CODE          ***"
echo "***            1. junit             ***"
echo "***            2. karma             ***"
echo "***            3. cucumber          ***"
echo "***            4. protractor        ***"
echo "***************************************"

command=$1

echo "COMMAND: "$command

if [ "$command" = "" ]; then
    echo "FAST Test"
    mvn test
fi;

if [ "$command" = "deep" ]; then
   echo "DEEP Test"
   mvn test
fi;
