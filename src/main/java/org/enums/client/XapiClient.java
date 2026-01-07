package org.enums.client;

import org.enums.xapi.model.XapiStatement;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class XapiClient {

    private final XapiPayloadService payloads = new XapiPayloadService();
    private final XapiRequestFactory factory = new XapiRequestFactory();
    private final XapiRequestSender sender;
    private final XapiClientConfig config;

    public XapiClient(XapiClientConfig config) {
        this(config, HttpClient.newHttpClient());
    }

    public XapiClient(XapiClientConfig config, HttpClient http) {
        this.config = config;
        this.sender = new XapiRequestSender(http);
    }

    public XapiResponse sendStatement(XapiStatement s) {
        return execute(() -> payloads.serialize(s));
    }

    public XapiResponse sendStatements(List<XapiStatement> list) {
        return execute(() -> payloads.serialize(list));
    }

    public CompletableFuture<XapiResponse> sendStatementAsync(XapiStatement s) {
        return executeAsync(() -> payloads.serialize(s));
    }

    public CompletableFuture<XapiResponse> sendStatementsAsync(List<XapiStatement> list) {
        return executeAsync(() -> payloads.serialize(list));
    }

    private XapiResponse execute(SerializerCall call) {
        try {
            var json = call.run();
            var req = factory.create(json, config, config.getBasicAuthHeader());
            return sender.send(req);
        } catch (Exception e) {
            return new XapiResponse(false, 400, e.getMessage());
        }
    }

    private CompletableFuture<XapiResponse> executeAsync(SerializerCall call) {
        try {
            var json = call.run();
            var req = factory.create(json, config, config.getBasicAuthHeader());
            return sender.sendAsync(req);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new XapiResponse(false, 400, e.getMessage())
            );
        }
    }

    public void validateAndSerialize(Object any) {
    }

    @FunctionalInterface
    private interface SerializerCall {
        String run() throws Exception;
    }
}
