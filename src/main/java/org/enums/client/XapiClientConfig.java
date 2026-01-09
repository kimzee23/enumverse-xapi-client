package org.enums.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;



@AllArgsConstructor
@Getter
@Setter
public class XapiClientConfig {

    private final String endpoint;
    private final String username;
    private final String password;
    private final int timeoutSeconds;

    private final int maxRetries;
    private final long initialBackoffMillis;

    //  Convenience constructor
    public XapiClientConfig(
            String endpoint,
            String username,
            String password,
            int timeoutSeconds
    ) {
        this(endpoint, username, password, timeoutSeconds, 0, 0);
    }

    public String getBasicAuthHeader() {
        if (username == null || password == null) {
            return null;
        }
        String raw = username + ":" + password;
        String encoded = java.util.Base64
                .getEncoder()
                .encodeToString(raw.getBytes());
        return "Basic " + encoded;
    }
}
