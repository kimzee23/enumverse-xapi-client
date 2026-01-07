package org.enums.client;

import lombok.Getter;

public class XapiResponse {

    @Getter
    private final boolean success;
    private final int status;
    @Getter
    private final String body;

    public XapiResponse(boolean success, int status, String body) {
        this.success = success;
        this.status = status;
        this.body = body;
    }

    public int getStatusCode() {
        return status;
    }


}
