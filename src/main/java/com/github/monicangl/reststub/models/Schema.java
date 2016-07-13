package com.github.monicangl.reststub.models;

import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Schema {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<RequestParameter> parameters = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<RequestHeader> headers = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    public void setHeaders(Set<RequestHeader> headers) {
        this.headers = headers;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Schema(String method, String contextPath, String requestBody, HttpStatus responseStatus, String responseBody) {
        this.method = method;
        this.contextPath = contextPath;
        this.requestBody = requestBody;
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
        this.requestBody = this.requestBody.replace(" ", "");
        this.requestBody = this.requestBody.replace("\n", "");
    }

    Schema() { // jpa only
    }

    public boolean equals(Object obj) {
        Schema other = (Schema) obj;
        return this.method.equals(other.method)
                && this.contextPath.compareToIgnoreCase(other.contextPath) == 0
                && this.requestBody.equals(other.requestBody)
                && this.headers.equals(other.headers)
                && this.parameters.equals(other.parameters);
    }

    public int hashCode() {
        return this.method.hashCode() * this.contextPath.hashCode() * this.requestBody.hashCode() * this.headers.hashCode() * this.parameters.hashCode();
    }
}
