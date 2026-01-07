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
import static org.mockito.Mockito.*;

class XapiStatementClientTest {

    private XapiClientConfig config;
    private XapiHttpClient mockHttp;
    private XapiStatementClient client;

    @BeforeEach
    void setup() {
        config = new XapiClientConfig("http://localhost:8000/xapi", "user", "pass", 5);
        mockHttp = mock(XapiHttpClient.class);

        // Inject the mock directly
        client = new XapiStatementClient(config, mockHttp);
    }

    @Test
    void sendStatement_success() throws Exception {
        UUID id = UUID.randomUUID();
        XapiStatement st = new XapiStatement();
        st.setId(id);

        XapiHttpResponse httpResponse = new XapiHttpResponse(200, "[]");

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(httpResponse);

        XapiResponse res = client.sendStatement(st);

        assertTrue(res.isSuccess());
        assertEquals(200, res.getStatusCode());
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

        XapiHttpResponse httpResponse = new XapiHttpResponse(204, "");

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(httpResponse);

        XapiResponse res = client.sendStatements(List.of(s1, s2));

        assertTrue(res.isSuccess());
        assertEquals(204, res.getStatusCode());
    }

    @Test
    void sendStatement_httpError_returnsFailure() throws Exception {
        XapiStatement st = new XapiStatement();
        st.setId(UUID.randomUUID());

        XapiHttpResponse httpResponse = new XapiHttpResponse(500, "server error");

        when(mockHttp.post(eq(config.getEndpoint() + "/statements"), anyString()))
                .thenReturn(httpResponse);

        XapiResponse res = client.sendStatement(st);

        assertFalse(res.isSuccess());
        assertEquals(500, res.getStatusCode());
        assertEquals("server error", res.getBody());
    }
}
