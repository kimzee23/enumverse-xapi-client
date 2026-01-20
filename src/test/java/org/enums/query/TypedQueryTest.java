package org.enums.query;

import org.enums.xapi.model.Agent;
import org.junit.jupiter.api.Test;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TypedQueryTest {

    @Test
    void actor_shouldSerializeAgent() {
        Agent agent = new Agent();
        agent.setMbox("mailto:test@gmail.com");

        QueryParams params =
                TypedQuery.create()
                        .actor(agent)
                        .limit(1)
                        .toParams();

        String queryString = params.toQueryString();

        assertTrue(queryString.contains("agent="));
        assertTrue(queryString.contains("mailto%3Atest%40gmail.com"));
    }
    @Test
    void agentJson_shouldAddAgentParam() {
        String agentJson = """
        {"mbox":"mailto:test@gmail.com","objectType":"Agent"}
    """.trim();

        QueryParams params = TypedQuery.create()
                .agentJson(agentJson)
                .toParams();

        Map<String, Object> map = params.asMap();

        assertTrue(map.containsKey("agent"));

        assertEquals(
                URLEncoder.encode(agentJson, StandardCharsets.UTF_8),
                map.get("agent")
        );
    }
    @Test
    void activity_shouldAddEncodedActivityParam() {
        String activityIri = "https://example.com/activity/lesson-1";

        QueryParams params = TypedQuery.create()
                .activity(activityIri)
                .toParams();

        Map<String, Object> map = params.asMap();

        assertTrue(map.containsKey("activity"));
        assertEquals(
                URLEncoder.encode(activityIri, StandardCharsets.UTF_8),
                map.get("activity")
        );
    }

    @Test
    void until_shouldAddEncodedInstantParam() {
        Instant until = Instant.parse("2024-01-01T00:00:00Z");

        QueryParams params = TypedQuery.create()
                .until(until)
                .toParams();

        Map<String, Object> map = params.asMap();

        assertTrue(map.containsKey("until"));
        assertEquals(
                URLEncoder.encode(until.toString(), StandardCharsets.UTF_8),
                map.get("until")
        );
    }




}