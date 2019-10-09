package com.duda.quantasvezeselapartiu.exception;

import com.duda.quantasvezeselapartiu.model.request.ElaPartiuRequestBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(ElaPartiuRequestBuilder request) {
        super(HttpStatus.NOT_FOUND, String.format("No route found from %s to %s", request.getFrom(), request.getTo()));
    }
}
