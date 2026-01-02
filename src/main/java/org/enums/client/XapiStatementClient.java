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
    //for testing
    public XapiStatementClient(XapiClientConfig config, XapiHttpClient httpClient) {
        this.config = config;
        this.http = httpClient;
    }

    protected XapiHttpClient createHttpClient() {
        return new XapiHttpClient(config);
    }


    // single statement
    public XapiResponse sendStatement(XapiStatement statement) throws Exception {

        if (statement == null) {
            return new XapiResponse(false, 400, "Statement is null");
        }

        String endpoint = config.getEndpoint() + "/statements";
        String json = mapper.writeValueAsString(statement);

        XapiHttpResponse response = http.post(endpoint, json);

        return new XapiResponse(
                response.getStatus() >= 200 && response.getStatus() < 300,
                response.getStatus(),
                response.getBody()
        );
    }


    // Send multiple statements
    public XapiResponse sendStatements(List<XapiStatement> statements) throws Exception {

        if (statements == null || statements.isEmpty()) {
            return new XapiResponse(false, 400, "Statements list is empty");
        }

        String endpoint = config.getEndpoint() + "/statements";
        String json = mapper.writeValueAsString(statements);

        XapiHttpResponse response = http.post(endpoint, json);

        return new XapiResponse(
                response.getStatus() >= 200 && response.getStatus() < 300,
                response.getStatus(),
                response.getBody()
        );
    }

}
