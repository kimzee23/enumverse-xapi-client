package org.enums.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.enums.xapi.model.XapiStatement;

public class StatementSerializer {

    public static String toJson(XapiStatement statement) {
        try {
            return JsonMapper.INSTANCE.writeValueAsString(statement);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize XapiStatement", e);
        }
    }
}
