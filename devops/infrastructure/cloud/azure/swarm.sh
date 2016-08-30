
sshpass -p pwc\!123 ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-master.eastus.cloudapp.azure.com bash -c "'
docker swarm init --listen-addr $prefix-swarm-master:2377
'" > x.txt

c1="$(grep -E -o '(docker swarm join --secret)([0-9 A-Za-z]*)' x.txt)"
c2="$(grep -E -o '(--ca-hash)([0-9 :A-Za-z]*)' x.txt)"


#Configuring Swarm Nodes
for n in $(seq 0 1  $((nodes -1)))
do
echo "*********** Configuring swarm-node$n Total: $nodes - $((nodes -1))"
ssh-keygen -f "/home/afuentes009/.ssh/known_hosts" -R $prefix-swarm-node$n.eastus.cloudapp.azure.com

sshpass -p pwc\!123 ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-node$n.eastus.cloudapp.azure.com bash -c "'
curl -fsSL https://experimental.docker.com/ | sudo sh
sudo gpasswd -a pwc docker
curl -L https://github.com/docker/compose/releases/download/1.6.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
'"

echo "*********** Configuring Swarm $n"
echo "*********** Node"
echo "$c1 $c2 $prefix-swarm-master:2377"
sshpass -p pwc\!123 ssh  -o "StrictHostKeyChecking no" pwc@$prefix-swarm-node$n.eastus.cloudapp.azure.com bash -c "'
$c1 $c2 $prefix-swarm-master:2377
'"
done
