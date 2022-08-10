package com.ibm.restconf.driver;

import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.ExecutionRequestPropertyValue;
import com.ibm.restconf.model.ResourceManagerDeploymentLocation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ibm.restconf.config.RCDriverConstants.RC_SERVER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;


public class CiscoCncServiceDriverTest {

    public static final String TEST_SERVER_BASE_URL = "http://localhost:8080";
    private static final String API_CONTEXT = "apiContext";
    private static final String API_AUTH = "apiAuth";
    private static final String API_SLICES = "apiSlices";
    private static final String API_SLICE_FILTER_NAME = "apiSliceFilterName";
    private static final String API_UPDATE_SUFFIX = "apiUpdateSuffix";


    private RestTemplate restTemplate= new RestTemplate();
    private MockRestServiceServer mockServer;
    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }



    @Test
    public void testGetTicket(){
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        Map<String, Object> properties = new HashMap<>();
        properties.put(API_CONTEXT, "/crosswork");
        properties.put(API_AUTH, "/sso/v1/tickets");
        properties.put(RC_SERVER_URL, "http://localhost:8080");
        resourceManagerDeploymentLocation.setProperties(properties);

        server.expect(requestTo(TEST_SERVER_BASE_URL + "/crosswork/sso/v1/tickets"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andRespond(withCreatedEntity(URI.create(TEST_SERVER_BASE_URL +  "/crosswork/sso/v1/tickets"))
                        .body("anyTicket")
                        .contentType(MediaType.APPLICATION_JSON));

        CiscoCncServiceDriver ciscoCncServiceDriver = new CiscoCncServiceDriver(restTemplate);
        final String ticket = ciscoCncServiceDriver.getTicket(resourceManagerDeploymentLocation);

        assertThat(ticket).isNotNull();
    }


    @Test
    public void testGetToken(){
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        Map<String, Object> properties = new HashMap<>();
        properties.put(API_CONTEXT, "/crosswork");
        properties.put(API_AUTH, "/sso/v1/tickets");
        properties.put(RC_SERVER_URL, "http://localhost:8080");
        resourceManagerDeploymentLocation.setProperties(properties);

        server.expect(requestTo(TEST_SERVER_BASE_URL + "/crosswork/sso/v1/tickets/anyTicket"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andRespond(withCreatedEntity(URI.create(TEST_SERVER_BASE_URL +  "/crosswork/sso/v1/tickets"))
                        .body("anyToken")
                        .contentType(MediaType.APPLICATION_JSON));

        CiscoCncServiceDriver ciscoCncServiceDriver = new CiscoCncServiceDriver(restTemplate);
        final String token = ciscoCncServiceDriver.getToken(resourceManagerDeploymentLocation,"anyTicket");

        assertThat(token).isNotNull();
    }



    @Test
    public void testCreateSlice(){
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        ExecutionRequest executionRequest = new ExecutionRequest();
        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        Map<String, Object> properties = new HashMap<>();
        properties.put(API_CONTEXT, "/crosswork");
        properties.put(API_SLICES, "/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice");
        properties.put(RC_SERVER_URL, "http://localhost:8080");
        resourceManagerDeploymentLocation.setProperties(properties);
        executionRequest.setDeploymentLocation(resourceManagerDeploymentLocation);
        Map<String, ExecutionRequestPropertyValue> map = new HashMap<>();
        ExecutionRequestPropertyValue executionRequestPropertyValue = new ExecutionRequestPropertyValue() {
            @Override
            public void setValue(Object value) {
                super.setValue(value);
            }
        };
        executionRequestPropertyValue.setValue(TEST_SERVER_BASE_URL);
        map.put(RC_SERVER_URL, executionRequestPropertyValue);
        executionRequest.setResourceProperties(map);

        server.expect(requestTo(TEST_SERVER_BASE_URL + "/crosswork/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, "application/yang-data+xml"))
                //.andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer jet"))
                .andRespond(withCreatedEntity(URI.create(TEST_SERVER_BASE_URL +  "/crosswork/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice"))
                        .body("created")
                        .contentType(MediaType.APPLICATION_JSON));

        CiscoCncServiceDriver ciscoCncServiceDriver = new CiscoCncServiceDriver(restTemplate);
        final String createdSlice = ciscoCncServiceDriver.createSlice(executionRequest,"jwt","payload", UUID.randomUUID().toString());

        assertThat(createdSlice).isNotNull();
    }


    @Test
    public void testUpdateSlice(){
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        ExecutionRequest executionRequest = new ExecutionRequest();
        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        Map<String, Object> properties = new HashMap<>();
        properties.put(API_CONTEXT, "/crosswork");
        properties.put(API_SLICES, "/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice");
        properties.put(API_SLICE_FILTER_NAME, "/dynamic");
        properties.put(API_UPDATE_SUFFIX, "/nsst");
        properties.put(RC_SERVER_URL, "http://localhost:8080");

        resourceManagerDeploymentLocation.setProperties(properties);
        executionRequest.setDeploymentLocation(resourceManagerDeploymentLocation);
        Map<String, ExecutionRequestPropertyValue> map = new HashMap<>();
        ExecutionRequestPropertyValue executionRequestPropertyValue = new ExecutionRequestPropertyValue() {
            @Override
            public void setValue(Object value) {
                super.setValue(value);
            }
        };
        executionRequestPropertyValue.setValue(TEST_SERVER_BASE_URL);
        map.put(RC_SERVER_URL, executionRequestPropertyValue);

        executionRequest.setResourceProperties(map);

        server.expect(requestTo(TEST_SERVER_BASE_URL + "/crosswork/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice/dynamic=sliceName/nsst"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, "application/yang-data+xml"))
                //.andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer jet"))
                .andRespond(withCreatedEntity(URI.create(TEST_SERVER_BASE_URL +  "/crosswork/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice/dynamic=sliceName/nsst"))
                        .body("updated")
                        .contentType(MediaType.APPLICATION_JSON));

        CiscoCncServiceDriver ciscoCncServiceDriver = new CiscoCncServiceDriver(restTemplate);
        final String updatedSlice = ciscoCncServiceDriver.updateSlice(executionRequest,"jwt","sliceName","payload", UUID.randomUUID().toString());

        assertThat(updatedSlice).isNotNull();
    }



    @Test
    public void testDeleteSlice(){
        final MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        ExecutionRequest executionRequest = new ExecutionRequest();
        ResourceManagerDeploymentLocation resourceManagerDeploymentLocation = new ResourceManagerDeploymentLocation();
        Map<String, Object> properties = new HashMap<>();
        properties.put(API_CONTEXT, "/crosswork");
        properties.put(API_SLICES, "/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice");
        properties.put(API_SLICE_FILTER_NAME, "/dynamic");
        properties.put(RC_SERVER_URL, "http://localhost:8080");

        resourceManagerDeploymentLocation.setProperties(properties);
        executionRequest.setDeploymentLocation(resourceManagerDeploymentLocation);
        Map<String, ExecutionRequestPropertyValue> map = new HashMap<>();
        ExecutionRequestPropertyValue executionRequestPropertyValue = new ExecutionRequestPropertyValue() {
            @Override
            public void setValue(Object value) {
                super.setValue(value);
            }
        };
        executionRequestPropertyValue.setValue(TEST_SERVER_BASE_URL);
        map.put(RC_SERVER_URL, executionRequestPropertyValue);

        executionRequest.setResourceProperties(map);

        server.expect(requestTo(TEST_SERVER_BASE_URL + "/crosswork/proxy/nso/restconf/data/cisco-5g-transport-cfp:transport-slice/dynamic=sliceName"))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, "application/yang-data+xml"))
                //.andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer jet"))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        CiscoCncServiceDriver ciscoCncServiceDriver = new CiscoCncServiceDriver(restTemplate);
        final String deletedSlice = ciscoCncServiceDriver.deleteSlice(executionRequest,"jwt","sliceName","payload", UUID.randomUUID().toString());

        assertThat(deletedSlice).isNull();
    }

}