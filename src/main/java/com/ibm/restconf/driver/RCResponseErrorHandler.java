package com.ibm.restconf.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component("RCResponseErrorHandler")
public class RCResponseErrorHandler extends CiscoCncResponseErrorHandler {

    @Autowired
    public RCResponseErrorHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected String endpointDescription() {
        return "RestConf";
    }

}