package com.github.monicangl.reststub.service;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.Response;
import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import com.github.monicangl.reststub.services.RestStubService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class RestStubServiceTest {
    @Mock
    private SchemaRepository schemaRepository;
    @InjectMocks
    private RestStubService restStubService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_be_able_to_return_present_optional_of_response_when_handle_a_supported_request() {
        // given
        Set<RequestHeader> headers = newHashSet(new RequestHeader("content-type", "application/json"));
        Schema schema = new Schema(HttpMethod.POST, "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.setHeaders(headers);
        Request request = new Request(HttpMethod.POST, "/stubs/user", newHashSet(), headers, "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(
                request.method, request.contextPath, request.requestBody))
                .thenReturn(newHashSet(schema));

        // when
        Optional<Response> response = restStubService.handleRequest(request);

        // then
        assertThat(response.isPresent(), is(true));
        assertThat(response.get().responseBody, is(schema.getResponseBody()));
        assertThat(response.get().responseStatus, is(schema.getResponseStatus()));
    }

    @Test
    public void should_be_able_to_return_empty_optional_of_response_when_handle_an_unsupported_request() {
        // given
        Request request = new Request(HttpMethod.POST, "/stubs/user", newHashSet(), newHashSet(), "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");
        when(schemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(
                request.method, request.contextPath, request.requestBody)).thenReturn(newHashSet());
        // when
        Optional<Response> response = restStubService.handleRequest(request);

        // then
        assertThat(response.isPresent(), is(false));
    }
}
