package org.enums.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.client.*;
import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QueryIntegrationTest {

    @Test
    void shouldQueryStatementsWithCursor() throws Exception {

        // Arrange
        XapiClientConfig config = new XapiClientConfig(
                "http://localhost:8000/xapi",
                "user",
                "pass",
                5
        );

        XapiHttpClient http = mock(XapiHttpClient.class);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(
                Map.of(
                        "statements", List.of(new XapiStatement()),
                        "more", "/statements?cursor=abc"
                )
        );

        when(http.get(anyString()))
                .thenReturn(new XapiHttpResponse(200, json));

        XapiQueryClient queryClient =
                new XapiQueryClient(config, http);

        QueryParams params =
                TypedQuery.create()
                        .verb("https://adlnet.gov/expapi/verbs/completed")
                        .limit(10)
                        .toParams();

        // Act
        QueryResult result = queryClient.queryStatements(params);


        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getStatements().size());
        assertEquals("/statements?cursor=abc", result.getMore());

        CursorIterator it = new CursorIterator(queryClient, params);
        assertTrue(it.hasNext());
    }
}
