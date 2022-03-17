# CP4NA Resource Package

## Resource Package Structure

Resource packages for CP4NA should contain (at a minimum) the following content for the RestConf lifecycle driver to work correctly.

```
helloworld.zip
+--- Definitions
|    +--- lm
|         +--- resource.yaml
+--- Docs
|    +--- Readme.md
+--- Lifecycle
     +--- lifecycle.mf
     +--- restconf
          +--- templates
               +--- Create.xml
               +--- Update.xml
               +--- Delete.xml 
```
The `resource.yaml` file is the resource descriptor used by LM. 

The templates in the `lifecycle/restconf/templates` directory can be used to override the default behaviour of the driver.
