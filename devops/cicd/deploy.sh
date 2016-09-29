#!/bin/sh

echo "***************************************"
echo "***           DEPLOY APP            ***"
echo "***            1. create RFC        ***"
echo "***            2. pull image        ***"
echo "***            3. deploy to swarm   ***"
echo "***            4. healthcheck       ***"
echo "***            5. closr RFC|Revert  ***"
echo "***************************************"

appname=$APP_NAME

branch=$DEPLOY_TO_SERVER
if [ "$branch" = "" ]; then
  branch="dev"
fi;


#
# Pull & Deploy
#

cp docker-compose.yml docker-compose-tmp.yml
sed -i 's/{version}/'"$branch"'/' docker-compose-tmp.yml
cat docker-compose-tmp.yml

if [ "$DOCKER_TARGET" = "" ]; then
    echo "Deploying at localhost"
    docker-compose --file docker-compose-tmp.yml rm -f app
    docker pull $DOCKER_USER/$appname:$branch
    docker-compose --file docker-compose-tmp.yml up -d app
else
    echo "Deploying at $DOCKER_TARGET"

    x1="$(docker -H $DOCKER_TARGET service inspect $appname)"
    echo "SERVICE: $x1"


    name="$(docker -H $DOCKER_TARGET service inspect $appname|grep Image)"
    echo "CHECK IMAGE NAME: $name"

    imageName="$(echo $name|awk '{split($0, array, "\"")} END{print array[4]}')"
    echo "Current Imagen Running $imageName"

    docker -H $DOCKER_TARGET tag $imageName  $DOCKER_USER/$appname:roolback

    echo "Images Before Deployment"
    docker -H $DOCKER_TARGET images|grep $appname

    echo "Create RFC"
    changeNumber="$(../devops/cicd/rfc-create.sh)"
    echo "RFC# $changeNumber"

    #docker -H $DOCKER_TARGET rm -f $DOCKER_USER/$appname
    docker -H $DOCKER_TARGET pull $DOCKER_USER/$appname:$branch
    if [ "$x1" = "[]" ]; then
        echo "***NEW Service"

        #
        # TODO: since docker-compose is not used. pls check all env variables are passed
        #

        #export DOCKER_HOST=$DOCKER_TARGET
        #docker -H $DOCKER_TARGET service create --replicas 1 --update-delay 10s --update-parallelism 1 -p 8080:8080 -e SERVICE_PORT=8080 -e SERVICE_ENV=dev --name $appname $DOCKER_USER/$appname

        #docker-compose service create app

        xxports=`yaml2json docker-compose-tmp.yml|jq '.services["app"].ports[]'|awk '{system("echo -p "$1)}'|awk '/START/{if (NR!=1)print " ";next}{printf $0" "}END{print " ";}'`
        xxargs=`yaml2json docker-compose-tmp.yml|jq '.services["app"].environment[]'|awk '{system("echo -e "$1)}'|awk '/START/{if (NR!=1)print " ";next}{printf $0" "}END{print " ";}'`
        echo "*****"
        echo $xxports
        echo "*****"
        echo $xxargs
        echo "*****"
        echo "docker -H $DOCKER_TARGET service create --replicas 1 $xxports $xxargs --name $appname $DOCKER_USER/$appname:$branch"

        docker -H $DOCKER_TARGET service create \
             --replicas 1 \
             $xxports \
             $xxargs \
             --name $appname $DOCKER_USER/$appname:$branch

        # docker -H $DOCKER_TARGET service create \
        #      --replicas 1 -p $SERVICE_PORT:$SERVICE_PORT \
        #      -e SERVICE_PORT=$SERVICE_PORT \
        #      -e SERVICE_ENV=$SERVICE_ENV \
        #      -e SERVICE_DB=$SERVICE_DB \
        #      -e DB_USER=$DB_USER \
        #      -e DB_PASSWORD=$DB_PASSWORD \
        #      -e SERVICE_ES_CLUSTER=$SERVICE_ES_CLUSTER \
        #      -e SERVICE_ES_NODE=$SERVICE_ES_NODE \
        #      --name $appname $DOCKER_USER/$appname:$branch

        docker -H $DOCKER_TARGET service scale $appname=3
    else
        echo "***UPDATE Service"
        docker -H $DOCKER_TARGET service update --image $DOCKER_USER/$appname:$branch $appname
    fi;

    echo "Images After Deployment"
    docker -H $DOCKER_TARGET images|grep $appname

    deploymentStatus="$(../devops/cicd/check.sh)"
    echo "Deployment Status: $deploymentStatus"
    if [ "$deploymentStatus" = "UP"]; then
       echo "Close RFC $changeNumber"
       ../devops/cicd/rfc-close.sh $changeNumber
    else
        echo "Something Went Wrong -- Revert Deployment"
        if [ "$x1" = "[]" ]; then
            echo "Nothing to revert"
        else
            echo "Reverting back to Image $imageName  "
            docker -H $DOCKER_TARGET rmi $imageName
            docker -H $DOCKER_TARGET tag $DOCKER_USER/$appname:roolback $imageName
            docker -H $DOCKER_TARGET service update --image $imageName $appname
        fi;
    fi;


    #Remove rollback point
    docker -H $DOCKER_TARGET rmi $DOCKER_USER/$appname:roolback

    #Remove untegged images -- Cleanup
    docker -H $DOCKER_TARGET images|grep none|awk '{system("docker -H $DOCKER_TARGET rmi " $3)}'

    echo "Images After Completion"
    docker -H $DOCKER_TARGET images|grep $appname


    x1="$(docker -H $DOCKER_TARGET service inspect $appname)"
    echo "SERVICE: $x1"

fi;

rm docker-compose-tmp.yml
