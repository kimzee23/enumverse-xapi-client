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
    public XapiPayloadService getPayloadService() {
        return payloads;
    }


    public XapiResponse sendStatement(XapiStatement statement) {
        return execute(() -> payloads.serialize(statement));
    }

    public XapiResponse sendStatements(List<XapiStatement> list) {
        return execute(() -> payloads.serialize(list));
    }

    public CompletableFuture<XapiResponse> sendStatementAsync(XapiStatement statement) {
        return executeAsync(() -> payloads.serialize(statement));
    }

    public CompletableFuture<XapiResponse> sendStatementsAsync(List<XapiStatement> list) {
        return executeAsync(() -> payloads.serialize(list));
    }

    private XapiResponse execute(SerializerCall call) {
        try {
            var json = call.run();
            var req = factory.create(json, config, config.getBasicAuthHeader());
            return sender.send(req);
        } catch (Exception error) {
            return new XapiResponse(false, 400, error.getMessage());
        }
    }

    private CompletableFuture<XapiResponse> executeAsync(SerializerCall call) {
        try {
            var json = call.run();
            var req = factory.create(json, config, config.getBasicAuthHeader());
            return sender.sendAsync(req);
        } catch (Exception error) {
            return CompletableFuture.completedFuture(
                    new XapiResponse(false, 400, error.getMessage())
            );
        }
    }


    @FunctionalInterface
    private interface SerializerCall {
        String run() throws Exception;
    }
}
