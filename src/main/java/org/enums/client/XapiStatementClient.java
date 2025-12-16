package org.enums.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.xapi.model.XapiStatement;

import java.util.List;

public class XapiStatementClient {

    private final XapiClientConfig config;
    private final XapiHttpClient http ;
    private final ObjectMapper mapper = new ObjectMapper();

    public XapiStatementClient(XapiClientConfig config) {
        this.config = config;
        this.http = new XapiHttpClient(config);
    }

    // single statement
    public String sendStatement(XapiStatement statement) throws Exception {
        String endpoint = config.getEndpoint() + "/statements";

        String json = mapper.writeValueAsString(statement);

        return http.post(endpoint, json);
    }

    // Send multiple statements
    public String sendStatements(List<XapiStatement> statements) throws Exception {
        String endpoint = config.getEndpoint() + "/statements";

        String json = mapper.writeValueAsString(statements);

        return http.post(endpoint, json);
    }
}
