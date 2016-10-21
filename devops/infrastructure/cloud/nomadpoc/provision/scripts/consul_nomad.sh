#!/bin/sh

slave=$1
MASTER_IP=$2
ADV_IP=$3

echo "Create directories for nomad and consul"
sudo mkdir -p /etc/nomad /var/lib/nomad /etc/consul
sudo cp /vagrant/bin/nomad /usr/local/bin/
sudo cp /vagrant/services/systemd/nomad.service /etc/systemd/system/
#sudo sed -e s/VAR_HOSTNAME/consul$HOSTNAME/ /vagrant/services/systemd/consul.service | sudo tee /etc/systemd/system/consul.service > /dev/null
sudo cp /vagrant/services/systemd/consul.service /etc/systemd/system/
sudo chmod a+x  /usr/local/bin/nomad
#sudo chown -R infra:infra /var/lib/nomad


if [ $slave = "false" ]
then
  sudo sed -e  s/VAR_ADV_IP_ADDRESS/$ADV_IP/ \
  -e s/VAR_BOOTSTRAP_FLAG/false/ \
  -e s/VAR_SERVER_FLAG/false/ \
  -e s/VAR_JOIN_IP_ADDRESSES/\"$MASTER_IP\"/ /vagrant/services/etc/agent_tmpl.json | sudo tee /etc/consul/agent.json > /dev/null

  sudo sed -e s/VAR_ADV_IP_ADDRESS/$ADV_IP/ \
  -e s/VAR_JOIN_IP_ADDRESSES/$MASTER_IP/  /vagrant/services/etc/nomad_client.hcl | sudo tee /etc/nomad/agent.hcl > /dev/null



else
  sudo sed -e  s/VAR_ADV_IP_ADDRESS/$MASTER_IP/ \
  -e s/VAR_BOOTSTRAP_FLAG/true/ \
  -e s/VAR_SERVER_FLAG/true/ \
  -e s/VAR_JOIN_IP_ADDRESSES/""/ /vagrant/services/etc/agent_tmpl.json | sudo tee /etc/consul/agent.json > /dev/null

  sudo sed -e s/VAR_ADV_IP_ADDRESS/$MASTER_IP/ /vagrant/services/etc/nomad_server.hcl | sudo tee  /etc/nomad/agent.hcl > /dev/null
fi


sudo chmod a+r /etc/consul/agent.json
sudo chmod a+r /etc/nomad/agent.hcl

sudo systemctl enable nomad
sudo systemctl enable consul
sudo systemctl start nomad
sudo systemctl start consul




# echo "Create directories for nomad"
# sudo mkdir -p /etc/nomad /var/lib/nomad
# sudo cp /vagrant/bin/nomad /usr/local/bin/
# sudo cp /vagrant/services/etc/nomad_server.hcl /etc/nomad/agent.hcl
# sudo cp /vagrant/services/systemd/nomad.service /etc/systemd/system/
#
# sudo chmod a+x  /usr/local/bin/nomad
# sudo chmod a+r /etc/nomad/agent.hcl
# sudo chown -R infra:infra /var/lib/nomad
#
# sudo systemctl enable nomad
# sudo systemctl start nomad
