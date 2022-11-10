package com.ibm.restconf.model;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * For use when interacting with resource manager API
 */
@Schema
public class ResourceManagerDeploymentLocation {

    @Schema(example = "brent", description = "Name of the resource manager")
    private String resourceManager;
    @Schema(example = "dev-cloud", description = "Name of the deployment location")
    private String name;
    @Schema(example = "default-rm::Cloud", description = "Type identifier for the deployment location")
    private String type;
    @Schema(example = "", description = "Properties required to gain access to the deployment location VIM", required = true)
    private Map<String, Object> properties = new HashMap<>();

    public ResourceManagerDeploymentLocation() {
        super();
    }

    public ResourceManagerDeploymentLocation(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public ResourceManagerDeploymentLocation(String name, String type, String resourceManager) {
        super();
        this.name = name;
        this.type = type;
        this.resourceManager = resourceManager;
    }

    public String getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(String resourceManager) {
        this.resourceManager = resourceManager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ResourceManagerDeploymentLocation [resourceManager=" + resourceManager + ", name=" + name + ", type=" + type + ", infrastructureSpecificProperties=" + properties.toString()
                + "]";
    }

}