package clientTest;

import org.enums.query.QueryParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryParamsTest {

    @Test
    void toQueryString_buildsCorrectly() {
        QueryParams params = QueryParams.builder()
                .build()
                .add("verb", "https://adlnet.gov/expapi/verbs/completed")
                .add("activityId", "https://example.com/activity/1")
                .add("limit", 10);

        String qs = params.toQueryString();

        assertTrue(qs.contains("verb="));
        assertTrue(qs.contains("activityId="));
        assertTrue(qs.contains("limit=10"));
    }

}