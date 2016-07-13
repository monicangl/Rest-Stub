package com.github.monicangl.reststub.models;

import org.springframework.http.HttpStatus;

public class Response {
    public HttpStatus responseStatus;
    public String responseBody;

    public Response(String responseBody, HttpStatus responseStatus) {
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
    }
}

