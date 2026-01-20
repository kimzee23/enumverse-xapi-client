package org.enums.query;

import java.util.Iterator;
import java.util.List;

public class CursorIterator implements Iterator<List<?>> {

    private final XapiQueryClient client;
    private QueryResult current;

    public CursorIterator(XapiQueryClient client, QueryParams params) throws Exception {
        this.client = client;
        this.current = client.queryStatements(params);
    }

    @Override
    public boolean hasNext() {
        return current != null && current.getMore() != null;
    }

    @Override
    public List<?> next() {
        List<?> data = current.getStatements();
        try {
            current = current.getMore() != null
                    ? client.more(current.getMore())
                    : null;
        } catch (Exception e) {
            current = null;
        }
        return data;
    }
}

