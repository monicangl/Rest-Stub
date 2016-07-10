package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.APISchema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.repositories.APISchemaRepository;
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
    private final APISchemaRepository apiSchemaRepository;
    private final RequestParameterRepository requestParameterRepository;
    private final RequestHeaderRepository requestHeaderRepository;

    @Autowired
    public APISchemaService(APISchemaRepository apiSchemaRepository
            , RequestParameterRepository requestParameterRepository
            , RequestHeaderRepository requestHeaderRepository) {
        this.apiSchemaRepository = apiSchemaRepository;
        this.requestParameterRepository = requestParameterRepository;
        this.requestHeaderRepository = requestHeaderRepository;
    }


    public List<APISchema> getAll() {
        return apiSchemaRepository.findAll();
    }

    public APISchema get(Long id) {
        return apiSchemaRepository.findOne(id);
    }

    public void add(APISchema schema) {
        schema.requestBody = schema.requestBody.replace(" ", "");
        schema.requestBody = schema.requestBody.replace("/n", "");
        //schema.contextPath = schema.contextPath.toLowerCase();
        if (!existing(schema)) {
            addSchema(schema);
        }
    }

    public void update(APISchema schema) {
        delete(schema.getId());
        addSchema(schema);
    }

    private boolean existing(APISchema schema) {
        Collection<APISchema> schemas = apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody);
        for (APISchema apiSchema : schemas) {
            if (apiSchema.getParameters().equals(schema.getParameters())
                    && apiSchema.getHeaders().equals(schema.getHeaders())) {
                return true;
            }
        }
        return false;
    }

    private void addSchema(APISchema schema) {
        APISchema apiSchema = apiSchemaRepository.save(new APISchema(schema.method, schema.contextPath, schema.requestBody, schema.responseStatus, schema.responseBody));
        for (RequestParameter parameter : schema.getParameters()) {
            requestParameterRepository.save(new RequestParameter(apiSchema, parameter.name, parameter.value));
        }
        for (RequestHeader header : schema.getHeaders()) {
            requestHeaderRepository.save(new RequestHeader(apiSchema, header.name, header.value));
        }
    }

    public void delete(Long id) {
        apiSchemaRepository.delete(id);
    }
}
