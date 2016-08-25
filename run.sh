#!/bin/sh
echo "******************************************"
echo "* STARTING MICROSERVICE [$SERVICE_NAME]  *"

echo "-*****************************************"
JAVA_MEM_OPTS="-Xms16m \
 -Xmx1g \
 -XX:MinHeapFreeRatio=10 \
 -XX:MaxHeapFreeRatio=25 \
 -Xss320k \
 -XX:MetaspaceSize=16m \
 -XX:MaxMetaspaceSize=1g"

JAVA_JMX_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote \
 -Dcom.sun.management.jmxremote.port=5000 \
 -Dcom.sun.management.jmxremote.ssl=false \
 -Djava.rmi.server.hostname=$(hostname -I) \
 -Dcom.sun.management.jmxremote.authenticate=false"

JAVA_DEBUG_OPTS="-Xdebug \
 -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n"

SERVICE_OPTS="--server.port=$SERVICE_PORT \
 --db.url=jdbc:mysql://$DB_URL?useUnicode=true&characterEncoding=utf8&useSSL=false \
 --db.username=$DB_USER \
 --db.password=$DB_PASSWORD \
 --spring.profiles.active=$SERVICE_ENV"

JAVA_PERFORMANCE_OPTS="-Djava.security.egd=file:/dev/./urandom"

echo "******************************************"
echo "COMMAND: java $JAVA_MEM_OPTS $JAVA_JMX_OPTS -jar /$SERVICE_NAME.war $SERVICE_OPTS"
echo "******************************************"
##java $JAVA_PERFORMANCE_OPTS $JAVA_MEM_OPTS -jar /$SERVICE_NAME.war $SERVICE_OPTS
java -jar /$SERVICE_NAME.war $SERVICE_OPTS
#java -jar /$SERVICE_NAME.war
