#!/bin/bash
#sudo useradd -u 1000 -G users,docker -m  -d /home/jenkins -r  jenkins
#sudo ln -s   /home/jenkins /var/jenkins_home
sudo mkdir -p /etc/docker
sudo echo "{ \"insecure-registries\":[\"$1:5000\"] }" | sudo tee /etc/docker/daemon.json
sudo service docker restart && echo "docker service restarted"



if [[ "$HOSTNAME" != manager* ]]
then
    echo "Create directories for  weave and scope"
    sudo mkdir -p /etc/weavescope
    sudo cp /vagrant/bin/scope /usr/local/bin/
    sudo chmod a+x /usr/local/bin/scope
    sudo cp /vagrant/scripts/systemd/scope.service /etc/systemd/system/
    sudo cp /vagrant/scripts/etc/weavescope.conf /etc/weavescope/weavescope.conf
    sudo echo "PEERS=\"$1\"" | sudo tee /etc/weavescope/weavescope.conf > /dev/null
    sudo systemctl enable scope
    sudo systemctl start scope

fi


if [[ "$HOSTNAME" == worker* ]]
then
    sudo useradd -u 1000 -G users,docker -m  -d /home/jenkins-slave -r  jenkins-slave
fi


if [[ "$HOSTNAME" == manager* ]]
then

 sudo docker swarm init --advertise-addr $2 | awk '/--token/ {print $2}' > /vagrant/data/swarm/token

 #Install docker compose and start weave scope
 sudo cp /vagrant/bin/docker-compose /usr/local/bin/docker-compose
 export PEERS=$1
 sudo chmod +x /usr/local/bin/docker-compose
 cd /vagrant/scripts/builds/weavescope
 sudo docker-compose up -d
else
 sudo docker swarm join --token `cat /vagrant/data/swarm/token` $2:2377
fi

if [ "$HOSTNAME" == cimaster ]
then
  sudo docker run -d --name registry -h registry -p 5000:5000 --restart=always -v /vagrant/data/registry:/var/lib/registry registry:2

  cd /vagrant/scripts/builds/jenkins
  sudo docker build -t $1:5000/jenkins .

  cd /vagrant/scripts/builds/jenkins_slave
  sudo docker build -t $1:5000/jenkins_slave .

  while ! echo exit | nc $1 5000; do echo "waiting for registry to be available ..."; sleep 10; done
  sudo docker push $1:5000/jenkins
  sudo docker push $1:5000/jenkins_slave

  sudo useradd -u 1000 -G users,docker -m  -d /home/jenkins -r  jenkins
  sudo ln -s   /home/jenkins /var/jenkins_home

  sudo docker run -d --name jenkins -h jenkins --publish 8080:8080 --restart=always --publish 50000:50000 -v '/home/jenkins:/var/jenkins_home' $1:5000/jenkins
fi
