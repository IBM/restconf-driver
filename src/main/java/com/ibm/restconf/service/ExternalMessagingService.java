package com.ibm.restconf.service;

import com.ibm.restconf.model.LcmOpOccPollingRequest;
import com.ibm.restconf.model.alm.ExecutionAsyncResponse;

import java.time.Duration;


public interface ExternalMessagingService {

    void sendExecutionAsyncResponse(ExecutionAsyncResponse request);

    void sendDelayedExecutionAsyncResponse(ExecutionAsyncResponse request, Duration delay);

    void sendLcmOpOccPollingRequest(LcmOpOccPollingRequest request);

}