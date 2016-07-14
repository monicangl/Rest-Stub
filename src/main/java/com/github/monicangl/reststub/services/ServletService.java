package com.github.monicangl.reststub.services;

import com.github.monicangl.reststub.models.Request;
import com.github.monicangl.reststub.models.RequestHeader;
import com.github.monicangl.reststub.models.RequestParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

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
        return new Request(HttpMethod.resolve(httpServletRequest.getMethod()), httpServletRequest.getRequestURI(), parameters, headers, body);
    }
}
