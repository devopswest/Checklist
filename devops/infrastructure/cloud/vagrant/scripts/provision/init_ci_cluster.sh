#!/bin/bash
cd /vagrant/scripts/builds/jenkins
sudo docker build -t 172.10.10.20:5000/jenkins .

cd /vagrant/scripts/builds/jenkins_slave
sudo docker build -t 172.10.10.20:5000/jenkins_slave .

while ! echo exit | nc 172.10.10.20 5000; do echo "waiting for registry to be available ..."; sleep 10; done
sudo docker push 172.10.10.20:5000/jenkins
sudo docker push 172.10.10.20:5000/jenkins_slave

sudo docker service create --name jenkins --publish 8080:8080  --publish 50000:50000 172.10.10.20:5000/jenkins
sudo docker service create --name  jenkins_slave --constraint 'node.role==worker' \
--mount type=bind,source=/var/run/docker.sock,destination=/var/run/docker.sock \
--mount type=bind,source=/usr/bin/docker,destination=/usr/bin/docker \
--mount type=bind,source=/home/jenkins-slave,destination=/home/jenkins-slave \
--mount type=bind,source=/tmp,destination=/tmp \
172.10.10.10:5000/jenkins_slave:latest \
/usr/local/bin/jenkins-slave.sh -master http://172.10.10.10:8080 \
 -username jenkins -password jenkins -showHostName -executors 1
