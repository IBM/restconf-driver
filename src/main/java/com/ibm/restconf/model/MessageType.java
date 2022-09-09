package com.ibm.restconf.model;

public enum MessageType {
    REQUEST,
    RESPONSE,
    MESSAGE;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}