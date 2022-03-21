package com.ibm.restconf.service.impl;

import java.time.Duration;

import com.ibm.restconf.config.RCDriverProperties;
import com.ibm.restconf.model.LcmOpOccPollingRequest;
import com.ibm.restconf.model.alm.ExecutionAsyncResponse;
import com.ibm.restconf.service.ExternalMessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaExternalMessagingServiceImpl implements ExternalMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaExternalMessagingServiceImpl.class);

    private final RCDriverProperties properties;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaExternalMessagingServiceImpl(RCDriverProperties properties, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.properties = properties;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override public void sendExecutionAsyncResponse(ExecutionAsyncResponse request) {
        try {
            final String message = objectMapper.writeValueAsString(request);
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(properties.getTopics().getLifecycleResponsesTopic(), message);

            future.addCallback(sendResult -> logger.debug("ExecutionAsyncResponse successfully sent"),
                    exception -> logger.warn("Exception sending ExecutionAsyncResponse", exception));
        } catch (JsonProcessingException e) {
            logger.warn("Exception generating message text from ExecutionAsyncResponse", e);
        }
    }

    @Override
    @Async
    public void sendDelayedExecutionAsyncResponse(ExecutionAsyncResponse request, Duration delay) {
        if (delay != null) {
            try {
                Thread.sleep(delay.toMillis());
            } catch (InterruptedException e) {
                logger.error("Thread interrupted during sleep", e);
            }
        }
        sendExecutionAsyncResponse(request);
    }

    @Override public void sendLcmOpOccPollingRequest(LcmOpOccPollingRequest request) {
        try {
            try {
                Thread.sleep(properties.getLcmOpOccPollingDelay().toMillis());
            } catch (InterruptedException e) {
                logger.error("Thread interrupted during sleep", e);
            }
            final String message = objectMapper.writeValueAsString(request);
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(properties.getTopics().getLcmOpOccPollingTopic(), message);

            future.addCallback(sendResult -> logger.debug("Submitted request to poll for LcmOpOcc [{}]", request.getVnfLcmOpOccId()),
                    exception -> logger.warn("Exception sending LcmOpOccPollingRequest", exception));
        } catch (JsonProcessingException e) {
            logger.warn("Exception generating message text from LcmOpOccPollingRequest", e);
        }
    }

}