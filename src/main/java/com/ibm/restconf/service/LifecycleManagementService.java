package com.ibm.restconf.service;

import com.ibm.restconf.config.RCDriverProperties;
import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.model.alm.ExecutionAsyncResponse;
import com.ibm.restconf.model.alm.ExecutionStatus;
import com.ibm.restconf.security.AccessDeniedException;
import com.ibm.restconf.service.impl.CiscoCncAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;


@Service("LifecycleManagementService")
public class LifecycleManagementService {

    private final static Logger logger = LoggerFactory.getLogger(LifecycleManagementService.class);
    private final static String SLICE_NAME = "sliceName";

    private final AuthService authService;
    private final CiscoCncServiceDriver ciscoCncServiceDriver;
    private final MessageConversionService messageConversionService;
    private final ExternalMessagingService externalMessagingService;
    private final RCDriverProperties rcDriverProperties;

    @Autowired
    public LifecycleManagementService(CiscoCncServiceDriver ciscoCncServiceDriver, CiscoCncAuthService ciscoCncAuthService, MessageConversionService messageConversionService, ExternalMessagingService externalMessagingService, RCDriverProperties rcDriverProperties){
        this.ciscoCncServiceDriver = ciscoCncServiceDriver;
        this.authService = ciscoCncAuthService;
        this.messageConversionService = messageConversionService;
        this.externalMessagingService = externalMessagingService;
        this.rcDriverProperties = rcDriverProperties;
    }

    public ExecutionAcceptedResponse executeLifecycle(ExecutionRequest executionRequest) throws MessageConversionException, AccessDeniedException {
        //Get JWT Token
        String jwt = this.authService.authenticate(executionRequest);
        final String requestId = UUID.randomUUID().toString();
        logger.info("Processing execution request");
        if(jwt != null) {
           /* if ("Get".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling GET API
                return new ExecutionAcceptedResponse(requestId, this.ciscoCncServiceDriver.getSlices(executionRequest.getDeploymentLocation(), jwt, getSliceName(executionRequest), getConfigFlag(executionRequest)));
            } else */
            if ("Create".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling CREATE API
                final String createPayload = messageConversionService.generateMessageFromRequest("Create", executionRequest);
                this.ciscoCncServiceDriver.createSlice(executionRequest, jwt, createPayload);

                // Delay sending the asynchronous response (from a different thread) as this method needs to complete first (to send the response back to Brent)
                externalMessagingService.sendDelayedExecutionAsyncResponse(new ExecutionAsyncResponse(requestId, ExecutionStatus.COMPLETE, null, Collections.emptyMap(), Collections.emptyMap()), rcDriverProperties.getExecutionResponseDelay());
                return new ExecutionAcceptedResponse(requestId);
            } else if ("Upgrade".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling UPDATE API when Upgrade cp4na lifecycle method is called
                final String updatePayload = messageConversionService.generateMessageFromRequest("Update", executionRequest);
                this.ciscoCncServiceDriver.updateSlice(executionRequest, jwt, getSliceName(executionRequest.getProperties()), updatePayload);
                // Delay sending the asynchronous response (from a different thread) as this method needs to complete first (to send the response back to Brent)
                externalMessagingService.sendDelayedExecutionAsyncResponse(new ExecutionAsyncResponse(requestId, ExecutionStatus.COMPLETE, null, Collections.emptyMap(), Collections.emptyMap()), rcDriverProperties.getExecutionResponseDelay());

                return new ExecutionAcceptedResponse(requestId);
            } else if ("Delete".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling DELETE API
                final String deletePayload = messageConversionService.generateMessageFromRequest("Delete", executionRequest);
                this.ciscoCncServiceDriver.deleteSlice(executionRequest, jwt, getSliceName(executionRequest.getProperties()), deletePayload);
                // Delay sending the asynchronous response (from a different thread) as this method needs to complete first (to send the response back to Brent)
                externalMessagingService.sendDelayedExecutionAsyncResponse(new ExecutionAsyncResponse(requestId, ExecutionStatus.COMPLETE, null, Collections.emptyMap(), Collections.emptyMap()), rcDriverProperties.getExecutionResponseDelay());
                return new ExecutionAcceptedResponse(requestId);
            } else {
                throw new IllegalArgumentException(String.format("Requested transition [%s] is not supported by this lifecycle driver", executionRequest.getLifecycleName()));
            }
        }else {
            logger.error("Unauthorized access");
            throw new AccessDeniedException("Invalid JWT Token. Unauthorized access");
        }
    }

    private String getSliceName(Map<String, Object> resourceProperties) throws MessageConversionException {
        String sliceName = (String)resourceProperties.get(SLICE_NAME);
        logger.debug("sliceName for Update/Delete is {}", sliceName);
        if(StringUtils.isEmpty(sliceName)){
            logger.error("sliceName cannot be empty for Update/Delete requests, make sure it is added in the resource properties");
            throw new MessageConversionException("sliceName cannot be empty for Update/Delete requests, make sure it is added in the resource properties");
        }
        return sliceName;
    }
}
