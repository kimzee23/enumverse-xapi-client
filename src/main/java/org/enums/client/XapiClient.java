package org.enums.client;

import org.enums.xapi.model.XapiStatement;
import org.enums.xapi.validation.XapiValidator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

public class XapiClient {

    private final HttpClient http;
    private final XapiClientConfig config;
    private final XapiValidator validator;

    public XapiClient(XapiClientConfig config) {
        this(config, HttpClient.newHttpClient());
    }

    public XapiClient(XapiClientConfig config, HttpClient httpClient) {
        this.config = config;
        this.http = httpClient;
        this.validator = new XapiValidator();
    }

    // BASIC AUTH
    protected String basicAuthHeader() {
        String raw = config.getUsername() + ":" + config.getPassword();
        return "Basic " +
                Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    // SEND SINGLE STATEMENT
    public XapiResponse sendStatement(XapiStatement statement) throws Exception {

        if (statement == null) {
            return new XapiResponse(false, 400, "Statement is null");
        }

        // instance validation (non-static)
        XapiValidator.ValidationResult vr = validator.validate(statement);
        if (!vr.isValid()) {
            return new XapiResponse(false, 400, String.join("\n", vr.getMessages()));
        }

        String json = StatementSerializer.toJson(statement);
        return getXapiResponse(json);
    }

    // SEND MULTIPLE STATEMENTS (BATCH)
    public XapiResponse sendStatements(List<XapiStatement> list) throws Exception {

        if (list == null || list.isEmpty()) {
            return new XapiResponse(false, 400, "Batch is empty");
        }

        // validate each statement safely
        for (XapiStatement s : list) {
            XapiValidator.ValidationResult vr = validator.validate(s);
            if (!vr.isValid()) {
                return new XapiResponse(
                        false,
                        400,
                        "Batch validation failed:\n" + String.join("\n", vr.getMessages())
                );
            }
        }

        String json = JsonMapper.INSTANCE.writeValueAsString(list);
        return getXapiResponse(json);
    }

    private XapiResponse getXapiResponse(String json)
            throws java.io.IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getEndpoint() + "/statements"))
                .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                .header("Authorization", basicAuthHeader())
                .header("X-Experience-API-Version", "1.0.3")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> httpRes =
                http.send(request, HttpResponse.BodyHandlers.ofString());

        return new XapiResponse(
                httpRes.statusCode() >= 200 && httpRes.statusCode() < 300,
                httpRes.statusCode(),
                httpRes.body()
        );
    }
}
