#!/bin/bash
echo "Launching $3 Node $2 with Ip $1"

if [ $3 = "server" ]
then

  docker run -d --net=weave -p 8443:8100 -p 8300:8300 -p 8301:8301 -p 8301:8301/udp \
  -p 8302:8302/udp -p 8302:8302 -p  8400:8400 -p 8500:8500 -p 8653:8600/udp \
  -h consul-server-$2 --name consul-server \
  -v "/vagrant/$2:/config" gliderlabs/consul-server:latest
fi

if [ $3 = "app" ]
then
  echo "provision servers in order"
  # docker  run -d  --net=weave  -p 8443:8100 -p 8300:8300 -p 8301:8301 -p 8301:8301/udp \
  # -p 8302:8302/udp -p 8302:8302 -p  8400:8400 -p 8500:8500 -p 8653:8600/udp \
  # -h consul-agent-$2 --name consul-agent  \
  # -v "/vagrant/$2:/config" gliderlabs/consul-agent:latest
  #y
  # agentIP=$(docker inspect -f "{{  .NetworkSettings.Networks.weave.IPAddress }}" consul-agent)
  #
  # docker  run --net=weave  -d  -h  registrator --name reg \
  # -v '/var/run/docker.sock:/tmp/docker.sock' \
  # gliderlabs/registrator:master -internal  consul://$agentIP:8500
  # docker  run -d --net=weave -h mtl --name mtl \
  # -P  -e 'SERVICE_80_CHECK_HTTP=/' kitematic/hello-world-nginx
  # docker  run -d --net=weave -h spil --name spil \
  # -P  -e 'SERVICE_80_CHECK_HTTP=/' kitematic/hello-world-nginx
  # docker  run -d --net=weave -h mil --name mil \
  # -P  -e 'SERVICE_80_CHECK_HTTP=/' kitematic/hello-world-nginx
  # docker  run -d --net=weave -h aps --name aps \
  # -P  -e 'SERVICE_80_CHECK_HTTP=/' kitematic/hello-world-nginx
fi

if [ $3 = "lb" ]
then
  docker run -d --net=weave -h haproxy --name haproxy \
  -p 80:80 -e CONSUL_URL=$4:8500 -v '/vagrant/consultemplates/haproxy.ctmpl:/templates/service.ctmpl' haproxy:consul-template
fi




#echo 'DOCKER_OPTS="--dns 172.20.20.10 --dns 172.20.20.20"' >/etc/default/docker

#
# echo "Attempting to find docker and remove priviously provisioned containers if any"
#
# command -v docker && \
# if [ $(docker ps -a -q| wc -l) -gt 0 ];
# then
#   docker stop $(docker ps -a -q) &&  docker rm $(docker ps -a -q)
# fi
