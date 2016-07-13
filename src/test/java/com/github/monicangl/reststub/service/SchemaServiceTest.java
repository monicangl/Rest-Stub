package com.github.monicangl.reststub.service;

import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import com.github.monicangl.reststub.services.SchemaService;
import com.github.monicangl.reststub.services.exception.BadRequestException;
import com.github.monicangl.reststub.services.exception.NotFoundException;
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

public class SchemaServiceTest {
    @Mock
    private SchemaRepository schemaRepository;
    @InjectMocks
    private SchemaService schemaService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_be_able_to_return_all_schemas_when_get_all_schemas() {
        // given
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        when(schemaRepository.findAll()).thenReturn(newArrayList(postSchema));

        // when
        List<Schema> schemas = schemaService.getAll();

        // then
        assertThat(schemas.size(), is(1));
    }

    @Test
    public void should_be_able_to_return_the_schema_when_get_an_existent_schema() {
        // given
        Schema postSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        when(schemaRepository.findOne(1L)).thenReturn(postSchema);

        // when
        Schema schema = schemaService.get(1L);

        // then
        assertThat(schema.getMethod(), is("POST"));
        assertThat(schema.getContextPath(), is("/stubs/user"));
        assertThat(schema.getParameters(), is(newHashSet()));
        assertThat(schema.getHeaders(), is(newHashSet(new RequestHeader("content-type", "application/json"))));
        assertThat(schema.getRequestBody(), is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"));
        assertThat(schema.getResponseBody(), is(""));
        assertThat(schema.getResponseStatus(), is(HttpStatus.CREATED));
    }

    @Test(expected = NotFoundException.class)
    public void should_be_able_to_raise_not_found_exception_when_get_a_non_existent_schema() {
        // given
        when(schemaRepository.findOne(1L)).thenReturn(null);

        // when
        schemaService.get(1L);
    }

    @Test
    public void should_be_able_to_create_a_non_existent_schema() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(1L);
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody)).thenReturn(newHashSet());
        when(schemaRepository.save(schema)).thenReturn(schema);

        // when
        Long id = schemaService.create(schema);

        //then
        assertThat(id, is(1L));
    }

    @Test(expected = BadRequestException.class)
    public void should_be_able_to_raise_bad_request_exception_when_create_an_existent_schema() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(schema.method, schema.contextPath, schema.requestBody)).thenReturn(newHashSet(schema));

        // when
        schemaService.create(schema);
    }

    @Test
    public void should_be_able_to_update_an_existent_schema() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(1L);
        when(schemaRepository.exists(1L)).thenReturn(true);

        // when
        schemaService.update(schema);
    }

    @Test(expected = BadRequestException.class)
    public void should_be_able_to_raise_bad_request_exception_when_update_a_non_existent_schema() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(1L);
        when(schemaRepository.exists(1L)).thenReturn(false);

        // when
        schemaService.update(schema);
    }

    @Test
    public void should_be_able_to_update_an_existent_schema_same_to_itself() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(1L);
        when(schemaRepository.exists(1L)).thenReturn(true);
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(
                schema.getMethod(), schema.getContextPath(), schema.getRequestBody()))
                .thenReturn(newHashSet(schema));

        // when
        schemaService.update(schema);
    }

    @Test(expected = BadRequestException.class)
    public void should_be_able_to_raise_bad_request_exception_when_update_an_existent_schema_same_to_another_existent_schema() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(1L);
        Schema anotherSchema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        schema.setId(2L);
        when(schemaRepository.exists(1L)).thenReturn(true);
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(
                schema.getMethod(), schema.getContextPath(), schema.getRequestBody()))
                .thenReturn(newHashSet(schema, anotherSchema));

        // when
        schemaService.update(schema);
    }

    @Test
    public void should_be_able_to_delete_an_existent_schema() {
        // given
        doNothing().when(schemaRepository).delete(1L);
        // when
        schemaService.delete(1L);
    }

    @Test(expected = BadRequestException.class)
    public void should_be_able_to_raise_bad_request_exception_when_delete_a_non_existent_schema() {
        // given
        doThrow(new EmptyResultDataAccessException(1)).when(schemaRepository).delete(1L);

        // when
        schemaService.delete(1L);
    }
}
