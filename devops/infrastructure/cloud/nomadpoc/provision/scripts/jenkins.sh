#!/bin/bash
if [ ! -f /usr/lib/jvm/java-7-oracle/bin/java ];
then
    echo "-------- PROVISIONING JAVA ------------"
	echo "---------------------------------------"

	## Make java install non-interactive
	## See http://askubuntu.com/questions/190582/installing-java-automatically-with-silent-option
	echo debconf shared/accepted-oracle-license-v1-1 select true | \
	  debconf-set-selections
	echo debconf shared/accepted-oracle-license-v1-1 seen true | \
	  debconf-set-selections

	## Install java 1.7
	## See http://www.webupd8.org/2012/06/how-to-install-oracle-java-7-in-debian.html
	echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
	echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
	apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
	apt-get update
	apt-get -y install oracle-java7-installer
else
	echo "CHECK - Java already installed"
fi

if [ ! -f /etc/init.d/jenkins ];
then
	echo "-------- PROVISIONING JENKINS ------------"
	echo "------------------------------------------"
	wget -q -O - http://pkg.jenkins-ci.org/debian/jenkins-ci.org.key | sudo apt-key add -
	sh -c 'echo deb http://pkg.jenkins-ci.org/debian binary/ > /etc/apt/sources.list.d/jenkins.list'
	apt-get update
	apt-get -y install jenkins
else
	echo "CHECK - Jenkins already installed"
fi

echo "DOCKER_OPTS=\"-H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock -G jenkins\"" | sudo tee -a /etc/default/docker > /dev/null

sudo service docker restart
