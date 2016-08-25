#!/bin/sh

appname=todos
#
# Create Docker Image
#
docker-compose build package-app

#
# For Maven BUilds
#
version="$(ls ./target/*.war|awk '{split($0,array,"-")} END{print "v" array[2] "-" array[3]}')"

#
# for NPM Builds
#
#version=$(cat package.json \
#  | grep version \
#  | head -1 \
#  | awk -F: '{ print $2 }' \
#  | sed 's/[",]//g' \
#  | tr -d '[[:space:]]')


#build="$(ls ./target/*.war|awk '{split($0,array,"-")} {split(array[4],array2,".")} END{print array2[1]}')"



imageId="$(docker images|grep $appname|head -1|awk '{print $3}')"



v1="$(echo $version|awk -F'.' '{print $1}')"
v2="$(echo $version|awk -F'.' '{print $2}')"
v3="$(echo $version|awk -F'.' '{print $3}')"

v1="$(echo $v1|awk '{print substr($0,2)}')"
v3="$(echo $v3|awk -F'-' '{print $1}')"

branch=$DEPLOY_TO_SERVER
if [ "$branch" = "" ]; then
  branch="dev"
fi;

docker tag $imageId $DOCKER_USER/$appname:$version
docker tag $imageId $DOCKER_USER/$appname:$branch



#
# Push Image to Registry
#
docker login -u $DOCKER_USER -p $DOCKER_PASSWORD
#docker rmi  $DOCKER_USER/$appname
docker push $DOCKER_USER/$appname:latest

#docker push $DOCKER_USER/$appname:$version
#docker rmi  $DOCKER_USER/$appname:$branch
docker push $DOCKER_USER/$appname:$branch

#if [ "$branch" = "stage" ]; then
  echo "Pushing Versioned Tags [$v1, $v2, $v3]"

  docker tag $imageId  $DOCKER_USER/$appname:$v1
  docker tag $imageId  $DOCKER_USER/$appname:$v1.$v2
  docker tag $imageId  $DOCKER_USER/$appname:$v1.$v2.$v3

  #docker rmi   $DOCKER_USER/$appname:$v1
  #docker rmi   $DOCKER_USER/$appname:$v1.$v2
  #docker rmi   $DOCKER_USER/$appname:$v1.$v2.$v3

  docker push   $DOCKER_USER/$appname:$v1
  docker push   $DOCKER_USER/$appname:$v1.$v2
  docker push   $DOCKER_USER/$appname:$v1.$v2.$v3
#fi;

echo "Docker Images on Build Server"
docker images|grep $appname
