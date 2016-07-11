package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.services.RestStubService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    @InjectMocks
    private RestStubController restStubController;


    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(restStubController).build();
    }

    @Test
    public void should_be_able_to_return_ok_and_response_resource_when_receive_valid_http_get_request() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        when(restStubService.handleRequest(Mockito.any(), eq(""))).thenReturn(
                new ResponseEntity<>("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", httpHeaders, HttpStatus.OK));
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
        when(restStubService.handleRequest(Mockito.any(), eq(""))).thenReturn(
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/stubs/1")
                .param("name", "user1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_post_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))).thenReturn(
                new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(post("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_post_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(post("/stubs/1")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_put_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(
                new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_put_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(put("/stubs/user")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_be_able_to_return_ok_when_receive_valid_http_delete_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))).thenReturn(
                new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/stubs/user/name")
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":15}"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_be_able_to_return_not_found_when_receive_invalid_http_delete_request() throws Exception {
        when(restStubService.handleRequest(Mockito.any(), eq(""))).thenReturn(
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(delete("/stubs/user"))
                .andExpect(status().isNotFound());
    }



//    @Test
//    public void stubGetUser() throws Exception {
//        APISchema getSchema = new APISchema("GET", "/stubs/user", "", HttpStatus.OK, "{\n    \"name\": \"user1\",\n    \"password\": \"123456\",   \n    \"age\": 10\n}");
//        getSchema.getParameters().add(new RequestParameter(getSchema, "name", "user1"));
//        apiSchemaService.add(getSchema);
//        mockMvc.perform(get("/stubs/user")
//                .param("name", "user1"))
//                .andExpect(status().isOk());
////                .andExpect();
//    }
//
//    protected String json(Object object) throws IOException {
//        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
//        this.mappingJackson2HttpMessageConverter.write(
//                object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
//        return mockHttpOutputMessage.getBodyAsString();
//    }
}
