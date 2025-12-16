package org.enums.client;

public class XapiResponse {

    private final boolean success;
    private final int status;
    private final String body;

    public XapiResponse(boolean success, int status, String body) {
        this.success = success;
        this.status = status;
        this.body = body;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
