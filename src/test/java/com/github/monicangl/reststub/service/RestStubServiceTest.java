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
import org.springframework.http.HttpStatus;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class RestStubServiceTest {
    @Mock
    private SchemaRepository apiSchemaRepository;
    @InjectMocks
    private RestStubService restStubService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_be_able_to_return_right_response_when_handle_a_supported_request() {
        // given
        Set<RequestHeader> headers = newHashSet(new RequestHeader("content-type", "application/json"));
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.setHeaders(headers);
        Request request = new Request("POST", "/stubs/user", newHashSet(), headers, "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");
        when(apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody(
                "POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))
                .thenReturn(newHashSet(schema));

        // when
        Response response = restStubService.handleRequest(request);

        // then
        assertThat(response.responseBody, is(schema.getResponseBody()));
        assertThat(response.responseStatus, is(schema.getResponseStatus()));
    }

    @Test
    public void should_be_able_to_return_not_found_when_handle_an_unsupported_request() {
        // given
        Set<RequestHeader> headers = newHashSet(new RequestHeader("content-type", "application/json"));
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.setHeaders(headers);
        Request request = new Request("POST", "/stubs/user", newHashSet(), headers, "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");

        // when
        Response response = restStubService.handleRequest(request);

        // then
//        assertThat(response, isNull());
    }
}
