package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.services.SchemaService;
import com.github.monicangl.reststub.services.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/schema")
public class SchemaController {
    private final SchemaService schemaService;

    @Autowired
    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Schema> getAll() {
        return schemaService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<Schema> schema = schemaService.get(id);
        if (schema.isPresent()) {
            return new ResponseEntity<>(schema.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Schema with id " + id + " is not found", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            schemaService.delete(id);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody Schema schema) {
        try {
            return new ResponseEntity<>("Id of created schema is " + schemaService.create(schema).toString(), HttpStatus.CREATED);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> update(@RequestBody Schema schema) {
        try {
            schemaService.update(schema);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
