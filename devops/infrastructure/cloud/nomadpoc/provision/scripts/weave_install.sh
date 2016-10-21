#!/bin/bash

master=$1

echo "Create directories for  weave and scope"
sudo mkdir -p /etc/nomad /etc/weave /etc/weavescope
sudo cp /vagrant/bin/weave /usr/local/bin/
sudo cp /vagrant/bin/scope /usr/local/bin/
sudo chmod a+x /usr/local/bin/weave  /usr/local/bin/scope

sudo cp /vagrant/services/systemd/weave.service /etc/systemd/system/
sudo cp /vagrant/services/systemd/scope.service /etc/systemd/system/
sudo cp /vagrant/services/etc/weave.conf /etc/weave/weave.conf
sudo cp /vagrant/services/etc/weavescope.conf /etc/weavescope/weavescope.conf

if [ $master = "false" ]
then
  sudo echo "PEERS=\"$2\"" | sudo tee /etc/weave/weave.conf > /dev/null
  sudo echo "PEERS=\"$2\"" | sudo tee /etc/weavescope/weavescope.conf > /dev/null
fi


sudo systemctl enable  weave scope
sudo systemctl start weave
sudo systemctl start scope

sudo systemctl restart weave
sudo systemctl restart scope
