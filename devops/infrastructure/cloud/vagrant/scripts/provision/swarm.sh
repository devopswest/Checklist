#!/bin/bash
master=$1



if [ $master = "true" ]
then
 sudo curl -L https://github.com/docker/compose/releases/download/1.8.1/docker-compose-`uname -s`-`uname -m`  | sudo tee /usr/local/bin/docker-compose > /dev/null
 sudo chmod +x /usr/local/bin/docker-compose

 sudo docker swarm init --advertise-addr $2 | awk '/--token/ {print $2}' > /vagrant/servers/data/swarm/token
 cd /vagrant/provision/docker_builds/weavescope
 sudo docker-compose up -d
else
 sudo docker swarm join --token `cat /vagrant/servers/data/swarm/token` $2:2377
fi


# sudo docker service create --name registry --publish  5000:5000 registry:2
#sudo ln -s   /home/jenkins /var/jenkins_home
#sudo docker service create --name jenkins_slave --constraint 'node.role==worker' 172.10.10.10:5000/jenkins_slave /usr/local/bin/jenkins-slave.sh -master http://172.10.10.10:8080 -username jenkins -password jenkins -executors 1
