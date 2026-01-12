package org.enums.query;

import org.enums.client.XapiClient;
import org.enums.client.XapiClientConfig;
import org.enums.client.XapiResponse;
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

class XapiClientTest {

    private HttpClient http;
    private XapiClient client;

    @BeforeEach
    void setUp() {
        http = mock(HttpClient.class);
        client = new XapiClient(config(), http);
    }

    private XapiClientConfig config() {
        return new XapiClientConfig(
                "http://localhost:8080/xapi",
                "user",
                "pass",
                5
        );
    }

    private XapiStatement validStatement() {
        Actor actor = new Actor("mailto:test@example.com", "Test");
        Verb verb = new Verb("https://example.com/verbs/viewed", "Viewed");
        Activity activity = new Activity("https://example.com/activity/1", "Lesson");
        return new XapiStatement(null, actor, verb, activity, Instant.now());
    }

    @Test
    void sendStatement_shouldReturn200() throws Exception {
        HttpResponse<String> res = mock(HttpResponse.class);
        when(res.statusCode()).thenReturn(200);
        when(res.body()).thenReturn("OK");

        when(http.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(res);

        XapiResponse response = client.sendStatement(validStatement());

        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getBody());
    }

    @Test
    void sendStatement_shouldReturn400_onNull() {
        XapiResponse res = client.sendStatement(null);

        assertFalse(res.isSuccess());
        assertEquals(400, res.getStatusCode());
    }

    @Test
    void sendStatementAsync_shouldReturn200() {
        HttpResponse<String> res = mock(HttpResponse.class);
        when(res.statusCode()).thenReturn(200);
        when(res.body()).thenReturn("ASYNC_OK");

        when(http.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(res));

        XapiResponse response =
                client.sendStatementAsync(validStatement()).join();

        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("ASYNC_OK", response.getBody());
    }

    @Test
    void sendStatementsAsync_shouldFailOnEmptyBatch() {
        XapiResponse res =
                client.sendStatementsAsync(List.of()).join();

        assertFalse(res.isSuccess());
        assertEquals(400, res.getStatusCode());
    }
}
