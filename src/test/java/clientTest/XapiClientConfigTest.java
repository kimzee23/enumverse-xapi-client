package clientTest;

import org.enums.client.XapiClientConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XapiClientConfigTest {

    @Test
    void basicPropertiesAndAuthHeader() {
        XapiClientConfig cfg = new XapiClientConfig("http://localhost:8000/xapi", "user", "pass", 10);

        assertEquals("http://localhost:8000/xapi", cfg.getEndpoint());
        assertEquals("user", cfg.getUsername());
        assertEquals("pass", cfg.getPassword());
        assertEquals(10, cfg.getTimeoutSeconds());

        String expected = "Basic " + java.util.Base64.getEncoder().encodeToString("user:pass".getBytes());
        assertEquals(expected, cfg.getBasicAuthHeader());
    }
}