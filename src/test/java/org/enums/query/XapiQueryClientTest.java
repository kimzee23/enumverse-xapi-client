package org.enums.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.client.*;
import org.enums.query.*;
import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XapiQueryClientTest {

    @Test
    void queryStatements_shouldReturnStatementsAndMore() throws Exception {
        XapiClientConfig config = new XapiClientConfig("http://lrs", "u", "p", 5);
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

        XapiQueryClient client = new XapiQueryClient(config, http);

        QueryResult result = client.queryStatements(new QueryParams());

        assertTrue(result.isSuccess());
        assertEquals(1, result.getStatements().size());
        assertEquals("/statements?cursor=abc", result.getMore());
    }
}
