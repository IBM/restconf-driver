package com.ibm.restconf.web;

import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.service.LifecycleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.ibm.restconf.test.TestConstants.TEST_DL_NO_AUTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "test" })
public class LifecycleControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private LifecycleManagementService lifecycleManagementService;

    @Test
    @DisplayName("Test Create All Slices Request")
    public void testExecuteLifecycle()throws Exception{
        final ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Create");
        executionRequest.setDeploymentLocation(TEST_DL_NO_AUTH);
        String payload = "{\"data\":\"no-data\"}";
        when(lifecycleManagementService.executeLifecycle(any())).thenReturn(new ExecutionAcceptedResponse(UUID.randomUUID().toString()));

        final ResponseEntity<ExecutionAcceptedResponse> responseEntity = testRestTemplate.postForEntity("/api/driver/lifecycle/execute", executionRequest, ExecutionAcceptedResponse.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getHeaders().getContentType()).isNotNull();
        assertThat(responseEntity.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getRequestId()).isNotEmpty();
    }
}