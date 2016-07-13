package com.github.monicangl.reststub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.services.APISchemaService;
import com.github.monicangl.reststub.services.InvalidRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class SchemaControllerTest {
    private MockMvc mockMvc;
    @Mock
    private APISchemaService apiSchemaService;
    @InjectMocks
    private SchemaController schemaController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(schemaController).build();
    }

    @Test
    public void should_be_able_to_get_all_schemas() throws Exception {
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        Schema getSchema = new Schema("GET", "/stubs/user", "", HttpStatus.OK, "{\n    \"name\": \"user1\",\n    \"password\": \"123456\",   \n    \"age\": 10\n}");
        getSchema.getParameters().add(new RequestParameter(getSchema, "name", "user1"));
        when(apiSchemaService.getAll()).thenReturn(Arrays.asList(postSchema, getSchema));
        mockMvc.perform(get("/schema"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(postSchemaId.intValue())))
                .andExpect(jsonPath("$[0].method", is("POST")))
                .andExpect(jsonPath("$[0].contextPath", is("/stubs/user")))
                .andExpect(jsonPath("$[0].requestBody", is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}")))
                .andExpect(jsonPath("$[0].responseStatus", is("CREATED")))
                .andExpect(jsonPath("$[0].responseBody", is("")))
                .andExpect(jsonPath("$[0].parameters", hasSize(0)))
                .andExpect(jsonPath("$[0].headers", hasSize(1)))
                .andExpect(jsonPath("$[0].headers[0].name", is("content-type")))
                .andExpect(jsonPath("$[0].headers[0].value", is("application/json")))
//                .andExpect(jsonPath("$[1].id", is(getSchemaId.intValue())))
                .andExpect(jsonPath("$[1].method", is("GET")))
                .andExpect(jsonPath("$[1].contextPath", is("/stubs/user")))
                .andExpect(jsonPath("$[1].requestBody", is("")))
                .andExpect(jsonPath("$[1].responseStatus", is("OK")))
                .andExpect(jsonPath("$[1].responseBody", is("{\n    \"name\": \"user1\",\n    \"password\": \"123456\",   \n    \"age\": 10\n}")))
                .andExpect(jsonPath("$[1].parameters", hasSize(1)))
                .andExpect(jsonPath("$[1].parameters[0].name", is("name")))
                .andExpect(jsonPath("$[1].parameters[0].value", is("user1")))
                .andExpect(jsonPath("$[1].headers", hasSize(0)));
    }

    @Test
    public void should_be_able_to_get_the_existent_schema() throws Exception {
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        when(apiSchemaService.get(1L)).thenReturn(postSchema);
        mockMvc.perform(get("/schema/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.method", is("POST")))
                .andExpect(jsonPath("$.contextPath", is("/stubs/user")))
                .andExpect(jsonPath("$.requestBody", is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}")))
                .andExpect(jsonPath("$.responseStatus", is("CREATED")))
                .andExpect(jsonPath("$.responseBody", is("")))
                .andExpect(jsonPath("$.parameters", hasSize(0)))
                .andExpect(jsonPath("$.headers", hasSize(1)))
                .andExpect(jsonPath("$.headers[0].name", is("content-type")))
                .andExpect(jsonPath("$.headers[0].value", is("application/json")));
    }

    @Test
    public void should_be_able_to_return_not_found_when_get_a_non_existent_schema() throws Exception {
        when(apiSchemaService.get(3L)).thenThrow(new InvalidRequestException(""));
        mockMvc.perform(get("/schema/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_return_created_when_add_a_non_existent_schema() throws Exception {
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        mockMvc.perform(post("/schema")
                .header("content-type", "application/json")
                .content(json(postSchema)))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_be_able_return_bad_request_when_add_an_existent_schema() throws Exception {
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        doThrow(new InvalidRequestException("")).when(apiSchemaService).create(Mockito.any());
        mockMvc.perform(post("/schema")
                .header("content-type", "application/json")
                .content(json(postSchema)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_be_able_return_ok_when_update_an_existent_schema() throws Exception {
        Schema getSchema = new Schema("GET", "/stubs/user", "", HttpStatus.OK, "{\n    \"name\": \"user1\",\n    \"password\": \"123456\",   \n    \"age\": 10\n}");
        getSchema.getParameters().add(new RequestParameter(getSchema, "name", "user1"));
        mockMvc.perform(put("/schema")
                .header("content-type", "application/json")
                .content(json(getSchema)))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_return_bad_request_when_update_an_non_existent_schema() throws Exception {
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        doThrow(new InvalidRequestException("")).when(apiSchemaService).update(Mockito.any());
        mockMvc.perform(put("/schema")
                .header("content-type", "application/json")
                .content(json(postSchema)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_be_able_return_ok_when_delete_an_existent_schema() throws Exception {
        mockMvc.perform(delete("/schema/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_return_not_found_when_delete_a_non_existent_schema() throws Exception {
        doThrow(new EmptyResultDataAccessException(3)).when(apiSchemaService).delete(3L);
        mockMvc.perform(delete("/schema/3")
                .header("content-type", "application/json"))
                .andExpect(status().isBadRequest());
    }

    private String json(Object object) throws IOException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
