yo jhipster
yo jhipster --skip-client --with-entities yo jhipster:server --with-entities
yo jhipster --skip-server --with-entities yo jhipster:client --with-entities
yo jhipster --force --with-entities.

yo jhipster:entity GlobalConfiguration --regenerate --skip-server --force 
yo jhipster:entity GlobalConfiguration --regenerate --skip-client --force 

yo jhipster:entity GlobalConfiguration --regenerate --force 

yo jhipster-module

Registry:
git clone https://github.com/jhipster/jhipster-registry.git
git clean -df;git reset --hard; git pull







gradle build
docker-compose build package-app
docker push andresfuentes/checklist

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service rm adc

#docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 rmi -f  andresfuentes/checklist
docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 pull andresfuentes/checklist

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 8080:9090 --name adc -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist 
#docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 8080:8080 --name adc -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist 

#docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 run -p 8080:9090 --name adc  -e DB_USER=root -e DB_PASSWORD=pwc123 BD_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist
#docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service update --image andresfuentes/checklist adc



docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service inspect adc
docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service tasks adc
docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 ps|awk '{system("docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 logs -f " $1)}'


#Checklist
docker run -p 8080:9090 --name checklist -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist 

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 1 -p 8090:9090 --name checklist -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist 


docker run -p 8080:9090 --name checklist -e SERVICE_ENV=prod -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306/Checklist andresfuentes/checklist 

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 9090:9090 --name checklist -e SERVICE_ENV=prod -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306/Checklist andresfuentes/checklist 


java -jar ./build/libs/checklist-0.0.1-SNAPSHOT.war --server.port=9090  --db.url=jdbc:mysql://adc-database.eastus.cloudapp.azure.com:3306/Checklist?useUnicode=true&characterEncoding=UTF-8  --db.username=root  --db.password=pwc123  --spring.profiles.active=prod



### Postgress

java -jar ./build/libs/checklist-0.0.1-SNAPSHOT.war --server.port=9090  --db.url=jdbc:postgresql://adc-database.eastus.cloudapp.azure.com:5432/Checklist  --db.username=postgres  --db.password=pwc123  --spring.profiles.active=prod



docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 9090:9090 --name checklist -e SERVICE_ENV=prod -e DB_USER=postgres -e DB_PASSWORD=pwc123 -e SERVICE_DB=jdbc:postgresql://adc-database.eastus.cloudapp.azure.com:5432/Checklist -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=adc-database.eastus.cloudapp.azure.com:9300 andresfuentes/checklist 



docker run -d -p 9090:9090 --restart=always --name checklist -e SERVICE_ENV=prod,swagger -e DB_USER=postgres -e DB_PASSWORD=pwc123 -e SERVICE_DB=jdbc:postgresql://adc-database.eastus.cloudapp.azure.com:5432/Checklist -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=adc-database.eastus.cloudapp.azure.com:9300 andresfuentes/checklist 



#Gradle
./gradlew -Pprod
./gradlew -Pprod bootRepackage




docker run -d --name database-es -v /var/data/search:/usr/share/elastichsearch/data/  -p 9200:9200 -p 9300:9300 --restart=always elasticsearch:2.3.5

docker run -d --name database-pg -v /var/data/postgresql:/data -p 5432:5432 --restart=always -e POSTGRES_PASSWORD=pwc123 -e PGDATA=/data postgres



docker run -p 8080:9090 --name checklist -e SERVICE_PORT=9090 -e DB_USER=root -e DB_PASSWORD=pwc123 -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=adc-database.eastus.cloudapp.azure.com:9300 -e SERVICE_DB=jdbc:postgresql://adc-database.eastus.cloudapp.azure.com:5432/Checklist  andresfuentes/checklist 


-----

JHIPSTER MARKETPLACE APPs


npm install -g generator-jhipster-entity-audit
npm install -g generator-jhipster-bootstrap-material-design
npm install -g generator-jhipster-swagger-cli
npm install -g generator-jhipster-docker
npm install -g generator-jhipster-elasticsearch-reindexer
npm install -g generator-jhipster-swagger2markup
npm install -g generator-jhipster-module
npm install -g generator-jhipster-basic-auth
npm install -g generator-jhipster-react
npm install -g generator-jhipster-bootswatch
npm install -g generator-jhipster-leaflet
npm install -g generator-jhipster-angular-ui
npm install -g generator-jhipster-mssql
npm install -g generator-jhipster-gitlab-ci-build-status
npm install -g generator-jhipster-ci
npm install -g generator-jhipster-google-maps
npm install -g generator-jhipster-fortune
npm install -g generator-jhipster-google-analytics


---
npm install -g generator-jhipster-debian-packager
dpkg-i target/myapp_0.0.1~SNAPSHOT_all.deb
sudo service <appname> start

-- ionic
npm install -g ionic yo bower gulp
npm install -g generator-jhipster-ionic
yo jhipster-ionic --force

