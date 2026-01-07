package clientTest;

import org.enums.client.XapiClient;
import org.enums.client.XapiClientConfig;
import org.enums.client.XapiResponse;
import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;

class XapiClientTest {

    private XapiClientConfig config() {
        return new XapiClientConfig(
                "https://lrs.example.com/xapi",
                "user",
                "pass",
                10
        );
    }

    @Test
    @DisplayName("Default constructor should create client with default HttpClient")
    void testDefaultConstructor() {
        XapiClient client = new XapiClient(config());
        assertNotNull(client);
    }

    @Test
    @DisplayName("Injected HttpClient constructor should work")
    void testInjectedHttpClientConstructor() {
        HttpClient httpClient = HttpClient.newHttpClient();
        XapiClient client = new XapiClient(config(), httpClient);
        assertNotNull(client);
    }

    @Test
    @DisplayName("Sending null statement returns 400 response")
    void testSendNullStatement() throws Exception {
        XapiClient client = new XapiClient(config());
        XapiResponse res = client.sendStatement(null);

        assertFalse(res.isSuccess());
        assertEquals(400, res.getStatus());
    }
}