package com.ibm.restconf.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.restconf.driver.CiscoCncResponseException;
import com.ibm.restconf.model.MessageDirection;
import com.ibm.restconf.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoggingUtils {

    private final static Logger logger = LoggerFactory.getLogger(LoggingUtils.class);

    private static final String LOGGING_CONTEXT_KEY_PREFIX = "traceCtx.".toLowerCase();
    private static final String LOGGING_CONTEXT_HEADER_PREFIX = "X-TraceCtx-".toLowerCase();
    private static final String TRANSACTION_ID_HEADER_KEY = "TransactionId".toLowerCase();

    private static final String LOG_MESSAGE_DIRECTION_KEY = "message_direction";

    private static final String LOG_EXTERNAL_REQUEST_ID_KEY = "tracectx.externalrequestid";

    private static final String LOG_CONTENT_TYPE_KEY = "content_type";

    private static final String LOG_PROTOCOL_KEY = "protocol";

    private static final String LOG_PROTOCOL_METADATA_KEY = "protocol_metadata";

    private static final String LOG_MSG_TYP_KEY= "message_type";

    private static final String LOG_DRIVER_REQUEST_ID ="tracectx.driverrequestid";

    public static Map<String, String> getContextMapFromHttpHeaders(HttpServletRequest servletRequest) {
        final Map<String, String> loggingContext = new HashMap<>();
        final Enumeration<String> headerNames = servletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.toLowerCase().startsWith(LOGGING_CONTEXT_HEADER_PREFIX)) {
                loggingContext.put(LOGGING_CONTEXT_KEY_PREFIX + headerName.substring(LOGGING_CONTEXT_HEADER_PREFIX.length()).toLowerCase(), servletRequest.getHeader(headerName));
            }
        }

        // Generate a random transaction Id, if one is not found
        if (!loggingContext.containsKey(LOGGING_CONTEXT_KEY_PREFIX + TRANSACTION_ID_HEADER_KEY)) {
            final String mdcValue = MDC.get(LOGGING_CONTEXT_KEY_PREFIX + TRANSACTION_ID_HEADER_KEY);
            if (mdcValue != null) {
                loggingContext.put(LOGGING_CONTEXT_KEY_PREFIX + TRANSACTION_ID_HEADER_KEY, mdcValue);
            } else {
                loggingContext.put(LOGGING_CONTEXT_KEY_PREFIX + TRANSACTION_ID_HEADER_KEY, UUID.randomUUID().toString());
            }
        }

        return loggingContext;
    }
    public static void logEnabledMDC(String message, MessageType messageType, MessageDirection messageDirection, String externalRequestId, String contentType, String protocol, Map<String,Object> protocolMetadata,String driverRequestId){
        try{
            MDC.put(LOG_MESSAGE_DIRECTION_KEY, messageDirection.toString());
            MDC.put(LOG_EXTERNAL_REQUEST_ID_KEY, externalRequestId);
            MDC.put(LOG_CONTENT_TYPE_KEY, contentType);
            MDC.put(LOG_PROTOCOL_KEY, protocol.toLowerCase());
            ObjectMapper jsonMapper = new ObjectMapper();
            try {
                MDC.put(LOG_PROTOCOL_METADATA_KEY, jsonMapper.writeValueAsString(protocolMetadata));
            } catch (JsonProcessingException e) {
                throw  new CiscoCncResponseException("Error in parsing protocol_metadata "+ protocolMetadata, e);
            }
            MDC.put(LOG_MSG_TYP_KEY,messageType.toString());
            MDC.put(LOG_DRIVER_REQUEST_ID,driverRequestId);

            logger.info(message);
        }finally{
            MDC.remove(LOG_MESSAGE_DIRECTION_KEY);
            MDC.remove(LOG_EXTERNAL_REQUEST_ID_KEY);
            MDC.remove(LOG_CONTENT_TYPE_KEY);
            MDC.remove(LOG_PROTOCOL_KEY);
            MDC.remove(LOG_PROTOCOL_METADATA_KEY);
            MDC.remove(LOG_MSG_TYP_KEY);
            MDC.remove(LOG_DRIVER_REQUEST_ID);
        }
    }

    public static void setHttpHeadersFromMDC(final HttpHeaders httpHeaders) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext == null) {
            // Can happen if there is no context set (mostly in unit tests)
            return;
        }
        mdcContext.keySet()
                .stream()
                .filter(k -> k.startsWith(LOGGING_CONTEXT_KEY_PREFIX))
                .forEach(k -> httpHeaders.set(LOGGING_CONTEXT_HEADER_PREFIX + k.substring(LOGGING_CONTEXT_KEY_PREFIX.length()), mdcContext.get(k)));
    }

    public static void populateMDCFromContextMap(Map<String, String> context) {
        context.keySet()
                .stream()
                .filter(k -> k.startsWith(LOGGING_CONTEXT_KEY_PREFIX))
                .forEach(k -> MDC.put(k, context.get(k)));
    }

}
