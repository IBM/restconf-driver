package com.ibm.restconf.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceManagerDeploymentLocationTest {

    @Test
    public void testResourceManagerDeploymentLocation() {

        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        resourceManagerDeploymentLocation.setResourceManager("ResourceManager");
        resourceManagerDeploymentLocation.setName("Name");
        resourceManagerDeploymentLocation.setProperties(getMap());
        resourceManagerDeploymentLocation.setType("Type");

        assertThat(resourceManagerDeploymentLocation.getResourceManager()).isEqualTo("ResourceManager");
        assertThat(resourceManagerDeploymentLocation.getName()).isEqualTo("Name");
        assertThat(resourceManagerDeploymentLocation.getType()).isEqualTo("Type");
        assertThat(resourceManagerDeploymentLocation.getProperties().get("String")).isEqualTo("Str");
    }

    private Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("String", new String("Str"));
        return map;
    }
}

