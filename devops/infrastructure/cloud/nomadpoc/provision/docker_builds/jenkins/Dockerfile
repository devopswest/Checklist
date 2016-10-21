FROM maven:3.3.9-jdk-8


RUN \
  apt-get update && \
  apt-get install -y openssh-server  && \
  apt-get install -y git-core && \
  mkdir  -p /var/run/sshd  && \
  useradd -u 1999 -g users -d /home/jenkins -s /bin/bash -p 'iQqmbIwWui11Y'  jenkins
