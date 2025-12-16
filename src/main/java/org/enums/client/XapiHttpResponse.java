package org.enums.client;

public class XapiHttpResponse {
    private final int status;
    private final String body;

    public XapiHttpResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}