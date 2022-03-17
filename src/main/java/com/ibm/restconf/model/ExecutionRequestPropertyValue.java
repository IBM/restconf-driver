package com.ibm.restconf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "type" })
@JsonDeserialize(using = ExecutionRequestPropertyValueDeserializer.class)
public abstract class ExecutionRequestPropertyValue {

    // Currently the main use of this is to indicate to a Driver that this property
    // should be treated as a Key (see KeyExecutionRequestPropertyValue)
    private String type;

    private Object value;

    public ExecutionRequestPropertyValue() {
        super();
        this.type = PropertyType.STRING.getValue();
    }

    public ExecutionRequestPropertyValue(Object value) {
        this(value, null);
    }

    public ExecutionRequestPropertyValue(Object value, String type) {
        this.value = value;
        if (type == null) {
            type = PropertyType.STRING.getValue();
        }
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("{\"_class\":\"%s\", \"type\":\"%s\", \"value\":%s}", getClass().getSimpleName(), type,
                value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutionRequestPropertyValue that = (ExecutionRequestPropertyValue) o;
        return Objects.equals(value, that.value) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
