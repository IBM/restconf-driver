package com.ibm.restconf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InternalResourceInstanceTest {

    @Test
    public void testInternalResourceInstance() {

        InternalResourceInstance internalResourceInstance = new InternalResourceInstance();
        internalResourceInstance.setId("id");
        internalResourceInstance.setName("Name");
        internalResourceInstance.setType("Type");

        assertThat(internalResourceInstance.getId()).isEqualTo("id");
        assertThat(internalResourceInstance.getName()).isEqualTo("Name");
        assertThat(internalResourceInstance.getType()).isEqualTo("Type");

    }
}
