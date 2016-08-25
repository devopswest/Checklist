#!/bin/sh
cd /var/app/ToDos

git checkout docker-compose.yml
git checkout *.sh
git pull

chmod +x *.sh

./compile.sh
./package.sh
./deploy.sh

