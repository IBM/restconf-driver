# Installation Guide

## Helm Install of Driver

Prior to installing the driver, it may be necessary to:
- configure a secret containing trusted client certificates. See [Configuring Certificates](ConfiguringCertificates.md)


Download the Helm chart for the required version of the RestConf Driver. Run the following command to install the Helm chart with the default values

```bash
helm install restconf-driver-<version>.tgz --name restconf-driver
```

## Onboarding Driver into LM

Onboarding Driver into LM
Use lmctl for onboard the driver into LM. For full details on how to install or use lmctl, refer to its documentation.

The following command will onboard the RestConf Driver into a TNC-O (< 1.3) environment called 'dev01':

```bash
lmctl lifecycledriver add --type restconf --url http://restconf-driver:8196 dev01
```

For CP4NA 1.3 or greater, use the folowing command:

```bash
lmctl resourcedriver add --type restconf --url http://restconf-driver:8196 dev01
```

**NOTES**:
- The above example assumes lmctl has been configured with an environment called 'dev01'. Replace this environment name accordingly
- If this configuration doesn't include the password for the environment, one will be prompted for
