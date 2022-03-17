# Configuring Certificates

In order to allow outbound calls from the driver to services with self-signed certificates, it will be necessary to trust these client certificates. This can be done by mapping a secret containing trusted client certificates.

The secret containing certificates should have one or more of them keyed on a filename with a `.cer` suffix, e.g. given the file `myservice.cer`, create a secret called `trusted-certs`
```
kubectl create secret generic trusted-certs --from-file=./myservice.cer
```

Then, when installing the driver, this value can be configured via the Helm values file as follows during the Helm install. 

###### Example of values passed to Helm chart during install
```yaml
app:
  certificateSecret: trusted-certs
```