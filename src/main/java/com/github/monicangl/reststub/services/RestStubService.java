package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.*;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestStubService {
    private final SchemaRepository apiSchemaRepository;

    @Autowired
    public RestStubService(SchemaRepository apiSchemaRepository) {
        this.apiSchemaRepository = apiSchemaRepository;
    }

    public Response handleRequest(HttpServletRequest httpServletRequest, String body) {
        Schema schema = getSchema(getRequest(httpServletRequest, body));
        return schema == null ? null : new Response(schema.getResponseBody(), schema.getResponseStatus());
    }

    private Schema getSchema(Request request) {
        Set<Schema> schemas = apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(request.method, request.contextPath, request.requestBody);
        for (Schema schema : schemas) {
            if (schema.getParameters().equals(request.parameters)) {
                if (request.headers.containsAll(schema.getHeaders())) {
                    return schema;
                }
            }
        }
        return null;
    }

    private Request getRequest(HttpServletRequest httpServletRequest, String body) {
        Set<RequestParameter> parameters = new HashSet<>();
        parameters.addAll(httpServletRequest.getParameterMap().keySet().stream().map(key -> new RequestParameter(key, httpServletRequest.getParameter(key))).collect(Collectors.toList()));
        Set<RequestHeader> headers = new HashSet<>();
        Enumeration<String> requestHeaders = httpServletRequest.getHeaderNames();
        while (requestHeaders.hasMoreElements()) {
            String key = requestHeaders.nextElement();
            headers.add(new RequestHeader(key, httpServletRequest.getHeader(key)));
        }
        return new Request(httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), parameters, headers, body);
    }
}
