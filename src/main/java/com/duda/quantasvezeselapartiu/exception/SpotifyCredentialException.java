package com.duda.quantasvezeselapartiu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SpotifyCredentialException extends ResponseStatusException {

    public SpotifyCredentialException(){
        super(HttpStatus.BAD_REQUEST, "Invalid credentials to Spotify");
    }
}
