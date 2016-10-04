##Project  Setup
#### Install Vagrant
Depending on the operating system, appropriate virtualbox package should be installed as well.

#### Execute Vagrant up
```
cd vagrant
vagrant up
```
Vagrant will download appropriate vagrant boxes to local machine and configure them appropriately. Once cimaster, manager1 , worker1-3 nodes are created. check the following links

#### Weave scope
  http://172.10.10.10:4040
  Weave scope provides you with visualization for the docker infrastructure
  
#### Jenkins 
  http://172.10.10.10:8080
  Jenkins will request you to provide the administrator password when it is accessed for the first time. use vagrant ssh command to connect to cimaster node and the execute the docker exe command on the jenkins image to retrieve the password from the location that is prompted to you in jenkins UI
  Create a username with "jenkins" and password as "jenkins" during the initial setup. you will use the same credentials later to start jenkins slaves
  
  ```
  vagrant ssh cimaster
  sudo docker exec -it jenkins cat <location will be provided by initial jenkins screen at http:/172.10.10.10:8080>
  ```
#### create jenksin slaevs in the swarm
  Connect to manager1 node using vagrant ssh from your host and execute the following commands
  
  ````
  vagrant ssh manager1
  sudo docker service create --name  jenkins_slave --constraint 'node.role==worker' \
  --mount type=bind,source=/var/run/docker.sock,destination=/var/run/docker.sock \
  --mount type=bind,source=/usr/bin/docker,destination=/usr/bin/docker \
  --mount type=bind,source=/home/jenkins-slave,destination=/home/jenkins-slave \
  --mount type=bind,source=/tmp,destination=/tmp \
  172.10.10.10:5000/jenkins_slave:latest \
  /usr/local/bin/jenkins-slave.sh -master http://172.10.10.10:8080 \
  -username jenkins -password jenkins -showHostName -executors 1
  `````
  Execute the following command to scale  jenkins slaves in the swarm
  
  ````
  sudo docker service scale jenkins_slave=4
  ````
  
  #### Configure Jenkins jobs
  This is a vanilla installation of jenkins. So there will not be any jobs. Please use the "Build inside docker" option when configuring the jobs. Make sure to bind volume  /home/jenkins_slave in docker advanced options  in thebuild step.
  Use top level maven targets in build steps to build maven projects.
  
  
  
