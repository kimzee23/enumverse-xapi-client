package clientTest;

import org.enums.client.*;
import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class XapiStatementClientTest {

    private XapiClientConfig config;
    private XapiHttpClient mockHttp;
    private XapiStatementClient client;

    @BeforeEach
    void setup() {
        config = new XapiClientConfig("http://localhost:8000/xapi", "user", "pass", 5);
        mockHttp = mock(XapiHttpClient.class);

        client = new XapiStatementClient(config) {
            @Override
            protected XapiHttpClient createHttpClient() {
                return mockHttp;
            }
        };
    }

    @Test
    void sendStatement_success() throws Exception {
        UUID id = UUID.randomUUID();
        XapiStatement st = new XapiStatement();
        st.setId(id);

        String jsonResponse = """
        {
            "status": 200,
            "success": true,
            "body": "[]"
        }
        """;

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(jsonResponse);

        XapiResponse res = client.sendStatement(st);

        assertTrue(res.isSuccess());
        assertEquals(200, res.getStatus());
        assertEquals("[]", res.getBody());

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockHttp).post(eq(config.getEndpoint() + "/statements"), bodyCaptor.capture());
        assertTrue(bodyCaptor.getValue().contains(id.toString()));
    }

    @Test
    void sendStatements_batch_success() throws Exception {
        XapiStatement s1 = new XapiStatement();
        s1.setId(UUID.randomUUID());
        XapiStatement s2 = new XapiStatement();
        s2.setId(UUID.randomUUID());

        String jsonResponse = """
        {
            "status": 204,
            "success": true
        }
        """;

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(jsonResponse);

        XapiResponse res = client.sendStatements(List.of(s1, s2));

        assertTrue(res.isSuccess());
        assertEquals(204, res.getStatus());
    }

    @Test
    void sendStatement_httpError_returnsFailure() throws Exception {
        XapiStatement st = new XapiStatement();
        st.setId(UUID.randomUUID());

        String jsonResponse = """
        {
            "status": 500,
            "success": false,
            "body": "server error"
        }
        """;

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(jsonResponse);

        XapiResponse res = client.sendStatement(st);

        assertFalse(res.isSuccess());
        assertEquals(500, res.getStatus());
        assertEquals("server error", res.getBody());
    }
}
