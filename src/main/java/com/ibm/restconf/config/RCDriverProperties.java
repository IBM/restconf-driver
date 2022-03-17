package com.ibm.restconf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Properties specific to the RestConf Driver.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "rcdriver")
public class RCDriverProperties {

    private final Logging logging = new Logging();
    private Duration restConnectTimeout = Duration.ofSeconds(10);
    private Duration restReadTimeout = Duration.ofSeconds(60);

    public Logging getLogging() {
        return logging;
    }


    public Duration getRestConnectTimeout() {
        return restConnectTimeout;
    }

    public Duration getRestReadTimeout() {
        return restReadTimeout;
    }

    public static class Logging {
        private int loggingRequestInterceptMaxBodySize = 10000000;

        public int getLoggingRequestInterceptMaxBodySize() {
            return loggingRequestInterceptMaxBodySize;
        }

        public void setLoggingRequestInterceptMaxBodySize(int loggingRequestInterceptMaxBodySize) {
            this.loggingRequestInterceptMaxBodySize = loggingRequestInterceptMaxBodySize;
        }
    }

}