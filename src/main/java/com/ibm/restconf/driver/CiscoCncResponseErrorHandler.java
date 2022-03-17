package com.ibm.restconf.driver;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CiscoCncResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public CiscoCncResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        try {
            super.handleError(clientHttpResponse);
        } catch (RestClientResponseException e) {
            // First, check that the response contains JSON
            if (e.getResponseHeaders() != null && e.getResponseHeaders().getContentType() != null && e.getResponseHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)) {
                // Retrieve the body of the response and check it's not empty
                String responseBody = e.getResponseBodyAsString();
                if (!StringUtils.isEmpty(responseBody)) {
                    // Attempt to parse a ProblemDetails record out of the body of the response
                        throw new CiscoCncResponseException(String.format("Received restconf-compliant error when communicating with %s: %s", endpointDescription()), e);
                }
            }
            // Else, attempt to extract information out of the error response (as best as possible)
            final String responseBody = e.getResponseBodyAsString();
            String detailsMessage = e.getStatusText();
            if (!StringUtils.isEmpty(responseBody)) {
                detailsMessage += ": " + responseBody;
            }
            throw new CiscoCncResponseException(String.format("Caught REST client exception when communicating with %s", endpointDescription()));
        } catch (Exception e) {
            throw new CiscoCncResponseException(String.format("Caught general exception when communicating with %s", endpointDescription()), e);
        }
    }

    protected abstract String endpointDescription();

}