package com.github.monicangl.reststub.service;

import com.github.monicangl.reststub.models.APISchema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.repositories.APISchemaRepository;
import com.github.monicangl.reststub.repositories.RequestHeaderRepository;
import com.github.monicangl.reststub.repositories.RequestParameterRepository;
import com.github.monicangl.reststub.services.APISchemaService;
import com.github.monicangl.reststub.services.InvalidRequestException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SchemaServceTest {
    @Mock
    private APISchemaRepository apiSchemaRepository;
    @Mock
    private RequestParameterRepository requestParameterRepository;
    @Mock
    private RequestHeaderRepository requestHeaderRepository;
    @InjectMocks
    private APISchemaService apiSchemaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_be_able_to_return_all_schemas_when_get_all_schemas() {
        // given
        APISchema postSchema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        APISchema getSchema = new APISchema("GET", "/stubs/user", "", HttpStatus.OK, "{\"name\": \"user1\",\"password\": \"123456\",\"age\": 10}");
        getSchema.getParameters().add(new RequestParameter(getSchema, "name", "user1"));
        when(apiSchemaRepository.findAll()).thenReturn(newArrayList(postSchema, getSchema));

        // when
        List<APISchema> schemas = apiSchemaService.getAll();

        // then
        assertThat(schemas.size(), is(2));
        assertThat(schemas.get(0).getMethod(), is("POST"));
        assertThat(schemas.get(0).getContextPath(), is("/stubs/user"));
        assertThat(schemas.get(0).getParameters(), is(newHashSet()));
        assertThat(schemas.get(0).getHeaders(), is(newHashSet(new RequestHeader(null, "content-type", "application/json"))));
        assertThat(schemas.get(0).getRequestBody(), is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"));
        assertThat(schemas.get(0).getResponseBody(), is(""));
        assertThat(schemas.get(0).getResponseStatus(), is(HttpStatus.CREATED));
        assertThat(schemas.get(1).getMethod(), is("GET"));
        assertThat(schemas.get(1).getContextPath(), is("/stubs/user"));
        assertThat(schemas.get(1).getParameters(), is(newHashSet(new RequestParameter(null, "name", "user1"))));
        assertThat(schemas.get(1).getHeaders(), is(newHashSet()));
        assertThat(schemas.get(1).getRequestBody(), is(""));
        assertThat(schemas.get(1).getResponseBody(), is("{\"name\": \"user1\",\"password\": \"123456\",\"age\": 10}"));
        assertThat(schemas.get(1).getResponseStatus(), is(HttpStatus.OK));
    }

    @Test
    public void should_be_able_to_return_the_schema_when_get_an_existent_schema() {
        // given
        APISchema postSchema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "content-type", "application/json"));
        when(apiSchemaRepository.exists(1L)).thenReturn(true);
        when(apiSchemaRepository.findOne(1L)).thenReturn(postSchema);

        // when
        APISchema schema = apiSchemaService.get(1L);

        // then
        assertThat(schema.getMethod(), is("POST"));
        assertThat(schema.getContextPath(), is("/stubs/user"));
        assertThat(schema.getParameters(), is(newHashSet()));
        assertThat(schema.getHeaders(), is(newHashSet(new RequestHeader(null, "content-type", "application/json"))));
        assertThat(schema.getRequestBody(), is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"));
        assertThat(schema.getResponseBody(), is(""));
        assertThat(schema.getResponseStatus(), is(HttpStatus.CREATED));
    }

    @Test(expected = InvalidRequestException.class)
    public void should_be_able_to_raise_exception_when_get_a_non_existent_schema() {
        // given
        when(apiSchemaRepository.exists(1L)).thenReturn(false);

        // when
        apiSchemaService.get(1L);
    }

    @Test
    public void should_be_able_to_add_a_non_existent_schema() {
        // given
        APISchema schema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader(schema, "content-type", "application/json"));
        when(apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody)).thenReturn(newHashSet());

        // when
        apiSchemaService.add(schema);
    }

    @Test(expected = InvalidRequestException.class)
    public void should_be_able_to_raise_exception_when_add_an_existent_schema() {
        // given
        APISchema schema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader(schema, "content-type", "application/json"));
        when(apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody)).thenReturn(newHashSet(schema));

        // when
        apiSchemaService.add(schema);
    }

    @Test
    public void should_be_able_to_update_an_existent_schema() {
        // given
        APISchema schema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader(schema, "content-type", "application/json"));
        schema.setId(1L);
        when(apiSchemaRepository.exists(1L)).thenReturn(true);
        when(apiSchemaRepository.findOne(1L)).thenReturn(schema);

        // when
        apiSchemaService.update(schema);
    }

    @Test(expected = InvalidRequestException.class)
    public void should_be_able_to_raise_exception_when_update_a_non_existent_schema() {
        // given
        APISchema schema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader(schema, "content-type", "application/json"));
        schema.setId(1L);
        when(apiSchemaRepository.exists(1L)).thenReturn(false);

        // when
        apiSchemaService.update(schema);
    }

    @Test
    public void should_be_able_to_delete_an_existent_schema() {
        // given
        doNothing().when(apiSchemaRepository).delete(1L);
        // when
        apiSchemaService.delete(1L);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void should_be_able_to_raise_exception_when_delete_a_non_existent_schema() {
        // given
        doThrow(new EmptyResultDataAccessException(1)).when(apiSchemaRepository).delete(1L);

        // when
        apiSchemaService.delete(1L);
    }
}
