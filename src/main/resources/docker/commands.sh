#!/bin/bash

KEYSTOREDIR=/var/lm/keystore
KEYSTORE=$KEYSTOREDIR/keystore.p12
CERTDIR=/var/restconfdriver/certs
CERTKEY=$CERTDIR/tls.key
CERT=$CERTDIR/tls.crt


cd ${alm_restconf_directory}
openssl pkcs12 -export -inkey $CERTKEY -in $CERT -out $KEYSTORE -password pass:password -name "restconf-driver"
java $JVM_OPTIONS -jar /data/restconf-driver-@project.version@.jar