package com.ibm.restconf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ExecutionAcceptedResponseTest {

    @Test
    public void executionAcceptedResponseTest() {

        ExecutionAcceptedResponse executionAcceptedResponse = new ExecutionAcceptedResponse();
        executionAcceptedResponse.setRequestId("requestId");
        assertThat(executionAcceptedResponse.getRequestId()).isEqualTo("requestId");

    }

}