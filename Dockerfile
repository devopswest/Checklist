FROM java:8
#FROM tomcat

MAINTAINER me@andresfuentes.com

ENV SERVICE_ENV dev
ENV SERVICE_NAME app
ENV SERVICE_PORT 9090

EXPOSE 9090

COPY build/libs/*.war /app.war
#COPY build/libs/*.war /usr/local/tomcat/webapps/


COPY  run.sh /run.sh
RUN chmod +x /run.sh

ENTRYPOINT ["/run.sh"]


