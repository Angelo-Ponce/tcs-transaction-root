package com.tcs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ModelNotFoundException extends ResponseStatusException {
    public ModelNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
