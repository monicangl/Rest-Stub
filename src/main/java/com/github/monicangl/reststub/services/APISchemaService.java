package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import com.github.monicangl.reststub.repositories.RequestHeaderRepository;
import com.github.monicangl.reststub.repositories.RequestParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class APISchemaService {
    private final SchemaRepository apiSchemaRepository;
    private final RequestParameterRepository requestParameterRepository;
    private final RequestHeaderRepository requestHeaderRepository;

    @Autowired
    public APISchemaService(SchemaRepository apiSchemaRepository
            , RequestParameterRepository requestParameterRepository
            , RequestHeaderRepository requestHeaderRepository) {
        this.apiSchemaRepository = apiSchemaRepository;
        this.requestParameterRepository = requestParameterRepository;
        this.requestHeaderRepository = requestHeaderRepository;
    }

    public List<Schema> getAll() {
        return apiSchemaRepository.findAll();
    }

    public Schema get(Long id) {
        if (!apiSchemaRepository.exists(id)) {
            throw new InvalidRequestException("Invalid request: get a non existed schema");
        }
        return apiSchemaRepository.findOne(id);
    }

    public void create(Schema schema) {
        if (existing(schema)) {
            throw new InvalidRequestException("Invalid request: create a existed schema");
        }
        schema.requestBody = schema.requestBody.replace(" ", "");
        schema.requestBody = schema.requestBody.replace("/n", "");
        addSchema(schema);
    }

    public void update(Schema schema) {
        if (!apiSchemaRepository.exists(schema.getId())) {
            throw new InvalidRequestException("Invalid request: update a non existed schema");
        }
        delete(schema.getId());
        addSchema(schema);
    }

    public void delete(Long id) {
        apiSchemaRepository.delete(id);
    }

    private boolean existing(Schema schema) {
        Collection<Schema> schemas = apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody);
        for (Schema apiSchema : schemas) {
            if (apiSchema.getParameters().equals(schema.getParameters())
                    && apiSchema.getHeaders().equals(schema.getHeaders())) {
                return true;
            }
        }
        return false;
    }

    private void addSchema(Schema schema) {
        Schema apiSchema = apiSchemaRepository.save(new Schema(schema.method, schema.contextPath, schema.requestBody, schema.responseStatus, schema.responseBody));
        for (RequestParameter parameter : schema.getParameters()) {
            requestParameterRepository.save(new RequestParameter(apiSchema, parameter.name, parameter.value));
        }
        for (RequestHeader header : schema.getHeaders()) {
            requestHeaderRepository.save(new RequestHeader(apiSchema, header.name, header.value));
        }
    }
}
