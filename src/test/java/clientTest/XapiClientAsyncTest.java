package clientTest;


import org.enums.client.*;
import org.enums.xapi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XapiClientAsyncTest {

    private HttpClient http;
    private XapiClient client;

    @BeforeEach
    void setUp() {
        http = mock(HttpClient.class);

        XapiClientConfig config = new XapiClientConfig(
                "http://localhost:8080/xapi",
                "user",
                "pass",
                5
        );

        client = new XapiClient(config, http);
    }

    private XapiStatement validStatement() {
        Actor actor = new Actor("mailto:test@example.com", "Test");
        Verb verb = new Verb("https://example.com/verbs/viewed", "Viewed");
        Activity activity = new Activity("https://example.com/activity/1", "Lesson");

        return new XapiStatement(null, actor, verb, activity, Instant.now());
    }
    @Test
    void sendStatements_shouldReturnSuccess_on200() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("OK");

        when(http.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        XapiResponse res = client.sendStatements(List.of(validStatement()));

        assertTrue(res.isSuccess());
        assertEquals(200, res.getStatusCode());
        assertEquals("OK", res.getBody());
    }
    @Test
    void sendStatements_shouldReturn400_onValidationError() {
        XapiStatement invalid = new XapiStatement(); // missing everything

        XapiResponse res = client.sendStatements(List.of(invalid));

        assertFalse(res.isSuccess());
        assertEquals(400, res.getStatusCode());
        assertNotNull(res.getBody());
    }

    @Test
    void sendStatementAsync_shouldCompleteSuccessfully() {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("");

        when(http.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        CompletableFuture<XapiResponse> future =
                client.sendStatementAsync(validStatement());

        XapiResponse res = future.join();

        assertTrue(res.isSuccess());
        assertEquals(200, res.getStatusCode());
    }



    @Test
    void sendStatementsAsync_shouldCompleteSuccessfully() {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("batch-ok");

        when(http.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        CompletableFuture<XapiResponse> future =
                client.sendStatementsAsync(List.of(validStatement()));

        XapiResponse res = future.join();

        assertTrue(res.isSuccess());
        assertEquals(200, res.getStatusCode());
        assertEquals("batch-ok", res.getBody());
    }
    @Test
    void sendStatementAsync_shouldReturn400_onValidationError() {
        XapiStatement invalid = new XapiStatement();

        CompletableFuture<XapiResponse> future =
                client.sendStatementAsync(invalid);

        XapiResponse res = future.join();

        assertFalse(res.isSuccess());
        assertEquals(400, res.getStatusCode());
    }
}


