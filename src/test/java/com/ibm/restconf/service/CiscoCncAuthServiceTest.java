package com.ibm.restconf.service;

import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.GenericExecutionRequestPropertyValue;
import com.ibm.restconf.model.ResourceManagerDeploymentLocation;
import com.ibm.restconf.service.impl.CiscoCncAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import static com.ibm.restconf.test.TestConstants.TEST_DL_NO_AUTH;

@ActiveProfiles("test")
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class CiscoCncAuthServiceTest {

    @InjectMocks
    private CiscoCncAuthService ciscoCncAuthService;

    @Mock
    private CiscoCncServiceDriver ciscoCncAuthServiceDriver;

    @Test
    public void authenticateTest(){

        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Create");
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);
        executionRequest.getResourceProperties().put("property1", new GenericExecutionRequestPropertyValue("value1"));
        executionRequest.getResourceProperties().put("property2", new GenericExecutionRequestPropertyValue("value2"));
        executionRequest.getResourceProperties().put("property3", new GenericExecutionRequestPropertyValue("value3"));
        UUID uuid = UUID.randomUUID();
        Mockito.when(ciscoCncAuthServiceDriver.getToken(Mockito.any(),Mockito.anyString(),Mockito.anyString() )).thenReturn("s,md;lndclcsacsd");
        Mockito.when(ciscoCncAuthServiceDriver.getTicket(Mockito.any(), Mockito.anyString())).thenReturn("s,md;lndclcsacsd");
        String auth=ciscoCncAuthService.authenticate(executionRequest, uuid.toString());
        assertThat(auth).isNotNull();
    }
}
