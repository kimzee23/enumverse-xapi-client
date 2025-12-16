package org.enums.query;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Builder
public class QueryParams {

    private final Map<String, String> map = new HashMap<>();

    public QueryParams add(String key, Object value) {
        if (value != null) {
            map.put(key, value.toString());
        }
        return this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public String toQueryString() {
        if (map.isEmpty()) return "";

        StringJoiner joiner = new StringJoiner("&");
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return "?" + joiner.toString();
    }
}
