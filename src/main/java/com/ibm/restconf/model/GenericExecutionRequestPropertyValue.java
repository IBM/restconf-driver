package com.ibm.restconf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Required as a general implementation of DriverPropertyValue so the
 * DriverPropertyDeserializer can deserialize into an instance for all non-key
 * types
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class GenericExecutionRequestPropertyValue extends ExecutionRequestPropertyValue {

    public GenericExecutionRequestPropertyValue() {
        super();
    }

    public GenericExecutionRequestPropertyValue(Object value) {
        super(value);
    }

    public GenericExecutionRequestPropertyValue(Object value, String type) {
        super(value, type);
    }

}
