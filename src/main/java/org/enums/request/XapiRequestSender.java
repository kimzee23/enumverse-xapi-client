package org.enums.request;


import org.enums.client.XapiResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

class XapiRequestSender {

    private final HttpClient http;

    XapiRequestSender(HttpClient http) {
        this.http = http;
    }

    XapiResponse send(HttpRequest request) throws Exception {
        HttpResponse<String> res =
                http.send(request, HttpResponse.BodyHandlers.ofString());

        return map(res);
    }

    CompletableFuture<XapiResponse> sendAsync(HttpRequest request) {
        return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::map);
    }

    private XapiResponse map(HttpResponse<String> res) {
        return new XapiResponse(
                res.statusCode() >= 200 && res.statusCode() < 300,
                res.statusCode(),
                res.body()
        );
    }
}
