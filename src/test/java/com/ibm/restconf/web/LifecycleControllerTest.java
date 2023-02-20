package com.ibm.restconf.web;

import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.service.LifecycleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.UUID;

import static com.ibm.restconf.test.TestConstants.TEST_DL_NO_AUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "test" })
@ExtendWith(MockitoExtension.class)
public class LifecycleControllerTest {

    @InjectMocks
    LifecycleController lifecycleController;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private LifecycleManagementService lifecycleManagementService;

    @Test
    @DisplayName("Test Create All Slices Request")
    public void testExecuteLifecycle()throws Exception{
        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Create");
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);
        when(httpServletRequest.getReader()).thenReturn(new BufferedReader(new StringReader("Testing")));
        final ResponseEntity<ExecutionAcceptedResponse> responseEntity = lifecycleController.executeLifecycle(executionRequest,"12345678", httpServletRequest);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}