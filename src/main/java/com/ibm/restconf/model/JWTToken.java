package com.ibm.restconf.model;

import lombok.Data;

@Data
public class JWTToken {
    private String token;
    @Override
    public String toString(){
        return "{\"token\":\"*********\"}";
    }
}
