package com.github.monicangl.reststub.services.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String name) {
        super(name);
    }
}
