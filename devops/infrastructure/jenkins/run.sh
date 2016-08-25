#! /bin/bash

sudo cp /var/app/settings.xml /usr/share/maven/conf/settings.xml
sudo cp -r /var/app/jobs/ /var/jenkins_home/
sudo cp -r /var/app/config/*.* /var/jenkins_home/

sudo chown -R jenkins /var/jenkins_home/jobs
sudo chgrp -R jenkins /var/jenkins_home/jobs

/usr/local/bin/jenkins.sh &

echo "**** Sleep 2m ..."
sleep 2m

echo "**** Update user credentials..."

curl -XPOST 'localhost:8080/credential-store/domain/_/createCredentials' \
    --data-urlencode 'json={
        "": "0",
        "credentials": {
            "scope": "GLOBAL",
            "id": "8ebc53ee-63af-46e6-ab05-0fc2c06d4808",
            "username": "$DEFAULT_USERNAME",
            "password": "$DEFAULT_PASSWORD",
            "description": "",
            "$class": "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl"
        }
    }'



tail -f /var/jenkins_home/*.log
