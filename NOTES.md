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

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 8090:9090 --name checklist -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306 andresfuentes/checklist 


docker run -p 8080:9090 --name checklist -e SERVICE_ENV=prod -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306/Checklist andresfuentes/checklist 

docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 9090:9090 --name checklist -e SERVICE_ENV=prod -e DB_USER=root -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:3306/Checklist andresfuentes/checklist 


java -jar ./build/libs/checklist-0.0.1-SNAPSHOT.war --server.port=9090  --db.url=jdbc:mysql://adc-database.eastus.cloudapp.azure.com:3306/Checklist?useUnicode=true&characterEncoding=UTF-8  --db.username=root  --db.password=pwc123  --spring.profiles.active=prod



### Postgress

java -jar ./build/libs/checklist-0.0.1-SNAPSHOT.war --server.port=9090  --db.url=jdbc:postgresql://adc-database.eastus.cloudapp.azure.com:5432/Checklist  --db.username=postgres  --db.password=pwc123  --spring.profiles.active=prod



docker -H tcp://adc-swarm-master.eastus.cloudapp.azure.com:4243 service create --replicas 5 -p 9090:9090 --name checklist-prod -e SERVICE_ENV=prod -e DB_USER=postgres -e DB_PASSWORD=pwc123 -e DB_URL=adc-database.eastus.cloudapp.azure.com:5432/Checklist -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=adc-database.eastus.cloudapp.azure.com:9300 andresfuentes/checklist 


#Gradle
./gradlew -Pprod
./gradlew -Pprod bootRepackage

docker run -d --name database-es -v /var/data/search:/usr/share/elastichsearch/data/ -p 9200:9200 -p 9300:9300 --restart=always elasticsearch:1.7.3

    search:
        image: 'elasticsearch:1.7.3'
        hostname: $HOSTNAME
        volumes:
             - /var/data/search:/usr/share/elastichsearch/data/
        ports:
             - "9200:9200"
             - '9300:9300'
        restart: always
