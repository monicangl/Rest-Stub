package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.services.APISchemaService;
import com.github.monicangl.reststub.services.exception.BadRequestException;
import com.github.monicangl.reststub.services.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schema")
public class SchemaController {
    private final APISchemaService apiSchemaService;

    @Autowired
    public SchemaController(APISchemaService apiSchemaService) {
        this.apiSchemaService = apiSchemaService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Schema> getAll() {
        return apiSchemaService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Schema> get(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(apiSchemaService.get(id), HttpStatus.OK);
        }
        catch (NotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            apiSchemaService.delete(id);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestBody Schema schema) {
        try {
            apiSchemaService.create(schema);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> update(@RequestBody Schema schema) {
        try {
            apiSchemaService.update(schema);
        }
        catch (BadRequestException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
