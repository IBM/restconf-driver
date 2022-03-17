package com.ibm.restconf.service;

import com.ibm.restconf.model.ExecutionRequest;

public interface MessageConversionService {

    String generateMessageFromRequest(String messageType, ExecutionRequest executionRequest) throws MessageConversionException;

}