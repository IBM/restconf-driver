#!/bin/bash

mkdir -p /var/lm/truststore/
TRUSTSTORE=/var/lm/truststore/truststore.p12
# Add certificates to the truststore
for cert in `find /var/lm -name *.cer -type l`; do
  echo "$(date) Importing certificate ${cert} into ${TRUSTSTORE}"
  $JAVA_HOME/bin/keytool -import -alias $(basename ${cert} .cer) -file ${cert} -keystore ${TRUSTSTORE} -storepass changeit -noprompt
  TS_OPTION=-Djavax.net.ssl.trustStore=${TRUSTSTORE}
done

cd ${alm_restconf_directory}
java $JVM_OPTIONS -jar /data/restconf-driver-@project.version@.jar