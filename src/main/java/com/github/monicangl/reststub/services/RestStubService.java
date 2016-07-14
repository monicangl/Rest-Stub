package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.Response;
import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class RestStubService {
    private final SchemaRepository apiSchemaRepository;

    @Autowired
    public RestStubService(SchemaRepository apiSchemaRepository) {
        this.apiSchemaRepository = apiSchemaRepository;
    }

    public Optional<Response> handleRequest(Request request) {
        Optional<Schema> schema = getSchema(request);
        if (schema.isPresent()) {
            return Optional.of(new Response(schema.get().getResponseBody(), schema.get().getResponseStatus()));
        }
        return Optional.empty();
    }

    private Optional<Schema> getSchema(Request request) {
        Set<Schema> schemas = apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(request.method, request.contextPath, request.requestBody);
        for (Schema schema : schemas) {
            if (schema.getParameters().equals(request.parameters)) {
                if (request.headers.containsAll(schema.getHeaders())) {
                    return Optional.of(schema);
                }
            }
        }
        return Optional.empty();
    }
}
