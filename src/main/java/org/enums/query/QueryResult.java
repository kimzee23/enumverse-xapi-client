package org.enums.query;

import lombok.Getter;
import org.enums.xapi.model.XapiStatement;

import java.util.List;

@Getter
public class QueryResult {

    private final List<XapiStatement> statements;
    private final String more;
    private final int status;

    public QueryResult(List<XapiStatement> statements, String more, int status) {
        this.statements = statements;
        this.more = more;
        this.status = status;
    }

    public boolean isSuccess() {
        return status >= 200 && status < 300;
    }
}
