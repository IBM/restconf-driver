package com.ibm.restconf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Details returned when a lifecycle execution request is accepted")
public class ExecutionAcceptedResponse {

    @Schema(description = "Request ID")
    private String requestId;

    public ExecutionAcceptedResponse() {}

    public ExecutionAcceptedResponse(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String infrastructureRequestId) {
        this.requestId = infrastructureRequestId;
    }

    @Override
    public String toString() {
        return "ExecutionAcceptedResponse{" + "requestId='" + requestId + '\'' + '}';
    }
}