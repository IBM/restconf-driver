# Installation Guide

## Helm Install of Driver

Prior to installing the driver, it may be necessary to:
- configure a secret containing trusted client certificates. See [Configuring Certificates](ConfiguringCertificates.md)


Download the Helm chart for the required version of the RestConf Driver. Run the following command to install the Helm chart with the default values

```bash
helm install restconf-driver restconf-driver-<version>.tgz
```
**NOTES**:
 Before installing the driver, add a secret for icr.io by editing secrets through the following OpenShift console:

```bash
https://console-openshift-console.apps.DEV-CLUSTER.cp.fyre.ibm.com/k8s/ns/openshift-config/secrets/pull-secret/edit
```
username: iamapikey
password: < API key generated through IBM cloud account https://cloud.ibm.com/iam/apikeys >

## Onboarding Driver into LM

Use lmctl for onboard the driver into LM. For full details on how to install or use lmctl, refer to its documentation.

Certificate used by RestConf driver can be obtained from the secret restconf-driver-tls. This certificate needs to be used while onboarding RestConf driver. Use the following command to obtain RestConf certificate.
```bash
oc get secret restconf-driver-tls -o 'go-template={{index .data "tls.crt"}}' | base64 -d > restconf-driver-tls.pem
```

The following command will onboard the RestConf Driver into CP4NA environment called 'dev01':

```bash
lmctl resourcedriver add --type restconf --url https://restconf-driver:8196 dev01  --certificate restconf-driver-tls.pem
```

**NOTES**:
- The above example assumes lmctl has been configured with an environment called 'dev01'. Replace this environment name accordingly
- If this configuration doesn't include the password for the environment, one will be prompted for
