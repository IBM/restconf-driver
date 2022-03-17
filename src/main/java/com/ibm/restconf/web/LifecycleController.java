package com.ibm.restconf.web;

import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.security.AccessDeniedException;
import com.ibm.restconf.service.LifecycleManagementService;
import com.ibm.restconf.service.MessageConversionException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController("LifecycleController")
@RequestMapping("/api/driver")
public class LifecycleController {

    private final static Logger logger = LoggerFactory.getLogger(LifecycleController.class);

    private final LifecycleManagementService lifecycleManagementService;

    @Autowired
    public LifecycleController(final LifecycleManagementService lifecycleManagementService) {
        this.lifecycleManagementService = lifecycleManagementService;
    }

    @PostMapping("/lifecycle/execute")
    @ApiOperation(value = "Execute a lifecycle against a RestConf", notes = "Initiates a lifecycle ")
    public ResponseEntity<ExecutionAcceptedResponse> executeLifecycle(@RequestBody ExecutionRequest executionRequest, HttpServletRequest servletRequest) throws MessageConversionException, AccessDeniedException {
        /*try (BufferedReader messageReader = servletRequest.getReader()) {
            String rawMessage = messageReader.lines().collect(Collectors.joining("\n"));
            logger.info("Received ExecutionRequest:\n{}", rawMessage);
        } catch (IOException e) {
            logger.warn(String.format("Exception caught logging ExecutionRequest message: %s", e.getMessage()), e);
        }*/
        logger.info("Received request to execute a lifecycle [{}] ", executionRequest.getLifecycleName());
        final ExecutionAcceptedResponse responseData = lifecycleManagementService.executeLifecycle(executionRequest);
        return ResponseEntity.accepted().body(responseData);
    }

}
