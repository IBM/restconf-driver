package com.ibm.restconf.model;

public enum MessageDirection {
    RECEIVED,
    SENT;
    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
