package org.enums.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enums.xapi.model.Agent;

import java.time.Instant;

public class TypedQuery {

    private final QueryParams params = new QueryParams();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static TypedQuery create() {
        return new TypedQuery();
    }

    public TypedQuery agentJson(String agentJson) {
        params.add("agent", agentJson);
        return this;
    }

    public TypedQuery verb(String verbIri) {
        params.add("verb", verbIri);
        return this;
    }

    public TypedQuery activity(String activityIri) {
        params.add("activity", activityIri);
        return this;
    }

    public TypedQuery since(Instant since) {
        params.add("since", since);
        return this;
    }

    public TypedQuery until(Instant until) {
        params.add("until", until);
        return this;
    }

    public TypedQuery limit(int limit) {
        params.add("limit", limit);
        return this;
    }

    /** ---------------- TYPED HELPERS ---------------- */

    /**
     * Typed Agent helper (recommended)
     * Serializes Agent to JSON per xAPI spec
     */
    public TypedQuery actor(Agent agent) {
        try {
            params.add("agent", mapper.writeValueAsString(agent));
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Agent", e);
        }
    }
    public QueryParams toParams() {
        return params;
    }
}
