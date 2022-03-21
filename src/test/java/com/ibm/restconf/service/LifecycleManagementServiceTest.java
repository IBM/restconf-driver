package com.ibm.restconf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.restconf.config.RCDriverProperties;
import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.ExecutionRequestPropertyValue;
import com.ibm.restconf.security.AccessDeniedException;
import com.ibm.restconf.service.impl.CiscoCncAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static com.ibm.restconf.test.TestConstants.TEST_DL_NO_AUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class LifecycleManagementServiceTest {


    @MockBean
    CiscoCncAuthService mockAuthDriver;
    @MockBean
    CiscoCncServiceDriver mockDriver;
    @MockBean
    MessageConversionService messageConversionService;
    @MockBean
    ExternalMessagingService mockExternalMessagingService;
    @MockBean
    RCDriverProperties mockRCDriverProperties;



    @Test
    public void testExecuteLifecycle() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        final LifecycleManagementService lifecycleManagementService = new LifecycleManagementService(mockDriver,  mockAuthDriver, messageConversionService, mockExternalMessagingService, mockRCDriverProperties);
        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Create");
        Map<String, ExecutionRequestPropertyValue> map = new HashMap<>();
        ExecutionRequestPropertyValue executionRequestPropertyValue = new ExecutionRequestPropertyValue() {
            @Override
            public void setValue(Object value) {
                super.setValue(value);
            }
        };
        executionRequestPropertyValue.setValue("someName");
        map.put("slicename", executionRequestPropertyValue);
        executionRequest.setResourceProperties(map);
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);

        when(mockAuthDriver.authenticate(executionRequest)).thenReturn("2312312312");
        when(mockDriver.createSlice(any(), eq("2312312312"), any())).thenReturn("");

        final ExecutionAcceptedResponse executionAcceptedResponse = lifecycleManagementService.executeLifecycle(executionRequest);

        assertThat(executionAcceptedResponse).isNotNull();
        assertThat(executionAcceptedResponse.getRequestId()).isNotNull();

    }

    @Test
    public void testExecuteLifecycleInvalidLifecycleName() {
        final LifecycleManagementService lifecycleManagementService = new LifecycleManagementService(mockDriver,  mockAuthDriver, messageConversionService, mockExternalMessagingService, mockRCDriverProperties);
        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Integrity");
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);

        when(mockAuthDriver.authenticate(executionRequest)).thenReturn("2312312312");

        assertThatThrownBy(() ->{
            lifecycleManagementService.executeLifecycle(executionRequest);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("Requested transition [Integrity] is not supported by this lifecycle driver");
    }


    @Test
    public void testAuthorizationIssue() {
        final LifecycleManagementService lifecycleManagementService = new LifecycleManagementService(mockDriver,  mockAuthDriver, messageConversionService, mockExternalMessagingService, mockRCDriverProperties);
        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Integrity");
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);

        when(mockAuthDriver.authenticate(executionRequest)).thenReturn(null);
        assertThatThrownBy(() ->{
            lifecycleManagementService.executeLifecycle(executionRequest);
        }).isInstanceOf(AccessDeniedException.class).hasMessage("Invalid JWT Token. Unauthorized access");
    }

}