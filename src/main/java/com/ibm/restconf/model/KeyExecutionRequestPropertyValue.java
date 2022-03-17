package com.ibm.restconf.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = JsonDeserializer.None.class)
public class KeyExecutionRequestPropertyValue extends ExecutionRequestPropertyValue {

    private String privateKey;
    private String publicKey;

    public KeyExecutionRequestPropertyValue(String keyName, String privateKey) {
        this(keyName, privateKey, null);
    }

    @JsonCreator
    public KeyExecutionRequestPropertyValue(@JsonProperty("keyName") String keyName,
                                            @JsonProperty("privateKey") String privateKey, @JsonProperty("publicKey") String publicKey) {
        super(keyName, PropertyType.KEY.getValue());
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getKeyName() {
        return (String) getValue();
    }

    public void setKeyName(String keyName) {
        setValue(keyName);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        KeyExecutionRequestPropertyValue that = (KeyExecutionRequestPropertyValue) o;
        return Objects.equals(getKeyName(), that.getKeyName()) && Objects.equals(privateKey, that.privateKey)
                && Objects.equals(publicKey, that.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKeyName(), privateKey, publicKey);
    }

    @Override
    public String toString() {
        return String.format("{\"_class\":\"%s\", \"keyName\":\"%s\", \"privateKey\":\"%s\", \"publicKey\":\"%s\"}",
                this.getClass().getSimpleName(), this.getKeyName(), LogSafeProperties.OBFUSCATED_VALUE,
                this.getPublicKey());
    }

}

