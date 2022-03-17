package com.ibm.restconf.model;

public enum AuthenticationType {
    NONE, BASIC, OAUTH2, COOKIE;

    public static AuthenticationType valueOfIgnoreCase(String value) {
        for (AuthenticationType type : AuthenticationType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}