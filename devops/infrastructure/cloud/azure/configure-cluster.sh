#!/bin/sh

echo "Installing Docker"

labName=$1
if ["$labName" == ""]; then
	labName="TestLab"
fi;

prefix="$(echo "$labName"|awk '{print tolower($0)}')"

nodes=$2
if ["$nodes" == ""]; then
	nodes="5"
fi;


desktops=$3
if ["$desktops" == ""]; then
	desktops="0"
fi;




echo "******************************************"
echo "* CREATING LAB  $labName - $prefix"
echo "-*****************************************"

action="azure group deployment create -n \"$labName\"GroupDeployment -f azuredeploy.js -g \"$labName\"Group -p '{\"newLabName\":{\"value\":\"$labName\"}, \"numberOfInstances\":{\"value\":$nodes}, \"numberOfDesktops\":{\"value\":$desktops}, \"artifactRepoDisplayName\":{\"value\":\"$labName-Repo\"}, \"serverPrefix\":{\"value\":\"$prefix\"}}'"
echo "$action"

azure group delete --quiet --name "$labName"Group
azure group create --name "$labName"Group --location eastus
echo "$action"| sh -


#Configuring Swarm Master
echo "*********** Configuring swarm-master"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-swarm-master.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-master.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
'"

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-master.eastus.cloudapp.azure.com bash -c "'
docker swarm init --listen-addr $prefix-swarm-master:2377
'" > x.txt

c1="$(grep -E -o '(docker swarm join --secret)([0-9 A-Za-z]*)' x.txt)"
c2="$(grep -E -o '(--ca-hash)([0-9 :A-Za-z]*)' x.txt)"

#Configure DTR
echo "*********** Configuring DTR"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-dtr$n.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-dtr.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
'"

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-dtr.eastus.cloudapp.azure.com bash -c "'
docker run -d -p 5000:5000 --restart=always --name registry registry:2
'"

#Configure Database
echo "*********** Configuring Database"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-database$n.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-database.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
'"

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-database.eastus.cloudapp.azure.com bash -c "'
docker run -d -p 5342:5342 --restart=always --name database-postgresql library/postgres:9.4
'"

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-database.eastus.cloudapp.azure.com bash -c "'
docker run -d -p 3306:3306 --restart=always --name database-mysql -e MYSQL_ROOT_PASSWORD=pwc\!123 mysql
'"


#Configure Builder
echo "*********** Configuring Builder"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-builder.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-builder.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
'"

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-builder.eastus.cloudapp.azure.com bash -c "'
docker run -d -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock --restart=always --name builder andresfuentes/builder
'"


#Configuring Swarm Nodes
for n in $(seq 0 1  $((nodes -1)))
do
echo "*********** Configuring swarm-node$n Total: $nodes - $((nodes -1))"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-swarm-node$n.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-node$n.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
'"

echo "*********** Configuring Swarm $n"
echo "*********** Node"
echo "$c1 $c2 $prefix-swarm-master:2377"
sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-node$n.eastus.cloudapp.azure.com bash -c "'
$c1 $c2 $prefix-swarm-master:2377
'"
done

#Configuring Desktops
for n in $(seq 0 1  $((desktops -1)))
do
echo "*********** Configuring desktop$n Total: $desktops - $((desktops -1))"
ssh-keygen -f "~/.ssh/known_hosts" -R $prefix-desktop$n.eastus.cloudapp.azure.com

sshpass -p {pwc-password} ssh  -o "StrictHostKeyChecking no" pwc@$prefix-desktop$n.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
sudo apt-get install -y parallel
sudo apt-get install -y xrdp
sudo apt-get update
sudo apt-get install -y xfce4
sudo service xrdp restart
sudo apt-get install -y eclipse
'"

done



#
# Move Resources from one group to the common group
# azure resource move -i "/subscriptions/{guid}/resourceGroups/OldRG/providers/Microsoft.Cache/Redis/examplecache" -d "$labName"
#
# azure group list|grep $labName-$prefix|awk '{system("azure resource list " $2)}'
# azure resource list ADC-adc-database-500255|awk '{print $2}'
# azure resource list ADC-adc-database-500255|awk '{system("azure resource move -q -i " $2 " -d $labName") }'


#azure group list|grep "adc"|awk '{system("azure group delete --quiet --name " $2)}'
