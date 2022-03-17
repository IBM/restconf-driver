# Adding a RestConf as a Deployment Location in CP4NA

The deployment location for the target RestConf can be added in the CP4NA UI, supplying the following information for infrastructure properties.

###### Example of JSON structure for Deployment Location
```jsonc
{
    "restconfServerUrl": "http://restconf-test-harness:8297",
    # Authentication details
    "username": "xxx",
    "password": "yyy",
    
    # RestConf API specific details 
    "apiContext" : "/crosswork",
    "apiAuth" : "/sso/v1/tickets",
    "apiSlices" : "/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice",
    "apiSliceFilterName" : "/dynamic",
    "apiUpdateSuffix" : "/nsst"
}
```