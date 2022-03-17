package com.ibm.restconf.security;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.*;

public class CookieAuthenticatedRestTemplate extends RestTemplate {

    private final CookieCredentials credentials;
    private final CookieContext context;
    private final CookieAuthenticator authenticator;

    public CookieAuthenticatedRestTemplate(CookieCredentials credentials) {
        super();
        if (credentials == null) {
            throw new IllegalArgumentException("User credentials must be supplied.");
        }

        this.credentials = credentials;
        this.context = new CookieContext();
        this.authenticator = new CookieAuthenticator();
    }

    @Override
    protected ClientHttpRequest createRequest(URI uri, HttpMethod method) throws IOException {
        HttpCookie sessionCookie = getSessionCookie();

        ClientHttpRequest req = super.createRequest(uri, method);
        req.getHeaders().set("Cookie", sessionCookie.toString());

        return req;
    }

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        HttpCookie sessionCookie = context.getSessionCookie();
        RuntimeException rethrow;
        try {
            return super.doExecute(url, method, requestCallback, responseExtractor);
        } catch (HttpClientErrorException.Unauthorized e) {
            // Should we check for the WWW-Authenticate header here?
            rethrow = e;
        }
        if (sessionCookie != null) {
            context.setSessionCookie(null);
            return super.doExecute(url, method, requestCallback, responseExtractor);
        }
        throw rethrow;
    }

    private HttpCookie getSessionCookie() throws AccessDeniedException {
        HttpCookie sessionCookie = context.getSessionCookie();

        if (sessionCookie == null || sessionCookie.hasExpired()) {
            sessionCookie = acquireSessionCookie(context);
        }

        return sessionCookie;
    }

    private HttpCookie acquireSessionCookie(CookieContext cookieContext) throws AccessDeniedException {
        HttpCookie sessionCookie = authenticator.authenticate(credentials);

        if (sessionCookie == null) {
            throw new IllegalStateException("Provider returned a null session cookie, which is illegal according to the contract.");
        }

        cookieContext.setSessionCookie(sessionCookie);
        return sessionCookie;
    }

}