#!/bin/bash

cd ${alm_restconf_directory}
openssl pkcs12 -export -inkey /var/restconfdriver/certs/tls.key -in /var/restconfdriver/certs/tls.crt -out /var/lm/keystore/keystore.p12 -password pass:password -name "restconf-driver"
java $JVM_OPTIONS -jar /data/restconf-driver-@project.version@.jar