package com.ibm.restconf.model.alm;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import static com.ibm.restconf.utils.Constants.KAFKA_MESSAGE_VERSION;


@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Details returned when an async lifecycle execution request is accepted")
public class ExecutionAsyncResponse {

    @ApiModelProperty(value = "Request ID")
    private String requestId;
    @ApiModelProperty(value = "Status")
    private ExecutionStatus status;
    @ApiModelProperty(value = "Failure Details")
    private FailureDetails failureDetails;
    @ApiModelProperty(value = "Outputs")
    private final Map<String, Object> outputs = new HashMap<>();
    @ApiModelProperty(value = "Associated Resource Instance Topology")
    private final Map<String, InternalResourceInstance> associatedTopology = new HashMap<>();
    @ApiModelProperty(value = "Timestamp")
    private Long timestamp;
    @ApiModelProperty(value = "version")
    private String version = KAFKA_MESSAGE_VERSION;

    public ExecutionAsyncResponse() {}

    public ExecutionAsyncResponse(String requestId, ExecutionStatus status, FailureDetails failureDetails,
                                  Map<String, Object> outputs, Map<String, InternalResourceInstance> associatedTopology) {
        this.requestId = requestId;
        this.status = status;
        this.failureDetails = failureDetails;
        if (outputs != null) {
            this.outputs.putAll(outputs);
        }
        if (associatedTopology != null) {
            this.associatedTopology.putAll(associatedTopology);
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public FailureDetails getFailureDetails() {
        return failureDetails;
    }

    public void setFailureDetails(FailureDetails failureDetails) {
        this.failureDetails = failureDetails;
    }

    public Map<String, Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, Object> outputs) {
        this.outputs.putAll(outputs);
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, InternalResourceInstance> getAssociatedTopology() {
        return associatedTopology;
    }

    public Long getTimestamp() { return timestamp; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "ExecutionAsyncResponse{" +
                "requestId='" + requestId + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", failureDetails=" + failureDetails +
                ", associatedTopology=" + associatedTopology +
                // don't print outputs (because they may contain private keys)
                ", timestamp=" + timestamp +
                '}';
    }
}