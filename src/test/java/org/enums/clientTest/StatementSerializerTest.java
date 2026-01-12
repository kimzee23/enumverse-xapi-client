package org.enums.clientTest;


import org.enums.client.JsonMapper;
import org.enums.client.StatementSerializer;
import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatementSerializerTest {

    @Test
    void serializesStatementToJson() {
        XapiStatement xapiStatement = new XapiStatement();
        xapiStatement.setId(UUID.randomUUID());

        String json = StatementSerializer.toJson(xapiStatement);
        assertNotNull(json);
        assertTrue(json.contains(xapiStatement.getId().toString()));
        assertDoesNotThrow(() -> JsonMapper.INSTANCE.readTree(json));
    }

}