package com.github.monicangl.reststub.services;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String name) {
        super(name);
    }
}
