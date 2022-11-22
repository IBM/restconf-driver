package com.ibm.restconf.model.alm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Execution Status")
public enum ExecutionStatus {

    COMPLETE("Complete"),
    FAILED("Failed"),
    IN_PROGRESS("In Progress");

    private String value;

    private ExecutionStatus(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static ExecutionStatus fromValue(String status) {
        for (ExecutionStatus executionStatus : values()) {
            if (executionStatus.value.equalsIgnoreCase(status)) {
                return executionStatus;
            }
        }
        return null;
    }
}