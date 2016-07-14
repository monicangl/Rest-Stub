package com.github.monicangl.reststub.service;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.services.ServletService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletServiceTest {
    @InjectMocks
    private  ServletService servletService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_be_able_to_return_request_when_get_request_from_http_servlet_request_and_request_body() {
        // given
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
        Map<String, String[]> parameterMap= new HashMap<>();
        String[] values = {"user1"};
        parameterMap.put("name", values);
        when(httpServletRequest.getParameterMap()).thenReturn(parameterMap);
        when(httpServletRequest.getParameter("name")).thenReturn("user1");
        String requestBody = "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}";

        // when
        Request request = servletService.getRequest(httpServletRequest, requestBody);

        // then
        assertThat(request.method, is(HttpMethod.POST));
        assertThat(request.contextPath, is("/stubs/user"));
        assertThat(request.parameters, is(newHashSet(new RequestParameter("name", "user1"))));
        assertThat(request.headers, is(newHashSet(new RequestHeader("content-type", "application/json"))));
        assertThat(request.requestBody, is("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"));
    }
}
