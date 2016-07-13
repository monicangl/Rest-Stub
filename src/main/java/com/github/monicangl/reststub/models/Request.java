package com.github.monicangl.reststub.models;

import java.util.Set;

public class Request {
    public String method;
    public String contextPath;
    public Set<RequestParameter> parameters;
    public Set<RequestHeader> headers;
    public String requestBody;

    public Request(String method, String contextPath, Set<RequestParameter> parameters, Set<RequestHeader> headers, String requestBody) {
        this.method = method;
        this.contextPath = contextPath;
        this.parameters = parameters;
        this.headers = headers;
        this.requestBody = requestBody;
        this.requestBody = this.requestBody.replace(" ", "");
        this.requestBody = this.requestBody.replace("\n", "");
    }

    public boolean equals(Object object) {
        Request other = (Request)object;
        return this.method.equals(other.method)
                && this.contextPath.equals(other.contextPath)
                && this.parameters.equals(other.parameters)
                && this.headers.equals(other.headers)
                && this.requestBody.equals(other.requestBody);
    }


}
