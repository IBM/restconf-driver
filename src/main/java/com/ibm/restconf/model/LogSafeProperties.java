package com.ibm.restconf.model;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LogSafeProperties {

    public static final String OBFUSCATED_VALUE = "######";

    public static Map<String, String> getLogSafeProperties(
            final Map<String, ? extends ExecutionRequestPropertyValue> properties) {
        return properties != null ? properties.entrySet().stream()
                .map(entry -> {
                    ExecutionRequestPropertyValue value = entry.getValue();
                    if (PropertyType.KEY.getValue().equals(value.getType())) {
                        final KeyExecutionRequestPropertyValue keyPropertyValue = (KeyExecutionRequestPropertyValue) value;
                        value = new KeyExecutionRequestPropertyValue(keyPropertyValue.getKeyName(), LogSafeProperties.OBFUSCATED_VALUE, keyPropertyValue.getPublicKey());
                    }
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), value.toString());
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : null;
    }
}