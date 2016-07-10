package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.models.APISchema;
import com.github.monicangl.reststub.services.APISchemaService;
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
    public List<APISchema> getAll() {
        return apiSchemaService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APISchema get(@PathVariable Long id) {
        return apiSchemaService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        apiSchemaService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestBody APISchema schema) {
        apiSchemaService.add(schema);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<String> update(@RequestBody APISchema schema) {
        apiSchemaService.update(schema);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
