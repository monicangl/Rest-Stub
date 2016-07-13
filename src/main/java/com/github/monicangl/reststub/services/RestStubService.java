package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.Response;
import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RestStubService {
    private final SchemaRepository apiSchemaRepository;

    @Autowired
    public RestStubService(SchemaRepository apiSchemaRepository) {
        this.apiSchemaRepository = apiSchemaRepository;
    }

    public Response handleRequest(Request request) {
        Schema schema = getSchema(request);
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
}
