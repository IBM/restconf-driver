package com.ibm.restconf.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Set;

public class RequestResponseLogUtilsTest {
    @Test
    public void testHeaderFilters(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("ContentType", List.of("application/json") );
        headers.put("authorization", List.of("Bearer token123") );
        headers.put("Authorization", List.of("Bearer token321") );
        headers.put("Set-Cookie", List.of("cookie1") );
        headers.put("set-cookie", List.of("cookie2") );
        HttpHeaders filteredHeaders = RequestResponseLogUtils.filterConfidentialData(headers);
        Set<String> headerNames = filteredHeaders.keySet();
        Assertions.assertThat(headerNames).doesNotContain("authorization");
        Assertions.assertThat(headerNames).doesNotContain("Authorization");
        Assertions.assertThat(headerNames).doesNotContain("Set-Cookie");
        Assertions.assertThat(headerNames).doesNotContain("set-cookie");
    }
}
