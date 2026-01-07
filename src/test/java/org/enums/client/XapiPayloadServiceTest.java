package org.enums.client;

import org.enums.client.XapiPayloadService;
import org.enums.xapi.model.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XapiPayloadServiceTest {

    private final XapiPayloadService service = new XapiPayloadService();

    private XapiStatement validStatement() {
        Actor actor = new Actor("mailto:test@example.com", "Test");
        Verb verb = new Verb("https://example.com/verbs/viewed", "Viewed");
        Activity activity = new Activity("https://example.com/activity/1", "Lesson");
        return new XapiStatement(null, actor, verb, activity, Instant.now());
    }

    @Test
    void serializeSingleStatement() throws Exception {
        String json = service.serialize(validStatement());
        assertNotNull(json);
        assertTrue(json.contains("actor"));
    }

    @Test
    void serializeBatch() throws Exception {
        String json = service.serialize(List.of(validStatement()));
        assertNotNull(json);
        assertTrue(json.startsWith("["));
    }

    @Test
    void serializeNull_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.serialize(null));
    }

    @Test
    void serializeEmptyBatch_shouldFail() {
        assertThrows(IllegalArgumentException.class,
                () -> service.serialize(List.of()));
    }
}