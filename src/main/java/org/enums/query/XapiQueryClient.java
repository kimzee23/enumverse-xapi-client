package org.enums.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.client.XapiClientConfig;
import org.enums.client.XapiHttpClient;
import org.enums.client.XapiHttpResponse;
import org.enums.xapi.model.XapiStatement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class XapiQueryClient {


    private final XapiClientConfig config;
    private final XapiHttpClient http;
    private final ObjectMapper mapper = new ObjectMapper();

    public XapiQueryClient(XapiClientConfig config, XapiHttpClient http) {
        this.config = config;
        this.http = http;
    }

    public QueryResult queryStatements(QueryParams params) throws Exception {
        String endpoint = config.getEndpoint() + "/statements" + params.toQueryString();
        return executeQuery(endpoint);
    }

    public QueryResult more(String moreUrl) throws Exception {
        String endpoint = config.getEndpoint() + moreUrl;
        return executeQuery(endpoint);
    }

    private QueryResult executeQuery(String endpoint) throws Exception {

        XapiHttpResponse response = http.get(endpoint);

        Map<String, Object> json =
                mapper.readValue(response.getBody(), Map.class);

        List<XapiStatement> statements =
                mapper.convertValue(
                        json.get("statements"),
                        mapper.getTypeFactory()
                                .constructCollectionType(List.class, XapiStatement.class)
                );

        String more = (String) json.getOrDefault("more", null);

        return new QueryResult(statements, more, response.getStatus());
    }
    public CompletableFuture<QueryResult> queryStatementsAsync(QueryParams params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return queryStatements(params);
            } catch (Exception e) {
                return new QueryResult(List.of(), null, 400);
            }
        });
    }
    public CompletableFuture<QueryResult> moreAsync(String moreUrl) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return more(moreUrl);
            } catch (Exception e) {
                return new QueryResult(List.of(), null, 400);
            }
        });
    }
}
