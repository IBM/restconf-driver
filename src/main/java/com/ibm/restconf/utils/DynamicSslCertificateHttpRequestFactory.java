package com.ibm.restconf.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class DynamicSslCertificateHttpRequestFactory extends SimpleClientHttpRequestFactory {

    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if (connection instanceof HttpsURLConnection) {
            this.prepareHttpsConnection((HttpsURLConnection) connection);
        }

        super.prepareConnection(connection, httpMethod);
    }

    private void prepareHttpsConnection(HttpsURLConnection connection) {
        connection.setHostnameVerifier(new SkipHostnameVerifier());

        try {
            connection.setSSLSocketFactory(this.createSslSocketFactory());
        } catch (Exception e) {
        }
    }

    private SSLSocketFactory createSslSocketFactory() throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[] { new SkipX509TrustManager() }, new SecureRandom());
        return context.getSocketFactory();
    }

    private static class SkipX509TrustManager implements X509TrustManager {
        private SkipX509TrustManager() {}

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
    }

    private static class SkipHostnameVerifier implements HostnameVerifier {
        private SkipHostnameVerifier() {}

        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }

}