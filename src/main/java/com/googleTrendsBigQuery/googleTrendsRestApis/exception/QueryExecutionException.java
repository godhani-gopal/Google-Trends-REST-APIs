package com.googleTrendsBigQuery.googleTrendsRestApis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class QueryExecutionException extends RuntimeException {
    private final String query;
    private final String errorCause;

    public QueryExecutionException(String query, String errorCause) {
        super(String.format("Error executing query: %s. Cause: %s.", query, errorCause));
        this.query = query;
        this.errorCause = errorCause;
    }

    public String getQuery() {
        return query;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
