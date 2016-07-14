package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import com.github.monicangl.reststub.services.exception.BadRequestException;
import com.github.monicangl.reststub.services.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SchemaService {
    private final SchemaRepository schemaRepository;

    @Autowired
    public SchemaService(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    public List<Schema> getAll() {
        return schemaRepository.findAll();
    }

    public Schema get(Long id) {
        Optional<Schema> schema = Optional.ofNullable(schemaRepository.findOne(id));
        if (schema.isPresent()){
            return schema.get();
        }
        throw new NotFoundException("Schema with id " + id + "is not found");
    }

    public Long create(Schema schema) {
        if (existing(schema)) {
            throw new BadRequestException("Can not create a schema having the same request with an existent schema");
        }
        schema.requestBody = schema.requestBody.replace(" ", "");
        schema.requestBody = schema.requestBody.replace("/n", "");
        return schemaRepository.save(schema).getId();
    }

    public void update(Schema schema) {
        if (!schemaRepository.exists(schema.getId())) {
            throw new BadRequestException("Schema with id" + schema.getId() + "is not existent");
        }
        if (existingSame(schema)) {
            throw new BadRequestException("Can not update a existent schema to have the same request with an existent schema");
        }
        schemaRepository.save(schema);
    }

    private boolean existingSame(Schema schema) {
        Collection<Schema> schemas = schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody);
        for (Schema anSchema : schemas) {
            if (!anSchema.getId().equals(schema.getId())
                    && anSchema.getParameters().equals(schema.getParameters())
                    && anSchema.getHeaders().equals(schema.getHeaders())) {
                return true;
            }
        }
        return false;
    }

    public void delete(Long id) {
        try {
            schemaRepository.delete(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new BadRequestException("Schema with id " + id + "is not existent");
        }
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
