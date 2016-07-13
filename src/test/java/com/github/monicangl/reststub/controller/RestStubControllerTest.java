package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.models.Response;
import com.github.monicangl.reststub.services.RestStubService;
import com.github.monicangl.reststub.services.ServletService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestStubControllerTest {
    private MockMvc mockMvc;
    @Mock
    private RestStubService restStubService;
    @Mock
    private ServletService servletService;
    @InjectMocks
    private RestStubController restStubController;


    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(restStubController).build();
    }

    @Test
    public void should_be_able_to_return_ok_and_response_resource_when_receive_valid_http_get_request() throws Exception {
        Request request = new Request(RequestMethod.GET, "/stubs/user", newHashSet(new RequestParameter("name", "user1")), newHashSet(), "");
        when(servletService.getRequest(Mockito.any(), Mockito.eq(""))).thenReturn(request);
        when(restStubService.handleRequest(eq(request))).thenReturn(
                new Response("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.OK));
        mockMvc.perform(get("/stubs/user")
                .param("name", "user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name", is("user1")))
                .andExpect(jsonPath("$.password", is("123456")))
                .andExpect(jsonPath("$.age", is(10)));
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_get_request() throws Exception {
        Request request = new Request(RequestMethod.GET, "/stubs/1", newHashSet(new RequestParameter("name", "user1")), newHashSet(), "");
        when(servletService.getRequest(Mockito.any(), eq(""))).thenReturn(request);
        when(restStubService.handleRequest(eq(request))).thenReturn(null);
        mockMvc.perform(get("/stubs/1")
                .param("name", "user1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_post_request() throws Exception {
        Request request = new Request(RequestMethod.POST, "/stubs/user", newHashSet(), newHashSet(), "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");
        when(servletService.getRequest(Mockito.any(), Mockito.eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))).thenReturn(request);
        when(restStubService.handleRequest(eq(request))).thenReturn(new Response("", HttpStatus.OK));
        mockMvc.perform(post("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_post_request() throws Exception {
        Request request = new Request(RequestMethod.POST, "/stubs/1", newHashSet(), newHashSet(), "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}");
        when(servletService.getRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))).thenReturn(request);
        when(restStubService.handleRequest(eq((request)))).thenReturn(null);
        mockMvc.perform(post("/stubs/1")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_put_request() throws Exception {
        Request request = new Request(RequestMethod.PUT, "/stubs/user", newHashSet(), newHashSet(), "{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}");
        when(servletService.getRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(request);
        when(restStubService.handleRequest(eq((request)))).thenReturn(
                new Response("", HttpStatus.OK));
        mockMvc.perform(put("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_put_request() throws Exception {
        Request request = new Request(RequestMethod.PUT, "/stubs/user", newHashSet(), newHashSet(), "{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}");
        when(servletService.getRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(request);
        when(restStubService.handleRequest(eq(request))).thenReturn(null);
        mockMvc.perform(put("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_delete_request() throws Exception {
        Request request = new Request(RequestMethod.DELETE, "/stubs/user/name", newHashSet(), newHashSet(), "");
        when(servletService.getRequest(Mockito.any(), Mockito.eq(""))).thenReturn(request);
        when(restStubService.handleRequest(eq(request))).thenReturn(new Response("", HttpStatus.OK));
        mockMvc.perform(delete("/stubs/user/name"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_delete_request() throws Exception {
        Request request = new Request(RequestMethod.DELETE, "/stubs/user", newHashSet(), newHashSet(), "");
        when(servletService.getRequest(Mockito.any(), eq(""))).thenReturn(request);
        when(restStubService.handleRequest(eq((request)))).thenReturn(null);
        mockMvc.perform(delete("/stubs/user"))
                .andExpect(status().isNotFound());
    }
}
