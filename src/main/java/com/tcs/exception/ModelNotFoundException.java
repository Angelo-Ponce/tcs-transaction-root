package com.tcs.exception;

public class ModelNotFoundException extends RuntimeException {

    // Se obliga en sobreescribir el contructor
    public ModelNotFoundException(String message) {
        super(message);
    }
}
