package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.models.Response;
import com.github.monicangl.reststub.services.RestStubService;
import com.github.monicangl.reststub.services.ServletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/stubs")
public class RestStubController {
    private final RestStubService restStubService;

    @Autowired
    public RestStubController(RestStubService restStubService) {
        this.restStubService = restStubService;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest, @RequestBody String body) {
        Response response = restStubService.handleRequest(ServletService.getRequest(httpServletRequest, body));
        if (null == response) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response.responseBody, httpHeaders, response.responseStatus);
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest) {
        Response response = restStubService.handleRequest(ServletService.getRequest(httpServletRequest, ""));
        if (null == response) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response.responseBody, httpHeaders, response.responseStatus);
    }
}
