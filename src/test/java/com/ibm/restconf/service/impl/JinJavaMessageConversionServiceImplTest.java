package com.ibm.restconf.service.impl;

import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.ExecutionRequestPropertyValue;
import com.ibm.restconf.model.GenericExecutionRequestPropertyValue;
import com.ibm.restconf.service.MessageConversionException;
import com.ibm.restconf.service.MissingPropertyException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JinJavaMessageConversionServiceImplTest {

    private static final String TEMPLATE_PATH = "templates-test";

    @Test
    public void testGenerateMessageFromRequestForCreate() throws MessageConversionException, IOException {
        JinJavaMessageConversionServiceImpl jinJavaMessageConversionService = new JinJavaMessageConversionServiceImpl();
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Create");
        Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();

        resourceProperties.put("slicename", new GenericExecutionRequestPropertyValue("demo-slice2"));
        resourceProperties.put("asNumber", new GenericExecutionRequestPropertyValue(64001));
        resourceProperties.put("endpointPe", new GenericExecutionRequestPropertyValue("atlanta-regional-pe"));
        resourceProperties.put("ifType",new GenericExecutionRequestPropertyValue("TenGigE"));
        resourceProperties.put("ifId",new GenericExecutionRequestPropertyValue("0/0/0/10.402"));
        resourceProperties.put("peIpAddress",new GenericExecutionRequestPropertyValue("172.16.2.1/29"));
        resourceProperties.put("neighborIPv4",new GenericExecutionRequestPropertyValue("172.16.2.2"));
        resourceProperties.put("remoteAsIPv4",new GenericExecutionRequestPropertyValue(65102));
        resourceProperties.put("nsst",new GenericExecutionRequestPropertyValue("eMBB"));

        executionRequest.setResourceProperties(resourceProperties);

        String parsedTemplate = jinJavaMessageConversionService.generateMessageFromRequest("Create", executionRequest);

        String expectedTemplate = "";
        try (InputStream inputStream = JinJavaMessageConversionServiceImplTest.class.getResourceAsStream("/" + TEMPLATE_PATH +  "/Create.xml" )) {
            if (inputStream != null) {
                expectedTemplate = IOUtils.toString(inputStream, Charset.defaultCharset());
            }
        } catch (IOException e) {
            throw e;
        }

        assertEquals(expectedTemplate, parsedTemplate);

    }


    @Test
    public void testGenerateMessageFromRequestForUpdate() throws MessageConversionException, IOException {
        JinJavaMessageConversionServiceImpl jinJavaMessageConversionService = new JinJavaMessageConversionServiceImpl();
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Update");
        Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();
        resourceProperties.put("nsst",new GenericExecutionRequestPropertyValue("URLLC"));

        executionRequest.setResourceProperties(resourceProperties);

        String parsedTemplate = jinJavaMessageConversionService.generateMessageFromRequest("Update", executionRequest);

        String expectedTemplate = "";
        try (InputStream inputStream = JinJavaMessageConversionServiceImplTest.class.getResourceAsStream("/" + TEMPLATE_PATH +  "/Update.xml" )) {
            if (inputStream != null) {
                expectedTemplate = IOUtils.toString(inputStream, Charset.defaultCharset());
            }
        } catch (IOException e) {
            throw e;
        }

        assertEquals(expectedTemplate, parsedTemplate);

    }

    @Test
    public void testGenerateMessageFromRequestForDelete() throws MessageConversionException, IOException {
        JinJavaMessageConversionServiceImpl jinJavaMessageConversionService = new JinJavaMessageConversionServiceImpl();
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Delete");
        Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();

        resourceProperties.put("slicename", new GenericExecutionRequestPropertyValue("demo-slice2"));
        resourceProperties.put("nsst",new GenericExecutionRequestPropertyValue("URLLC"));

        executionRequest.setResourceProperties(resourceProperties);

        String parsedTemplate = jinJavaMessageConversionService.generateMessageFromRequest("Delete", executionRequest);

        String expectedTemplate = "";
        try (InputStream inputStream = JinJavaMessageConversionServiceImplTest.class.getResourceAsStream("/" + TEMPLATE_PATH +  "/Delete.xml" )) {
            if (inputStream != null) {
                expectedTemplate = IOUtils.toString(inputStream, Charset.defaultCharset());
            }
        } catch (IOException e) {
            throw e;
        }

        assertEquals(expectedTemplate, parsedTemplate);

    }


    @Test
    public void testErrorScenario() {
        JinJavaMessageConversionServiceImpl jinJavaMessageConversionService = new JinJavaMessageConversionServiceImpl();
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Delete");
        Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();

        resourceProperties.put("slicename", new GenericExecutionRequestPropertyValue("demo-slice2"));
        resourceProperties.put("nsst",new GenericExecutionRequestPropertyValue("URLLC"));
        executionRequest.setResourceProperties(resourceProperties);

        assertThrows(IllegalArgumentException.class, ()->{
            jinJavaMessageConversionService.generateMessageFromRequest("Delete-NotFound", executionRequest);
        });
    }

    @Test
    public void testMissingRequiredProperty() {
        JinJavaMessageConversionServiceImpl jinJavaMessageConversionService = new JinJavaMessageConversionServiceImpl();
        ExecutionRequest executionRequest = new ExecutionRequest();
        executionRequest.setLifecycleName("Delete");
        Map<String, ExecutionRequestPropertyValue> resourceProperties = new HashMap<>();

        resourceProperties.put("slicename", new GenericExecutionRequestPropertyValue("demo-slice2"));
        //resourceProperties.put("nsst",new GenericExecutionRequestPropertyValue("URLLC"));
        executionRequest.setResourceProperties(resourceProperties);

        assertThrows(MissingPropertyException.class, ()->{
            jinJavaMessageConversionService.generateMessageFromRequest("Delete", executionRequest);
        });
    }
}