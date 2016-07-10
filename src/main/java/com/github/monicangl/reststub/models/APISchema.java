package com.github.monicangl.reststub.models;

import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class APISchema {
    @OneToMany(mappedBy = "schema", cascade = CascadeType.REMOVE)
    private Set<RequestParameter> parameters = new HashSet<>();

    @OneToMany(mappedBy = "schema", cascade = CascadeType.REMOVE)
    private Set<RequestHeader> headers = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    public Set<RequestParameter> getParameters() {
        return parameters;
    }

    public Set<RequestHeader> getHeaders() {
        return headers;
    }

    public Long getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String method;
    public String contextPath;
    public String requestBody;
    public HttpStatus responseStatus;
    public String responseBody;

    public APISchema(String method, String contextPath, String requestBody, HttpStatus responseStatus, String responseBody) {
        this.method = method;
        this.contextPath = contextPath;//.toLowerCase();
        this.requestBody = requestBody;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
        this.requestBody = this.requestBody.replace(" ","");
        this.requestBody = this.requestBody.replace("\n","");
    }

    APISchema() { // jpa only
    }
}
