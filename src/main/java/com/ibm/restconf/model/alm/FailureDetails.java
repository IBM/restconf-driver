package com.ibm.restconf.model.alm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Details regarding a failure in VIM infrastructure responses")
public class FailureDetails {

    @Schema(example = "RESOURCE_NOT_FOUND", description = "If the request has failed, this can be set to provide a code for the failure")
    private FailureCode failureCode;

    @Schema(example = "Could not connect", description = "Optional string to give descriptive information as to the current state of this request, e.g. to indicate what error has occurred in failure scenarios")
    private String description;

    public FailureDetails() {}

    public FailureDetails(FailureCode failureCode, String description) {
        this.failureCode = failureCode;
        this.description = description;
    }

    public FailureCode getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(FailureCode failureCode) {
        this.failureCode = failureCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FailureDetails{" +
                "failureCode=" + failureCode +
                ", description='" + description + '\'' +
                '}';
    }

    public enum FailureCode {

        RESOURCE_ALREADY_EXISTS,
        RESOURCE_NOT_FOUND,
        INFRASTRUCTURE_ERROR,
        INSUFFICIENT_CAPACITY,
        INTERNAL_ERROR,
        UNKNOWN;

        @JsonCreator
        public static FailureCode valueFrom(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }

        @JsonValue
        public String toString() {
            return super.toString();
        }
    }

}
