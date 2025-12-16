package org.enums.client;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XapiHttpClient {

    private final XapiClientConfig config;
    private final HttpClient client;

    public XapiHttpClient(XapiClientConfig config) {
        this.config = config;
        this.client = HttpClient.newHttpClient();
    }



    private String authHeader() {
        String raw = config.getUsername() + ":" + config.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(raw.getBytes());
    }

    public XapiHttpResponse get(String url) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader())
                .header("X-Experience-API-Version", "1.0.3")
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new XapiHttpResponse(
                response.statusCode(),
                response.body()
        );
    }


    public XapiHttpResponse post(String url, String jsonBody) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader())
                .header("X-Experience-API-Version", "1.0.3")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return new XapiHttpResponse(
                response.statusCode(),
                response.body()
        );
    }
}