package com.github.monicangl.reststub.controller;

import com.github.monicangl.reststub.services.RestStubService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return restStubService.handleRequest(httpServletRequest, body);
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.DELETE})
    public ResponseEntity<String> handleRequest(HttpServletRequest httpServletRequest) {
        return restStubService.handleRequest(httpServletRequest, "");
    }


}
