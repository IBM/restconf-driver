package com.ibm.restconf.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ibm.restconf.driver.CiscoCncResponseException;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestResponseLogUtils {
    public static final String LOG_META_DATA_HTTP_URI= "uri";
    public static final String LOG_META_DATA_HTTP_METHOD= "method";
    public static final String LOG_META_DATA_HTTP_STATUS_CODE= "status_code";
    public static final String LOG_META_DATA_HTTP_STATUS_REASON= "status_reason_phrase";
    public static final String LOG_META_DATA_HTTP_HEADERS= "headers";
    public static Map<String,Object> getResponseReceivedProtocolMetaData(int statusCode, String statusReasonPhrase, HttpHeaders headers){
        Map<String,Object> protocolMetadata=new HashMap<>();
        protocolMetadata.put(LOG_META_DATA_HTTP_STATUS_CODE,statusCode);
        protocolMetadata.put(LOG_META_DATA_HTTP_STATUS_REASON, statusReasonPhrase);
        if(headers!=null && headers.size()>0) {
            protocolMetadata.put(LOG_META_DATA_HTTP_HEADERS, filterConfidentialData(headers));
        }
        return protocolMetadata;

    }

    public static Map<String,Object> getRequestSentProtocolMetaData(String uri, String method, HttpHeaders headers){
        Map<String,Object> protocolMetadata=new HashMap<>();
        protocolMetadata.put(LOG_META_DATA_HTTP_URI,uri);
        protocolMetadata.put(LOG_META_DATA_HTTP_METHOD, method);
        if(headers!=null && headers.size()>0) {
            protocolMetadata.put(LOG_META_DATA_HTTP_HEADERS, filterConfidentialData(headers));
        }
        return protocolMetadata;
    }

    public static Map<String,Object> getRequestReceivedProtocolMetaData(String uri, String method, HttpHeaders headers){
        Map<String,Object> protocolMetadata=new HashMap<>();
        protocolMetadata.put(LOG_META_DATA_HTTP_URI, uri);
        protocolMetadata.put(LOG_META_DATA_HTTP_METHOD, method);
        if(headers!=null && headers.size()>0) {
            protocolMetadata.put(LOG_META_DATA_HTTP_HEADERS, filterConfidentialData(headers));
        }
        return protocolMetadata;
    }

    public static Map<String,Object> getResponseSentProtocolMetaData(int status_code, String statusReasonPhrase, HttpHeaders headers){
        Map<String,Object> protocolMetadata=new HashMap<>();
        protocolMetadata.put(LOG_META_DATA_HTTP_STATUS_CODE,status_code);
        protocolMetadata.put(LOG_META_DATA_HTTP_STATUS_REASON, statusReasonPhrase);
        if(headers!=null && headers.size()>0) {
            protocolMetadata.put(LOG_META_DATA_HTTP_HEADERS, filterConfidentialData(headers));
        }
        return protocolMetadata;
    }

    public static HttpHeaders filterConfidentialData(HttpHeaders headers){
        final HttpHeaders filteredHeaders = new HttpHeaders();
        if(headers!=null && (headers.containsKey("authorization") || headers.containsKey("Set-Cookie"))) {
            Set<String> headerNames = headers.keySet();
            List<String> filteredHeaderNames = headerNames.stream().filter(header -> !header.equalsIgnoreCase("Authorization") && !header.equalsIgnoreCase("Set-Cookie")).collect(Collectors.toList());
            filteredHeaderNames.forEach(header -> {
                List<String> headerValue = headers.get(header);
                if (headerValue != null) {
                    filteredHeaders.put(header, headerValue);
                }
            });
            return filteredHeaders;
        }else{
            return headers;
        }

    }

    public static String convertToJson(Object message){
        ObjectMapper jsonMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return jsonMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw  new CiscoCncResponseException("Error in parsing Object "+ message, e);
        }
    }
}