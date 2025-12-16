package clientTest;

import org.enums.query.QueryParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParamsTest {

    @Test
    void toQueryString_buildsCorrectly() {
        QueryParams queryParams = QueryParams.builder()
                .verb("https://adlnet.gov/expapi/verbs/completed")
                .activityId("https://example.com/activity/1")
                .limit(10)
                .build();

        String qs = queryParams.toQueryString();
        assertNotNull(qs);
        assertTrue(qs.contains("verb="));
        assertTrue(qs.contains("activityId="));
        assertTrue(qs.contains("limit="));
    }
}