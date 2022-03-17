package com.ibm.restconf.security;

public class CookieCredentials {

    private String usernameTokenName;
    private String username;
    private String passwordTokenName;
    private String password;
    private String authenticationUrl;

    public String getUsernameTokenName() {
        return usernameTokenName;
    }

    public void setUsernameTokenName(String usernameTokenName) {
        this.usernameTokenName = usernameTokenName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordTokenName() {
        return passwordTokenName;
    }

    public void setPasswordTokenName(String passwordTokenName) {
        this.passwordTokenName = passwordTokenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

}