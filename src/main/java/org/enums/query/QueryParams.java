package org.enums.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class QueryParams {

    private final Map<String, Object> map = new HashMap<>();

    public QueryParams add(String key, Object value) {
        if (key != null && value != null) {
            map.put(key, encode(value.toString()));
        }
        return this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
    public Map<String, Object> asMap() {
        return map;
    }

    public String toQueryString() {
        if (map.isEmpty()) return "";

        StringJoiner joiner = new StringJoiner("&");
        for (var entry : map.entrySet()) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return "?" + joiner;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
