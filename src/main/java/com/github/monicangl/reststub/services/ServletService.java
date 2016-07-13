package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServletService {
    public Request getRequest(HttpServletRequest httpServletRequest, String body) {
        Set<RequestParameter> parameters = new HashSet<>();
        parameters.addAll(httpServletRequest.getParameterMap().keySet().stream()
                .map(key -> new RequestParameter(key, httpServletRequest.getParameter(key))).collect(Collectors.toList()));
        Set<RequestHeader> headers = new HashSet<>();
        Enumeration<String> requestHeaders = httpServletRequest.getHeaderNames();
        while (requestHeaders.hasMoreElements()) {
            String key = requestHeaders.nextElement();
            headers.add(new RequestHeader(key, httpServletRequest.getHeader(key)));
        }
        return new Request(getMethod(httpServletRequest.getMethod()), httpServletRequest.getRequestURI(), parameters, headers, body);
    }

    private RequestMethod getMethod(String method) {
        switch (method) {
            case "POST":
                return RequestMethod.POST;
            case "GET":
                return RequestMethod.GET;
            case "PUT":
                return RequestMethod.PUT;
            case "DELETE":
                return RequestMethod.DELETE;
            default:
                return RequestMethod.HEAD;
        }
    }
}
