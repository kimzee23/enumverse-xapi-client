package org.enums.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.client.XapiClientConfig;
import org.enums.client.XapiHttpClient;
import org.enums.client.XapiHttpResponse;
import org.enums.xapi.model.XapiStatement;

import java.util.List;
import java.util.Map;

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
        return getQueryResult(endpoint);
    }

    public QueryResult more(String moreUrl) throws Exception {
        String endpoint = config.getEndpoint() + moreUrl;
        return getQueryResult(endpoint);
    }

    private QueryResult getQueryResult(String endpoint) throws Exception {

        XapiHttpResponse response = http.get(endpoint);

        String responseJson = response.getBody();
        int status = response.getStatus();

        Map<String, Object> json = mapper.readValue(responseJson, Map.class);

        List<XapiStatement> statements =
                mapper.convertValue(
                        json.get("statements"),
                        mapper.getTypeFactory()
                                .constructCollectionType(List.class, XapiStatement.class)
                );

        String more = json.containsKey("more") ? (String) json.get("more") : null;

        return new QueryResult(statements, more, status);
    }
}
