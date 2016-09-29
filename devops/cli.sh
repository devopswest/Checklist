#!/bin/sh

command=$1

if [ "$command" = "" ]; then
   command="help"
fi;

echo "Current Folder: $PWD"

#./devops/cicd/pull.sh

echo $command|awk '{system("../devops/cicd/"$command".sh")}'

