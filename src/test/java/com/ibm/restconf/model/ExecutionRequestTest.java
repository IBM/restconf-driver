package com.ibm.restconf.model;

import com.ibm.restconf.service.MessageConversionException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ExecutionRequestTest {

    @Test
    public void testGetProperties() {
        // verify only StringPropertyValue types returned
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.getResourceProperties().put("key1", new GenericExecutionRequestPropertyValue("value1"));
        executionRequest.getResourceProperties().put("key2", new GenericExecutionRequestPropertyValue("value2"));
        executionRequest.getResourceProperties().put("key3", new KeyExecutionRequestPropertyValue("keyName", "privateKey"));

        assertThat(executionRequest.getProperties().keySet()).containsOnly("key1", "key2");
        assertThat(executionRequest.getProperties().get("key1")).isEqualTo("value1");
        assertThat(executionRequest.getProperties().get("key2")).isEqualTo("value2");
    }

    @Test
    public void testGetStringRequestProperty() throws MessageConversionException {
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.getResourceProperties().put("key1", new GenericExecutionRequestPropertyValue("value1"));
        executionRequest.getResourceProperties().put("key2", new GenericExecutionRequestPropertyValue(null));
        executionRequest.getResourceProperties().put("key3", new KeyExecutionRequestPropertyValue("keyName", "privateKey"));

        assertThat(executionRequest.getStringResourceProperty("key1")).isEqualTo("value1");
        assertThat(executionRequest.getStringResourceProperty("key2")).isNull();
        assertThat(executionRequest.getStringResourceProperty("unknown")).isNull();
        assertThatThrownBy(() -> executionRequest.getStringResourceProperty("key3")).isInstanceOf(MessageConversionException.class).hasMessage(
                "Expecting requestProperty of type string but found key");

    }
}
