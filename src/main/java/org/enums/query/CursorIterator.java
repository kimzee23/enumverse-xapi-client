package org.enums.query;

import org.enums.xapi.model.XapiStatement;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CursorIterator implements Iterator<List<XapiStatement>> {

    private final XapiQueryClient client;
    private QueryResult current;
    private boolean exhausted = false;

    public CursorIterator(XapiQueryClient client, QueryParams params) throws Exception {
        this.client = client;
        this.current = client.queryStatements(params);
    }

    @Override
    public boolean hasNext() {
        return !exhausted && current != null;
    }

    @Override
    public List<XapiStatement> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        List<XapiStatement> data = current.getStatements();

        try {
            if (current.getMore() != null) {
                current = client.more(current.getMore());
            } else {
                exhausted = true;
            }
        } catch (Exception e) {
            exhausted = true;
        }

        return data;
    }
}
