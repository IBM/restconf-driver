package com.ibm.restconf.service;

public class MessageConversionException extends Exception {

    public MessageConversionException() {
    }

    public MessageConversionException(String message) {
        super(message);
    }

    public MessageConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageConversionException(Throwable cause) {
        super(cause);
    }

}
