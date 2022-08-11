#!/bin/bash

mkdir -p /var/lm/truststore/
TRUSTSTORE=/var/lm/truststore/truststore.p12
# Add certificates to the truststore
for cert in `find /var/lm -name *.cer -type l`; do
  echo "$(date) Importing certificate ${cert} into ${TRUSTSTORE}"
  $JAVA_HOME/bin/keytool -import -alias $(basename ${cert} .cer) -file ${cert} -keystore ${TRUSTSTORE} -storepass changeit -noprompt
  TS_OPTION=-Djavax.net.ssl.trustStore=${TRUSTSTORE}
done

KEYSTOREDIR=/var/lm/keystore
KEYSTORE=$KEYSTOREDIR/keystore.p12
CERTDIR=/var/restconfdriver/certs
CERTKEY=$CERTDIR/tls.key
CERT=$CERTDIR/tls.crt

cd ${alm_restconf_directory}
openssl pkcs12 -export -inkey $CERTKEY -in $CERT -out $KEYSTORE -password pass:password -name "restconf-driver"
java $JVM_OPTIONS -jar /data/restconf-driver-@project.version@.jar