package com.ibm.restconf.common.security;

import com.fasterxml.jackson.core.JsonStreamContext;
import net.logstash.logback.mask.ValueMasker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class PasswordMasker implements ValueMasker {
    private final Pattern multilinePattern = Pattern.compile("\"Password\"\\s*:\\s*\"(.*?)\"", Pattern.MULTILINE| Pattern.CASE_INSENSITIVE);;
    private String maskMessage(String message) {
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
                if (matcher.group(group) != null) {
                    IntStream.range(matcher.start(group), matcher.end(group)).forEach(i -> sb.setCharAt(i, 'x'));
                }
            });
        }
        return sb.toString();
    }

    @Override
    public Object mask(JsonStreamContext context, Object value) {
        if (value instanceof CharSequence) {
            return maskMessage((String) value);
        }
        return value;
    }
}
