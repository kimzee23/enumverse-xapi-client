package org.enums.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@Getter
@AllArgsConstructor
public class XapiClientConfig {

    private final String endpoint;
    private final String username;
    private final String password;
    private final int timeoutSeconds;

    private final int maxRetries;
    private final long initialBackoffMillis;


    public String getBasicAuthHeader() {
        if (username == null || password == null) {
            return null; // no auth
        }
        String raw = username + ":" + password;
        String encoded = java.util.Base64.getEncoder().encodeToString(raw.getBytes());
        return "Basic " + encoded;
    }

}
