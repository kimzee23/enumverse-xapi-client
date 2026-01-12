package org.enums.query;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParamsTest {

    @Test
    void emptyParams_shouldProduceEmptyQueryString() {
        QueryParams params = new QueryParams();
        assertEquals("", params.toQueryString());
    }

    @Test
    void singleParam_shouldRenderCorrectly() {
        QueryParams params = new QueryParams()
                .add("limit", 10);

        assertEquals("?limit=10", params.toQueryString());
    }

    @Test
    void multipleParams_shouldJoinWithAmpersand() {
        QueryParams params = new QueryParams()
                .add("limit", 10)
                .add("ascending", true);

        String qs = params.toQueryString();
        assertTrue(qs.contains("limit=10"));
        assertTrue(qs.contains("ascending=true"));
        assertTrue(qs.startsWith("?"));
        assertTrue(qs.contains("&"));
    }

    @Test
    void valuesShouldBeUrlEncoded() {
        QueryParams params = new QueryParams()
                .add("verb", "http://adlnet.gov/expapi/verbs/answered");

        assertTrue(params.toQueryString().contains("http%3A"));
    }
}
