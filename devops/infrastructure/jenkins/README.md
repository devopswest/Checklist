docker-compose build develop

docker run -v /var/run/docker.sock:/var/run/docker.sock -e DOCKER_TARGET=tcp://pirates-swarm-master:4243 -e DOCKER_USER=andresfuentes -e DOCKER_PASSWORD=tresm1104 todobuild_develop

mvn sonar:sonar -Dsonar.host.url=http://http://pirates-builder.eastus.cloudapp.azure.com:9000

http://pirates-builder.eastus.cloudapp.azure.com:9000/dashboard?id=com.af.todos%3Atodos&did=1
