package com.ibm.restconf.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.ibm.restconf.service.MessageConversionException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Request used to execute lifecycle")
public class ExecutionRequest {

    @ApiModelProperty(value = "Lifecycle Name")
    private String lifecycleName;
    @ApiModelProperty(value = "Driver files")
    private String driverFiles;
    @ApiModelProperty(value = "System Properties")
    private Map<String, ExecutionRequestPropertyValue> systemProperties = new HashMap<>();
    @ApiModelProperty(value = "Resource Properties")
    private Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();
    @ApiModelProperty(value = "Request Properties")
    private Map<String, ExecutionRequestPropertyValue> requestProperties = new HashMap<>();
    @ApiModelProperty(value = "Deployment Location")
    private ResourceManagerDeploymentLocation deploymentLocation;
    @ApiModelProperty(value = "Associated Topology")
    private Map<String, InternalResourceInstance> associatedTopology = new HashMap<>();

    public ExecutionRequest() {}

    public ExecutionRequest(String lifecycleName, String driverFiles, Map<String, ExecutionRequestPropertyValue> systemProperties,
                            Map<String, ExecutionRequestPropertyValue> resourceProperties, Map<String, ExecutionRequestPropertyValue> requestProperties, ResourceManagerDeploymentLocation deploymentLocation,
                            Map<String, InternalResourceInstance> associatedTopology) {
        this.lifecycleName = lifecycleName;
        this.driverFiles = driverFiles;
        this.systemProperties = systemProperties;
        this.requestProperties = requestProperties;
        this.resourceProperties = resourceProperties;
        this.deploymentLocation = deploymentLocation;
        this.associatedTopology = associatedTopology;
    }

    public String getLifecycleName() {
        return lifecycleName;
    }

    public void setLifecycleName(String lifecycleName) {
        this.lifecycleName = lifecycleName;
    }

    public String getDriverFiles() {
        return driverFiles;
    }

    public void setDriverFiles(String driverFiles) {
        this.driverFiles = driverFiles;
    }

    public Map<String, ExecutionRequestPropertyValue> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map<String, ExecutionRequestPropertyValue> systemProperties) {
        this.systemProperties.putAll(systemProperties);
    }

    public Map<String, ExecutionRequestPropertyValue> getResourceProperties() {
        return resourceProperties;
    }

    public void setResourceProperties(Map<String, ExecutionRequestPropertyValue> resourceProperties) {
        this.resourceProperties.putAll(resourceProperties);
    }

    public Map<String, ExecutionRequestPropertyValue> getRequestProperties() {
        return requestProperties;
    }

    public void setRequestProperties(Map<String, ExecutionRequestPropertyValue> requestProperties) {
        this.requestProperties.putAll(requestProperties);
    }

    /**
     * Legacy support for getProperties method which may be referenced in Javascript libraries. Will return a filtered version of the resource properties with String values instead of PropertyValue values
     *
     * @return map containing resourceProperties values as simple String types
     */
    public Map<String, Object> getProperties() {
        return resourceProperties.entrySet().stream().filter(entry -> !PropertyType.KEY.getValue().equals(entry.getValue().getType()))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
    }

    /**
     * Get a resource property as a simple String type if it exists
     * @param propertyName name of property
     * @return inner property value of the resource property
     * @throws MessageConversionException if the property exists and is of type other than StringPropertyValue
     */
    public String getStringResourceProperty(String propertyName) throws MessageConversionException {
        ExecutionRequestPropertyValue propertyValue = resourceProperties.get(propertyName);
        if(propertyValue != null) {
            if( PropertyType.STRING.getValue().equals(propertyValue.getType()) ) {
                return (String) propertyValue.getValue();
            } else {
                throw new MessageConversionException(String.format("Expecting requestProperty of type %s but found %s", PropertyType.STRING.getValue(), propertyValue.getType()));
            }
        } else {
            return null;
        }

    }

    public Map<String, InternalResourceInstance> getAssociatedTopology() {
        return associatedTopology;
    }

    public void setAssociatedTopology(Map<String, InternalResourceInstance> associatedTopology) {
        this.associatedTopology.putAll(associatedTopology);
    }

    public ResourceManagerDeploymentLocation getDeploymentLocation() {
        return deploymentLocation;
    }

    public void setDeploymentLocation(ResourceManagerDeploymentLocation deploymentLocation) {
        this.deploymentLocation = deploymentLocation;
    }

    @Override
    public String toString() {
        return "ExecutionRequest{" +
                "lifecycleName='" + lifecycleName + '\'' +
                ", driverFiles='" + driverFiles + '\'' +
                ", systemProperties=" + LogSafeProperties.getLogSafeProperties(systemProperties) +
                ", resourceProperties=" + LogSafeProperties.getLogSafeProperties(resourceProperties) +
                ", requestProperties=" + LogSafeProperties.getLogSafeProperties(requestProperties) +
                ", associatedTopology=" + associatedTopology +
                ", deploymentLocation=" + deploymentLocation +
                '}';
    }
}
