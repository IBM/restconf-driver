package com.ibm.restconf.security;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

class CookieAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(CookieAuthenticator.class);
    private static final String SET_COOKIE_HEADER_NAME = "Set-Cookie";

    private RestOperations restTemplate;

    HttpCookie authenticate(CookieCredentials cookieCredentials) throws AccessDeniedException {
        try {
            // Put credentials into a map (used to generate the form)
            final MultiValueMap<String, String> credentialsForm = new LinkedMultiValueMap<>();
            credentialsForm.add(cookieCredentials.getUsernameTokenName(), cookieCredentials.getUsername());
            credentialsForm.add(cookieCredentials.getPasswordTokenName(), cookieCredentials.getPassword());

            // Set the appropriate Content-Type header
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Create the request and submit
            final HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(credentialsForm, headers);
            final ResponseEntity<Void> authenticationResponse = getRestTemplate().exchange(cookieCredentials.getAuthenticationUrl(), HttpMethod.POST, requestEntity, Void.class);

            // Check we get the correct response code (assume all 2xx are successful)
            if (authenticationResponse.getStatusCode().is2xxSuccessful() || authenticationResponse.getStatusCode() == HttpStatus.FOUND) {
                // Extract the value from the Set-Cookie header
                if (authenticationResponse.getHeaders().containsKey(SET_COOKIE_HEADER_NAME)) {
                    final List<String> setCookieHeaderValues = authenticationResponse.getHeaders().get(SET_COOKIE_HEADER_NAME);
                    logger.debug("Authentication request returned the following cookies [{}]", setCookieHeaderValues);
                    if (setCookieHeaderValues != null) {
                        List<HttpCookie> sessionCookies = setCookieHeaderValues.stream()
                                .flatMap(c -> HttpCookie.parse(c).stream())
                                .filter(c -> c.getName().matches(".*SESSION.*"))
                                .collect(Collectors.toList());
                        // What if we get multiple cookies here?
                        if (!sessionCookies.isEmpty()) {
                            return sessionCookies.get(0);
                        }
                    }
                }
            }
        } catch (RestClientException rce) {
            throw new AccessDeniedException("Error requesting session cookie", rce);
        }
        return null;
    }

    private ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
        @Override
        protected void prepareConnection(HttpURLConnection connection, String httpMethod)
                throws IOException {
            super.prepareConnection(connection, httpMethod);
            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(false);
        }
    };

    private RestOperations getRestTemplate() {
        if (restTemplate == null) {
            synchronized (this) {
                if (restTemplate == null) {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.setRequestFactory(requestFactory);
                    this.restTemplate = restTemplate;
                }
            }
        }
        return restTemplate;
    }

}