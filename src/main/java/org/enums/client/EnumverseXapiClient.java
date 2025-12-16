package org.enums.client;


import org.enums.query.XapiQueryClient;

public class EnumverseXapiClient {

    private final XapiClientConfig config;

    private final XapiStatementClient statementClient;
    private final XapiQueryClient queryClient;

    public EnumverseXapiClient(XapiClientConfig config) {
        this.config = config;

        // Shared HTTP client
        XapiHttpClient http = new XapiHttpClient(config);

        // Create internal clients
        this.statementClient = new XapiStatementClient(config);
        this.queryClient = new XapiQueryClient(config, http);
    }


    public XapiStatementClient getStatementClient() {
        return statementClient;
    }

    public XapiQueryClient getQueryClient() {
        return queryClient;
    }

    public XapiClientConfig getConfig() {
        return config;
    }
}