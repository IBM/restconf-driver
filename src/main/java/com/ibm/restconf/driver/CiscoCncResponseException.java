package com.ibm.restconf.driver;

import com.ibm.restconf.model.ProblemDetails;
import org.springframework.web.client.RestClientException;


public class CiscoCncResponseException extends RestClientException {

    public static final int DEFAULT_STATUS_VALUE = 0;

    private final ProblemDetails problemDetails;

    public CiscoCncResponseException(String msg) {
        this(msg, new ProblemDetails(DEFAULT_STATUS_VALUE, msg));
    }

    public CiscoCncResponseException(Throwable ex) {
        this(ex.getLocalizedMessage(), ex, new ProblemDetails(DEFAULT_STATUS_VALUE, ex.getLocalizedMessage()));
    }

    public CiscoCncResponseException(String msg, Throwable ex) {
        this(msg, ex, new ProblemDetails(DEFAULT_STATUS_VALUE, String.format("%s: %s", msg, ex.getLocalizedMessage())));
    }

    public CiscoCncResponseException(String msg, ProblemDetails problemDetails) {
        super(msg);
        this.problemDetails = problemDetails;
    }

    public CiscoCncResponseException(String msg, Throwable ex, ProblemDetails problemDetails) {
        super(msg, ex);
        this.problemDetails = problemDetails;
    }

    public ProblemDetails getProblemDetails() {
        return problemDetails;
    }

}