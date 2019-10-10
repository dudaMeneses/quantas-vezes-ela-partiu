package com.duda.quantasvezeselapartiu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MusicNotFoundException extends ResponseStatusException {
    public MusicNotFoundException(){
        super(HttpStatus.BAD_REQUEST, "Music not found");
    }
}
