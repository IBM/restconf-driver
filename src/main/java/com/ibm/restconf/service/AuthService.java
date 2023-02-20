package com.ibm.restconf.service;

import com.ibm.restconf.model.ExecutionRequest;

public interface AuthService {
    String authenticate(ExecutionRequest executionRequest, String driverRequestId);
}
