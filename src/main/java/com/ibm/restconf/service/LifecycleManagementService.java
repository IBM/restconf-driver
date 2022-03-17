package com.ibm.restconf.service;

import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionAcceptedResponse;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.security.AccessDeniedException;
import com.ibm.restconf.service.impl.CiscoCncAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;


@Service("LifecycleManagementService")
public class LifecycleManagementService {

    private final static Logger logger = LoggerFactory.getLogger(LifecycleManagementService.class);
    private final static String SLICE_NAME = "sliceName";

    private final AuthService authService;
    private final CiscoCncServiceDriver ciscoCncServiceDriver;
    private final MessageConversionService messageConversionService;
    @Autowired
    public LifecycleManagementService(CiscoCncServiceDriver ciscoCncServiceDriver, CiscoCncAuthService ciscoCncAuthService, MessageConversionService messageConversionService){
        this.ciscoCncServiceDriver = ciscoCncServiceDriver;
        this.authService = ciscoCncAuthService;
        this.messageConversionService = messageConversionService;
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
                return new ExecutionAcceptedResponse(requestId);
            } else if ("Update".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling UPDATE API
                final String updatePayload = messageConversionService.generateMessageFromRequest("Update", executionRequest);
                this.ciscoCncServiceDriver.updateSlice(executionRequest, jwt, getSliceName(executionRequest), updatePayload);
                return new ExecutionAcceptedResponse(requestId);
            } else if ("Delete".equalsIgnoreCase(executionRequest.getLifecycleName())) {
                //Calling DELETE API
                final String deletePayload = messageConversionService.generateMessageFromRequest("Delete", executionRequest);
                this.ciscoCncServiceDriver.deleteSlice(executionRequest, jwt, getSliceName(executionRequest), deletePayload);
                return new ExecutionAcceptedResponse(requestId);
            } else {
                throw new IllegalArgumentException(String.format("Requested transition [%s] is not supported by this lifecycle driver", executionRequest.getLifecycleName()));
            }
        }else {
            logger.error("Unauthorized access");
            throw new AccessDeniedException("Invalid JWT Token. Unauthorized access");
        }
    }

    private String getSliceName(ExecutionRequest executionRequest) throws MessageConversionException{
        String sliceName = (String)executionRequest.getResourceProperties().get(SLICE_NAME).getValue();
        if(StringUtils.isEmpty(sliceName)){
            logger.error("sliceName cannot be empty for Update request, make sure it is added in resource properties");
            throw new MessageConversionException("sliceName cannot be empty for Update/Delete request, make sure added in the ResourceProperties");
        }
        return sliceName;
    }
}
