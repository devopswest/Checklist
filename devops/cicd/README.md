

docker run -it --entrypoint=sh -v /var/run/docker.sock:/var/run/docker.sock -v $PWD:/var/app -v $HOME/.m2:/root/.m2 -e DOCKER_TARGET=tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 -e DEPLOY_TO_SERVER=dev -e APP_NAME=checklist checklist_cicd 
