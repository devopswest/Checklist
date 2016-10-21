FROM haproxy:alpine

ENTRYPOINT ["bin/start.sh"]

Volume /templates
ENV CONSUL_URL consul:8500

ADD start.sh /bin/start.sh
RUN chmod u+x /bin/start.sh
ADD hap.sh /usr/local/bin/hap.sh
RUN chmod u+x /usr/local/bin/hap.sh
ADD consul-template /usr/local/bin/consul-template
RUN chmod u+x /usr/local/bin/consul-template
