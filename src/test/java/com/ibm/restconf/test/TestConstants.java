package com.ibm.restconf.test;

import static com.ibm.restconf.config.RCDriverConstants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ibm.restconf.model.ResourceManagerDeploymentLocation;
import org.assertj.core.api.Condition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.ibm.restconf.model.AuthenticationType;

public abstract class TestConstants {

    public static final String TEST_SERVER_BASE_URL = "http://localhost:8080";
    public static final String SECURE_TEST_SERVER_BASE_URL = "https://localhost:8080";
    public static final String BASIC_AUTHORIZATION_HEADER = "Basic YmFzaWNfdXNlcjpiYXNpY19wYXNzd29yZA==";
    public static final String TEST_SESSION_COOKIE = "JSESSIONID=Rg3vHJZnehYLjVg7qi3bZjzg; Domain=foo.com; Path=/; Expires=Wed, 13 Jan 2021 22:23:01 GMT; Secure; HttpOnly";
    public static final String TEST_SESSION_TOKEN = "JSESSIONID=Rg3vHJZnehYLjVg7qi3bZjzg";
    public static final String TEST_ACCESS_TOKEN = "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3";
    public static final String TEST_ACCESS_TOKEN_RESPONSE = "{\"access_token\":\"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3\",\"token_type\":\"bearer\",\"expires_in\":3600,\"refresh_token\":\"IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk\",\"scope\":\"none\"}";
    public static final String NOTIFICATIONS_ENDPOINT = "http://localhost:8080/restconf/v1/notifications";
    public static final String EMPTY_JSON = "{}";
    public static final String TEST_EXCEPTION_MESSAGE = "TestExceptionMessage";

    public static final ResourceManagerDeploymentLocation TEST_DL_NO_AUTH = new ResourceManagerDeploymentLocation("test-location", "restconf");
    public static final ResourceManagerDeploymentLocation TEST_DL_BASIC_AUTH = new ResourceManagerDeploymentLocation("test-basic-location", "restconf");
    public static final ResourceManagerDeploymentLocation TEST_DL_OAUTH2_AUTH = new ResourceManagerDeploymentLocation("test-oauth2-location", "restconf");
    public static final ResourceManagerDeploymentLocation TEST_DL_SESSION_AUTH = new ResourceManagerDeploymentLocation("test-session-location", "restconf");

    public static final HttpEntity<String> EMPTY_JSON_ENTITY;

    static {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        EMPTY_JSON_ENTITY = new HttpEntity<>(EMPTY_JSON, headers);

        TEST_DL_NO_AUTH.getProperties().put(RC_SERVER_URL, TEST_SERVER_BASE_URL);

        TEST_DL_BASIC_AUTH.getProperties().put(RC_SERVER_URL, SECURE_TEST_SERVER_BASE_URL);
        TEST_DL_BASIC_AUTH.getProperties().put(AUTHENTICATION_TYPE, AuthenticationType.BASIC.toString());
        TEST_DL_BASIC_AUTH.getProperties().put(AUTHENTICATION_USERNAME, "basic_user");
        TEST_DL_BASIC_AUTH.getProperties().put(AUTHENTICATION_PASSWORD, "basic_password");

        TEST_DL_OAUTH2_AUTH.getProperties().put(RC_SERVER_URL, SECURE_TEST_SERVER_BASE_URL);
        TEST_DL_OAUTH2_AUTH.getProperties().put(AUTHENTICATION_TYPE, AuthenticationType.OAUTH2.toString());
        TEST_DL_OAUTH2_AUTH.getProperties().put(AUTHENTICATION_ACCESS_TOKEN_URI, "http://localhost:15080/oauth/token");
        TEST_DL_OAUTH2_AUTH.getProperties().put(AUTHENTICATION_CLIENT_ID, "LmClient");
        TEST_DL_OAUTH2_AUTH.getProperties().put(AUTHENTICATION_CLIENT_SECRET, "pass123");

        TEST_DL_SESSION_AUTH.getProperties().put(RC_SERVER_URL, SECURE_TEST_SERVER_BASE_URL);
        TEST_DL_SESSION_AUTH.getProperties().put(AUTHENTICATION_TYPE, AuthenticationType.COOKIE.toString());
        TEST_DL_SESSION_AUTH.getProperties().put(AUTHENTICATION_URL, "http://localhost:15080/login");
        TEST_DL_SESSION_AUTH.getProperties().put(AUTHENTICATION_USERNAME, "Administrator");
        TEST_DL_SESSION_AUTH.getProperties().put(AUTHENTICATION_PASSWORD, "TestPassw0rd");
    }

    public static final Condition<String> UUID_CONDITION = new Condition<String>("UUID Condition") {
        @Override
        public boolean matches(String value) {
            try {
                UUID.fromString(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    };

    public static String loadFileIntoString(final String fileName) throws IOException {
        // This appears to be the fastest way to load a file into a String (circa 2017 (JDK8))
        // http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string

        try (
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                InputStream inputStream = TestConstants.class.getResourceAsStream(fileName.startsWith("/") ? fileName : "/" + fileName)
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }
    }

    public static byte[] loadFileIntoByteArray(final String fileName) throws IOException {

        try (
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                InputStream inputStream = TestConstants.class.getResourceAsStream(fileName.startsWith("/") ? fileName : "/" + fileName)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toByteArray();
        }
    }

    public static String loadZipIntoBase64String(final String fileName) throws IOException {
        try (
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                InputStream inputStream = TestConstants.class.getResourceAsStream(fileName.startsWith("/") ? fileName : "/" + fileName)
        ) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return Base64.getEncoder().encodeToString(result.toByteArray());
        }
    }

    public static List<String> listZipContents(InputStream zipAsInputStream) throws IOException {
        List<String> zipContents = new ArrayList<String>();

        try (ZipInputStream zipInputStream = new ZipInputStream(zipAsInputStream)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    zipContents.add(zipEntry.getName());
                }
            }
        }
        return zipContents;
    }

}