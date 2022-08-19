package com.ibm.restconf.service;

import com.ibm.restconf.model.LcmOpOccPollingRequest;
import com.ibm.restconf.model.alm.ExecutionAsyncResponse;

import java.time.Duration;


public interface ExternalMessagingService {

    void sendExecutionAsyncResponse(ExecutionAsyncResponse request, String tenantId);

    void sendDelayedExecutionAsyncResponse(ExecutionAsyncResponse request, String tenantId, Duration delay);

    void sendLcmOpOccPollingRequest(LcmOpOccPollingRequest request);

}