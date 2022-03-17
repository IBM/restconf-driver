package com.ibm.restconf.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProblemDetailsTest {

    @Test
    public void testProblemDetails() {

        ProblemDetails problemDetails = new ProblemDetails(10, "string");
        problemDetails.setInstance("Instance");
        problemDetails.setTitle("Title");
        problemDetails.setType("Type");

        assertThat(problemDetails.getInstance()).isEqualTo("Instance");
        assertThat(problemDetails.getTitle()).isEqualTo("Title");
        assertThat(problemDetails.getType()).isEqualTo("Type");

    }

}
