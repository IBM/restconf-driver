package com.ibm.restconf.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;

/**
 * The supported property value types. This enum is not an exhaustive list, as custom data types may also be used
 */
public enum PropertyType {

    STRING("string"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    TIMESTAMP("timestamp"),
    FLOAT("float"),
    LIST("list"),
    MAP("map"),
    KEY("key");

    private final String value;

    private PropertyType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

    @JsonCreator
    public static PropertyType fromJson(String type) {
        for (PropertyType propertyType : PropertyType.values()) {
            if (propertyType.toString().equalsIgnoreCase(type)) {
                return propertyType;
            }
        }
        return null;
    }

    public static List<String> validOptions() {
        List<String> result = new ArrayList<>(PropertyType.values().length);
        for (PropertyType type : PropertyType.values()) {
            result.add(type.getValue());
        }
        return result;
    }
}
