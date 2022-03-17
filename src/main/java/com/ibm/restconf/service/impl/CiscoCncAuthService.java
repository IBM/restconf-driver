package com.ibm.restconf.service.impl;

import com.ibm.restconf.driver.CiscoCncServiceDriver;
import com.ibm.restconf.model.ExecutionRequest;
import com.ibm.restconf.service.AuthService;
import com.ibm.restconf.service.LifecycleManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CiscoCncAuthService")
public class CiscoCncAuthService implements AuthService {

    private final static Logger logger = LoggerFactory.getLogger(CiscoCncAuthService.class);

    private CiscoCncServiceDriver ciscoCncAuthServiceDriver;

    @Autowired
    public CiscoCncAuthService(CiscoCncServiceDriver ciscoCncAuthServiceDriver){
        this.ciscoCncAuthServiceDriver = ciscoCncAuthServiceDriver;
    }

    @Override
    public String authenticate(ExecutionRequest executionRequest) {
        logger.info("Calling Cisco CNC API to get JWT Token.");
        return getToken(executionRequest, getTicket(executionRequest));
    }

    public String getTicket(ExecutionRequest executionRequest){
        logger.info("Calling Cisco CNC API to get Ticket.");
        return this.ciscoCncAuthServiceDriver.getTicket(executionRequest.getDeploymentLocation());
    }
    private String getToken(ExecutionRequest executionRequest, String ticket){
        return this.ciscoCncAuthServiceDriver.getToken(executionRequest.getDeploymentLocation(), ticket);
    }

}
