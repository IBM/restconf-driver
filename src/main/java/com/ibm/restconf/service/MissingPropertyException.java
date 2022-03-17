package com.ibm.restconf.service;

public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException() {
    }

    public MissingPropertyException(String message) {
        super(message);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingPropertyException(Throwable cause) {
        super(cause);
    }

}
