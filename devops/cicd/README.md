

docker run -it --entrypoint=bash -v /var/run/docker.sock:/var/run/docker.sock -v $PWD:/var/app -v $HOME/.m2:/root/.m2 checklist_cicd 
