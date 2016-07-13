package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class APISchemaService {
    private final SchemaRepository schemaRepository;

    @Autowired
    public APISchemaService(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public List<Schema> getAll() {
        return schemaRepository.findAll();
    }

    public Schema get(Long id) {
        if (!schemaRepository.exists(id)) {
            throw new InvalidRequestException("Invalid request: get a non existed schema");
        }
        return schemaRepository.findOne(id);
    }

    public void create(Schema schema) {
        if (existing(schema)) {
            throw new InvalidRequestException("Invalid request: create a existed schema");
        }
        schema.requestBody = schema.requestBody.replace(" ", "");
        schema.requestBody = schema.requestBody.replace("/n", "");
        schemaRepository.save(schema);
    }

    public void update(Schema schema) {
        if (!schemaRepository.exists(schema.getId())) {
            throw new InvalidRequestException("Invalid request: update a non existed schema");
        }

        schemaRepository.save(schema);
    }

    public void delete(Long id) {
        schemaRepository.delete(id);
    }

    private boolean existing(Schema schema) {
        Collection<Schema> schemas = schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody);
        for (Schema apiSchema : schemas) {
            if (apiSchema.getParameters().equals(schema.getParameters())
                    && apiSchema.getHeaders().equals(schema.getHeaders())) {
                return true;
            }
        }
        return false;
    }
}
