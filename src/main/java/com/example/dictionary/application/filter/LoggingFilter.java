package com.example.dictionary.application.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.valueOf;


@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private static final List<HttpStatus> HTTP_STATUSES = List.of(
            BAD_REQUEST,
            NOT_FOUND,
            CONFLICT,
            FORBIDDEN,
            METHOD_NOT_ALLOWED,
            UNAUTHORIZED,
            INTERNAL_SERVER_ERROR
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String user = request.getRemoteUser();
        String method = request.getMethod();

        LOGGER.info("{} request {}: by user {}", method, requestURI, user);

        filterChain.doFilter(request, response);
        int responseStatus = response.getStatus();

        if (HTTP_STATUSES.contains(valueOf(responseStatus))) {
            LOGGER.error("{} response {}: status {}", method, requestURI, valueOf(responseStatus));
        } else {
            LOGGER.info("{} response {}: status {}", method, requestURI, valueOf(responseStatus));
        }
    }
}
