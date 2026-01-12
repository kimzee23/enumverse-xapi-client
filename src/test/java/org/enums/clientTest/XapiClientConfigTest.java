package org.enums.clientTest;

import org.enums.client.XapiClientConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XapiClientConfigTest {

    @Test
    void basicPropertiesAndAuthHeader() {
        XapiClientConfig xapiClientConfig = new XapiClientConfig("http://localhost:8000/xapi", "username", "pass1234", 10);

        assertEquals("http://localhost:8000/xapi", xapiClientConfig.getEndpoint());
        assertEquals("username", xapiClientConfig.getUsername());
        assertEquals("pass1234", xapiClientConfig.getPassword());
        assertEquals(10, xapiClientConfig.getTimeoutSeconds());

        String expected = "Basic " + java.util.Base64.getEncoder().encodeToString("username:pass1234".getBytes());
        assertEquals(expected, xapiClientConfig.getBasicAuthHeader());
    }
}