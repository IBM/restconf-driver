# RESTCONF Driver for CP4NA

## Introduction

RESTCONF uses HTTP methods to provide CRUD operations on a conceptual datastore containing YANG-defined data, which is
compatible with a server that implements NETCONF datastores. The RESTCONF protocol operates on a hierarchy of resources,
starting with the top-level API resource itself.  Each resource represents a manageable component within the device.

## Connecting to a RESTCONF server:

For information on how to configure the connection details for a RestConf server within ALM, please see the guide on Adding a Deployment Location, please see the [Adding a Deployment Location](docs/AddingDeploymentLocation.md)

## Creating a Resource Package

For information on how to create a resource package to load into CP4NA, please see the guide on [Creating a Resource Package](docs/CreatingResourcePackage.md)