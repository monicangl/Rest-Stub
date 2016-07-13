package com.github.monicangl.reststub.services.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String name) {
        super(name);
    }
}
