package com.duda.quantasvezeselapartiu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class QueryParameterNotFound extends ResponseStatusException {
    public QueryParameterNotFound(String param) {
        super(HttpStatus.BAD_REQUEST, String.format("Query Parameter '%s' is required", param));
    }
}
