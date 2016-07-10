package com.github.monicangl.reststub.models;

import java.util.Set;

public class APIRequest {
    public String method;
    public String contextPath;
    public Set<RequestParameter> parameters;
    public Set<RequestHeader> headers;
    public String requestBody;

    public APIRequest(String method, String contextPath, Set<RequestParameter> parameters, Set<RequestHeader> headers, String requestBody) {
        this.method = method;
        this.contextPath = contextPath;//.toLowerCase();
//        this.contextPath = this.contextPath.toLowerCase();
        this.parameters = parameters;
        this.headers = headers;
        this.requestBody = requestBody;
        this.requestBody = this.requestBody.replace(" ", "");
        this.requestBody = this.requestBody.replace("\n", "");
    }
}
