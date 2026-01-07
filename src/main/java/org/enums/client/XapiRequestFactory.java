package org.enums.client;



import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

class XapiRequestFactory {

    HttpRequest create(String json, XapiClientConfig config, String authHeader) {
        return HttpRequest.newBuilder()
                .uri(URI.create(config.getEndpoint() + "/statements"))
                .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                .header("Authorization", authHeader)
                .header("X-Experience-API-Version", "1.0.3")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
}
