package com.ibm.restconf.model.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Common representation of an error
 */
public class ErrorInfo {

    private String url;

    private String localizedMessage;

    private Map<String, Object> details;

    public ErrorInfo() {
        super();
    }

    public ErrorInfo(String url, String localizedMessage, Map<String, Object> details) {
        super();
        this.url = url;
        this.localizedMessage = localizedMessage;
        this.details = details;
    }

    /**
     * The URL of the request (if known)
     *
     * @return the URL or null if unknown
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The most user friendly description of the error that has occurred
     *
     * @return
     */
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }

    /**
     * Any additional information related to the error that may be useful during debugging
     *
     * @return
     */
    public Map<String, Object> getDetails() {
        if (details == null) {
            details = new HashMap<>();
        }
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

}