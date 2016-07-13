package com.github.monicangl.reststub.service;

import com.github.monicangl.reststub.models.Schema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.repositories.SchemaRepository;
import com.github.monicangl.reststub.services.RestStubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestStubServiceTest {
    @Mock
    private SchemaRepository apiSchemaRepository;
    @InjectMocks
    private RestStubService restStubService;

    @Test
    public void should_be_able_to_return_right_response_when_handle_a_supported_request() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/stubs/user");
        when(httpServletRequest.getHeaderNames()).thenReturn(new Enumeration<String>() {
            private boolean hasNextElement = true;

            public boolean hasMoreElements() {
                return hasNextElement;
            }

            public String nextElement() {
                hasNextElement = false;
                return "content-type";
            }
        });
        when(httpServletRequest.getHeader("content-type")).thenReturn("application/json");
        when(apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}")).thenReturn(newHashSet(schema));

        // when
        ResponseEntity<String> responseEntity = restStubService.handleRequest(httpServletRequest, "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");

        // then
        assertThat(responseEntity.<String>getBody(), is(schema.getResponseBody()));
        assertThat(responseEntity.getStatusCode(), is(schema.getResponseStatus()));
    }

    @Test
    public void should_be_able_to_return_not_found_when_handle_an_unsupported_request() {
        // given
        Schema schema = new Schema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        schema.getHeaders().add(new RequestHeader("content-type", "application/json"));
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/stubs/user");
        when(httpServletRequest.getHeaderNames()).thenReturn(new Enumeration<String>() {
            private boolean hasNextElement = true;

            public boolean hasMoreElements() {
                return hasNextElement;
            }

            public String nextElement() {
                hasNextElement = false;
                return "content-type";
            }
        });
        when(httpServletRequest.getHeader("content-type")).thenReturn("application/json");
        when(apiSchemaRepository.findByMethodAndContextPathIgnoringCaseAndRequestBody("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}")).thenReturn(newHashSet());

        // when
        ResponseEntity<String> responseEntity = restStubService.handleRequest(httpServletRequest, "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");

        // then
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
