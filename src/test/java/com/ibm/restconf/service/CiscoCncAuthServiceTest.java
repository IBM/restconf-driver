package com.ibm.restconf.service;


import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.GenericExecutionRequestPropertyValue;
import com.ibm.restconf.model.ResourceManagerDeploymentLocation;
import com.ibm.restconf.service.impl.CiscoCncAuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import static com.ibm.restconf.test.TestConstants.TEST_DL_NO_AUTH;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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

        Mockito.when(ciscoCncAuthServiceDriver.getToken(Mockito.any(ResourceManagerDeploymentLocation.class),Mockito.anyString())).thenReturn("s,md;lndclcsacsd");
        Mockito.when(ciscoCncAuthServiceDriver.getTicket(Mockito.any(ResourceManagerDeploymentLocation.class))).thenReturn("s,md;lndclcsacsd");
        String auth=ciscoCncAuthService.authenticate(executionRequest);
        assertThat(auth).isNotNull();
    }
}
