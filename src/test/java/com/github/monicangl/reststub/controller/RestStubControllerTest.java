package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.Application;
import com.github.monicangl.reststub.models.APISchema;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import com.github.monicangl.reststub.services.APISchemaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class RestStubControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());
//            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    APISchemaService apiSchemaService;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        apiSchemaService.getAll().forEach(schema -> apiSchemaService.delete(schema.getId()));
    }

    @Test
    public void createUser() throws Exception {
        APISchema postSchema = new APISchema("POST", "/stubs/user", "{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}", HttpStatus.CREATED, "");
        postSchema.getHeaders().add(new RequestHeader(postSchema, "Content-Type", "application/json"));
        apiSchemaService.add(postSchema);
        mockMvc.perform(post("/stubs/user")
                .contentType(contentType)
                .content("{\"name\":\"user1\",\"password\":\"123456\",\"age\":10}"))
                .andExpect(status().isCreated());
    }


    @Test
    public void stubGetUser() throws Exception {
        APISchema getSchema = new APISchema("GET", "/stubs/user", "", HttpStatus.OK, "{\n    \"name\": \"user1\",\n    \"password\": \"123456\",   \n    \"age\": 10\n}");
        getSchema.getParameters().add(new RequestParameter(getSchema, "name", "user1"));
        apiSchemaService.add(getSchema);
        mockMvc.perform(get("/stubs/user")
                .param("name", "user1"))
                .andExpect(status().isOk());
//                .andExpect();
    }

    protected String json(Object object) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                object, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
