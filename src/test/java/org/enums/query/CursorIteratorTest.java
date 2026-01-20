package org.enums.query;

import org.enums.xapi.model.XapiStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursorIteratorTest {

    @Test
    void shouldIterateThroughAllPages() throws Exception {

        // mock client
        XapiQueryClient client = mock(XapiQueryClient.class);

        QueryParams params = new QueryParams();

        // page 1
        QueryResult first = new QueryResult(
                List.of(new XapiStatement()),
                "/statements?cursor=abc",
                200
        );

        // page 2 (last)
        QueryResult second = new QueryResult(
                List.of(new XapiStatement()),
                null,
                200
        );

        when(client.queryStatements(params)).thenReturn(first);
        when(client.more("/statements?cursor=abc")).thenReturn(second);

        CursorIterator it = new CursorIterator(client, params);

        assertTrue(it.hasNext());

        List<?> page1 = it.next();
        assertEquals(1, page1.size());

        assertTrue(it.hasNext());

        List<?> page2 = it.next();
        assertEquals(1, page2.size());

        assertFalse(it.hasNext());
    }
}
