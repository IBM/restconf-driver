ARG JDK_IMAGE=openjdk
ARG JDK_VERSION=11.0.16-jre
FROM ${JDK_IMAGE}:${JDK_VERSION}
ENV JVM_OPTIONS=-Xmx256m
COPY *.sh /data/
RUN groupadd -g 1000 -r sld \
 && useradd -u 1000 -g 1000 -r -m sld \
 && chmod +x /data/*.sh \
 && mkdir /var/lm \
 # set group to 0 to allow arbitrary users in OCP to write to these directories
 && chown -R sld:0 /home/sld /var/lm /data \
 && chmod -R 775 /home/sld \
 && chmod -R 775 /var/lm
RUN apt update -y \
 && apt upgrade -y \
 && rm -rf /var/lib/apt/lists/*
USER sld
COPY restconf-driver-@project.version@.jar /data/restconf-driver-@project.version@.jar
CMD sh /data/commands.sh
