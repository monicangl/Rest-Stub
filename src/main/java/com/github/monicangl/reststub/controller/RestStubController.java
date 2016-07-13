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
import java.util.Optional;

@RestController
@RequestMapping("/stubs")
public class RestStubController {
    private final RestStubService restStubService;
    private final ServletService servletService;

    @Autowired
    public RestStubController(RestStubService restStubService, ServletService servletService) {
        this.restStubService = restStubService;
        this.servletService = servletService;
    }

    @RequestMapping(value = "/**", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest, @RequestBody String body) {
        Response aa = restStubService.handleRequest(servletService.getRequest(httpServletRequest, body));
        Optional<Response> response = Optional.ofNullable(aa);
        if (response.isPresent()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(response.get().responseBody, httpHeaders, response.get().responseStatus);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest) {
        Optional<Response> response = Optional.ofNullable(restStubService.handleRequest(servletService.getRequest(httpServletRequest, "")));
        if (response.isPresent()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(response.get().responseBody, httpHeaders, response.get().responseStatus);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
