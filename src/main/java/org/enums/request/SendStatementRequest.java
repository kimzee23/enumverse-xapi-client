package org.enums.request;

import lombok.Data;
import org.enums.xapi.model.XapiStatement;

@Data
public class SendStatementRequest {
    private XapiStatement statement;
}
