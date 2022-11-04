package com.ibm.restconf.config;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private final String requestBody;

    BufferedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = IOUtils.toString(request.getReader());
    }

    @Override public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());

            @Override public int read() {
                return byteArrayInputStream.read();
            }

            @Override public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override public boolean isReady() {
                return true;
            }

            @Override public void setReadListener(final ReadListener listener) {
                throw new UnsupportedOperationException("Not implemented");
            }
        };
    }

    @Override public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}